package me.principality.protocol.sql.util

import java.util
import java.util.List

object PagerUtils {
  def count(sql: String, dbType: String): String = {
    val stmtList: util.List[SQLStatement] = SQLUtils.parseStatements(sql, dbType)
    if (stmtList.size != 1) throw new IllegalArgumentException("sql not support count : " + sql)
    val stmt: SQLStatement = stmtList.get(0)
    if (!stmt.isInstanceOf[SQLSelectStatement]) throw new IllegalArgumentException("sql not support count : " + sql)
    val selectStmt: SQLSelectStatement = stmt.asInstanceOf[SQLSelectStatement]
    count(selectStmt.getSelect, dbType)
  }

  def limit(sql: String, dbType: String, offset: Int, count: Int): String = {
    val stmtList: util.List[SQLStatement] = SQLUtils.parseStatements(sql, dbType)
    if (stmtList.size != 1) throw new IllegalArgumentException("sql not support count : " + sql)
    val stmt: SQLStatement = stmtList.get(0)
    if (!stmt.isInstanceOf[SQLSelectStatement]) throw new IllegalArgumentException("sql not support count : " + sql)
    val selectStmt: SQLSelectStatement = stmt.asInstanceOf[SQLSelectStatement]
    limit(selectStmt.getSelect, dbType, offset, count)
  }

  def limit(select: SQLSelect, dbType: String, offset: Int, count: Int): String = {
    limit(select, dbType, offset, count, false)
    SQLUtils.toSQLString(select, dbType)
  }

  def limit(select: SQLSelect, dbType: String, offset: Int, count: Int, check: Boolean): Boolean = {
    val query: SQLSelectQuery = select.getQuery
    if (JdbcConstants.ORACLE == dbType) return limitOracle(select, dbType, offset, count, check)
    if (JdbcConstants.DB2 == dbType) return limitDB2(select, dbType, offset, count, check)
    if (JdbcConstants.SQL_SERVER == dbType || JdbcUtils.JTDS == dbType) return limitSQLServer(select, dbType, offset, count, check)
    if (query.isInstanceOf[SQLSelectQueryBlock]) return limitQueryBlock(select, dbType, offset, count, check)
    throw new UnsupportedOperationException
  }

  private def limitQueryBlock(select: SQLSelect, dbType: String, offset: Int, count: Int, check: Boolean): Boolean = {
    val queryBlock: SQLSelectQueryBlock = select.getQuery.asInstanceOf[SQLSelectQueryBlock]
    if (JdbcConstants.MYSQL == dbType || //
      JdbcConstants.MARIADB == dbType || JdbcConstants.H2 == dbType) return limitMySqlQueryBlock(queryBlock, dbType, offset, count, check)
    if (JdbcConstants.POSTGRESQL == dbType) return limitPostgreSQLQueryBlock(queryBlock.asInstanceOf[PGSelectQueryBlock], dbType, offset, count, check)
    throw new UnsupportedOperationException
  }

  private def limitPostgreSQLQueryBlock(queryBlock: PGSelectQueryBlock, dbType: String, offset: Int, count: Int, check: Boolean): Boolean = {
    var limit: SQLLimit = queryBlock.getLimit
    if (limit != null) {
      if (offset > 0) limit.setOffset(new SQLIntegerExpr(offset))
      if (check && limit.getRowCount.isInstanceOf[SQLNumericLiteralExpr]) {
        val rowCount: Int = limit.getRowCount.asInstanceOf[SQLNumericLiteralExpr].getNumber.intValue
        if (rowCount <= count && offset <= 0) return false
      }
      limit.setRowCount(new SQLIntegerExpr(count))
    }
    limit = new SQLLimit
    if (offset > 0) limit.setOffset(new SQLIntegerExpr(offset))
    limit.setRowCount(new SQLIntegerExpr(count))
    queryBlock.setLimit(limit)
    true
  }

  private def limitDB2(select: SQLSelect, dbType: String, offset: Int, count: Int, check: Boolean): Boolean = {
    val query: SQLSelectQuery = select.getQuery
    val gt: SQLBinaryOpExpr = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.GreaterThan, new SQLNumberExpr(offset), JdbcConstants.DB2)
    val lteq: SQLBinaryOpExpr = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count + offset), JdbcConstants.DB2)
    val pageCondition: SQLBinaryOpExpr = new SQLBinaryOpExpr(gt, SQLBinaryOperator.BooleanAnd, lteq, JdbcConstants.DB2)
    if (query.isInstanceOf[SQLSelectQueryBlock]) {
      val queryBlock: DB2SelectQueryBlock = query.asInstanceOf[DB2SelectQueryBlock]
      if (offset <= 0) {
        val first: SQLExpr = queryBlock.getFirst
        if (check && first != null && first.isInstanceOf[SQLNumericLiteralExpr]) {
          val rowCount: Int = first.asInstanceOf[SQLNumericLiteralExpr].getNumber.intValue
          if (rowCount < count) return false
        }
        queryBlock.setFirst(new SQLIntegerExpr(count))
        return true
      }
      val aggregateExpr: SQLAggregateExpr = new SQLAggregateExpr("ROW_NUMBER")
      var orderBy: SQLOrderBy = select.getOrderBy
      if (orderBy == null && select.getQuery.isInstanceOf[SQLSelectQueryBlock]) {
        val selectQueryBlcok: SQLSelectQueryBlock = select.getQuery.asInstanceOf[SQLSelectQueryBlock]
        orderBy = selectQueryBlcok.getOrderBy
        selectQueryBlcok.setOrderBy(null)
      }
      else select.setOrderBy(null)
      aggregateExpr.setOver(new SQLOver(orderBy))
      queryBlock.getSelectList.add(new SQLSelectItem(aggregateExpr, "ROWNUM"))
      val countQueryBlock: DB2SelectQueryBlock = new DB2SelectQueryBlock
      countQueryBlock.getSelectList.add(new SQLSelectItem(new SQLAllColumnExpr))
      countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone, "XX"))
      countQueryBlock.setWhere(pageCondition)
      select.setQuery(countQueryBlock)
      return true
    }
    val countQueryBlock: DB2SelectQueryBlock = new DB2SelectQueryBlock
    countQueryBlock.getSelectList.add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")))
    val aggregateExpr: SQLAggregateExpr = new SQLAggregateExpr("ROW_NUMBER")
    val orderBy: SQLOrderBy = select.getOrderBy
    aggregateExpr.setOver(new SQLOver(orderBy))
    select.setOrderBy(null)
    countQueryBlock.getSelectList.add(new SQLSelectItem(aggregateExpr, "ROWNUM"))
    countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone, "XX"))
    if (offset <= 0) {
      select.setQuery(countQueryBlock)
      return true
    }
    val offsetQueryBlock: DB2SelectQueryBlock = new DB2SelectQueryBlock
    offsetQueryBlock.getSelectList.add(new SQLSelectItem(new SQLAllColumnExpr))
    offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(countQueryBlock), "XXX"))
    offsetQueryBlock.setWhere(pageCondition)
    select.setQuery(offsetQueryBlock)
    true
  }

  private def limitSQLServer(select: SQLSelect, dbType: String, offset: Int, count: Int, check: Boolean): Boolean = {
    val query: SQLSelectQuery = select.getQuery
    val gt: SQLBinaryOpExpr = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.GreaterThan, new SQLNumberExpr(offset), JdbcConstants.SQL_SERVER)
    val lteq: SQLBinaryOpExpr = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count + offset), JdbcConstants.SQL_SERVER)
    val pageCondition: SQLBinaryOpExpr = new SQLBinaryOpExpr(gt, SQLBinaryOperator.BooleanAnd, lteq, JdbcConstants.SQL_SERVER)
    if (query.isInstanceOf[SQLSelectQueryBlock]) {
      val queryBlock: SQLServerSelectQueryBlock = query.asInstanceOf[SQLServerSelectQueryBlock]
      if (offset <= 0) {
        val top: SQLServerTop = queryBlock.getTop
        if (check && top != null && !top.isPercent && top.getExpr.isInstanceOf[SQLNumericLiteralExpr]) {
          val rowCount: Int = top.getExpr.asInstanceOf[SQLNumericLiteralExpr].getNumber.intValue
          if (rowCount <= count) return false
        }
        queryBlock.setTop(new SQLServerTop(new SQLNumberExpr(count)))
        return true
      }
      val aggregateExpr: SQLAggregateExpr = new SQLAggregateExpr("ROW_NUMBER")
      val orderBy: SQLOrderBy = select.getOrderBy
      aggregateExpr.setOver(new SQLOver(orderBy))
      select.setOrderBy(null)
      queryBlock.getSelectList.add(new SQLSelectItem(aggregateExpr, "ROWNUM"))
      val countQueryBlock: SQLServerSelectQueryBlock = new SQLServerSelectQueryBlock
      countQueryBlock.getSelectList.add(new SQLSelectItem(new SQLAllColumnExpr))
      countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone, "XX"))
      countQueryBlock.setWhere(pageCondition)
      select.setQuery(countQueryBlock)
      return true
    }
    val countQueryBlock: SQLServerSelectQueryBlock = new SQLServerSelectQueryBlock
    countQueryBlock.getSelectList.add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")))
    countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone, "XX"))
    if (offset <= 0) {
      countQueryBlock.setTop(new SQLServerTop(new SQLNumberExpr(count)))
      select.setQuery(countQueryBlock)
      return true
    }
    val aggregateExpr: SQLAggregateExpr = new SQLAggregateExpr("ROW_NUMBER")
    val orderBy: SQLOrderBy = select.getOrderBy
    aggregateExpr.setOver(new SQLOver(orderBy))
    select.setOrderBy(null)
    countQueryBlock.getSelectList.add(new SQLSelectItem(aggregateExpr, "ROWNUM"))
    val offsetQueryBlock: SQLServerSelectQueryBlock = new SQLServerSelectQueryBlock
    offsetQueryBlock.getSelectList.add(new SQLSelectItem(new SQLAllColumnExpr))
    offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(countQueryBlock), "XXX"))
    offsetQueryBlock.setWhere(pageCondition)
    select.setQuery(offsetQueryBlock)
    true
  }

  private def limitOracle(select: SQLSelect, dbType: String, offset: Int, count: Int, check: Boolean): Boolean = {
    val query: SQLSelectQuery = select.getQuery
    if (query.isInstanceOf[SQLSelectQueryBlock]) {
      val queryBlock: OracleSelectQueryBlock = query.asInstanceOf[OracleSelectQueryBlock]
      var orderBy: SQLOrderBy = select.getOrderBy
      if (orderBy == null && queryBlock.getOrderBy != null) orderBy = queryBlock.getOrderBy
      if (queryBlock.getGroupBy == null && orderBy == null && offset <= 0) {
        val where: SQLExpr = queryBlock.getWhere
        if (check && where.isInstanceOf[SQLBinaryOpExpr]) {
          val binaryOpWhere: SQLBinaryOpExpr = where.asInstanceOf[SQLBinaryOpExpr]
          if (binaryOpWhere.getOperator eq SQLBinaryOperator.LessThanOrEqual) {
            val left: SQLExpr = binaryOpWhere.getLeft
            val right: SQLExpr = binaryOpWhere.getRight
            if (left.isInstanceOf[SQLIdentifierExpr] && left.asInstanceOf[SQLIdentifierExpr].getName.equalsIgnoreCase("ROWNUM") && right.isInstanceOf[SQLNumericLiteralExpr]) {
              val rowCount: Int = right.asInstanceOf[SQLNumericLiteralExpr].getNumber.intValue
              if (rowCount <= count) return false
            }
          }
        }
        val condition: SQLExpr = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count), JdbcConstants.ORACLE)
        if (queryBlock.getWhere == null) queryBlock.setWhere(condition)
        else queryBlock.setWhere(new SQLBinaryOpExpr(queryBlock.getWhere, SQLBinaryOperator.BooleanAnd, condition, JdbcConstants.ORACLE))
        return true
      }
    }
    val countQueryBlock: OracleSelectQueryBlock = new OracleSelectQueryBlock
    countQueryBlock.getSelectList.add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")))
    countQueryBlock.getSelectList.add(new SQLSelectItem(new SQLIdentifierExpr("ROWNUM"), "RN"))
    countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone, "XX"))
    countQueryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), SQLBinaryOperator.LessThanOrEqual, new SQLNumberExpr(count + offset), JdbcConstants.ORACLE))
    select.setOrderBy(null)
    if (offset <= 0) {
      select.setQuery(countQueryBlock)
      return true
    }
    val offsetQueryBlock: OracleSelectQueryBlock = new OracleSelectQueryBlock
    offsetQueryBlock.getSelectList.add(new SQLSelectItem(new SQLAllColumnExpr))
    offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(countQueryBlock), "XXX"))
    offsetQueryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("RN"), SQLBinaryOperator.GreaterThan, new SQLNumberExpr(offset), JdbcConstants.ORACLE))
    select.setQuery(offsetQueryBlock)
    true
  }

  private def limitMySqlQueryBlock(queryBlock: SQLSelectQueryBlock, dbType: String, offset: Int, count: Int, check: Boolean): Boolean = {
    var limit: SQLLimit = queryBlock.getLimit
    if (limit != null) {
      if (offset > 0) limit.setOffset(new SQLIntegerExpr(offset))
      if (check && limit.getRowCount.isInstanceOf[SQLNumericLiteralExpr]) {
        val rowCount: Int = limit.getRowCount.asInstanceOf[SQLNumericLiteralExpr].getNumber.intValue
        if (rowCount <= count && offset <= 0) return false
      }
      else if (check && limit.getRowCount.isInstanceOf[SQLVariantRefExpr]) return false
      limit.setRowCount(new SQLIntegerExpr(count))
    }
    if (limit == null) {
      limit = new SQLLimit
      if (offset > 0) limit.setOffset(new SQLIntegerExpr(offset))
      limit.setRowCount(new SQLIntegerExpr(count))
      queryBlock.setLimit(limit)
    }
    true
  }

  private def count(select: SQLSelect, dbType: String): String = {
    if (select.getOrderBy != null) select.setOrderBy(null)
    val query: SQLSelectQuery = select.getQuery
    clearOrderBy(query)
    if (query.isInstanceOf[SQLSelectQueryBlock]) {
      val countItem: SQLSelectItem = createCountItem(dbType)
      val queryBlock: SQLSelectQueryBlock = query.asInstanceOf[SQLSelectQueryBlock]
      val selectList: util.List[SQLSelectItem] = queryBlock.getSelectList
      if (queryBlock.getGroupBy != null && queryBlock.getGroupBy.getItems.size > 0) return createCountUseSubQuery(select, dbType)
      val option: Int = queryBlock.getDistionOption
      if (option == SQLSetQuantifier.DISTINCT && selectList.size >= 1) {
        val countExpr: SQLAggregateExpr = new SQLAggregateExpr("COUNT", SQLAggregateOption.DISTINCT)
        var i: Int = 0
        while ( {
          i < selectList.size
        }) {
          countExpr.addArgument(selectList.get(i).getExpr)

          {
            i += 1; i
          }
        }
        selectList.clear()
        queryBlock.setDistionOption(0)
        queryBlock.addSelectItem(countExpr)
      }
      else {
        selectList.clear()
        selectList.add(countItem)
      }
      return SQLUtils.toSQLString(select, dbType)
    }
    else if (query.isInstanceOf[SQLUnionQuery]) return createCountUseSubQuery(select, dbType)
    throw new IllegalStateException
  }

  private def createCountUseSubQuery(select: SQLSelect, dbType: String): String = {
    val countSelectQuery: SQLSelectQueryBlock = createQueryBlock(dbType)
    val countItem: SQLSelectItem = createCountItem(dbType)
    countSelectQuery.getSelectList.add(countItem)
    val fromSubquery: SQLSubqueryTableSource = new SQLSubqueryTableSource(select)
    fromSubquery.setAlias("ALIAS_COUNT")
    countSelectQuery.setFrom(fromSubquery)
    val countSelect: SQLSelect = new SQLSelect(countSelectQuery)
    val countStmt: SQLSelectStatement = new SQLSelectStatement(countSelect, dbType)
    SQLUtils.toSQLString(countStmt, dbType)
  }

  private def createQueryBlock(dbType: String): SQLSelectQueryBlock = {
    if (JdbcConstants.MYSQL == dbType || JdbcConstants.MARIADB == dbType || JdbcConstants.ALIYUN_ADS == dbType) return new MySqlSelectQueryBlock
    if (JdbcConstants.MARIADB == dbType) return new MySqlSelectQueryBlock
    if (JdbcConstants.H2 == dbType) return new MySqlSelectQueryBlock
    if (JdbcConstants.ORACLE == dbType) return new OracleSelectQueryBlock
    if (JdbcConstants.POSTGRESQL == dbType) return new PGSelectQueryBlock
    if (JdbcConstants.SQL_SERVER == dbType || JdbcUtils.JTDS == dbType) return new SQLServerSelectQueryBlock
    if (JdbcConstants.DB2 == dbType) return new DB2SelectQueryBlock
    new SQLSelectQueryBlock
  }

  private def createCountItem(dbType: String): SQLSelectItem = {
    val countExpr: SQLAggregateExpr = new SQLAggregateExpr("COUNT")
    countExpr.addArgument(new SQLAllColumnExpr)
    val countItem: SQLSelectItem = new SQLSelectItem(countExpr)
    countItem
  }

  private def clearOrderBy(query: SQLSelectQuery): Unit = {
    if (query.isInstanceOf[SQLSelectQueryBlock]) {
      val queryBlock: SQLSelectQueryBlock = query.asInstanceOf[SQLSelectQueryBlock]
      if (queryBlock.getOrderBy != null) queryBlock.setOrderBy(null)
      return
    }
    if (query.isInstanceOf[SQLUnionQuery]) {
      val union: SQLUnionQuery = query.asInstanceOf[SQLUnionQuery]
      if (union.getOrderBy != null) union.setOrderBy(null)
      clearOrderBy(union.getLeft)
      clearOrderBy(union.getRight)
    }
  }

  /**
    *
    * @param sql
    * @param dbType
    * @return if not exists limit, return -1;
    */
  def getLimit(sql: String, dbType: String): Int = {
    val stmtList: util.List[SQLStatement] = SQLUtils.parseStatements(sql, dbType)
    if (stmtList.size != 1) return -1
    val stmt: SQLStatement = stmtList.get(0)
    if (stmt.isInstanceOf[SQLSelectStatement]) {
      val selectStmt: SQLSelectStatement = stmt.asInstanceOf[SQLSelectStatement]
      val query: SQLSelectQuery = selectStmt.getSelect.getQuery
      if (query.isInstanceOf[SQLSelectQueryBlock]) {
        if (query.isInstanceOf[MySqlSelectQueryBlock]) {
          val limit: SQLLimit = query.asInstanceOf[MySqlSelectQueryBlock].getLimit
          if (limit == null) return -1
          val rowCountExpr: SQLExpr = limit.getRowCount
          if (rowCountExpr.isInstanceOf[SQLNumericLiteralExpr]) {
            val rowCount: Int = rowCountExpr.asInstanceOf[SQLNumericLiteralExpr].getNumber.intValue
            return rowCount
          }
          return Integer.MAX_VALUE
        }
        if (query.isInstanceOf[OdpsSelectQueryBlock]) {
          val limit: SQLLimit = query.asInstanceOf[OdpsSelectQueryBlock].getLimit
          val rowCountExpr: SQLExpr = if (limit != null) limit.getRowCount
          else null
          if (rowCountExpr.isInstanceOf[SQLNumericLiteralExpr]) {
            val rowCount: Int = rowCountExpr.asInstanceOf[SQLNumericLiteralExpr].getNumber.intValue
            return rowCount
          }
          return Integer.MAX_VALUE
        }
        return -1
      }
    }
    -1
  }

  def hasUnorderedLimit(sql: String, dbType: String): Boolean = {
    val stmtList: util.List[SQLStatement] = SQLUtils.parseStatements(sql, dbType)
    if (JdbcConstants.MYSQL == dbType) {
      val visitor: PagerUtils.MySqlUnorderedLimitDetectVisitor = new PagerUtils.MySqlUnorderedLimitDetectVisitor
      import scala.collection.JavaConversions._
      for (stmt <- stmtList) {
        stmt.accept(visitor)
      }
      return visitor.unorderedLimitCount > 0
    }
    if (JdbcConstants.ORACLE == dbType) {
      val visitor: PagerUtils.OracleUnorderedLimitDetectVisitor = new PagerUtils.OracleUnorderedLimitDetectVisitor
      import scala.collection.JavaConversions._
      for (stmt <- stmtList) {
        stmt.accept(visitor)
      }
      return visitor.unorderedLimitCount > 0
    }
    throw new DruidRuntimeException("not supported. dbType : " + dbType)
  }

  private class MySqlUnorderedLimitDetectVisitor extends MySqlASTVisitorAdapter {
    var unorderedLimitCount: Int = 0

    override def visit(x: MySqlSelectQueryBlock): Boolean = {
      val orderBy: SQLOrderBy = x.getOrderBy
      val limit: SQLLimit = x.getLimit
      if (limit != null && (orderBy == null || orderBy.getItems.size == 0)) {
        var subQueryHasOrderBy: Boolean = false
        val from: SQLTableSource = x.getFrom
        if (from.isInstanceOf[SQLSubqueryTableSource]) {
          val subqueryTabSrc: SQLSubqueryTableSource = from.asInstanceOf[SQLSubqueryTableSource]
          val select: SQLSelect = subqueryTabSrc.getSelect
          if (select.getQuery.isInstanceOf[SQLSelectQueryBlock]) {
            val subquery: SQLSelectQueryBlock = select.getQuery.asInstanceOf[SQLSelectQueryBlock]
            if (subquery.getOrderBy != null && subquery.getOrderBy.getItems.size > 0) subQueryHasOrderBy = true
          }
        }
        if (!subQueryHasOrderBy) unorderedLimitCount += 1
      }
      true
    }
  }

  private class OracleUnorderedLimitDetectVisitor extends OracleASTVisitorAdapter {
    var unorderedLimitCount: Int = 0

    override def visit(x: SQLBinaryOpExpr): Boolean = {
      val left: SQLExpr = x.getLeft
      val right: SQLExpr = x.getRight
      var rownum: Boolean = false
      if (left.isInstanceOf[SQLIdentifierExpr] && left.asInstanceOf[SQLIdentifierExpr].getName.equalsIgnoreCase("ROWNUM") && right.isInstanceOf[SQLLiteralExpr]) rownum = true
      else if (right.isInstanceOf[SQLIdentifierExpr] && right.asInstanceOf[SQLIdentifierExpr].getName.equalsIgnoreCase("ROWNUM") && left.isInstanceOf[SQLLiteralExpr]) rownum = true
      var selectQuery: OracleSelectQueryBlock = null
      if (rownum) {
        var parent: SQLObject = x.getParent
        while ( {
          parent != null
        }) {
          if (parent.isInstanceOf[SQLSelectQuery]) {
            if (parent.isInstanceOf[OracleSelectQueryBlock]) {
              val queryBlock: OracleSelectQueryBlock = parent.asInstanceOf[OracleSelectQueryBlock]
              val from: SQLTableSource = queryBlock.getFrom
              if (from.isInstanceOf[SQLExprTableSource]) selectQuery = queryBlock
              else if (from.isInstanceOf[SQLSubqueryTableSource]) {
                val subSelect: SQLSelect = from.asInstanceOf[SQLSubqueryTableSource].getSelect
                if (subSelect.getQuery.isInstanceOf[OracleSelectQueryBlock]) selectQuery = subSelect.getQuery.asInstanceOf[OracleSelectQueryBlock]
              }
            }
            break //todo: break is not supported
          }

          parent = parent.getParent
        }
      }
      if (selectQuery != null) {
        var orderBy: SQLOrderBy = selectQuery.getOrderBy
        val parent: SQLObject = selectQuery.getParent
        if (orderBy == null && parent.isInstanceOf[SQLSelect]) {
          val select: SQLSelect = parent.asInstanceOf[SQLSelect]
          orderBy = select.getOrderBy
        }
        if (orderBy == null || orderBy.getItems.size == 0) unorderedLimitCount += 1
      }
      true
    }

    override def visit(queryBlock: OracleSelectQueryBlock): Boolean = {
      val isExprTableSrc: Boolean = queryBlock.getFrom.isInstanceOf[SQLExprTableSource]
      if (!isExprTableSrc) return true
      var rownum: Boolean = false
      import scala.collection.JavaConversions._
      for (item <- queryBlock.getSelectList) {
        val itemExpr: SQLExpr = item.getExpr
        if (itemExpr.isInstanceOf[SQLIdentifierExpr]) if (itemExpr.asInstanceOf[SQLIdentifierExpr].getName.equalsIgnoreCase("ROWNUM")) {
          rownum = true
          break //todo: break is not supported
        }
      }
      if (!rownum) return true
      val parent: SQLObject = queryBlock.getParent
      if (!parent.isInstanceOf[SQLSelect]) return true
      val select: SQLSelect = parent.asInstanceOf[SQLSelect]
      if (select.getOrderBy == null || select.getOrderBy.getItems.size == 0) unorderedLimitCount += 1
      false
    }
  }

}
