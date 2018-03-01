package me.principality.protocol.sql.builder

import me.principality.protocol.sql.util.JdbcConstants

trait SQLSelectBuilder {
  def getSQLSelectStatement: SQLSelectStatement

  def select(column: String*): SQLSelectBuilder

  def selectWithAlias(column: String, alias: String): SQLSelectBuilder

  def from(table: String): SQLSelectBuilder

  def from(table: String, alias: String): SQLSelectBuilder

  def orderBy(columns: String*): SQLSelectBuilder

  def groupBy(expr: String): SQLSelectBuilder

  def having(expr: String): SQLSelectBuilder

  def into(expr: String): SQLSelectBuilder

  def limit(rowCount: Int): SQLSelectBuilder

  def limit(rowCount: Int, offset: Int): SQLSelectBuilder

  def where(sql: String): SQLSelectBuilder

  def whereAnd(sql: String): SQLSelectBuilder

  def whereOr(sql: String): SQLSelectBuilder

  override def toString: String
}

class SQLSelectBuilderImpl extends SQLSelectBuilder {
  private var stmt = null
  private var dbType = null

  def this(dbType: String) {
    this(new SQLSelectStatement, dbType)
  }

  def this(sql: String, dbType: String)

  def this(stmt: SQLSelectStatement, dbType: String) {
    this()
    this.stmt = stmt
    this.dbType = dbType
  }

  def getSQLSelect: SQLSelect = {
    if (stmt.getSelect == null) stmt.setSelect(createSelect)
    stmt.getSelect
  }

  override def getSQLSelectStatement: SQLSelectStatement = stmt

  override def select(columns: String*): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    for (column <- columns) {
      val selectItem = SQLUtils.toSelectItem(column, dbType)
      queryBlock.addSelectItem(selectItem)
    }
    this
  }

  override def selectWithAlias(column: String, alias: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    val columnExpr = SQLUtils.toSQLExpr(column, dbType)
    val selectItem = new SQLSelectItem(columnExpr, alias)
    queryBlock.addSelectItem(selectItem)
    this
  }

  override def from(table: String): SQLSelectBuilderImpl = from(table, null)

  override def from(table: String, alias: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    val from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias)
    queryBlock.setFrom(from)
    this
  }

  override def orderBy(columns: String*): SQLSelectBuilderImpl = {
    val select = this.getSQLSelect
    var orderBy = select.getOrderBy
    if (orderBy == null) {
      orderBy = createOrderBy
      select.setOrderBy(orderBy)
    }
    for (column <- columns) {
      val orderByItem = SQLUtils.toOrderByItem(column, dbType)
      orderBy.addItem(orderByItem)
    }
    this
  }

  override def groupBy(expr: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    var groupBy = queryBlock.getGroupBy
    if (groupBy == null) {
      groupBy = createGroupBy
      queryBlock.setGroupBy(groupBy)
    }
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    groupBy.addItem(exprObj)
    this
  }

  override def having(expr: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    var groupBy = queryBlock.getGroupBy
    if (groupBy == null) {
      groupBy = createGroupBy
      queryBlock.setGroupBy(groupBy)
    }
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    groupBy.setHaving(exprObj)
    this
  }

  override def into(expr: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    queryBlock.setInto(exprObj)
    this
  }

  override def where(expr: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    queryBlock.setWhere(exprObj)
    this
  }

  override def whereAnd(expr: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    queryBlock.addWhere(SQLUtils.toSQLExpr(expr, dbType))
    this
  }

  override def whereOr(expr: String): SQLSelectBuilderImpl = {
    val queryBlock = getQueryBlock
    val exprObj = SQLUtils.toSQLExpr(expr, dbType)
    val newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, queryBlock.getWhere)
    queryBlock.setWhere(newCondition)
    this
  }

  override def limit(rowCount: Int): SQLSelectBuilderImpl = limit(rowCount, 0)

  override def limit(rowCount: Int, offset: Int): SQLSelectBuilderImpl = {
    getQueryBlock.limit(rowCount, offset)
    this
  }

  protected def getQueryBlock: SQLSelectQueryBlock = {
    val select = getSQLSelect
    var query = select.getQuery
    if (query == null) {
      query = createSelectQueryBlock
      select.setQuery(query)
    }
    if (!query.isInstanceOf[SQLSelectQueryBlock]) throw new IllegalStateException("not support from, class : " + query.getClass.getName)
    val queryBlock = query.asInstanceOf[SQLSelectQueryBlock]
    queryBlock
  }

  protected def createSelect = new SQLSelect

  protected def createSelectQueryBlock: SQLSelectQuery = {
    if (JdbcConstants.MYSQL == dbType) return new MySqlSelectQueryBlock
    if (JdbcConstants.POSTGRESQL == dbType) return new PGSelectQueryBlock
    if (JdbcConstants.SQL_SERVER == dbType) return new SQLServerSelectQueryBlock
    if (JdbcConstants.ORACLE == dbType) return new OracleSelectQueryBlock
    new SQLSelectQueryBlock
  }

  protected def createOrderBy = new SQLOrderBy

  protected def createGroupBy = new SQLSelectGroupByClause

  override def toString: String = SQLUtils.toSQLString(stmt, dbType)
}
