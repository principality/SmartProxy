package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

import me.principality.protocol.sql.visitor.SQLASTVisitor

object SQLOver {

  object WindowingType extends Enumeration {
    type WindowingType = Value
    val ROWS, RANGE = Value
  }

}

class SQLOver() extends SQLObjectImpl {
  final protected val partitionBy: util.List[SQLExpr] = new util.ArrayList[SQLExpr]
  protected var orderBy: SQLOrderBy = null
  // for db2
  protected var of: SQLName = null
  protected var windowing: SQLExpr = null
  protected var windowingType: SQLOver.WindowingType = SQLOver.WindowingType.ROWS
  protected var windowingPreceding: Boolean = false
  protected var windowingFollowing: Boolean = false
  protected var windowingBetweenBegin: SQLExpr = null
  protected var windowingBetweenBeginPreceding: Boolean = false
  protected var windowingBetweenBeginFollowing: Boolean = false
  protected var windowingBetweenEnd: SQLExpr = null
  protected var windowingBetweenEndPreceding: Boolean = false
  protected var windowingBetweenEndFollowing: Boolean = false

  def this(orderBy: SQLOrderBy) {
    this()
    this.setOrderBy(orderBy)
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.partitionBy)
      acceptChild(visitor, this.orderBy)
      acceptChild(visitor, this.of)
    }
    visitor.endVisit(this)
  }

  def getOrderBy: SQLOrderBy = orderBy

  def setOrderBy(orderBy: SQLOrderBy): Unit = {
    if (orderBy != null) orderBy.setParent(this)
    this.orderBy = orderBy
  }

  def getOf: SQLName = of

  def setOf(of: SQLName): Unit = {
    if (of != null) of.setParent(this)
    this.of = of
  }

  def getPartitionBy: util.List[SQLExpr] = partitionBy

  def getWindowing: SQLExpr = windowing

  def setWindowing(windowing: SQLExpr): Unit = {
    this.windowing = windowing
  }

  def getWindowingType: SQLOver.WindowingType = windowingType

  def setWindowingType(windowingType: SQLOver.WindowingType): Unit = {
    this.windowingType = windowingType
  }

  def isWindowingPreceding: Boolean = windowingPreceding

  def setWindowingPreceding(windowingPreceding: Boolean): Unit = {
    this.windowingPreceding = windowingPreceding
  }

  def isWindowingFollowing: Boolean = windowingFollowing

  def setWindowingFollowing(windowingFollowing: Boolean): Unit = {
    this.windowingFollowing = windowingFollowing
  }

  def getWindowingBetweenBegin: SQLExpr = windowingBetweenBegin

  def setWindowingBetweenBegin(windowingBetweenBegin: SQLExpr): Unit = {
    this.windowingBetweenBegin = windowingBetweenBegin
  }

  def isWindowingBetweenBeginPreceding: Boolean = windowingBetweenBeginPreceding

  def setWindowingBetweenBeginPreceding(windowingBetweenBeginPreceding: Boolean): Unit = {
    this.windowingBetweenBeginPreceding = windowingBetweenBeginPreceding
  }

  def isWindowingBetweenBeginFollowing: Boolean = windowingBetweenBeginFollowing

  def setWindowingBetweenBeginFollowing(windowingBetweenBeginFollowing: Boolean): Unit = {
    this.windowingBetweenBeginFollowing = windowingBetweenBeginFollowing
  }

  def getWindowingBetweenEnd: SQLExpr = windowingBetweenEnd

  def setWindowingBetweenEnd(windowingBetweenEnd: SQLExpr): Unit = {
    this.windowingBetweenEnd = windowingBetweenEnd
  }

  def isWindowingBetweenEndPreceding: Boolean = windowingBetweenEndPreceding

  def setWindowingBetweenEndPreceding(windowingBetweenEndPreceding: Boolean): Unit = {
    this.windowingBetweenEndPreceding = windowingBetweenEndPreceding
  }

  def isWindowingBetweenEndFollowing: Boolean = windowingBetweenEndFollowing

  def setWindowingBetweenEndFollowing(windowingBetweenEndFollowing: Boolean): Unit = {
    this.windowingBetweenEndFollowing = windowingBetweenEndFollowing
  }

  override def equals(o: Any): Boolean = {
    if (this eq o) return true
    if (o == null || (getClass ne o.getClass)) return false
    val sqlOver: SQLOver = o.asInstanceOf[SQLOver]
    if (windowingPreceding != sqlOver.windowingPreceding) return false
    if (windowingFollowing != sqlOver.windowingFollowing) return false
    if (windowingBetweenBeginPreceding != sqlOver.windowingBetweenBeginPreceding) return false
    if (windowingBetweenBeginFollowing != sqlOver.windowingBetweenBeginFollowing) return false
    if (windowingBetweenEndPreceding != sqlOver.windowingBetweenEndPreceding) return false
    if (windowingBetweenEndFollowing != sqlOver.windowingBetweenEndFollowing) return false
    if (if (partitionBy != null) !(partitionBy == sqlOver.partitionBy)
    else sqlOver.partitionBy != null) return false
    if (if (orderBy != null) !(orderBy == sqlOver.orderBy)
    else sqlOver.orderBy != null) return false
    if (if (of != null) !(of == sqlOver.of)
    else sqlOver.of != null) return false
    if (if (windowing != null) !(windowing == sqlOver.windowing)
    else sqlOver.windowing != null) return false
    if (windowingType ne sqlOver.windowingType) return false
    if (if (windowingBetweenBegin != null) !(windowingBetweenBegin == sqlOver.windowingBetweenBegin)
    else sqlOver.windowingBetweenBegin != null) return false
    if (windowingBetweenEnd != null) windowingBetweenEnd == sqlOver.windowingBetweenEnd
    else sqlOver.windowingBetweenEnd == null
  }

  override def hashCode: Int = {
    var result: Int = if (partitionBy != null) partitionBy.hashCode
    else 0
    result = 31 * result + (if (orderBy != null) orderBy.hashCode
    else 0)
    result = 31 * result + (if (of != null) of.hashCode
    else 0)
    result = 31 * result + (if (windowing != null) windowing.hashCode
    else 0)
    result = 31 * result + (if (windowingType != null) windowingType.hashCode
    else 0)
    result = 31 * result + (if (windowingPreceding) 1
    else 0)
    result = 31 * result + (if (windowingFollowing) 1
    else 0)
    result = 31 * result + (if (windowingBetweenBegin != null) windowingBetweenBegin.hashCode
    else 0)
    result = 31 * result + (if (windowingBetweenBeginPreceding) 1
    else 0)
    result = 31 * result + (if (windowingBetweenBeginFollowing) 1
    else 0)
    result = 31 * result + (if (windowingBetweenEnd != null) windowingBetweenEnd.hashCode
    else 0)
    result = 31 * result + (if (windowingBetweenEndPreceding) 1
    else 0)
    result = 31 * result + (if (windowingBetweenEndFollowing) 1
    else 0)
    result
  }

  def cloneTo(x: SQLOver): Unit = {
    import scala.collection.JavaConversions._
    for (item <- partitionBy) {
      val item1: SQLExpr = item.clone
      item1.setParent(x)
      x.partitionBy.add(item)
    }
    if (orderBy != null) x.setOrderBy(orderBy.clone)
    if (of != null) x.setOf(of.clone)
    if (windowing != null) x.setWindowing(windowing.clone)
    x.windowingType = windowingType
    x.windowingPreceding = windowingPreceding
    x.windowingFollowing = windowingFollowing
    if (windowingBetweenBegin != null) x.setWindowingBetweenBegin(windowingBetweenBegin.clone)
    x.windowingBetweenBeginPreceding = windowingBetweenBeginPreceding
    x.windowingBetweenBeginFollowing = windowingBetweenBeginFollowing
    if (windowingBetweenEnd != null) x.setWindowingBetweenEnd(windowingBetweenEnd.clone)
    x.windowingBetweenEndPreceding = windowingBetweenEndPreceding
    x.windowingBetweenEndFollowing = windowingBetweenEndFollowing
  }

  override def clone: SQLOver = {
    val x: SQLOver = new SQLOver
    cloneTo(x)
    x
  }
}
