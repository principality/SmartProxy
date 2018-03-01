package me.principality.protocol.sql.ast

trait SQLHint extends SQLObject {
  override def clone: SQLHint
}