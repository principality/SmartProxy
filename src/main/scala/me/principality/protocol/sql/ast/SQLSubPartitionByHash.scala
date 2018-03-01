package me.principality.protocol.sql.ast

class SQLSubPartitionByHash extends SQLSubPartitionBy {
  protected var expr: SQLExpr = null
  // for aliyun ads
  private var key = false

  def getExpr: SQLExpr = expr

  def setExpr(expr: SQLExpr): Unit = {
    if (expr != null) expr.setParent(this)
    this.expr = expr
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, expr)
      acceptChild(visitor, subPartitionsCount)
    }
    visitor.endVisit(this)
  }

  def isKey: Boolean = key

  def setKey(key: Boolean): Unit = {
    this.key = key
  }

  override def clone: SQLSubPartitionByHash = {
    val x = new SQLSubPartitionByHash
    if (expr != null) x.setExpr(expr.clone)
    x.key = key
    x
  }
}
