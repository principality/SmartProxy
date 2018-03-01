package me.principality.protocol.sql.visitor

import java.util
import functions.Function

object SQLEvalVisitor {
  val EVAL_VALUE: String = "eval.value"
  val EVAL_EXPR: String = "eval.expr"
  val EVAL_ERROR: Any = new Any
  val EVAL_VALUE_COUNT: Any = new Any
  val EVAL_VALUE_NULL: Any = new Any
}

trait SQLEvalVisitor extends SQLASTVisitor {
  def getFunction(funcName: String): Function

  def registerFunction(funcName: String, function: Function): Unit

  def unRegisterFunction(funcName: String): Unit

  def getParameters: util.List[AnyRef]

  def setParameters(parameters: util.List[AnyRef]): Unit

  def incrementAndGetVariantIndex: Int

  def isMarkVariantIndex: Boolean

  def setMarkVariantIndex(markVariantIndex: Boolean): Unit
}