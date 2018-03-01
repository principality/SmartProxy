package me.principality.protocol.sql.ast

import me.principality.protocol.sql.visitor.SQLASTVisitor

object SQLKeep {

  object DenseRank extends Enumeration {
    type DenseRank = Value
    val FIRST, //
    LAST = Value
  }

}

final class SQLKeep extends SQLObjectImpl {
  protected var denseRank: SQLKeep.DenseRank = null
  protected var orderBy: SQLOrderBy = null

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) acceptChild(visitor, this.orderBy)
    visitor.endVisit(this)
  }

  def getDenseRank: SQLKeep.DenseRank = denseRank

  def setDenseRank(denseRank: SQLKeep.DenseRank): Unit = {
    this.denseRank = denseRank
  }

  def getOrderBy: SQLOrderBy = orderBy

  def setOrderBy(orderBy: SQLOrderBy): Unit = {
    if (orderBy != null) orderBy.setParent(this)
    this.orderBy = orderBy
  }

  override def clone: SQLKeep = {
    val x = new SQLKeep
    x.denseRank = denseRank
    if (orderBy != null) x.setOrderBy(orderBy.clone)
    x
  }
}
