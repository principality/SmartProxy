package me.principality.protocol.sql.ast

import java.util
import java.util.List


trait SQLExpr extends SQLObject with Cloneable {
  override def clone: SQLExpr

  def computeDataType: SQLDataType

  def getChildren: util.List[SQLObject]
}

