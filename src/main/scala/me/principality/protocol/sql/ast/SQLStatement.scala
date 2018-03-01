package me.principality.protocol.sql.ast

import java.util
import java.util.List

trait SQLStatement extends SQLObject {
  def getDbType: String

  def isAfterSemi: Boolean

  def setAfterSemi(afterSemi: Boolean): Unit

  override def clone: SQLStatement

  def getChildren: util.List[SQLObject]
}
