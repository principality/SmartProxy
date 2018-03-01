package me.principality.protocol.sql.ast

trait SQLReplaceable {
  def replace(expr: SQLExpr, target: SQLExpr): Boolean
}

