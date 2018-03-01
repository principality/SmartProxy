package me.principality.protocol.sql.ast

trait SQLName extends SQLExpr {
  def getSimpleName: String

  override def clone: SQLName

  def nameHashCode64: Long

  def hashCode64: Long
}