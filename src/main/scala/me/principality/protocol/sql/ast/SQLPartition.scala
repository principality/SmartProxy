package me.principality.protocol.sql.ast

import java.util
import java.util.{ArrayList, List}

class SQLPartition extends OracleSegmentAttributesImpl with OracleSegmentAttributes {
  protected var name: SQLName = null
  protected var subPartitionsCount: SQLExpr = null
  protected var subPartitions: util.List[SQLSubPartition] = new util.ArrayList[SQLSubPartition]
  protected var values: SQLPartitionValue = null
  // for mysql
  protected var dataDirectory: SQLExpr = null
  protected var indexDirectory: SQLExpr = null
  protected var maxRows: SQLExpr = null
  protected var minRows: SQLExpr = null
  protected var engine: SQLExpr = null
  protected var comment: SQLExpr = null
  // for oracle
  protected var segmentCreationImmediate: Boolean = false
  protected var segmentCreationDeferred: Boolean = false
  private var lobStorage: SQLObject = null

  def getName: SQLName = name

  def setName(name: SQLName): Unit = {
    if (name != null) name.setParent(this)
    this.name = name
  }

  def getSubPartitionsCount: SQLExpr = subPartitionsCount

  def setSubPartitionsCount(subPartitionsCount: SQLExpr): Unit = {
    if (subPartitionsCount != null) subPartitionsCount.setParent(this)
    this.subPartitionsCount = subPartitionsCount
  }

  def getValues: SQLPartitionValue = values

  def setValues(values: SQLPartitionValue): Unit = {
    if (values != null) values.setParent(this)
    this.values = values
  }

  def getSubPartitions: util.List[SQLSubPartition] = subPartitions

  def addSubPartition(partition: SQLSubPartition): Unit = {
    if (partition != null) partition.setParent(this)
    this.subPartitions.add(partition)
  }

  def getIndexDirectory: SQLExpr = indexDirectory

  def setIndexDirectory(indexDirectory: SQLExpr): Unit = {
    if (indexDirectory != null) indexDirectory.setParent(this)
    this.indexDirectory = indexDirectory
  }

  def getDataDirectory: SQLExpr = dataDirectory

  def setDataDirectory(dataDirectory: SQLExpr): Unit = {
    if (dataDirectory != null) dataDirectory.setParent(this)
    this.dataDirectory = dataDirectory
  }

  def getMaxRows: SQLExpr = maxRows

  def setMaxRows(maxRows: SQLExpr): Unit = {
    if (maxRows != null) maxRows.setParent(this)
    this.maxRows = maxRows
  }

  def getMinRows: SQLExpr = minRows

  def setMinRows(minRows: SQLExpr): Unit = {
    if (minRows != null) minRows.setParent(this)
    this.minRows = minRows
  }

  def getEngine: SQLExpr = engine

  def setEngine(engine: SQLExpr): Unit = {
    if (engine != null) engine.setParent(this)
    this.engine = engine
  }

  def getComment: SQLExpr = comment

  def setComment(comment: SQLExpr): Unit = {
    if (comment != null) comment.setParent(this)
    this.comment = comment
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    if (visitor.visit(this)) {
      acceptChild(visitor, name)
      acceptChild(visitor, values)
      acceptChild(visitor, dataDirectory)
      acceptChild(visitor, indexDirectory)
      acceptChild(visitor, tablespace)
      acceptChild(visitor, maxRows)
      acceptChild(visitor, minRows)
      acceptChild(visitor, engine)
      acceptChild(visitor, comment)
      acceptChild(visitor, storage)
    }
    visitor.endVisit(this)
  }

  def getLobStorage: SQLObject = lobStorage

  def setLobStorage(lobStorage: SQLObject): Unit = {
    if (lobStorage != null) lobStorage.setParent(this)
    this.lobStorage = lobStorage
  }

  def isSegmentCreationImmediate: Boolean = segmentCreationImmediate

  def setSegmentCreationImmediate(segmentCreationImmediate: Boolean): Unit = {
    this.segmentCreationImmediate = segmentCreationImmediate
  }

  def isSegmentCreationDeferred: Boolean = segmentCreationDeferred

  def setSegmentCreationDeferred(segmentCreationDeferred: Boolean): Unit = {
    this.segmentCreationDeferred = segmentCreationDeferred
  }

  override def clone: SQLPartition = {
    val x: SQLPartition = new SQLPartition
    if (name != null) x.setName(name.clone)
    if (subPartitionsCount != null) x.setSubPartitionsCount(subPartitionsCount.clone)
    import scala.collection.JavaConversions._
    for (p <- subPartitions) {
      val p2: SQLSubPartition = p.clone
      p2.setParent(x)
      x.subPartitions.add(p2)
    }
    if (values != null) x.setValues(values.clone)
    if (dataDirectory != null) x.setDataDirectory(dataDirectory.clone)
    if (indexDirectory != null) x.setDataDirectory(indexDirectory.clone)
    if (maxRows != null) x.setDataDirectory(maxRows.clone)
    if (minRows != null) x.setDataDirectory(minRows.clone)
    if (engine != null) x.setDataDirectory(engine.clone)
    if (comment != null) x.setDataDirectory(comment.clone)
    x.segmentCreationImmediate = segmentCreationImmediate
    x.segmentCreationDeferred = segmentCreationDeferred
    if (lobStorage != null) x.setLobStorage(lobStorage.clone)
    x
  }
}
