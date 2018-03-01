package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, Collections, List}

import me.principality.protocol.sql.visitor.SQLASTVisitor

object SQLStructDataType {

  class Field(val name: SQLName, val dataType: SQLDataType) extends SQLObjectImpl {
    setName(name)
    setDataType(dataType)
    private var name: SQLName = null
    private var dataType: SQLDataType = null

    override protected def accept0(visitor: SQLASTVisitor): Unit = {
      if (visitor.visit(this)) {
        acceptChild(visitor, name)
        acceptChild(visitor, dataType)
      }
      visitor.endVisit(this)
    }

    def getName: SQLName = name

    def setName(x: SQLName): Unit = {
      if (x != null) x.setParent(this)
      this.name = x
    }

    def getDataType: SQLDataType = dataType

    def setDataType(x: SQLDataType): Unit = {
      if (x != null) x.setParent(this)
      this.dataType = x
    }
  }

}

class SQLStructDataType() extends SQLObjectImpl with SQLDataType {
  private var dbType: String = null
  private val fields: util.List[SQLStructDataType.Field] = new util.ArrayList[SQLStructDataType.Field]

  def this(dbType: String) {
    this()
    this.dbType = dbType
  }

  override def getName: String = "STRUCT"

  override def nameHashCode64: Long = FnvHash.Constants.STRUCT

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
    if (visitor.visit(this)) acceptChild(visitor, fields)
    visitor.endVisit(this)
  }

  override def clone: SQLStructDataType = {
    val x: SQLStructDataType = new SQLStructDataType(dbType)
    import scala.collection.JavaConversions._
    for (field <- fields) {
      x.addField(field.name, field.dataType.clone)
    }
    x
  }

  def getFields: util.List[SQLStructDataType.Field] = fields

  def addField(name: SQLName, dataType: SQLDataType): Unit = {
    val field: SQLStructDataType.Field = new SQLStructDataType.Field(name, dataType)
    field.setParent(this)
    fields.add(field)
  }
}
