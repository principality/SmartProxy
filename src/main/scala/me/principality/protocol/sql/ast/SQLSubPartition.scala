package me.principality.protocol.sql.ast

import me.principality.protocol.sql.visitor.SQLASTVisitor

class SQLSubPartition extends SQLObjectImpl {
  protected var name: SQLName = null
  protected var values: SQLPartitionValue = null
  protected var tableSpace: SQLName = null

  def getName: SQLName = name

  def setName(name: SQLName): Unit = {
    if (name != null) name.setParent(this)
    this.name = name
  }

  def getValues: SQLPartitionValue = values

  def setValues(values: SQLPartitionValue): Unit = {
    if (values != null) values.setParent(this)
    this.values = values
  }

  def getTableSpace: SQLName = tableSpace

  def setTableSpace(tableSpace: SQLName): Unit = {
    if (tableSpace != null) tableSpace.setParent(this)
    this.tableSpace = tableSpace
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, name)
      acceptChild(visitor, tableSpace)
      acceptChild(visitor, values)
    }
    visitor.endVisit(this)
  }

  override def clone: SQLSubPartition = {
    val x = new SQLSubPartition
    if (name != null) x.setName(name.clone)
    if (values != null) x.setValues(values.clone)
    if (tableSpace != null) x.setTableSpace(tableSpace.clone)
    x
  }
}

