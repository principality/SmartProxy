package me.principality.protocol.sql.ast

import me.principality.protocol.sql.visitor.SQLASTVisitor

class SQLPartitionByHash extends SQLPartitionBy { // for aliyun ads
  protected var key = false

  def isKey: Boolean = key

  def setKey(key: Boolean): Unit = {
    this.key = key
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, partitionsCount)
      acceptChild(visitor, getPartitions)
      acceptChild(visitor, subPartitionBy)
    }
    visitor.endVisit(this)
  }

  override def clone: SQLPartitionByHash = {
    val x = new SQLPartitionByHash
    x.key = key
    import scala.collection.JavaConversions._
    for (column <- columns) {
      val c2 = column.clone
      c2.setParent(x)
      x.columns.add(c2)
    }
    x
  }
}

