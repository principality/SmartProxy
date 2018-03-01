package me.principality.protocol.sql.builder

import java.util

import me.principality.protocol.sql.util.JdbcConstants

trait SQLUpdateBuilder {
  def from(table: String): SQLUpdateBuilder

  def from(table: String, alias: String): SQLUpdateBuilder

  def limit(rowCount: Int): SQLUpdateBuilder

  def limit(rowCount: Int, offset: Int): SQLUpdateBuilder

  def where(sql: String): SQLUpdateBuilder

  def whereAnd(sql: String): SQLUpdateBuilder

  def whereOr(sql: String): SQLUpdateBuilder

  def set(items: String*): SQLUpdateBuilder
}

class SQLUpdateBuilderImpl extends SQLBuilderImpl with SQLUpdateBuilder {
  private var stmt: SQLUpdateStatement = null
  private var dbType: String = null

  def this(dbType: String) {
    this()
    this.dbType = dbType
  }

  def this(sql: String, dbType: String)

  def this(stmt: SQLUpdateStatement, dbType: String) {
    this()
    this.stmt = stmt
    this.dbType = dbType
  }

  override def limit(rowCount: Int): SQLUpdateBuilderImpl = throw new UnsupportedOperationException

  override def limit(rowCount: Int, offset: Int): SQLUpdateBuilderImpl = throw new UnsupportedOperationException

  override def from(table: String): SQLUpdateBuilderImpl = from(table, null)

  override def from(table: String, alias: String): SQLUpdateBuilderImpl = {
    val update: SQLUpdateStatement = getSQLUpdateStatement
    val from: SQLExprTableSource = new SQLExprTableSource(new SQLIdentifierExpr(table), alias)
    update.setTableSource(from)
    this
  }

  override def where(expr: String): SQLUpdateBuilderImpl = {
    val update: SQLUpdateStatement = getSQLUpdateStatement
    val exprObj: SQLExpr = SQLUtils.toSQLExpr(expr, dbType)
    update.setWhere(exprObj)
    this
  }

  override def whereAnd(expr: String): SQLUpdateBuilderImpl = {
    val update: SQLUpdateStatement = getSQLUpdateStatement
    val exprObj: SQLExpr = SQLUtils.toSQLExpr(expr, dbType)
    val newCondition: SQLExpr = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, update.getWhere)
    update.setWhere(newCondition)
    this
  }

  override def whereOr(expr: String): SQLUpdateBuilderImpl = {
    val update: SQLUpdateStatement = getSQLUpdateStatement
    val exprObj: SQLExpr = SQLUtils.toSQLExpr(expr, dbType)
    val newCondition: SQLExpr = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, update.getWhere)
    update.setWhere(newCondition)
    this
  }

  override def set(items: String*): SQLUpdateBuilderImpl = {
    val update: SQLUpdateStatement = getSQLUpdateStatement
    for (item <- items) {
      val updateSetItem: SQLUpdateSetItem = SQLUtils.toUpdateSetItem(item, dbType)
      update.addItem(updateSetItem)
    }
    this
  }

  def setValue(values: util.Map[String, AnyRef]): SQLUpdateBuilderImpl = {
    import scala.collection.JavaConversions._
    for (entry <- values.entrySet) {
      setValue(entry.getKey, entry.getValue)
    }
    this
  }

  def setValue(column: String, value: Any): SQLUpdateBuilderImpl = {
    val update: SQLUpdateStatement = getSQLUpdateStatement
    val columnExpr: SQLExpr = SQLUtils.toSQLExpr(column, dbType)
    val valueExpr: SQLExpr = toSQLExpr(value, dbType)
    val item: SQLUpdateSetItem = new SQLUpdateSetItem
    item.setColumn(columnExpr)
    item.setValue(valueExpr)
    update.addItem(item)
    this
  }

  def getSQLUpdateStatement: SQLUpdateStatement = {
    if (stmt == null) stmt = createSQLUpdateStatement
    stmt
  }

  def createSQLUpdateStatement: SQLUpdateStatement = {
    if (JdbcConstants.MYSQL == dbType) return new MySqlUpdateStatement
    if (JdbcConstants.ORACLE == dbType) return new OracleUpdateStatement
    if (JdbcConstants.POSTGRESQL == dbType) return new PGUpdateStatement
    if (JdbcConstants.SQL_SERVER == dbType) return new SQLServerUpdateStatement
    new SQLUpdateStatement
  }

  override def toString: String = SQLUtils.toSQLString(stmt, dbType)
}
