package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

final class SQLOrderBy() extends SQLObjectImpl {
  final protected val items: util.List[SQLSelectOrderByItem] = new util.ArrayList[SQLSelectOrderByItem]
  // for postgres
  private var sibings: Boolean = false

  def this(expr: SQLExpr)

  def addItem(item: SQLSelectOrderByItem): Unit = {
    if (item != null) item.setParent(this)
    this.items.add(item)
  }

  def getItems: util.List[SQLSelectOrderByItem] = this.items

  def isSibings: Boolean = this.sibings

  def setSibings(sibings: Boolean): Unit = {
    this.sibings = sibings
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) acceptChild(visitor, this.items)
    visitor.endVisit(this)
  }

  override def hashCode: Int = {
    val prime: Int = 31
    var result: Int = 1
    result = prime * result + (if (items == null) 0
    else items.hashCode)
    result = prime * result + (if (sibings) 1231
    else 1237)
    result
  }

  override def equals(obj: Any): Boolean = {
    if (this eq obj) return true
    if (obj == null) return false
    if (getClass ne obj.getClass) return false
    val other: SQLOrderBy = obj.asInstanceOf[SQLOrderBy]
    if (items == null) if (other.items != null) return false
    else if (!(items == other.items)) return false
    if (sibings != other.sibings) return false
    true
  }

  def addItem(expr: SQLExpr, `type`: SQLOrderingSpecification): Unit = {
    val item: SQLSelectOrderByItem = createItem
    item.setExpr(expr)
    item.setType(`type`)
    addItem(item)
  }

  protected def createItem: SQLSelectOrderByItem = new SQLSelectOrderByItem

  override def clone: SQLOrderBy = {
    val x: SQLOrderBy = new SQLOrderBy
    import scala.collection.JavaConversions._
    for (item <- items) {
      val item1: SQLSelectOrderByItem = item.clone
      item1.setParent(x)
      x.items.add(item1)
    }
    x.sibings = sibings
    x
  }
}
