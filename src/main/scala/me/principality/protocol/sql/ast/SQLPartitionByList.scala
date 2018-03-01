package me.principality.protocol.sql.ast

class SQLPartitionByList extends SQLPartitionBy {
  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, columns)
      acceptChild(visitor, partitionsCount)
      acceptChild(visitor, getPartitions)
      acceptChild(visitor, subPartitionBy)
    }
    visitor.endVisit(this)
  }

  override def clone: SQLPartitionByList = {
    val x = new SQLPartitionByList
    import scala.collection.JavaConversions._
    for (column <- columns) {
      val c2 = column.clone
      c2.setParent(x)
      x.columns.add(c2)
    }
    x
  }
}
