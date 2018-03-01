package me.principality.protocol.sql.ast

import me.principality.protocol.sql.visitor.SQLASTVisitor

class SQLSubPartitionByList extends SQLSubPartitionBy {
  protected var column: SQLName = null

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, column)
      acceptChild(visitor, subPartitionsCount)
    }
    visitor.endVisit(this)
  }

  def getColumn: SQLName = column

  def setColumn(column: SQLName): Unit = {
    if (column != null) column.setParent(this)
    this.column = column
  }

  override def clone: SQLSubPartitionByList = {
    val x = new SQLSubPartitionByList
    if (column != null) x.setColumn(column.clone)
    x
  }
}