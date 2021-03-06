package com.ms.qaTools.interpreter

import scala.io.Source
import scala.sys.process._
import com.ms.qaTools.{toolkit => tk}

/**
 * TODO: Run process concurrently
 *       Batch read from process stdout/stdin
 *       ResultValidator for !errorMessage.toString.isEmpty
 */

case class ShellInterpreterResult(
  status: tk.Status,
  command: String,
  exitCode: Option[Int] = None,
  stdout: Option[String] = None,
  stderr: Option[String] = None,
  exception: Option[Throwable] = None
) extends InterpreterResult {
  lazy val stdoutStr = stdout.getOrElse("")
  lazy val stderrStr = stderr.getOrElse("")
}

abstract class AbstractShellInterpreter extends Interpreter[String, ShellInterpreterResult] {
  def extraEnv: Seq[(String, String)]
  val tee: Boolean
  val shell = "/bin/sh"

  protected def createTempScript(command: String): java.io.File = {
    val cmdFileTemp = java.io.File.createTempFile("runCmds", ".tmp")
    cmdFileTemp.deleteOnExit()
    val writer = new java.io.FileWriter(cmdFileTemp)
    writer.write(command.replaceAll("\r\n", "\n"))
    writer.close()
    cmdFileTemp
  }

  def run(command: String): ShellInterpreterResult = {
    val message: StringBuilder = new StringBuilder
    val errorMessage: StringBuilder = new StringBuilder

    val pIO = new ProcessIO(
      _.close(),
      stdout => {
        Source.fromInputStream(stdout).addString(message)
        if (tee) {
          System.out.print(message.toString)
          System.out.flush
        }
        stdout.close()
      },
      stderr => {
        Source.fromInputStream(stderr).addString(errorMessage)
        if (tee) {
          System.err.print(errorMessage.toString)
          System.err.flush
        }
        stderr.close()
      })

    util.Try {
      Process(List(shell, createTempScript(command).getAbsolutePath), None, extraEnv: _*).run(pIO).exitValue
    } match {
      case util.Success(exitCode) =>
        ShellInterpreterResult(
          if (exitCode == 0 && errorMessage.toString.isEmpty) tk.Passed else tk.Failed,
          command,
          Option(exitCode),
          Option(message.toString),
          Option(errorMessage.toString).filter{_.nonEmpty})
      case util.Failure(t) =>
        ShellInterpreterResult(tk.Failed, command, exception = Option(t))
    }
  }
}

case class ShellInterpreter(tee: Boolean = false, extraEnv: Seq[(String, String)] = Seq.empty) extends AbstractShellInterpreter

case class WindowsShellInterpreter(tee: Boolean = false, extraEnv: Seq[(String, String)] = Seq.empty) extends AbstractShellInterpreter {
  override def run(command: String) =
    super.run(System.getenv("WINDIR") + "\\system32\\cmd.exe /c " + command)
}
/*
Copyright 2017 Morgan Stanley

Licensed under the GNU Lesser General Public License Version 3 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

https://www.gnu.org/licenses/lgpl-3.0.en.html

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

IN ADDITION, THE FOLLOWING DISCLAIMER APPLIES IN CONNECTION WITH THIS PROGRAM:

THIS SOFTWARE IS LICENSED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND ANY
WARRANTY OF NON-INFRINGEMENT, ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
OF SUCH DAMAGE. THIS SOFTWARE MAY BE REDISTRIBUTED TO OTHERS ONLY BY EFFECTIVELY
USING THIS OR ANOTHER EQUIVALENT DISCLAIMER IN ADDITION TO ANY OTHER REQUIRED
LICENSE TERMS.
*/
