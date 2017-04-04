package com.ms.qaTools.tcpConnection

import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.Channels
import java.nio.channels.SocketChannel
import java.nio.channels.UnresolvedAddressException

trait Connection {
  def connect(server: InetSocketAddress): SocketChannel
  def isConnected(socketChannel: SocketChannel): Boolean
  def send(data: Array[Byte], socketChannel: SocketChannel)
  def receive(socketChannel: SocketChannel): Array[Byte]
  def close(socketChannel: SocketChannel)
  def getBuffer(directBuffer: Boolean, bufferLength: Int): ByteBuffer
}

class NIOConnectionImpl(bufferLength: Int, directBuffer: Boolean, confBlock: Boolean, soTimeOut: Int) extends Connection {

  override def getBuffer(directBuffer: Boolean, bufferLength: Int): ByteBuffer = {
    if (directBuffer) ByteBuffer.allocateDirect(bufferLength).order(ByteOrder.BIG_ENDIAN) else
      ByteBuffer.allocate(bufferLength).order(ByteOrder.BIG_ENDIAN)
  }

  override def connect(server: InetSocketAddress): SocketChannel = {
    var socketChannel: SocketChannel = SocketChannel.open()
    socketChannel.configureBlocking(confBlock)

    try {
      socketChannel.connect(server)
      socketChannel.isConnected()
      socketChannel.socket().setSoTimeout(soTimeOut)
      socketChannel
    } catch {
      case uae: UnresolvedAddressException => throw new Error("Connection has not been made to host: " + server.getHostName() + ":" + server.getPort())
      case e: Exception => throw new Error("Connection has not been made.")
    }
  }

  override def isConnected(socketChannel: SocketChannel): Boolean = {
    if (null != socketChannel) socketChannel.isConnected() else false
  }

  override def send(data: Array[Byte], socketChannel: SocketChannel) = {
    if (null == socketChannel) throw new Error("Connection has not been made.")
    val buffer = getBuffer(directBuffer, bufferLength)
    var pos = 0
    while (pos < data.length) {
      val bytesToWrite = if ((data.length - pos) > buffer.limit()) {
        buffer.limit()
      } else {
        val len = data.length - pos
        len
      }
      buffer.clear()
      buffer.put(data, pos, bytesToWrite)
      buffer.flip()

      while (buffer.hasRemaining()) {
        socketChannel.write(buffer)
      }
      pos = pos + bytesToWrite
    }
  }

  override def receive(socketChannel: SocketChannel): Array[Byte] = {
    if (null == socketChannel) throw new Error("Connection has not been made.")
    val buffer = getBuffer(directBuffer, bufferLength)
    buffer.clear()

    //SocketChannel will not timeout for its read operation but you can get this effect from reading from the channel in another way
    //val bytesRead = socketChannel.read(buffer)
    val inStream = socketChannel.socket().getInputStream();
    val wrappedChannel = Channels.newChannel(inStream);
    val bytesRead = try { wrappedChannel.read(buffer) } catch { case e: Exception => 0 }
    bytesRead match {
      case 0 | -1  => Array[Byte]()
      case _  => {
        val data: Array[Byte] = new Array[Byte](bytesRead)
        val byteStream = new ByteArrayOutputStream()
        buffer.rewind()
        buffer.get(data, 0, data.length)
        byteStream.write(data)
        buffer.clear()
        byteStream.toByteArray()
      }
    }
  }

  override def close(socketChannel: SocketChannel) = {
    socketChannel.close()
  }
}/*
COPYRIGHT NOTICE AND DISCLAIMER
Copyright (c) 2009-2016, Contributor

This program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License, version 3, as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License, version 3 for more details.

You should have received a copy of the GNU Lesser General Public License, version 3, along with this program; if not, see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
THE FOLLOWING DISCLAIMER APPLIES TO ALL SOFTWARE CODE AND OTHER MATERIALS CONTRIBUTED IN CONNECTION WITH THIS PROGRAM:
THIS SOFTWARE IS LICENSED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND ANY WARRANTY OF NON-INFRINGEMENT, ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THIS SOFTWARE MAY BE REDISTRIBUTED TO OTHERS ONLY BY EFFECTIVELY USING THIS OR ANOTHER EQUIVALENT DISCLAIMER AS WELL AS ANY OTHER LICENSE TERMS THAT MAY APPLY.
*/