package me.principality.protocol.sql.ast

trait SQLObjectWithDataType extends SQLObject {

  def getDataType: SQLDataType

  def setDataType(dataType: SQLDataType): Unit
}

