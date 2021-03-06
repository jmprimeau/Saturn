package com.ms.qaTools.codeGen.scala

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.Properties
import scala.util.Success
import scala.util.Try

import com.ms.qaTools.MonadSeqUtil
import com.ms.qaTools.TryUtil
import com.ms.qaTools.saturn.codeGen.Context

trait ScalaGen {
  self =>
  def generate():Try[String]
  def +(that:ScalaGen):ScalaGen = new ScalaGen(){
    def generate():Try[String] = for {
      thisStr <- self.generate()
      thatStr <- that.generate()
    } yield s"$thisStr\n$thatStr"
  }
}

case class ConcatGen(generators:Seq[ScalaGen], separator:String="", start:String="", end:String="") extends ScalaGen {
  def generate:Try[String] = generators.map{_.generate()}.toTrySeq.map{generatesStrings => generatesStrings.mkString(start, separator, end)}
}

trait StringGen extends ScalaGen
trait CanMapGen extends ScalaGen { // Shouldn't this be called MonadGen?
  def withFlatMap(expr:CanMapGen):CanMapGen
  def withMap(expr:ScalaGen):CanMapGen
}

abstract class MethodCallGen(rootObjectExpr:ScalaGen,methodName:String, parmExprs:Seq[ScalaGen]) extends ScalaGen {
  def generate:Try[String] = for {
    rootObjectStr <- rootObjectExpr.generate
    parmsStr      <- parmExprs.map{_.generate}.flatMkString(", ")
  } yield s"{$rootObjectStr}.$methodName($parmsStr)"
}

case class SeqMethodCallGen(rootObjectExpr:SeqGen,methodName:String, parmExprs:Seq[ScalaGen]) extends MethodCallGen(rootObjectExpr,methodName,parmExprs) with SeqGen
case class OptionMethodCallGen(rootObjectExpr:OptionGen,methodName:String, parmExprs:Seq[ScalaGen]) extends MethodCallGen(rootObjectExpr,methodName,parmExprs) with OptionGen
case class TryMethodCallGen(rootObjectExpr:TryGen,methodName:String, parmExprs:Seq[ScalaGen]) extends MethodCallGen(rootObjectExpr,methodName,parmExprs) with TryGen
case class FutureMethodCallGen(rootObjectExpr:FutureGen,methodName:String, parmExprs:Seq[ScalaGen]) extends MethodCallGen(rootObjectExpr,methodName,parmExprs) with FutureGen

case class FunctionCallGen(fn: String, args: ScalaGen*) extends ScalaGen {
  def generate() = args.map(_.generate()).toTrySeq.map(fn + _.mkString("(", ", ", ")"))
}

trait SeqGen extends CanMapGen {
  override def withFlatMap(expr:CanMapGen):SeqGen = SeqMethodCallGen(this, "flatMap", Seq(expr))
  override def withMap(expr:ScalaGen):SeqGen = SeqMethodCallGen(this, "map", Seq(expr))
}

trait OptionGen extends CanMapGen {
  override def withFlatMap(expr:CanMapGen):OptionGen = OptionMethodCallGen(this, "flatMap", Seq(expr))
  override def withMap(expr:ScalaGen):OptionGen = OptionMethodCallGen(this, "map", Seq(expr))
}

trait TryGen extends CanMapGen {
  override def withFlatMap(expr:CanMapGen):TryGen = TryMethodCallGen(this, "flatMap", Seq(expr))
  override def withMap(expr:ScalaGen):TryGen = TryMethodCallGen(this, "map", Seq(expr))
  def withRethrow(message:StringGen):TryGen = TryFnExpr(FunctionCallGen("rethrow", this, message))
  def withRethrow(messageStr:String):TryGen = withRethrow(StringExpr(messageStr, noInterpolate = true))
  def withMethod(method: String, args: ScalaExpr*) = TryMethodCallGen(this, method, args)
}

trait FutureGen extends CanMapGen {
  override def withFlatMap(expr:CanMapGen):FutureGen = FutureMethodCallGen(this, "flatMap", Seq(expr))
  override def withMap(expr:ScalaGen):FutureGen = FutureMethodCallGen(this, "map", Seq(expr))
}

case class ConnectTry(resExpr:TryGen, name:String, isReference: Boolean = false) extends TryGen {
  def generate() = resExpr.generate().map{resStr => s"""connectTry(context, $resStr, "$name", $isReference)"""}
}

case class ConnectTrySeq(trySeqResExpr:SeqExpr[TryGen], name:String) extends TryGen {
  def generate() = trySeqResExpr.generate().map(seqResExpr => s"""toTrySeq($seqResExpr.zipWithIndex.map{ case(res,i) => connectTry(context, res, "${name}_" + i.toString, false)})""")
}

case class AttributeTry(attrExpr:TryGen, name:String) extends TryGen {
  def generate() = attrExpr.generate().map{attrStr => s"""attributeTry(context, $attrStr, "$name")"""}
}

case class ValDefinition(name:String, varType:String) extends ScalaGen {
  def generate() = Try{s"val $name:$varType"}
}

case class StringExpr(expr:String, noInterpolate:Boolean=false) extends StringGen {
  val qqq = "\"\"\""
  val prefix = if(noInterpolate) "" else "s"
  def quote (s: String) = prefix + qqq + s + qqq
  def escape (s: String) = if(noInterpolate) s else s.replace("\\","\\\\")
  def generate: Try[String] =
    Try(escape(expr).split(qqq, -1).map(quote).mkString(""" + "\"\"\"" + """))
}

case class ScalaExpr(expr:String) extends ScalaGen {
  def generate():Try[String] = Success( expr )
}

class SeqExpr[ItemType <: ScalaGen](exprs:Seq[ItemType], `type`: Option[String] = None) extends SeqGen {
  def asType = `type` map {t => s"[$t]"} getOrElse("")
  def generate():Try[String] = exprs.map{_.generate()}.toTrySeq.map{exprStrs => s"Seq${asType}(${exprStrs.mkString(", ")})"}
}

case class SeqTryExpr(exprs:Seq[TryGen], `type`: Option[String] = None) extends SeqExpr[TryGen](exprs, `type`) {
  self =>
  override def asType = `type` map {t => s"[Try[$t]]"} getOrElse("")
  override def generate():Try[String] = exprs.map{_.generate()}.toTrySeq.map{exprStrs => s"Seq${asType}(${exprStrs.mkString(", ")})"}
  def toTryGen:TryGen = new TryGen() {
    override def generate():Try[String] = self.generate().map{str => s"toTrySeq({$str})"}
  }
}

case class SeqTryFnExpr(expr:TryGen) extends SeqExpr[TryGen](Seq(expr)) {
  override def generate():Try[String] = expr.generate
}

object SeqTryFnExpr {
  def apply(exprStr:String):SeqTryFnExpr = SeqTryFnExpr(TryFnExpr(exprStr))
}

case class FoldExpr(seq: Seq[TryGen], z: ScalaGen, f: ScalaGen) extends TryGen {
  def generate: Try[String] = for {
    z <- z.generate
    seq <- SeqTryExpr(seq).generate
    f <- f.generate
  } yield s"$seq.foldLeft($z)($f)"
}

case class FnExpr(args: Seq[String], body: ScalaGen, typ: Option[String] = None) extends TryGen {
  def generate: Try[String] = body.generate.map(b => s"{case ${args.mkString("(", ", ", ")")} => $b} ${typ.map(": " + _).getOrElse("")}")
}

case class OptionExpr(expr:ScalaGen) extends OptionGen {
  def generate():Try[String] = expr.generate().map{e => s"Option($e)" }
}

case class TryExpr(expr:ScalaGen, guaranteedSuccess: Boolean = false) extends TryGen {
  def f = if (guaranteedSuccess) "Success" else "Try"
  def generate():Try[String] = expr.generate().map{e => s"$f{$e${System.getProperty("line.separator")}}"}
}

case class Tuple2Expr(_1: ScalaGen, _2: ScalaGen) extends ScalaGen {
  def generate():Try[String] = {
    for{
      first <- _1.generate
      second <- _2.generate
    } yield s"($first, $second)"
  }
}

object TryExpr {
  def apply(exprStr:String, guaranteedSuccess: Boolean):TryExpr = TryExpr(ScalaExpr(exprStr), guaranteedSuccess)
  def apply(exprStr:String): TryExpr = apply(exprStr, false)
}

case class TryFnExpr(expr:ScalaGen) extends TryGen {
  def generate() = expr.generate()
}

object TryFnExpr {
  def apply(exprStr:String):TryFnExpr = TryFnExpr(ScalaExpr(exprStr))
}

case class FutureExpr(expr:ScalaGen, guaranteedSuccess: Boolean = false) extends FutureGen {
  def generate():Try[String] = expr.generate().map {
    "Future%s { %s }".format(if (guaranteedSuccess) ".successful" else "", _)
  }
}

object FutureExpr {
  def apply(exprStr:String):FutureExpr = FutureExpr(ScalaExpr(exprStr))
}

case class FutureFnExpr(expr:ScalaGen) extends FutureGen {
  def generate() = expr.generate()
}

object FutureFnExpr {
  def apply(exprStr:String):FutureFnExpr = FutureFnExpr(ScalaExpr(exprStr))
}

case class MapAssignment[+LHS <: ScalaGen, +RHS <: ScalaGen](lhsGen:LHS,rhsGen:RHS) extends ScalaGen {
  def generate() = {
    for {
      lhsStr <- lhsGen.generate
      rhsStr <- rhsGen.generate
    } yield {
      s"$lhsStr -> $rhsStr"
    }
  }
}

sealed trait ForArgument extends ScalaGen

case class ForAssignment(name:String,rhs:CanMapGen) extends ForArgument {
  def generate() = rhs.generate().map{rhs => s"$name <- $rhs"}
}

case class ForAssignmentEq(name: String, rhs: ScalaGen) extends ForArgument {
  def generate() = rhs.generate().map{rhs => s"$name = $rhs"}
}

case class ForCondition(boolExpr:ScalaGen) extends ForArgument {
  def generate() = boolExpr.generate().map{boolStr => s"if($boolStr)"}
}

abstract class ForExpr(assignments:Seq[ForArgument], bodyExpr:ScalaGen) extends ScalaGen {
  def generateEmpty():Try[String]
  def generate() = if(assignments.isEmpty) generateEmpty()
                else for{
                  assignmentStrs <- assignments.map{assignment => assignment.generate()}.toTrySeq
                  yieldStr <- bodyExpr.generate()
                } yield s"for{  ${assignmentStrs.mkString("\n  ")}\n} yield { $yieldStr }"
}

case class ForOptionExpr(assignments:Seq[ForArgument], bodyExpr:ScalaGen) extends ForExpr(assignments, bodyExpr) with OptionGen {
  override def generateEmpty:Try[String] = bodyExpr.generate().map(bodyStr => s"Option($bodyStr)")
}

case class ForTryExpr(assignments:Seq[ForArgument], bodyExpr:ScalaGen) extends ForExpr(assignments, bodyExpr) with TryGen {
  override def generateEmpty:Try[String] = bodyExpr.generate().map(bodyStr => s"Try($bodyStr)")

  @tailrec private def findDep(x: ScalaGen): ScalaGen = x match {
    case x: FunctionCallGen if x.fn == "rethrow" && x.args.size == 2 => findDep(x.args(0))
    case AttributeTry(dep, _)               => findDep(dep)
    case ConnectTry(dep, _, _)              => findDep(dep)
    case TryFnExpr(dep)                     => findDep(dep)
    case _                                  => x
  }

  protected def wrap(buf: ListBuffer[String], name: String, rhs: ScalaGen, callGet: Boolean): ListBuffer[String] = {
    val x0 = rhs.generate(). get
    val x = if (callGet) s"{$x0}. get" else x0
    val dep = findDep(rhs) match {
      case dep if dep == rhs => x0
      case dep               => dep.generate(). get
    }
    if (dep.contains(name)) {
      // Generate temporary name to work around the limit that Scala cannot shadow a val using a val in the outer scope with same name
      val tmp = s"__outer_$name"
      s"val $tmp = $x; {" +=: s"val $name = $tmp;" +=: buf += "}"
    } else
      s"val $name = $x;" +=: buf
  }

  override def generate() = for (body <- bodyExpr.generate) yield {
    val buf = (assignments :\ ListBuffer(body)) {
      case (ForAssignment(name, rhs), buf) =>
        wrap(buf, name, rhs, true)
      case (ForAssignmentEq(name, rhs), buf) =>
        wrap(buf, name, rhs, false)
      case (ForCondition(pred), buf) =>
        val p = pred.generate(). get
        s"""if (!($p)) throw new NoSuchElementException("Predicate does not hold");""" +=: buf
    }
    buf.mkString("Try {", Properties.lineSeparator, "}")
  }
}

case class ForFutureExpr(assignments:Seq[ForArgument], bodyExpr:ScalaGen) extends ForExpr(assignments, bodyExpr) with FutureGen {
  override def generateEmpty:Try[String] = bodyExpr.generate().map(bodyStr => s"Future($bodyStr)")
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
