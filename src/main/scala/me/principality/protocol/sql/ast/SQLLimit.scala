package me.principality.protocol.sql.ast

import me.principality.protocol.sql.visitor.SQLASTVisitor

final class SQLLimit() extends SQLObjectImpl {
  def this(rowCount: SQLExpr) {
    this()
    this.setRowCount(rowCount)
  }

  def this(offset: SQLExpr, rowCount: SQLExpr) {
    this()
    this.setOffset(offset)
    this.setRowCount(rowCount)
  }

  private var rowCount = null
  private var offset = null

  def getRowCount: SQLExpr = rowCount

  def setRowCount(rowCount: SQLExpr): Unit = {
    if (rowCount != null) rowCount.setParent(this)
    this.rowCount = rowCount
  }

  def setRowCount(rowCount: Int): Unit = {
    this.setRowCount(new SQLIntegerExpr(rowCount))
  }

  def getOffset: SQLExpr = offset

  def setOffset(offset: Int): Unit = {
    this.setOffset(new SQLIntegerExpr(offset))
  }

  def setOffset(offset: SQLExpr): Unit = {
    if (offset != null) offset.setParent(this)
    this.offset = offset
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, offset)
      acceptChild(visitor, rowCount)
    }
    visitor.endVisit(this)
  }

  override def clone: SQLLimit = {
    val x = new SQLLimit
    if (offset != null) x.setOffset(offset.clone)
    if (rowCount != null) x.setRowCount(rowCount.clone)
    x
  }
}

