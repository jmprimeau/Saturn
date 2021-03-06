package com.ms.qaTools.saturn.runtime.runner
import com.fasterxml.jackson.databind.JsonNode
import com.ms.qaTools.complexValues.getXPathValue
import com.ms.qaTools.io.definition.FixIO
import com.ms.qaTools.io.definition.JsonIO
import com.ms.qaTools.io.Input
import com.ms.qaTools.io.rowSource.ColumnDefinitions
import com.ms.qaTools.io.rowSource.file.FixRowSource
import com.ms.qaTools.io.rowSource.JsonPathRowSource
import com.ms.qaTools.io.rowSource.Utils.ToW3cDocument
import com.ms.qaTools.IteratorUtil
import com.ms.qaTools.MonadSeqUtil
import com.ms.qaTools.saturn.codeGen.ComplexValueGenerator
import com.ms.qaTools.saturn.codeGen.Context
import com.ms.qaTools.saturn.codeGen.Utils.connectTry
import com.ms.qaTools.saturn.runtime.Dml
import com.ms.qaTools.saturn.runtime.EnvVar
import com.ms.qaTools.saturn.runtime.File
import com.ms.qaTools.saturn.runtime.Groovy
import com.ms.qaTools.saturn.runtime.SaturnExecutionContext
import com.ms.qaTools.saturn.runtime.Shell
import com.ms.qaTools.saturn.types.{InterpretersEnum => MInterpretersEnum}
import com.ms.qaTools.TryUtil
import com.ms.qaTools.xml.NamespaceContextImpl
import com.ms.qaTools.xml.xpath.FixPath
import com.ms.qaTools.xml.xpath.XPathAware
import javax.xml.namespace.QName
import scala.util.Try

object ComplexValueMap {
  def apply(cvIdxPairTrys: Seq[(Int, Try[String])]): Try[Map[Int, String]] =
    for {
      cvIdxPairs <- cvIdxPairTrys.map {
        case (idx, tryString) =>
          tryString map (str => (idx, str))
      }.toTrySeq
    } yield cvIdxPairs.toMap
}

object CellValueTry {
  def apply(context: Context,
            resourceIOTry: Try[Input[Iterator[Seq[String]] with ColumnDefinitions]],
            indexTry: Try[String],
            columnNameTry: Try[String]): Try[String] = {
    for {
      resourceIO <- resourceIOTry.rethrow("An exception occurred while connection cell value resource IO.")
      resource <- connectTry(context, resourceIO.input, "CellValueResource", false).rethrow("An exception occurred while connection cell value resource.")
      index <- indexTry.rethrow("An exception occurred while generating cell value index.")
      columnName <- columnNameTry.rethrow("An exception occurred while generating cell value column name.")
      row <- Try { resource(index.toInt) }.rethrow(s"Row $index does not exist in the data set.")
    } yield resource.colDefs.find(_.name == columnName).map { colDef =>
      Option(row(colDef.index)).getOrElse(ComplexValueGenerator.nullStr)
    }.getOrElse(throw new Exception("No column named or wrong column name specified '%s' inside the value".format(columnName)))
  }.rethrow("An exception occurred while building cell value.")
}

object CodeScalaValueTry {
  def apply(context: Context,
            codeTry: Try[Any]): Try[String] = {
    for {
      code <- codeTry.rethrow("An exception occurred while generating scala code value.")
    } yield code.toString
  }
}

object EnvVarTry {
  def apply(context: Context,
            envVarName: Try[String])(implicit sc: SaturnExecutionContext): Try[String] = {
    for {
      envVar <- envVarName.rethrow("An exception occurred while generating env var name.")
    } yield EnvVar(envVar)
  }.rethrow("An exception occurred while building env var.")
}

object FileValueTry {
  def apply(context: Context,
            filenameTry: Try[String]): Try[String] = {
    for {
      filename <- filenameTry.rethrow("An exception occurred while generating file value filename.")
    } yield File(filename)
  }.rethrow("An exception occurred while building file value.")
}

object FixPathValueTry {
  def apply(context: Context,
            resourceIOTry: Try[Input[FixRowSource]],
            rowTry: Try[String],
            fixPathTry: Try[String],
            messageTypeTry: Try[String],
            returnType: QName): Try[String] = {
    for {
      resourceIO <- resourceIOTry.rethrow("An exception occurred while connecting fixPath value resource IO.")
      fixRowSource <- connectTry(context, resourceIO.input, "FixPathValueResource", false).rethrow("An exception occurred while connecting fixPath value resource.")
      row <- rowTry.rethrow("An exception occurred while generating xPath value row.")
      fixPathStr <- fixPathTry.rethrow("An exception occurred while generating fixPath value fixPath.")
      messageType <- messageTypeTry.rethrow("An exception occurred while generating fixPath value message type.")
    } yield {
      val fixXmlRowSource = fixRowSource.toDocumentIterator
      val fixPath = new FixPath(fixPathStr)(fixRowSource.dataDictionary) with XPathAware
      val xPath = fixPath asXPath (messageType)
      getXPathValue(fixXmlRowSource, NamespaceContextImpl(), xPath.toString, row.toInt, true, returnType)
    }
  }.rethrow("An exception occurred while building fixPath value.")
}

object JSONValueTry {
  def apply(context: Context,
            resourceIOTry: Try[Input[Iterator[JsonNode]]],
            rowTry: Try[String],
            jsonPathTry: Try[String]): Try[String] = {
    for {
      resourceIO <- resourceIOTry.rethrow("An exception occurred while connecting JSON value resource IO.")
      resource <- connectTry(context, resourceIO.input, "JSONValueResource", false).rethrow("An exception occurred while connecting JSON value resource.")
      row <- rowTry.rethrow("An exception occurred while generating JSON value row.")
      jsonPath <- jsonPathTry.rethrow("An exception occurred while generating JSON value jsonPath.")
    } yield {
      val jsonPathRs = JsonPathRowSource(resource, Seq((jsonPath, "")))
      jsonPathRs.asDelimitedRowIterator(row.toInt).ensuring(_.size == 1).head
    }
  }.rethrow("An exception occurred while building JSON value.")
}

object PropertyValueTry {
  def apply(context: Context,
            resourceIOTry: Try[Input[Iterator[Seq[String]] with ColumnDefinitions]],
            propertyTry: Try[String]): Try[String] = {
    for {
      resourceIO <- resourceIOTry.rethrow("An exception occurred while connecting property value resource IO.")
      resource <- connectTry(context, resourceIO.input, "PropertyValueResource", false).rethrow("An exception occurred while connecting property value resource.")
      propertyName <- propertyTry.rethrow("An exception occurred while generating property name.")
      properties <- Try { resource.next }.rethrow("An exception occurred while getting properties from resource.")
    } yield resource.colDefs.find(_.name == propertyName).map { colDef =>
      properties(colDef.index)
    }.getOrElse(throw new Exception("No property named '%s'".format(propertyName)))
  }.rethrow("An exception occurred while building property value.")
}

object XPathValueTry {
  def apply[A: ToW3cDocument](context: Context,
            resourceIOTry: Try[Input[Iterator[A]]],
            rowTry: Try[String],
            xPathTry: Try[String],
            namespaceTry: Try[Iterator[Seq[String]]],
            fragment: Boolean,
            returnType: QName): Try[String] = {
    for {
      resourceIO <- resourceIOTry.rethrow("An exception occurred while connecting xPath value resource IO.")
      resourceA <- connectTry(context, resourceIO.input, "XPathValueResource", false).rethrow("An exception occurred while connecting xPath value resource.")
      resource = resourceA.map(implicitly[ToW3cDocument[A]].convert)
      row <- rowTry.rethrow("An exception occurred while generating xPath value row.")
      xPath <- xPathTry.rethrow("An exception occurred while generating xPath value xPath.")
      namespace <- connectTry(context, namespaceTry, "XPathValueNamespaceResource", false).rethrow("An exception occurred while connecting xPath value namespace resource.")
    } yield if (resource.hasNext) {
      val head = resource.next
      val userDefinedNs = namespace.toList.map { r => (r(0) -> r(1)) }.toMap
      val nsContext = NamespaceContextImpl(head, userDefinedNs)
      getXPathValue(Iterator(head) ++ resource, nsContext, xPath, row.toInt, fragment, returnType)
    }
    else throw new Exception("Resource for XPathValue is empty.")
  }.rethrow("An exception occurred while building xPath value.")
}

object CodeValueTry {
  def apply(context: Context,
            codeTry: Try[String],
            interpreterType: MInterpretersEnum): Try[String] = {
    for {
      code <- codeTry.rethrow("An exception occurred while generating code value code string.")
    } yield interpreterType match {
      case MInterpretersEnum.DML    => Dml(code)
      case MInterpretersEnum.GROOVY => Groovy(code)
      case MInterpretersEnum.SHELL  => Shell(code)
      case MInterpretersEnum.PERL   => throw new Exception("Scala code complex value should not be handled here.")
      case MInterpretersEnum.SCALA  => throw new Exception("Scala code complex value should not be handled here.")
      case _                        => throw new Exception(s"Interpreter '$interpreterType' is not valid.")
    }
  }
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
