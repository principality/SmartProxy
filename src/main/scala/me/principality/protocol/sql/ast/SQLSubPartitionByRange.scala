package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

import me.principality.protocol.sql.visitor.SQLASTVisitor

class SQLSubPartitionByRange extends SQLSubPartitionBy {
  private val columns: util.List[SQLName] = new util.ArrayList[SQLName]

  def getColumns: util.List[SQLName] = columns

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
  }

  override def clone: SQLSubPartitionByRange = {
    val x: SQLSubPartitionByRange = new SQLSubPartitionByRange
    import scala.collection.JavaConversions._
    for (column <- columns) {
      val c2: SQLName = column.clone
      c2.setParent(x)
      x.columns.add(c2)
    }
    x
  }
}
