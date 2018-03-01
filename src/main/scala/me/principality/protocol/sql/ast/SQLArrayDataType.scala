package me.principality.protocol.sql.ast

import java.util
import java.util.{Collections, List}

class SQLArrayDataType extends SQLObjectImpl with SQLDataType {
  private var dbType: String = _
  private var componentType: SQLDataType = _

  def this(componentType: SQLDataType) {
    this()
    setComponentType(componentType)
  }

  def this(componentType: SQLDataType, dbType: String) {
    this()
    this.dbType = dbType
    setComponentType(componentType)
  }

  override def getName: String = "ARRAY"

  override def nameHashCode64: Long = FnvHash.Constants.ARRAY

  override def setName(name: String): Unit = {
    throw new UnsupportedOperationException
  }

  override def getArguments: util.List[SQLExpr] = Collections.emptyList

  override def getWithTimeZone: Boolean = null

  override def setWithTimeZone(value: Boolean): Unit = {
    throw new UnsupportedOperationException
  }

  override def isWithLocalTimeZone: Boolean = false

  override def setWithLocalTimeZone(value: Boolean): Unit = {
    throw new UnsupportedOperationException
  }

  override def setDbType(dbType: String): Unit = {
    dbType = dbType
  }

  override def getDbType: String = dbType

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) acceptChild(visitor, componentType)
    visitor.endVisit(this)
  }

  override def clone: SQLArrayDataType = null

  def getComponentType: SQLDataType = componentType

  def setComponentType(x: SQLDataType): Unit = {
    if (x != null) x.setParent(this)
    this.componentType = x
  }
}
