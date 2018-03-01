package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

object SQLParameter {

  object ParameterType extends Enumeration {
    type ParameterType = Value
    val DEFAULT, //
    IN, // in
    OUT, // out
    INOUT // inout = Value
  }

}

final class SQLParameter extends SQLObjectImpl with SQLObjectWithDataType {
  private var name: SQLName = null
  private var dataType: SQLDataType = null
  private var defaultValue: SQLExpr = null
  private var paramType: SQLParameter.ParameterType = null
  private var noCopy: Boolean = false
  private var constant: Boolean = false
  private var cursorName: SQLName = null
  final private val cursorParameters: util.List[SQLParameter] = new util.ArrayList[SQLParameter]
  private var order: Boolean = false
  private var map: Boolean = false
  private var member: Boolean = false

  def getDefaultValue: SQLExpr = defaultValue

  def setDefaultValue(deaultValue: SQLExpr): Unit = {
    if (deaultValue != null) deaultValue.setParent(this)
    this.defaultValue = deaultValue
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

  def getParamType: SQLParameter.ParameterType = paramType

  def setParamType(paramType: SQLParameter.ParameterType): Unit = {
    this.paramType = paramType
  }

  override def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, name)
      acceptChild(visitor, dataType)
      acceptChild(visitor, defaultValue)
    }
    visitor.endVisit(this)
  }

  def isNoCopy: Boolean = noCopy

  def setNoCopy(noCopy: Boolean): Unit = {
    this.noCopy = noCopy
  }

  def isConstant: Boolean = constant

  def setConstant(constant: Boolean): Unit = {
    this.constant = constant
  }

  def getCursorParameters: util.List[SQLParameter] = cursorParameters

  def getCursorName: SQLName = cursorName

  def setCursorName(cursorName: SQLName): Unit = {
    if (cursorName != null) cursorName.setParent(this)
    this.cursorName = cursorName
  }

  override def clone: SQLParameter = {
    val x: SQLParameter = new SQLParameter
    if (name != null) x.setName(name.clone)
    if (dataType != null) x.setDataType(dataType.clone)
    if (defaultValue != null) x.setDefaultValue(defaultValue.clone)
    x.paramType = paramType
    x.noCopy = noCopy
    x.constant = constant
    x.order = order
    x.map = map
    if (cursorName != null) x.setCursorName(cursorName.clone)
    import scala.collection.JavaConversions._
    for (p <- cursorParameters) {
      val p2: SQLParameter = p.clone
      p2.setParent(x)
      x.cursorParameters.add(p2)
    }
    x
  }

  def isOrder: Boolean = order

  def setOrder(order: Boolean): Unit = {
    this.order = order
  }

  def isMap: Boolean = map

  def setMap(map: Boolean): Unit = {
    this.map = map
  }

  def isMember: Boolean = member

  def setMember(member: Boolean): Unit = {
    this.member = member
  }
}
