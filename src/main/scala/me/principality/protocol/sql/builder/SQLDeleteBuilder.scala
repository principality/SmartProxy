package me.principality.protocol.sql.builder

import me.principality.protocol.sql.util.JdbcConstants

trait SQLDeleteBuilder {
  def from(table: String): SQLDeleteBuilder

  def from(table: String, alias: String): SQLDeleteBuilder

  def limit(rowCount: Int): SQLDeleteBuilder

  def limit(rowCount: Int, offset: Int): SQLDeleteBuilder

  def where(sql: String): SQLDeleteBuilder

  def whereAnd(sql: String): SQLDeleteBuilder

  def whereOr(sql: String): SQLDeleteBuilder
}

class SQLDeleteBuilderImpl extends SQLDeleteBuilder {
  private var stmt = null
  private var dbType = null

  def this(dbType: String) {
    this()
    this.dbType = dbType
  }

  def this(sql: String, dbType: String)

  def this(stmt: SQLDeleteStatement, dbType: String) {
    this()
    this.stmt = stmt
    this.dbType = dbType
  }

  override def limit(rowCount: Int) = throw new UnsupportedOperationException

  override def limit(rowCount: Int, offset: Int) = throw new UnsupportedOperationException

  override def from(table: String): SQLDeleteBuilder = from(table, null)

  override def from(table: String, alias: String): SQLDeleteBuilder = {
    val delete = getSQLDeleteStatement
    val from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias)
    delete.setTableSource(from)
    this
  }

  override def where(expr: String): SQLDeleteBuilder = {
    val delete = getSQLDeleteStatement
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    delete.setWhere(exprObj)
    this
  }

  override def whereAnd(expr: String): SQLDeleteBuilder = {
    val delete = getSQLDeleteStatement
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    val newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, delete.getWhere)
    delete.setWhere(newCondition)
    this
  }

  override def whereOr(expr: String): SQLDeleteBuilder = {
    val delete = getSQLDeleteStatement
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    val newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, delete.getWhere)
    delete.setWhere(newCondition)
    this
  }

  def getSQLDeleteStatement: SQLDeleteStatement = {
    if (stmt == null) stmt = createSQLDeleteStatement
    stmt
  }

  def createSQLDeleteStatement: SQLDeleteStatement = {
    if (JdbcConstants.ORACLE == dbType) return new OracleDeleteStatement
    if (JdbcConstants.MYSQL == dbType) return new MySqlDeleteStatement
    if (JdbcConstants.POSTGRESQL == dbType) return new PGDeleteStatement
    new SQLDeleteStatement
  }

  override def toString: String = SQLUtils.toSQLString(stmt, dbType)
}
