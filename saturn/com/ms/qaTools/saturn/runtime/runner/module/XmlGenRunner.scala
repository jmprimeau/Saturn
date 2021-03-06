package com.ms.qaTools.saturn.runtime.runner.module

import scala.util.Try

import org.w3c.dom.Document

import com.ms.qaTools.io.Input
import com.ms.qaTools.io.definition.XmlIO
import com.ms.qaTools.io.rowSource.ColumnDefinitions
import com.ms.qaTools.saturn.codeGen.Context
import com.ms.qaTools.saturn.codeGen.Utils.connectTry
import com.ms.qaTools.toolkit.XmlGen

object XmlGenRunner {
  def apply[TEMPLATE_TYPE](context: Context,
                           templateIOTry: Try[Input[Iterator[TEMPLATE_TYPE]]],
                           outputIOTry: Try[XmlIO],
                           inputIOTry: Try[Input[Iterator[Seq[String]] with ColumnDefinitions]],
                           isLegacy: Boolean)(implicit templateTypeToSeqString: Iterator[TEMPLATE_TYPE] => Iterator[Document]): Try[XmlGen] =
    XmlGen(templateIOTry.flatMap(input => connectTry(context, input.input, "TemplateInput", false)),
           outputIOTry.flatMap(output => connectTry(context, output.output, "Output", false)),
           inputIOTry.flatMap(input => connectTry(context, input.input, "DsInput", false)),
           isLegacy)
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
