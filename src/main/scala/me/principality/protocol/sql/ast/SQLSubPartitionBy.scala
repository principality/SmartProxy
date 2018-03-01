package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

abstract class SQLSubPartitionBy extends SQLObjectImpl {
  protected var subPartitionsCount: SQLExpr = null
  protected var linear: Boolean = false
  protected var options: util.List[SQLAssignItem] = new util.ArrayList[SQLAssignItem]
  protected var subPartitionTemplate: util.List[SQLSubPartition] = new util.ArrayList[SQLSubPartition]

  def getSubPartitionsCount: SQLExpr = subPartitionsCount

  def setSubPartitionsCount(subPartitionsCount: SQLExpr): Unit = {
    if (subPartitionsCount != null) subPartitionsCount.setParent(this)
    this.subPartitionsCount = subPartitionsCount
  }

  def isLinear: Boolean = linear

  def setLinear(linear: Boolean): Unit = {
    this.linear = linear
  }

  def getOptions: util.List[SQLAssignItem] = options

  def getSubPartitionTemplate: util.List[SQLSubPartition] = subPartitionTemplate

  def cloneTo(x: SQLSubPartitionBy): Unit = {
    if (subPartitionsCount != null) x.setSubPartitionsCount(subPartitionsCount.clone)
    x.linear = linear
    import scala.collection.JavaConversions._
    for (option <- options) {
      val option2: SQLAssignItem = option.clone
      option2.setParent(x)
      x.options.add(option2)
    }
    import scala.collection.JavaConversions._
    for (p <- subPartitionTemplate) {
      val p2: SQLSubPartition = p.clone
      p2.setParent(x)
      x.subPartitionTemplate.add(p2)
    }
  }

  override def clone: SQLSubPartitionBy
}
