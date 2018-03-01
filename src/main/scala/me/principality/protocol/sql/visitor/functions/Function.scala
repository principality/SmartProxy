package me.principality.protocol.sql.visitor.functions

trait Function {
  def eval(visitor: SQLEvalVisitor, x: SQLMethodInvokeExpr): Any
}

