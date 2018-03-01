package me.principality.protocol.sql.ast

class SQLPartitionByRange() extends SQLPartitionBy {
  protected var interval: SQLExpr = null

  def getInterval: SQLExpr = interval

  def setInterval(interval: SQLExpr): Unit = {
    if (interval != null) interval.setParent(this)
    this.interval = interval
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, columns)
      acceptChild(visitor, interval)
      acceptChild(visitor, storeIn)
      acceptChild(visitor, partitions)
    }
    visitor.endVisit(this)
  }

  override def clone: SQLPartitionByRange = {
    val x = new SQLPartitionByRange
    if (interval != null) x.setInterval(interval.clone)
    import scala.collection.JavaConversions._
    for (column <- columns) {
      val c2 = column.clone
      c2.setParent(x)
      x.columns.add(c2)
    }
    x
  }
}
