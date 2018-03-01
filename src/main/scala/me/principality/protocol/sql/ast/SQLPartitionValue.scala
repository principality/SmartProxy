package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

object SQLPartitionValue {

  object Operator extends Enumeration {
    type Operator = Value
    val LessThan, //
    In, List = Value
  }

}

class SQLPartitionValue(var operator: SQLPartitionValue.Operator) extends SQLObjectImpl {
  final protected val items: util.List[SQLExpr] = new util.ArrayList[SQLExpr]

  def getItems: util.List[SQLExpr] = items

  def addItem(item: SQLExpr): Unit = {
    if (item != null) item.setParent(this)
    this.items.add(item)
  }

  def getOperator: SQLPartitionValue.Operator = operator

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) acceptChild(visitor, getItems)
    visitor.endVisit(this)
  }

  override def clone: SQLPartitionValue = {
    val x: SQLPartitionValue = new SQLPartitionValue(operator)
    import scala.collection.JavaConversions._
    for (item <- items) {
      val item2: SQLExpr = item.clone
      item2.setParent(x)
      x.items.add(item2)
    }
    x
  }
}
