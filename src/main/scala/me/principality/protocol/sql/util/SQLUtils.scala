
package me.principality.protocol.sql.util

object SQLUtils {
  private val FORMAT_DEFAULT_FEATURES = Array(SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup)
  var DEFAULT_FORMAT_OPTION = new SQLUtils.FormatOption(true, true)
  var DEFAULT_LCASE_FORMAT_OPTION = new SQLUtils.FormatOption(false, true)
  private val LOG = LogFactory.getLog(classOf[SQLUtils])

  def toSQLString(sqlObject: SQLObject, dbType: String): String = toSQLString(sqlObject, dbType, null)

  def toSQLString(sqlObject: SQLObject, dbType: String, option: SQLUtils.FormatOption): String = {
    val out = new StringBuilder
    val visitor = createOutputVisitor(out, dbType)
    if (option == null) option = DEFAULT_FORMAT_OPTION
    visitor.setUppCase(option.isUppCase)
    visitor.setPrettyFormat(option.isPrettyFormat)
    visitor.setParameterized(option.isParameterized)
    visitor.setFeatures(option.features)
    sqlObject.accept(visitor)
    val sql = out.toString
    sql
  }

  def toSQLString(sqlObject: SQLObject): String = {
    val out = new StringBuilder
    sqlObject.accept(new SQLASTOutputVisitor(out))
    val sql = out.toString
    sql
  }

  def toOdpsString(sqlObject: SQLObject): String = toOdpsString(sqlObject, null)

  def toOdpsString(sqlObject: SQLObject, option: SQLUtils.FormatOption): String = toSQLString(sqlObject, JdbcConstants.ODPS, option)

  def toMySqlString(sqlObject: SQLObject): String = toMySqlString(sqlObject, null.asInstanceOf[SQLUtils.FormatOption])

  def toMySqlString(sqlObject: SQLObject, features: VisitorFeature*): String = toMySqlString(sqlObject, new SQLUtils.FormatOption(features))

  def toMySqlString(sqlObject: SQLObject, option: SQLUtils.FormatOption): String = toSQLString(sqlObject, JdbcConstants.MYSQL, option)

  def toMySqlExpr(sql: String): SQLExpr = toSQLExpr(sql, JdbcConstants.MYSQL)

  def formatMySql(sql: String): String = format(sql, JdbcConstants.MYSQL)

  def formatMySql(sql: String, option: SQLUtils.FormatOption): String = format(sql, JdbcConstants.MYSQL, option)

  def formatOracle(sql: String): String = format(sql, JdbcConstants.ORACLE)

  def formatOracle(sql: String, option: SQLUtils.FormatOption): String = format(sql, JdbcConstants.ORACLE, option)

  def formatOdps(sql: String): String = format(sql, JdbcConstants.ODPS)

  def formatHive(sql: String): String = format(sql, JdbcConstants.HIVE)

  def formatOdps(sql: String, option: SQLUtils.FormatOption): String = format(sql, JdbcConstants.ODPS, option)

  def formatHive(sql: String, option: SQLUtils.FormatOption): String = format(sql, JdbcConstants.HIVE, option)

  def formatSQLServer(sql: String): String = format(sql, JdbcConstants.SQL_SERVER)

  def toOracleString(sqlObject: SQLObject): String = toOracleString(sqlObject, null)

  def toOracleString(sqlObject: SQLObject, option: SQLUtils.FormatOption): String = toSQLString(sqlObject, JdbcConstants.ORACLE, option)

  def toPGString(sqlObject: SQLObject): String = toPGString(sqlObject, null)

  def toPGString(sqlObject: SQLObject, option: SQLUtils.FormatOption): String = toSQLString(sqlObject, JdbcConstants.POSTGRESQL, option)

  def toDB2String(sqlObject: SQLObject): String = toDB2String(sqlObject, null)

  def toDB2String(sqlObject: SQLObject, option: SQLUtils.FormatOption): String = toSQLString(sqlObject, JdbcConstants.DB2, option)

  def toSQLServerString(sqlObject: SQLObject): String = toSQLServerString(sqlObject, null)

  def toSQLServerString(sqlObject: SQLObject, option: SQLUtils.FormatOption): String = toSQLString(sqlObject, JdbcConstants.SQL_SERVER, option)

  def formatPGSql(sql: String, option: SQLUtils.FormatOption): String = format(sql, JdbcConstants.POSTGRESQL, option)

  def toSQLExpr(sql: String, dbType: String): SQLExpr = {
    val parser = SQLParserUtils.createExprParser(sql, dbType)
    val expr = parser.expr
    if (parser.getLexer.token ne Token.EOF) throw new ParserException("illegal sql expr : " + sql)
    expr
  }

  def toOrderByItem(sql: String, dbType: String): SQLSelectOrderByItem = {
    val parser = SQLParserUtils.createExprParser(sql, dbType)
    val orderByItem = parser.parseSelectOrderByItem
    if (parser.getLexer.token ne Token.EOF) throw new ParserException("illegal sql expr : " + sql)
    orderByItem
  }

  def toUpdateSetItem(sql: String, dbType: String): SQLUpdateSetItem = {
    val parser = SQLParserUtils.createExprParser(sql, dbType)
    val updateSetItem = parser.parseUpdateSetItem
    if (parser.getLexer.token ne Token.EOF) throw new ParserException("illegal sql expr : " + sql)
    updateSetItem
  }

  def toSelectItem(sql: String, dbType: String): SQLSelectItem = {
    val parser = SQLParserUtils.createExprParser(sql, dbType)
    val selectItem = parser.parseSelectItem
    if (parser.getLexer.token ne Token.EOF) throw new ParserException("illegal sql expr : " + sql)
    selectItem
  }

  def toStatementList(sql: String, dbType: String): List[SQLStatement] = {
    val parser = SQLParserUtils.createSQLStatementParser(sql, dbType)
    parser.parseStatementList
  }

  def toSQLExpr(sql: String): SQLExpr = toSQLExpr(sql, null)

  def format(sql: String, dbType: String): String = format(sql, dbType, null, null)

  def format(sql: String, dbType: String, option: SQLUtils.FormatOption): String = format(sql, dbType, null, option)

  def format(sql: String, dbType: String, parameters: List[AnyRef]): String = format(sql, dbType, parameters, null)

  def format(sql: String, dbType: String, parameters: List[AnyRef], option: SQLUtils.FormatOption): String = try {
    val parser = SQLParserUtils.createSQLStatementParser(sql, dbType, FORMAT_DEFAULT_FEATURES)
    val statementList = parser.parseStatementList
    toSQLString(statementList, dbType, parameters, option)
  } catch {
    case ex: ClassCastException =>
      LOG.warn("format error, dbType : " + dbType, ex)
      sql
    case ex: ParserException =>
      LOG.warn("format error", ex)
      sql
  }

  def toSQLString(statementList: List[SQLStatement], dbType: String): String = toSQLString(statementList, dbType, null.asInstanceOf[List[AnyRef]])

  def toSQLString(statementList: List[SQLStatement], dbType: String, option: SQLUtils.FormatOption): String = toSQLString(statementList, dbType, null, option)

  def toSQLString(statementList: List[SQLStatement], dbType: String, parameters: List[AnyRef]): String = toSQLString(statementList, dbType, parameters, null, null)

  def toSQLString(statementList: List[SQLStatement], dbType: String, parameters: List[AnyRef], option: SQLUtils.FormatOption): String = toSQLString(statementList, dbType, parameters, option, null)

  def toSQLString(statementList: List[SQLStatement], dbType: String, parameters: List[AnyRef], option: SQLUtils.FormatOption, tableMapping: Map[String, String]): String = {
    val out = new StringBuilder
    val visitor = createFormatOutputVisitor(out, statementList, dbType)
    if (parameters != null) visitor.setInputParameters(parameters)
    if (option == null) option = DEFAULT_FORMAT_OPTION
    visitor.setFeatures(option.features)
    if (tableMapping != null) visitor.setTableMapping(tableMapping)
    var printStmtSeperator = false
    if (JdbcConstants.SQL_SERVER == dbType) printStmtSeperator = false
    else printStmtSeperator = !(JdbcConstants.ORACLE == dbType)
    var i = 0
    val size = statementList.size
    while ( {
      i < size
    }) {
      val stmt = statementList.get(i)
      if (i > 0) {
        val preStmt = statementList.get(i - 1)
        if (printStmtSeperator && !preStmt.isAfterSemi) visitor.print(";")
        val comments = preStmt.getAfterCommentsDirect
        if (comments != null) {
          var j = 0
          while ( {
            j < comments.size
          }) {
            val comment = comments.get(j)
            if (j != 0) visitor.println()
            visitor.printComment(comment)

            {
              j += 1; j
            }
          }
        }
        if (printStmtSeperator) visitor.println()
        if (!stmt.isInstanceOf[SQLSetStatement]) visitor.println()
      }
      val comments = stmt.getBeforeCommentsDirect
      if (comments != null) {
        import scala.collection.JavaConversions._
        for (comment <- comments) {
          visitor.printComment(comment)
          visitor.println()
        }
      }

      stmt.accept(visitor)
      if (i == size - 1) {
        val comments = stmt.getAfterCommentsDirect
        if (comments != null) {
          var j = 0
          while ( {
            j < comments.size
          }) {
            val comment = comments.get(j)
            if (j != 0) visitor.println()
            visitor.printComment(comment)

            {
              j += 1; j
            }
          }
        }
      }

      {
        i += 1; i - 1
      }
    }
    out.toString
  }

  def createOutputVisitor(out: Appendable, dbType: String): SQLASTOutputVisitor = createFormatOutputVisitor(out, null, dbType)

  def createFormatOutputVisitor(out: Appendable, //
                                statementList: List[SQLStatement], dbType: String): SQLASTOutputVisitor = {
    if (JdbcConstants.ORACLE == dbType || JdbcConstants.ALI_ORACLE == dbType) if (statementList == null || statementList.size == 1) return new OracleOutputVisitor(out, false)
    else return new OracleOutputVisitor(out, true)
    if (JdbcConstants.MYSQL == dbType // || JdbcConstants.MARIADB == dbType)  { return new MySqlOutputVisitor(out)
  }

  if (JdbcConstants.POSTGRESQL == dbType) return new PGOutputVisitor(out)
  if (JdbcConstants.SQL_SERVER == dbType || JdbcConstants.JTDS == dbType) return new SQLServerOutputVisitor(out)
  if (JdbcConstants.DB2 == dbType) return new DB2OutputVisitor(out)
  if (JdbcConstants.ODPS == dbType) return new OdpsOutputVisitor(out)
  if (JdbcConstants.H2 == dbType) return new H2OutputVisitor(out)
  if (JdbcConstants.HIVE == dbType) return new HiveOutputVisitor(out)
  if (JdbcConstants.ELASTIC_SEARCH == dbType) return new MySqlOutputVisitor(out)
  return new SQLASTOutputVisitor(out, dbType)
}

@deprecated def createSchemaStatVisitor (statementList: List[SQLStatement], dbType: String): SchemaStatVisitor = {
return createSchemaStatVisitor (dbType)
}
def createSchemaStatVisitor (dbType: String): SchemaStatVisitor = {
if (JdbcConstants.ORACLE == dbType || JdbcConstants.ALI_ORACLE == dbType) {
return new OracleSchemaStatVisitor
}
if (JdbcConstants.MYSQL == dbType || JdbcConstants.MARIADB == dbType) {
return new MySqlSchemaStatVisitor
}
if (JdbcConstants.POSTGRESQL == dbType) {
return new PGSchemaStatVisitor
}
if (JdbcConstants.SQL_SERVER == dbType || JdbcConstants.JTDS == dbType) {
return new SQLServerSchemaStatVisitor
}
if (JdbcConstants.DB2 == dbType) {
return new DB2SchemaStatVisitor
}
if (JdbcConstants.ODPS == dbType) {
return new OdpsSchemaStatVisitor
}
if (JdbcConstants.H2 == dbType) {
return new H2SchemaStatVisitor
}
if (JdbcConstants.HIVE == dbType) {
return new HiveSchemaStatVisitor
}
if (JdbcConstants.ELASTIC_SEARCH == dbType) {
return new MySqlSchemaStatVisitor
}
return new SchemaStatVisitor
}
def parseStatements (sql: String, dbType: String): List[SQLStatement] = {
val parser: SQLStatementParser = SQLParserUtils.createSQLStatementParser (sql, dbType)
val stmtList: List[SQLStatement] = parser.parseStatementList
if (parser.getLexer.token ne Token.EOF) {
throw new ParserException ("syntax error : " + sql)
}
return stmtList
}
def parseStatements (sql: String, dbType: String, keepComments: Boolean): List[SQLStatement] = {
val parser: SQLStatementParser = SQLParserUtils.createSQLStatementParser (sql, dbType, keepComments)
val stmtList: List[SQLStatement] = parser.parseStatementList
if (parser.getLexer.token ne Token.EOF) {
throw new ParserException ("syntax error. " + sql)
}
return stmtList
}

/**
  * @author owenludong.lud
  * @param columnName
  * @param tableAlias
  * @param pattern if pattern is null,it will be set {%Y-%m-%d %H:%i:%s} as mysql default value and set {yyyy-mm-dd
  *                hh24:mi:ss} as oracle default value
  * @param dbType  { @link JdbcConstants} if dbType is null ,it will be set the mysql as a default value
  */
def buildToDate (columnName: String, tableAlias: String, pattern: String, dbType: String): String = {
val sql: StringBuilder = new StringBuilder
if (StringUtils.isEmpty (columnName) ) {
return ""
}
if (StringUtils.isEmpty (dbType) ) {
dbType = JdbcConstants.MYSQL
}
var formatMethod: String = ""
if (JdbcConstants.MYSQL.equalsIgnoreCase (dbType) ) {
formatMethod = "STR_TO_DATE"
if (StringUtils.isEmpty (pattern) ) {
pattern = "%Y-%m-%d %H:%i:%s"
}
}
else {
if (JdbcConstants.ORACLE.equalsIgnoreCase (dbType) ) {
formatMethod = "TO_DATE"
if (StringUtils.isEmpty (pattern) ) {
pattern = "yyyy-mm-dd hh24:mi:ss"
}
}
else {
return ""
// expand date's handle method for other database
}
}
sql.append (formatMethod).append ("(")
if (! (StringUtils.isEmpty (tableAlias) ) ) {
sql.append (tableAlias).append (".")
}
sql.append (columnName).append (",")
sql.append ("'")
sql.append (pattern)
sql.append ("')")
return sql.toString
}
def split (x: SQLBinaryOpExpr): List[SQLExpr] = {
return SQLBinaryOpExpr.split (x)
}
def translateOracleToMySql (sql: String): String = {
val stmtList: List[SQLStatement] = toStatementList (sql, JdbcConstants.ORACLE)
val out: StringBuilder = new StringBuilder
val visitor: OracleToMySqlOutputVisitor = new OracleToMySqlOutputVisitor (out, false)
var i: Int = 0
while ( {
i < stmtList.size
}) {
stmtList.get (i).accept (visitor) {
i += 1
i
}
}
val mysqlSql: String = out.toString
return mysqlSql
}
def addCondition (sql: String, condition: String, dbType: String): String = {
val result: String = addCondition (sql, condition, SQLBinaryOperator.BooleanAnd, false, dbType)
return result
}
def addCondition (sql: String, condition: String, op: SQLBinaryOperator, left: Boolean, dbType: String): String = {
if (sql == null) {
throw new IllegalArgumentException ("sql is null")
}
if (condition == null) {
return sql
}
if (op == null) {
op = SQLBinaryOperator.BooleanAnd
}
if ((op ne SQLBinaryOperator.BooleanAnd) && (op ne SQLBinaryOperator.BooleanOr) ) {
throw new IllegalArgumentException ("add condition not support : " + op)
}
val stmtList: List[SQLStatement] = parseStatements (sql, dbType)
if (stmtList.size == 0) {
throw new IllegalArgumentException ("not support empty-statement :" + sql)
}
if (stmtList.size > 1) {
throw new IllegalArgumentException ("not support multi-statement :" + sql)
}
val stmt: SQLStatement = stmtList.get (0)
val conditionExpr: SQLExpr = toSQLExpr (condition, dbType)
addCondition (stmt, op, conditionExpr, left)
return toSQLString (stmt, dbType)
}
def addCondition (stmt: SQLStatement, op: SQLBinaryOperator, condition: SQLExpr, left: Boolean): Unit = {
if (stmt.isInstanceOf[SQLSelectStatement] ) {
val query: SQLSelectQuery = (stmt.asInstanceOf[SQLSelectStatement] ).getSelect.getQuery
if (query.isInstanceOf[SQLSelectQueryBlock] ) {
val queryBlock: SQLSelectQueryBlock = query.asInstanceOf[SQLSelectQueryBlock]
val newCondition: SQLExpr = buildCondition (op, condition, left, queryBlock.getWhere)
queryBlock.setWhere (newCondition)
}
else {
throw new IllegalArgumentException ("add condition not support " + stmt.getClass.getName)
}
return
}
if (stmt.isInstanceOf[SQLDeleteStatement] ) {
val delete: SQLDeleteStatement = stmt.asInstanceOf[SQLDeleteStatement]
val newCondition: SQLExpr = buildCondition (op, condition, left, delete.getWhere)
delete.setWhere (newCondition)
return
}
if (stmt.isInstanceOf[SQLUpdateStatement] ) {
val update: SQLUpdateStatement = stmt.asInstanceOf[SQLUpdateStatement]
val newCondition: SQLExpr = buildCondition (op, condition, left, update.getWhere)
update.setWhere (newCondition)
return
}
throw new IllegalArgumentException ("add condition not support " + stmt.getClass.getName)
}
def buildCondition (op: SQLBinaryOperator, condition: SQLExpr, left: Boolean, where: SQLExpr): SQLExpr = {
if (where == null) {
return condition
}
var newCondition: SQLBinaryOpExpr = null
if (left) {
newCondition = new SQLBinaryOpExpr (condition, op, where)
}
else {
newCondition = new SQLBinaryOpExpr (where, op, condition)
}
return newCondition
}
def addSelectItem (selectSql: String, expr: String, alias: String, dbType: String): String = {
return addSelectItem (selectSql, expr, alias, false, dbType)
}
def addSelectItem (selectSql: String, expr: String, alias: String, first: Boolean, dbType: String): String = {
val stmtList: List[SQLStatement] = parseStatements (selectSql, dbType)
if (stmtList.size == 0) {
throw new IllegalArgumentException ("not support empty-statement :" + selectSql)
}
if (stmtList.size > 1) {
throw new IllegalArgumentException ("not support multi-statement :" + selectSql)
}
val stmt: SQLStatement = stmtList.get (0)
val columnExpr: SQLExpr = toSQLExpr (expr, dbType)
addSelectItem (stmt, columnExpr, alias, first)
return toSQLString (stmt, dbType)
}
def addSelectItem (stmt: SQLStatement, expr: SQLExpr, alias: String, first: Boolean): Unit = {
if (expr == null) {
return
}
if (stmt.isInstanceOf[SQLSelectStatement] ) {
val query: SQLSelectQuery = (stmt.asInstanceOf[SQLSelectStatement] ).getSelect.getQuery
if (query.isInstanceOf[SQLSelectQueryBlock] ) {
val queryBlock: SQLSelectQueryBlock = query.asInstanceOf[SQLSelectQueryBlock]
addSelectItem (queryBlock, expr, alias, first)
}
else {
throw new IllegalArgumentException ("add condition not support " + stmt.getClass.getName)
}
return
}
throw new IllegalArgumentException ("add selectItem not support " + stmt.getClass.getName)
}
def addSelectItem (queryBlock: SQLSelectQueryBlock, expr: SQLExpr, alias: String, first: Boolean): Unit = {
val selectItem: SQLSelectItem = new SQLSelectItem (expr, alias)
queryBlock.getSelectList.add (selectItem)
selectItem.setParent (selectItem)
}

class FormatOption() {
  private var features = VisitorFeature.of(VisitorFeature.OutputUCase, VisitorFeature.OutputPrettyFormat)

  def this(features: VisitorFeature*) {
    this()
    this.features = VisitorFeature.of(features)
  }

  def this(ucase: Boolean, prettyFormat: Boolean) {
    this(ucase, prettyFormat, false)
  }

  def this(ucase: Boolean) {
    this(ucase, true)
  }

  def this(ucase: Boolean, prettyFormat: Boolean, parameterized: Boolean) {
    this()
    this.features = VisitorFeature.config(this.features, VisitorFeature.OutputUCase, ucase)
    this.features = VisitorFeature.config(this.features, VisitorFeature.OutputPrettyFormat, prettyFormat)
    this.features = VisitorFeature.config(this.features, VisitorFeature.OutputParameterized, parameterized)
  }

  def isDesensitize: Boolean = isEnabled(VisitorFeature.OutputDesensitize)

  def setDesensitize(`val`: Boolean): Unit = {
    config(VisitorFeature.OutputDesensitize, `val`)
  }

  def isUppCase: Boolean = isEnabled(VisitorFeature.OutputUCase)

  def setUppCase(`val`: Boolean): Unit = {
    config(VisitorFeature.OutputUCase, `val`)
  }

  def isPrettyFormat: Boolean = isEnabled(VisitorFeature.OutputPrettyFormat)

  def setPrettyFormat(prettyFormat: Boolean): Unit = {
    config(VisitorFeature.OutputPrettyFormat, prettyFormat)
  }

  def isParameterized: Boolean = isEnabled(VisitorFeature.OutputParameterized)

  def setParameterized(parameterized: Boolean): Unit = {
    config(VisitorFeature.OutputParameterized, parameterized)
  }

  def config(feature: VisitorFeature, state: Boolean): Unit = {
    features = VisitorFeature.config(features, feature, state)
  }

  final def isEnabled(feature: VisitorFeature): Boolean = VisitorFeature.isEnabled(this.features, feature)
}

def refactor (sql: String, dbType: String, tableMapping: Map[String, String] ): String = {
val stmtList: List[SQLStatement] = parseStatements (sql, dbType)
return SQLUtils.toSQLString (stmtList, dbType, null, null, tableMapping)
}
def hash (sql: String, dbType: String): Long = {
val lexer: Lexer = SQLParserUtils.createLexer (sql, dbType)
val buf: StringBuilder = new StringBuilder (sql.length)

while ( {
true
}) {
lexer.nextToken ()
val token: Token = lexer.token
if (token eq Token.EOF) {
break //todo: break is not supported
}
if (token eq Token.ERROR) {
return Utils.fnv_64 (sql)
}
if (buf.length != 0) {
}
}
return buf.hashCode
}
def not (expr: SQLExpr): SQLExpr = {
if (expr.isInstanceOf[SQLBinaryOpExpr] ) {
val binaryOpExpr: SQLBinaryOpExpr = expr.asInstanceOf[SQLBinaryOpExpr]
val op: SQLBinaryOperator = binaryOpExpr.getOperator
var notOp: SQLBinaryOperator = null
op match {
case Equality =>
notOp = SQLBinaryOperator.LessThanOrGreater
break //todo: break is not supported
case LessThanOrEqualOrGreaterThan =>
notOp = SQLBinaryOperator.Equality
break //todo: break is not supported
case LessThan =>
notOp = SQLBinaryOperator.GreaterThanOrEqual
break //todo: break is not supported
case LessThanOrEqual =>
notOp = SQLBinaryOperator.GreaterThan
break //todo: break is not supported
case GreaterThan =>
notOp = SQLBinaryOperator.LessThanOrEqual
break //todo: break is not supported
case GreaterThanOrEqual =>
notOp = SQLBinaryOperator.LessThan
break //todo: break is not supported
case Is =>
notOp = SQLBinaryOperator.IsNot
break //todo: break is not supported
case IsNot =>
notOp = SQLBinaryOperator.Is
break //todo: break is not supported
case _ =>
break //todo: break is not supported
}
if (notOp != null) {
return new SQLBinaryOpExpr (binaryOpExpr.getLeft, notOp, binaryOpExpr.getRight)
}
}
if (expr.isInstanceOf[SQLInListExpr] ) {
val inListExpr: SQLInListExpr = expr.asInstanceOf[SQLInListExpr]
val newInListExpr: SQLInListExpr = new SQLInListExpr (inListExpr)
newInListExpr.getTargetList.addAll (inListExpr.getTargetList)
newInListExpr.setNot (! (inListExpr.isNot) )
return newInListExpr
}
return new SQLUnaryExpr (SQLUnaryOperator.Not, expr)
}
def normalize (name: String): String = {
return normalize (name, null)
}
def normalize (name: String, dbType: String): String = {
if (name == null) {
return null
}
if (name.length > 2) {
val c0: Char = name.charAt (0)
val x0: Char = name.charAt (name.length - 1)
if ((c0 == '"' && x0 == '"') || (c0 == '`' && x0 == '`') ) {
var normalizeName: String = name.substring (1, name.length - 1)
if (c0 == '`') {
normalizeName = normalizeName.replaceAll ("`\\.`", ".")
}
if (JdbcConstants.ORACLE == dbType) {
if (OracleUtils.isKeyword (normalizeName) ) {
return name
}
}
else {
if (JdbcConstants.MYSQL == dbType) {
if (MySqlUtils.isKeyword (normalizeName) ) {
return name
}
}
else {
if (JdbcConstants.POSTGRESQL == dbType || JdbcConstants.ENTERPRISEDB == dbType) {
if (PGUtils.isKeyword (normalizeName) ) {
return name
}
}
}
}
return normalizeName
}
}
return name
}
def nameEquals (a: SQLName, b: SQLName): Boolean = {
if (a eq b) {
return true
}
if (a == null || b == null) {
return false
}
return a.nameHashCode64 == b.nameHashCode64
}
def nameEquals (a: String, b: String): Boolean = {
if (a eq b) {
return true
}
if (a == null || b == null) {
return false
}
if (a.equalsIgnoreCase (b) ) {
return true
}
val normalize_a: String = normalize (a)
val normalize_b: String = normalize (b)
return normalize_a.equalsIgnoreCase (normalize_b)
}
def isValue (expr: SQLExpr): Boolean = {
if (expr.isInstanceOf[SQLLiteralExpr] ) {
return true
}
if (expr.isInstanceOf[SQLVariantRefExpr] ) {
return true
}
if (expr.isInstanceOf[SQLBinaryOpExpr] ) {
val binaryOpExpr: SQLBinaryOpExpr = expr.asInstanceOf[SQLBinaryOpExpr]
val op: SQLBinaryOperator = binaryOpExpr.getOperator
if ((op eq SQLBinaryOperator.Add) || (op eq SQLBinaryOperator.Subtract) || (op eq SQLBinaryOperator.Multiply) ) {
return isValue (binaryOpExpr.getLeft) && isValue (binaryOpExpr.getRight)
}
}
return false
}
def replaceInParent (expr: SQLExpr, target: SQLExpr): Boolean = {
if (expr == null) {
return false
}
val parent: SQLObject = expr.getParent
if (parent.isInstanceOf[SQLReplaceable] ) {
return (parent.asInstanceOf[SQLReplaceable] ).replace (expr, target)
}
return false
}
def desensitizeTable (tableName: String): String = {
if (tableName == null) {
return null
}
tableName = normalize (tableName)
val hash: Long = FnvHash.hashCode64 (tableName)
return Utils.hex_t (hash)
}

/**
  * 重新排序建表语句，解决建表语句的依赖关系
  *
  * @param sql
  * @param dbType
  * @return
  */
def sort (sql: String, dbType: String): String = {
val stmtList: List[_] = SQLUtils.parseStatements (sql, JdbcConstants.ORACLE)
SQLCreateTableStatement.sort (stmtList)
return SQLUtils.toSQLString (stmtList, dbType)
}
}

class SQLUtils {}

