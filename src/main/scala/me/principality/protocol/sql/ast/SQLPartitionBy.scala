package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

abstract class SQLPartitionBy extends SQLObjectImpl {
  protected var subPartitionBy: SQLSubPartitionBy = null
  protected var partitionsCount: SQLExpr = null
  protected var linear: Boolean = false
  protected var partitions: util.List[SQLPartition] = new util.ArrayList[SQLPartition]
  protected var storeIn: util.List[SQLName] = new util.ArrayList[SQLName]
  protected var columns: util.List[SQLExpr] = new util.ArrayList[SQLExpr]

  def getPartitions: util.List[SQLPartition] = partitions

  def addPartition(partition: SQLPartition): Unit = {
    if (partition != null) partition.setParent(this)
    this.partitions.add(partition)
  }

  def getSubPartitionBy: SQLSubPartitionBy = subPartitionBy

  def setSubPartitionBy(subPartitionBy: SQLSubPartitionBy): Unit = {
    if (subPartitionBy != null) subPartitionBy.setParent(this)
    this.subPartitionBy = subPartitionBy
  }

  def getPartitionsCount: SQLExpr = partitionsCount

  def setPartitionsCount(partitionsCount: SQLExpr): Unit = {
    if (partitionsCount != null) partitionsCount.setParent(this)
    this.partitionsCount = partitionsCount
  }

  def isLinear: Boolean = linear

  def setLinear(linear: Boolean): Unit = {
    this.linear = linear
  }

  def getStoreIn: util.List[SQLName] = storeIn

  def getColumns: util.List[SQLExpr] = columns

  def addColumn(column: SQLExpr): Unit = {
    if (column != null) column.setParent(this)
    this.columns.add(column)
  }

  def cloneTo(x: SQLPartitionBy): Unit = {
    if (subPartitionBy != null) x.setSubPartitionBy(subPartitionBy.clone)
    if (partitionsCount != null) x.setPartitionsCount(partitionsCount.clone)
    x.linear = linear
    import scala.collection.JavaConversions._
    for (p <- partitions) {
      val p2: SQLPartition = p.clone
      p2.setParent(x)
      x.partitions.add(p2)
    }
    import scala.collection.JavaConversions._
    for (name <- storeIn) {
      val name2: SQLName = name.clone
      name2.setParent(x)
      x.storeIn.add(name2)
    }
  }

  override def clone: SQLPartitionBy
}

