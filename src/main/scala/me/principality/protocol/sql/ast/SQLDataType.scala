package me.principality.protocol.sql.ast

import java.util

object SQLDataType {

  object Constants {
    val CHAR: String = "CHAR"
    val NCHAR: String = "NCHAR"
    val VARCHAR: String = "VARCHAR"
    val DATE: String = "DATE"
    val TIMESTAMP: String = "TIMESTAMP"
    val XML: String = "XML"
    val DECIMAL: String = "DECIMAL"
    val NUMBER: String = "NUMBER"
    val REAL: String = "REAL"
    val DOUBLE_PRECISION: String = "DOUBLE PRECISION"
    val TINYINT: String = "TINYINT"
    val SMALLINT: String = "SMALLINT"
    val INT: String = "INT"
    val BIGINT: String = "BIGINT"
    val TEXT: String = "TEXT"
    val BYTEA: String = "BYTEA"
    val BOOLEAN: String = "BOOLEAN"
  }

}

trait SQLDataType extends SQLObject {
  def getName: String

  def nameHashCode64: Long

  def setName(name: String): Unit

  def getArguments: util.List[SQLExpr]

  def getWithTimeZone: Boolean

  def setWithTimeZone(value: Boolean): Unit

  def isWithLocalTimeZone: Boolean

  def setWithLocalTimeZone(value: Boolean): Unit

  override def clone: SQLDataType

  def setDbType(dbType: String): Unit

  def getDbType: String
}
