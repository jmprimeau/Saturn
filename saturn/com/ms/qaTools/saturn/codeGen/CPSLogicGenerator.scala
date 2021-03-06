package com.ms.qaTools.saturn.codeGen
import com.ms.qaTools.codeGen.scala.ConnectTry
import com.ms.qaTools.codeGen.scala.FnExpr
import com.ms.qaTools.codeGen.scala.FoldExpr
import com.ms.qaTools.codeGen.scala.ForAssignment
import com.ms.qaTools.codeGen.scala.ForTryExpr
import com.ms.qaTools.codeGen.scala.FutureExpr
import com.ms.qaTools.codeGen.scala.FutureGen
import com.ms.qaTools.codeGen.scala.OptionExpr
import com.ms.qaTools.codeGen.scala.ScalaExpr
import com.ms.qaTools.codeGen.scala.ScalaGen
import com.ms.qaTools.codeGen.scala.SeqTryExpr
import com.ms.qaTools.codeGen.scala.TryExpr
import com.ms.qaTools.codeGen.scala.TryFnExpr
import com.ms.qaTools.codeGen.scala.TryGen
import com.ms.qaTools.saturn.CpsStep
import com.ms.qaTools.saturn.modules.cpsModule.AbstractCpsOperation
import com.ms.qaTools.saturn.modules.cpsModule.AbstractTerminationCondition
import com.ms.qaTools.saturn.modules.cpsModule.PerlTerminationCondition
import com.ms.qaTools.saturn.modules.cpsModule.CpsGetOperation
import com.ms.qaTools.saturn.modules.cpsModule.CpsPutOperation
import com.ms.qaTools.saturn.modules.cpsModule.CpsSowDeleteOperation
import com.ms.qaTools.saturn.modules.cpsModule.CpsSubscribeOperation
import com.ms.qaTools.saturn.modules.cpsModule.CpsUnsubscribeOperation
import com.ms.qaTools.saturn.resources.referenceResource.ReferenceResource
import com.ms.qaTools.saturn.resources.soapIOResource.CpsMessageTypes
import com.ms.qaTools.saturn.resources.soapIOResource.CpsResource
import com.ms.qaTools.saturn.types.ResourceDefinition
import com.ms.qaTools.saturn.utils.SaturnEObjectUtils._
import scala.annotation.tailrec
import scala.collection.JavaConversions.asScalaBuffer
import scala.util.Try

object CPSLogicGenerator {
  def genTerminationStr(condition: AbstractTerminationCondition) =
    Option(condition) match {
      case Some(c: PerlTerminationCondition) =>
        for {
          generator <- ComplexValueCodeGenerator(c.getPerlCode)
          code <- generator.generate
        } yield s"Option(TerminationCondition.untilElement[CPSMessage](${code}. get))"
      case None => Try{"None: Option[Iterator[CPSMessage] => Iterator[CPSMessage]]"}
      case _ => throw new Error("Unsupported termination condition type")
    }

  protected def typeAndImplicits(typ: CpsMessageTypes): (String, String) = typ match {
    case CpsMessageTypes.CPS0 => ("CPSMessage", "implicit val publisher = cpsResource.output. get.publisher")
    case CpsMessageTypes.SOAP => ("msjava.hdom.Document", "")
    case CpsMessageTypes.GPB  => ("GeneratedMessage", "")
  }

  @tailrec protected def findRootCpsResource(res: ResourceDefinition)
                                            (implicit codeGenUtil: SaturnCodeGenUtil): CpsResource = res match {
    case res: CpsResource       => res
    case ref: ReferenceResource => findRootCpsResource(ref.deref)
    case _                      => throw new IllegalArgumentException(s"Cannot get CpsResource from $res")
  }

  def apply(cps: CpsStep)(implicit codeGenUtil: SaturnCodeGenUtil): Try[FutureGen] = {
    implicit val appendOptions = ContextAppendOptions(true)
    for {
      cfg <- Try { val c = cps.getCpsConfiguration(); if (c == null) throw new NullPointerException(s"Configuration for Cps Step: $cps cannot be null.") else c }
      cpsResource <- ResourceGenerator(cfg.getCpsResource)
      (msgType, implicits) <- Try(typeAndImplicits(findRootCpsResource(cfg.getCpsResource).getTransport.getCpsMessageType)).recover{
        case e => throw new Exception("Failed to get CPS message type", e)}
      ioType = s"CPSIO[$msgType, $msgType]"
      fold <- cfg.getOperations.zipWithIndex.map{case (op, i) => genOperation(op, implicits, i).map(
        o => FnExpr(Seq("cpsResource"), o, Option(s"$ioType => Try[($ioType, Result)]")))}.toTrySeq.map(
        ops => FoldExpr(ops, ScalaExpr("Try((cpsResource, Result(Passed)))"), FnExpr(
          Seq("t", "op"),
          ScalaExpr("t match {case Success((cpsResource, result)) => if(result.status == Passed) op(cpsResource) else Try((cpsResource, result)) case f => f}"),
          Option(s"(Try[($ioType, Result)], $ioType => Try[($ioType, Result)]) =>  Try[($ioType, Result)]"))))
    } yield FutureExpr(ForTryExpr(Seq(
      ForAssignment("cpsResource", cpsResource),
      ForAssignment("(_, _result)", fold),
      ForAssignment("result", TryExpr("CpsResult(if(_result.status == Passed) Passed else Failed)"))),
      ScalaExpr(s"IterationResult(result.status, context, iterationMetaData, result, ${codeGenUtil.getDefaultIterationNo(cps)})")))
  }

  def genOperation(op: AbstractCpsOperation, implicits: String, index: Int)(implicit appendOptions: ContextAppendOptions): Try[ScalaGen] = op match {
    case op: CpsSubscribeOperation =>
      for {
        topic   <- Option(op.getTopic).map(f => ComplexValueStringGenerator(f)).getOrElse(Try{TryExpr("null")})map(_.withMap(OptionExpr(ScalaExpr("_"))))
        filter  <- Option(op.getFilter).map(f => ComplexValueStringGenerator(f)).getOrElse(Try{TryExpr("null")})map(_.withMap(OptionExpr(ScalaExpr("_"))))
        sowInit <- Try{op.isSetSowInit && op.isSowInit}
      } yield ForTryExpr(Seq(
        ForAssignment("topic", topic),
        ForAssignment("filter", filter),
        ForAssignment("cpsResource", TryExpr(s"cpsResource.copy(i = cpsResource.i.map(_.changeTopic(topic, filter, $sowInit)))"))),
        ScalaExpr(s"""val cpsContext = context.appendMetaDataContext("CpsSubscribeOperation$index", Option("CpsSubscribeOperation"))
                      topic.foreach(t => cpsContext.appendMetaDataContext("Topic", t))
                      cpsContext.appendMetaDataContext("Filter", filter)
                      (cpsResource, Result(Passed))"""))

    case op: CpsUnsubscribeOperation =>
      for {
        topic <- ComplexValueStringGenerator(op.getTopic)
      } yield ForTryExpr(Seq(
        ForAssignment("topic", topic),
        ForAssignment("cpsResource", TryExpr("""require(cpsResource.input.toOption.flatMap(_.topic).exists(_ == topic), "Not subscribed to topic " + topic)
                                                cpsResource.copy(i = cpsResource.input.map(old => old.changeTopic(None, old.filter, old.withSow)))"""))),
        ScalaExpr(s"""val cpsContext = context.appendMetaDataContext("CpsUnsubscribeOperation$index", Option("CpsUnsubscribeOperation"))
                      cpsContext.appendMetaDataContext("Topic", topic)
                      (cpsResource, Result(Passed))"""))

    case op: CpsGetOperation =>
      for {
        outputIO    <- ResourceGenerator(op.getOutput)
        timeOut     <- Option(op.getTimeOut).map(t => ComplexValueStringGenerator(t)).getOrElse(Try{TryExpr("\"\"")})
        termination <- genTerminationStr(op.getTerminationCondition)
      } yield ForTryExpr(Seq(
        ForAssignment("outputIO", ConnectTry(outputIO, "Output")),
        ForAssignment("timeout", timeOut),
        ForAssignment("termination", TryExpr(termination)),
        ForAssignment("rowSource", TryFnExpr("""cpsResource.input.map { rs =>
                                                  val rs1 = termination.map(_(rs)).getOrElse(rs)
                                                  if (timeout.isEmpty) rs1
                                                  else TerminationCondition.timeOut(timeout.toLong).apply(rs1)
                                                }""")),
        ForAssignment("write", TryFnExpr("outputIO.output.map(_.write(rowSource))"))),
        ScalaExpr(s"""val cpsContext = context.appendMetaDataContext("CpsGetOperation$index", Option("CpsGetOperation"))
                      cpsContext.appendMetaDataContext("Timeout", timeout)
                      cpsContext.metaDataContexts += (("Output", context.metaDataContexts("Output")))
                      (cpsResource, Result(Passed))"""))

    case op: CpsPutOperation =>
      for {
        inputIO <- ResourceGenerator(op.getInput)
        topic   <- Option(op.getTopic).map(f => ComplexValueStringGenerator(f)).getOrElse(Try{TryExpr("null")})map(_.withMap(OptionExpr(ScalaExpr("_"))))
        timeOut <- Option(op.getTimeOut).map(t => ComplexValueStringGenerator(t)).getOrElse(Try{TryExpr("\"\"")})
      } yield ForTryExpr(Seq(
        ForAssignment("inputIO", inputIO),
        ForAssignment("input", ConnectTry(TryFnExpr("inputIO.input"), "Input")),
        ForAssignment("topic", topic),
        ForAssignment("timeout", timeOut),
        ForAssignment("rowWriter", TryFnExpr("""if(topic.isEmpty) cpsResource.output
                                                else cpsResource.output.map(w => CpsRowWriter(w.publisher, topic))""")),
        ForAssignment("write", TryFnExpr(s"Try{$implicits; if(timeout.isEmpty) rowWriter.write(input) else rowWriter.write(input, timeout.toInt)}"))),
        ScalaExpr(s"""val cpsContext = context.appendMetaDataContext("CpsPutOperation$index", "CpsPutOperation")
                      topic.foreach(t => cpsContext.appendMetaDataContext("Topic", t))
                      cpsContext.metaDataContexts += (("Input", context.metaDataContexts("Input")))
                      cpsContext.appendMetaDataContext("Timeout", Option(timeout))
                      (cpsResource, Result(Passed))"""))

    case op: CpsSowDeleteOperation =>
      for {
        topic   <- Option(op.getTopic).map(f => ComplexValueStringGenerator(f)).getOrElse(Try{TryExpr("null")}).map(_.withMap(OptionExpr(ScalaExpr("_"))))
        filter  <- Option(op.getFilter).map(f => ComplexValueStringGenerator(f)).getOrElse(Try{TryExpr("null")}).map(_.withMap(OptionExpr(ScalaExpr("_"))))
      } yield ForTryExpr(Seq(
        ForAssignment("topic", topic),
        ForAssignment("filter", filter),
        ForAssignment("_", TryExpr(s"cpsResource.output.foreach(o => CpsRowWriter(o.publisher, topic).deleteSow(filter))"))),
        ScalaExpr(s"""val cpsContext = context.appendMetaDataContext("CpsSowDeleteOperation$index", Option("CpsSowDeleteOperation"))
                      cpsContext.appendMetaDataContext("Topic", topic)
                      cpsContext.appendMetaDataContext("Filter", filter)
                      (cpsResource, Result(Passed))"""))
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
