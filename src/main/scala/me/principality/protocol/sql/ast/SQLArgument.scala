package me.principality.protocol.sql.ast

import me.principality.protocol.sql.visitor.SQLASTVisitor

class SQLArgument extends SQLObjectImpl {
  private var `type` = null
  private var expr = null

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) acceptChild(visitor, expr)
    visitor.endVisit(this)
  }

  override def clone: SQLArgument = {
    val x = new SQLArgument
    x.`type` = `type`
    if (expr != null) x.setExpr(expr.clone)
    x
  }

  def getType: SQLParameter.ParameterType = `type`

  def getExpr: SQLExpr = expr

  def setType(`type`: SQLParameter.ParameterType): Unit = {
    this.`type` = `type`
  }

  def setExpr(x: SQLExpr): Unit = {
    if (x != null) x.setParent(this)
    this.expr = x
  }
}

