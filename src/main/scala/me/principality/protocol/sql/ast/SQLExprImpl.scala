package me.principality.protocol.sql.ast

abstract class SQLExprImpl() extends SQLObjectImpl with SQLExpr {
  override def equals(o: Any): Boolean

  override def hashCode: Int

  override def clone: SQLExpr

  override def computeDataType: SQLDataType = null
}