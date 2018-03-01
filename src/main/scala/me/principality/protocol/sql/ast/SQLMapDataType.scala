package me.principality.protocol.sql.ast

import java.util
import java.util.{Collections, List}

import me.principality.protocol.sql.visitor.SQLASTVisitor

class SQLMapDataType() extends SQLObjectImpl with SQLDataType {
  private var dbType: String = null
  private var keyType: SQLDataType = null
  private var valueType: SQLDataType = null

  def this(keyType: SQLDataType, valueType: SQLDataType) {
    this()
    this.setKeyType(keyType)
    this.setValueType(valueType)
  }

  def this(keyType: SQLDataType, valueType: SQLDataType, dbType: String) {
    this()
    this.setKeyType(keyType)
    this.setValueType(valueType)
    this.dbType = dbType
  }

  override def getName: String = "MAP"

  override def nameHashCode64: Long = FnvHash.Constants.MAP

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
    if (visitor.visit(this)) {
      acceptChild(visitor, keyType)
      acceptChild(visitor, valueType)
    }
    visitor.endVisit(this)
  }

  override def clone: SQLMapDataType = {
    val x: SQLMapDataType = new SQLMapDataType
    x.dbType = dbType
    if (keyType != null) x.setKeyType(keyType.clone)
    if (valueType != null) x.setValueType(valueType.clone)
    x
  }

  def getKeyType: SQLDataType = keyType

  def setKeyType(x: SQLDataType): Unit = {
    if (x != null) x.setParent(this)
    this.keyType = x
  }

  def getValueType: SQLDataType = valueType

  def setValueType(x: SQLDataType): Unit = {
    if (x != null) x.setParent(this)
    this.valueType = x
  }
}

