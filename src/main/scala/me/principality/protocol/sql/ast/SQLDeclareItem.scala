package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

object SQLDeclareItem {

  object Type extends Enumeration {
    type Type = Value
    val TABLE, LOCAL, CURSOR = Value
  }

}

class SQLDeclareItem() extends SQLObjectImpl with SQLObjectWithDataType {
  protected var `type`: SQLDeclareItem.Type = null
  protected var name: SQLName = null
  protected var dataType: SQLDataType = null
  protected var value: SQLExpr = null
  protected var tableElementList: util.List[SQLTableElement] = new util.ArrayList[SQLTableElement]
  protected var resolvedObject: SQLObject = null

  def this(name: SQLName, dataType: SQLDataType) {
    this()
    this.setName(name)
    this.setDataType(dataType)
  }

  def this(name: SQLName, dataType: SQLDataType, value: SQLExpr) {
    this()
    this.setName(name)
    this.setDataType(dataType)
    this.setValue(value)
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.name)
      acceptChild(visitor, this.dataType)
      acceptChild(visitor, this.value)
      acceptChild(visitor, this.tableElementList)
    }
    visitor.endVisit(this)
  }

  def getName: SQLName = name

  def setName(name: SQLName): Unit = {
    if (name != null) name.setParent(this)
    this.name = name
  }

  override def getDataType: SQLDataType = dataType

  override def setDataType(dataType: SQLDataType): Unit = {
    if (dataType != null) dataType.setParent(this)
    this.dataType = dataType
  }

  def getValue: SQLExpr = value

  def setValue(value: SQLExpr): Unit = {
    if (value != null) value.setParent(this)
    this.value = value
  }

  def getTableElementList: util.List[SQLTableElement] = tableElementList

  def setTableElementList(tableElementList: util.List[SQLTableElement]): Unit = {
    this.tableElementList = tableElementList
  }

  def getType: SQLDeclareItem.Type = `type`

  def setType(`type`: SQLDeclareItem.Type): Unit = {
    this.`type` = `type`
  }

  def getResolvedObject: SQLObject = resolvedObject

  def setResolvedObject(resolvedObject: SQLObject): Unit = {
    this.resolvedObject = resolvedObject
  }
}
