package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

class SQLDataTypeImpl() extends SQLObjectImpl with SQLDataType {
  private var name: String = null
  private var nameHashCode64: Long = 0L
  final protected val arguments: util.List[SQLExpr] = new util.ArrayList[SQLExpr]
  private var withTimeZone: Boolean = false
  private var withLocalTimeZone: Boolean = false
  private var dbType: String = null
  private var unsigned: Boolean = false
  private var zerofill: Boolean = false

  def this(name: String) {
    this()
    this.name = name
  }

  def this(name: String, precision: Int) {
    this(name)
    addArgument(new SQLIntegerExpr(precision))
  }

  def this(name: String, arg: SQLExpr) {
    this(name)
    addArgument(arg)
  }

  def this(name: String, precision: Int, scale: Int) {
    this(name)
    addArgument(new SQLIntegerExpr(precision))
    addArgument(new SQLIntegerExpr(scale))
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) acceptChild(visitor, this.arguments)
    visitor.endVisit(this)
  }

  override def getName: String = this.name

  override def nameHashCode64: Long = {
    if (nameHashCode64 == 0) nameHashCode64 = FnvHash.hashCode64(name)
    nameHashCode64
  }

  override def setName(name: String): Unit = {
    this.name = name
    nameHashCode64 = 0L
  }

  override def getArguments: util.List[SQLExpr] = this.arguments

  def addArgument(argument: SQLExpr): Unit = {
    if (argument != null) argument.setParent(this)
    this.arguments.add(argument)
  }

  override def equals(o: Any): Boolean = {
    if (this eq o) return true
    if (o == null || (getClass ne o.getClass)) return false
    val dataType: SQLDataTypeImpl = o.asInstanceOf[SQLDataTypeImpl]
    if (if (name != null) !(name == dataType.name)
    else dataType.name != null) return false
    if (if (arguments != null) !(arguments == dataType.arguments)
    else dataType.arguments != null) return false
    if (withTimeZone != null) withTimeZone == dataType.withTimeZone
    else dataType.withTimeZone == null
  }

  override def hashCode: Int = {
    val value: Long = nameHashCode64
    (value ^ (value >>> 32)).toInt
  }

  override def getWithTimeZone: Boolean = withTimeZone

  override def setWithTimeZone(withTimeZone: Boolean): Unit = {
    this.withTimeZone = withTimeZone
  }

  override def isWithLocalTimeZone: Boolean = withLocalTimeZone

  override def setWithLocalTimeZone(withLocalTimeZone: Boolean): Unit = {
    this.withLocalTimeZone = withLocalTimeZone
  }

  override def getDbType: String = dbType

  override def setDbType(dbType: String): Unit = {
    this.dbType = dbType
  }

  override def clone: SQLDataTypeImpl = {
    val x: SQLDataTypeImpl = new SQLDataTypeImpl
    cloneTo(x)
    x
  }

  def cloneTo(x: SQLDataTypeImpl): Unit = {
    x.dbType = dbType
    x.name = name
    x.nameHashCode64 = nameHashCode64
    import scala.collection.JavaConversions._
    for (arg <- arguments) {
      x.addArgument(arg.clone)
    }
    x.withTimeZone = withTimeZone
    x.withLocalTimeZone = withLocalTimeZone
    x.zerofill = zerofill
    x.unsigned = unsigned
  }

  override def toString: String = SQLUtils.toSQLString(this, dbType)

  def isUnsigned: Boolean = unsigned

  def setUnsigned(unsigned: Boolean): Unit = {
    this.unsigned = unsigned
  }

  def isZerofill: Boolean = zerofill

  def setZerofill(zerofill: Boolean): Unit = {
    this.zerofill = zerofill
  }
}
