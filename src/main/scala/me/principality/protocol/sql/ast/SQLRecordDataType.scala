package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

class SQLRecordDataType extends SQLDataTypeImpl with SQLDataType {
  final private val columns: util.List[SQLColumnDefinition] = new util.ArrayList[SQLColumnDefinition]

  def getColumns: util.List[SQLColumnDefinition] = columns

  def addColumn(column: SQLColumnDefinition): Unit = {
    column.setParent(this)
    this.columns.add(column)
  }

  override def clone: SQLRecordDataType = {
    val x: SQLRecordDataType = new SQLRecordDataType
    cloneTo(x)
    import scala.collection.JavaConversions._
    for (c <- columns) {
      val c2: SQLColumnDefinition = c.clone
      c2.setParent(x)
      x.columns.add(c2)
    }
    x
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) acceptChild(visitor, this.columns)
    visitor.endVisit(this)
  }
}

