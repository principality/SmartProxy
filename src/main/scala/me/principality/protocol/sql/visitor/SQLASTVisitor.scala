package me.principality.protocol.sql.visitor

trait SQLASTVisitor {
  def endVisit(x: SQLAllColumnExpr): Unit

  def endVisit(x: SQLBetweenExpr): Unit

  def endVisit(x: SQLBinaryOpExpr): Unit

  def endVisit(x: SQLCaseExpr): Unit

  def endVisit(x: SQLCaseExpr.Item): Unit

  def endVisit(x: SQLCaseStatement): Unit

  def endVisit(x: SQLCaseStatement.Item): Unit

  def endVisit(x: SQLCharExpr): Unit

  def endVisit(x: SQLIdentifierExpr): Unit

  def endVisit(x: SQLInListExpr): Unit

  def endVisit(x: SQLIntegerExpr): Unit

  def endVisit(x: SQLExistsExpr): Unit

  def endVisit(x: SQLNCharExpr): Unit

  def endVisit(x: SQLNotExpr): Unit

  def endVisit(x: SQLNullExpr): Unit

  def endVisit(x: SQLNumberExpr): Unit

  def endVisit(x: SQLPropertyExpr): Unit

  def endVisit(x: SQLSelectGroupByClause): Unit

  def endVisit(x: SQLSelectItem): Unit

  def endVisit(selectStatement: SQLSelectStatement): Unit

  def postVisit(x: SQLObject): Unit

  def preVisit(x: SQLObject): Unit

  def visit(x: SQLAllColumnExpr): Boolean

  def visit(x: SQLBetweenExpr): Boolean

  def visit(x: SQLBinaryOpExpr): Boolean

  def visit(x: SQLCaseExpr): Boolean

  def visit(x: SQLCaseExpr.Item): Boolean

  def visit(x: SQLCaseStatement): Boolean

  def visit(x: SQLCaseStatement.Item): Boolean

  def visit(x: SQLCastExpr): Boolean

  def visit(x: SQLCharExpr): Boolean

  def visit(x: SQLExistsExpr): Boolean

  def visit(x: SQLIdentifierExpr): Boolean

  def visit(x: SQLInListExpr): Boolean

  def visit(x: SQLIntegerExpr): Boolean

  def visit(x: SQLNCharExpr): Boolean

  def visit(x: SQLNotExpr): Boolean

  def visit(x: SQLNullExpr): Boolean

  def visit(x: SQLNumberExpr): Boolean

  def visit(x: SQLPropertyExpr): Boolean

  def visit(x: SQLSelectGroupByClause): Boolean

  def visit(x: SQLSelectItem): Boolean

  def endVisit(x: SQLCastExpr): Unit

  def visit(astNode: SQLSelectStatement): Boolean

  def endVisit(astNode: SQLAggregateExpr): Unit

  def visit(astNode: SQLAggregateExpr): Boolean

  def visit(x: SQLVariantRefExpr): Boolean

  def endVisit(x: SQLVariantRefExpr): Unit

  def visit(x: SQLQueryExpr): Boolean

  def endVisit(x: SQLQueryExpr): Unit

  def visit(x: SQLUnaryExpr): Boolean

  def endVisit(x: SQLUnaryExpr): Unit

  def visit(x: SQLHexExpr): Boolean

  def endVisit(x: SQLHexExpr): Unit

  def visit(x: SQLSelect): Boolean

  def endVisit(select: SQLSelect): Unit

  def visit(x: SQLSelectQueryBlock): Boolean

  def endVisit(x: SQLSelectQueryBlock): Unit

  def visit(x: SQLExprTableSource): Boolean

  def endVisit(x: SQLExprTableSource): Unit

  def visit(x: SQLOrderBy): Boolean

  def endVisit(x: SQLOrderBy): Unit

  def visit(x: SQLSelectOrderByItem): Boolean

  def endVisit(x: SQLSelectOrderByItem): Unit

  def visit(x: SQLDropTableStatement): Boolean

  def endVisit(x: SQLDropTableStatement): Unit

  def visit(x: SQLCreateTableStatement): Boolean

  def endVisit(x: SQLCreateTableStatement): Unit

  def visit(x: SQLColumnDefinition): Boolean

  def endVisit(x: SQLColumnDefinition): Unit

  def visit(x: SQLColumnDefinition.Identity): Boolean

  def endVisit(x: SQLColumnDefinition.Identity): Unit

  def visit(x: SQLDataType): Boolean

  def endVisit(x: SQLDataType): Unit

  def visit(x: SQLCharacterDataType): Boolean

  def endVisit(x: SQLCharacterDataType): Unit

  def visit(x: SQLDeleteStatement): Boolean

  def endVisit(x: SQLDeleteStatement): Unit

  def visit(x: SQLCurrentOfCursorExpr): Boolean

  def endVisit(x: SQLCurrentOfCursorExpr): Unit

  def visit(x: SQLInsertStatement): Boolean

  def endVisit(x: SQLInsertStatement): Unit

  def visit(x: SQLInsertStatement.ValuesClause): Boolean

  def endVisit(x: SQLInsertStatement.ValuesClause): Unit

  def visit(x: SQLUpdateSetItem): Boolean

  def endVisit(x: SQLUpdateSetItem): Unit

  def visit(x: SQLUpdateStatement): Boolean

  def endVisit(x: SQLUpdateStatement): Unit

  def visit(x: SQLCreateViewStatement): Boolean

  def endVisit(x: SQLCreateViewStatement): Unit

  def visit(x: SQLCreateViewStatement.Column): Boolean

  def endVisit(x: SQLCreateViewStatement.Column): Unit

  def visit(x: SQLNotNullConstraint): Boolean

  def endVisit(x: SQLNotNullConstraint): Unit

  def endVisit(x: SQLMethodInvokeExpr): Unit

  def visit(x: SQLMethodInvokeExpr): Boolean

  def endVisit(x: SQLUnionQuery): Unit

  def visit(x: SQLUnionQuery): Boolean

  def endVisit(x: SQLSetStatement): Unit

  def visit(x: SQLSetStatement): Boolean

  def endVisit(x: SQLAssignItem): Unit

  def visit(x: SQLAssignItem): Boolean

  def endVisit(x: SQLCallStatement): Unit

  def visit(x: SQLCallStatement): Boolean

  def endVisit(x: SQLJoinTableSource): Unit

  def visit(x: SQLJoinTableSource): Boolean

  def endVisit(x: SQLSomeExpr): Unit

  def visit(x: SQLSomeExpr): Boolean

  def endVisit(x: SQLAnyExpr): Unit

  def visit(x: SQLAnyExpr): Boolean

  def endVisit(x: SQLAllExpr): Unit

  def visit(x: SQLAllExpr): Boolean

  def endVisit(x: SQLInSubQueryExpr): Unit

  def visit(x: SQLInSubQueryExpr): Boolean

  def endVisit(x: SQLListExpr): Unit

  def visit(x: SQLListExpr): Boolean

  def endVisit(x: SQLSubqueryTableSource): Unit

  def visit(x: SQLSubqueryTableSource): Boolean

  def endVisit(x: SQLTruncateStatement): Unit

  def visit(x: SQLTruncateStatement): Boolean

  def endVisit(x: SQLDefaultExpr): Unit

  def visit(x: SQLDefaultExpr): Boolean

  def endVisit(x: SQLCommentStatement): Unit

  def visit(x: SQLCommentStatement): Boolean

  def endVisit(x: SQLUseStatement): Unit

  def visit(x: SQLUseStatement): Boolean

  def visit(x: SQLAlterTableAddColumn): Boolean

  def endVisit(x: SQLAlterTableAddColumn): Unit

  def visit(x: SQLAlterTableDropColumnItem): Boolean

  def endVisit(x: SQLAlterTableDropColumnItem): Unit

  def visit(x: SQLAlterTableDropIndex): Boolean

  def endVisit(x: SQLAlterTableDropIndex): Unit

  def visit(x: SQLDropIndexStatement): Boolean

  def endVisit(x: SQLDropIndexStatement): Unit

  def visit(x: SQLDropViewStatement): Boolean

  def endVisit(x: SQLDropViewStatement): Unit

  def visit(x: SQLSavePointStatement): Boolean

  def endVisit(x: SQLSavePointStatement): Unit

  def visit(x: SQLRollbackStatement): Boolean

  def endVisit(x: SQLRollbackStatement): Unit

  def visit(x: SQLReleaseSavePointStatement): Boolean

  def endVisit(x: SQLReleaseSavePointStatement): Unit

  def endVisit(x: SQLCommentHint): Unit

  def visit(x: SQLCommentHint): Boolean

  def endVisit(x: SQLCreateDatabaseStatement): Unit

  def visit(x: SQLCreateDatabaseStatement): Boolean

  def endVisit(x: SQLOver): Unit

  def visit(x: SQLOver): Boolean

  def endVisit(x: SQLKeep): Unit

  def visit(x: SQLKeep): Boolean

  def endVisit(x: SQLColumnPrimaryKey): Unit

  def visit(x: SQLColumnPrimaryKey): Boolean

  def visit(x: SQLColumnUniqueKey): Boolean

  def endVisit(x: SQLColumnUniqueKey): Unit

  def endVisit(x: SQLWithSubqueryClause): Unit

  def visit(x: SQLWithSubqueryClause): Boolean

  def endVisit(x: SQLWithSubqueryClause.Entry): Unit

  def visit(x: SQLWithSubqueryClause.Entry): Boolean

  def endVisit(x: SQLAlterTableAlterColumn): Unit

  def visit(x: SQLAlterTableAlterColumn): Boolean

  def visit(x: SQLCheck): Boolean

  def endVisit(x: SQLCheck): Unit

  def visit(x: SQLAlterTableDropForeignKey): Boolean

  def endVisit(x: SQLAlterTableDropForeignKey): Unit

  def visit(x: SQLAlterTableDropPrimaryKey): Boolean

  def endVisit(x: SQLAlterTableDropPrimaryKey): Unit

  def visit(x: SQLAlterTableDisableKeys): Boolean

  def endVisit(x: SQLAlterTableDisableKeys): Unit

  def visit(x: SQLAlterTableEnableKeys): Boolean

  def endVisit(x: SQLAlterTableEnableKeys): Unit

  def visit(x: SQLAlterTableStatement): Boolean

  def endVisit(x: SQLAlterTableStatement): Unit

  def visit(x: SQLAlterTableDisableConstraint): Boolean

  def endVisit(x: SQLAlterTableDisableConstraint): Unit

  def visit(x: SQLAlterTableEnableConstraint): Boolean

  def endVisit(x: SQLAlterTableEnableConstraint): Unit

  def visit(x: SQLColumnCheck): Boolean

  def endVisit(x: SQLColumnCheck): Unit

  def visit(x: SQLExprHint): Boolean

  def endVisit(x: SQLExprHint): Unit

  def visit(x: SQLAlterTableDropConstraint): Boolean

  def endVisit(x: SQLAlterTableDropConstraint): Unit

  def visit(x: SQLUnique): Boolean

  def endVisit(x: SQLUnique): Unit

  def visit(x: SQLPrimaryKeyImpl): Boolean

  def endVisit(x: SQLPrimaryKeyImpl): Unit

  def visit(x: SQLCreateIndexStatement): Boolean

  def endVisit(x: SQLCreateIndexStatement): Unit

  def visit(x: SQLAlterTableRenameColumn): Boolean

  def endVisit(x: SQLAlterTableRenameColumn): Unit

  def visit(x: SQLColumnReference): Boolean

  def endVisit(x: SQLColumnReference): Unit

  def visit(x: SQLForeignKeyImpl): Boolean

  def endVisit(x: SQLForeignKeyImpl): Unit

  def visit(x: SQLDropSequenceStatement): Boolean

  def endVisit(x: SQLDropSequenceStatement): Unit

  def visit(x: SQLDropTriggerStatement): Boolean

  def endVisit(x: SQLDropTriggerStatement): Unit

  def endVisit(x: SQLDropUserStatement): Unit

  def visit(x: SQLDropUserStatement): Boolean

  def endVisit(x: SQLExplainStatement): Unit

  def visit(x: SQLExplainStatement): Boolean

  def endVisit(x: SQLGrantStatement): Unit

  def visit(x: SQLGrantStatement): Boolean

  def endVisit(x: SQLDropDatabaseStatement): Unit

  def visit(x: SQLDropDatabaseStatement): Boolean

  def endVisit(x: SQLAlterTableAddIndex): Unit

  def visit(x: SQLAlterTableAddIndex): Boolean

  def endVisit(x: SQLAlterTableAddConstraint): Unit

  def visit(x: SQLAlterTableAddConstraint): Boolean

  def endVisit(x: SQLCreateTriggerStatement): Unit

  def visit(x: SQLCreateTriggerStatement): Boolean

  def endVisit(x: SQLDropFunctionStatement): Unit

  def visit(x: SQLDropFunctionStatement): Boolean

  def endVisit(x: SQLDropTableSpaceStatement): Unit

  def visit(x: SQLDropTableSpaceStatement): Boolean

  def endVisit(x: SQLDropProcedureStatement): Unit

  def visit(x: SQLDropProcedureStatement): Boolean

  def endVisit(x: SQLBooleanExpr): Unit

  def visit(x: SQLBooleanExpr): Boolean

  def endVisit(x: SQLUnionQueryTableSource): Unit

  def visit(x: SQLUnionQueryTableSource): Boolean

  def endVisit(x: SQLTimestampExpr): Unit

  def visit(x: SQLTimestampExpr): Boolean

  def endVisit(x: SQLRevokeStatement): Unit

  def visit(x: SQLRevokeStatement): Boolean

  def endVisit(x: SQLBinaryExpr): Unit

  def visit(x: SQLBinaryExpr): Boolean

  def endVisit(x: SQLAlterTableRename): Unit

  def visit(x: SQLAlterTableRename): Boolean

  def endVisit(x: SQLAlterViewRenameStatement): Unit

  def visit(x: SQLAlterViewRenameStatement): Boolean

  def endVisit(x: SQLShowTablesStatement): Unit

  def visit(x: SQLShowTablesStatement): Boolean

  def endVisit(x: SQLAlterTableAddPartition): Unit

  def visit(x: SQLAlterTableAddPartition): Boolean

  def endVisit(x: SQLAlterTableDropPartition): Unit

  def visit(x: SQLAlterTableDropPartition): Boolean

  def endVisit(x: SQLAlterTableRenamePartition): Unit

  def visit(x: SQLAlterTableRenamePartition): Boolean

  def endVisit(x: SQLAlterTableSetComment): Unit

  def visit(x: SQLAlterTableSetComment): Boolean

  def endVisit(x: SQLAlterTableSetLifecycle): Unit

  def visit(x: SQLAlterTableSetLifecycle): Boolean

  def endVisit(x: SQLAlterTableEnableLifecycle): Unit

  def visit(x: SQLAlterTableEnableLifecycle): Boolean

  def endVisit(x: SQLAlterTableDisableLifecycle): Unit

  def visit(x: SQLAlterTableDisableLifecycle): Boolean

  def endVisit(x: SQLAlterTableTouch): Unit

  def visit(x: SQLAlterTableTouch): Boolean

  def endVisit(x: SQLArrayExpr): Unit

  def visit(x: SQLArrayExpr): Boolean

  def endVisit(x: SQLOpenStatement): Unit

  def visit(x: SQLOpenStatement): Boolean

  def endVisit(x: SQLFetchStatement): Unit

  def visit(x: SQLFetchStatement): Boolean

  def endVisit(x: SQLCloseStatement): Unit

  def visit(x: SQLCloseStatement): Boolean

  def visit(x: SQLGroupingSetExpr): Boolean

  def endVisit(x: SQLGroupingSetExpr): Unit

  def visit(x: SQLIfStatement): Boolean

  def endVisit(x: SQLIfStatement): Unit

  def visit(x: SQLIfStatement.ElseIf): Boolean

  def endVisit(x: SQLIfStatement.ElseIf): Unit

  def visit(x: SQLIfStatement.Else): Boolean

  def endVisit(x: SQLIfStatement.Else): Unit

  def visit(x: SQLLoopStatement): Boolean

  def endVisit(x: SQLLoopStatement): Unit

  def visit(x: SQLParameter): Boolean

  def endVisit(x: SQLParameter): Unit

  def visit(x: SQLCreateProcedureStatement): Boolean

  def endVisit(x: SQLCreateProcedureStatement): Unit

  def visit(x: SQLCreateFunctionStatement): Boolean

  def endVisit(x: SQLCreateFunctionStatement): Unit

  def visit(x: SQLBlockStatement): Boolean

  def endVisit(x: SQLBlockStatement): Unit

  def visit(x: SQLAlterTableDropKey): Boolean

  def endVisit(x: SQLAlterTableDropKey): Unit

  def visit(x: SQLDeclareItem): Boolean

  def endVisit(x: SQLDeclareItem): Unit

  def visit(x: SQLPartitionValue): Boolean

  def endVisit(x: SQLPartitionValue): Unit

  def visit(x: SQLPartition): Boolean

  def endVisit(x: SQLPartition): Unit

  def visit(x: SQLPartitionByRange): Boolean

  def endVisit(x: SQLPartitionByRange): Unit

  def visit(x: SQLPartitionByHash): Boolean

  def endVisit(x: SQLPartitionByHash): Unit

  def visit(x: SQLPartitionByList): Boolean

  def endVisit(x: SQLPartitionByList): Unit

  def visit(x: SQLSubPartition): Boolean

  def endVisit(x: SQLSubPartition): Unit

  def visit(x: SQLSubPartitionByHash): Boolean

  def endVisit(x: SQLSubPartitionByHash): Unit

  def visit(x: SQLSubPartitionByList): Boolean

  def endVisit(x: SQLSubPartitionByList): Unit

  def visit(x: SQLAlterDatabaseStatement): Boolean

  def endVisit(x: SQLAlterDatabaseStatement): Unit

  def visit(x: SQLAlterTableConvertCharSet): Boolean

  def endVisit(x: SQLAlterTableConvertCharSet): Unit

  def visit(x: SQLAlterTableReOrganizePartition): Boolean

  def endVisit(x: SQLAlterTableReOrganizePartition): Unit

  def visit(x: SQLAlterTableCoalescePartition): Boolean

  def endVisit(x: SQLAlterTableCoalescePartition): Unit

  def visit(x: SQLAlterTableTruncatePartition): Boolean

  def endVisit(x: SQLAlterTableTruncatePartition): Unit

  def visit(x: SQLAlterTableDiscardPartition): Boolean

  def endVisit(x: SQLAlterTableDiscardPartition): Unit

  def visit(x: SQLAlterTableImportPartition): Boolean

  def endVisit(x: SQLAlterTableImportPartition): Unit

  def visit(x: SQLAlterTableAnalyzePartition): Boolean

  def endVisit(x: SQLAlterTableAnalyzePartition): Unit

  def visit(x: SQLAlterTableCheckPartition): Boolean

  def endVisit(x: SQLAlterTableCheckPartition): Unit

  def visit(x: SQLAlterTableOptimizePartition): Boolean

  def endVisit(x: SQLAlterTableOptimizePartition): Unit

  def visit(x: SQLAlterTableRebuildPartition): Boolean

  def endVisit(x: SQLAlterTableRebuildPartition): Unit

  def visit(x: SQLAlterTableRepairPartition): Boolean

  def endVisit(x: SQLAlterTableRepairPartition): Unit

  def visit(x: SQLSequenceExpr): Boolean

  def endVisit(x: SQLSequenceExpr): Unit

  def visit(x: SQLMergeStatement): Boolean

  def endVisit(x: SQLMergeStatement): Unit

  def visit(x: SQLMergeStatement.MergeUpdateClause): Boolean

  def endVisit(x: SQLMergeStatement.MergeUpdateClause): Unit

  def visit(x: SQLMergeStatement.MergeInsertClause): Boolean

  def endVisit(x: SQLMergeStatement.MergeInsertClause): Unit

  def visit(x: SQLErrorLoggingClause): Boolean

  def endVisit(x: SQLErrorLoggingClause): Unit

  def visit(x: SQLNullConstraint): Boolean

  def endVisit(x: SQLNullConstraint): Unit

  def visit(x: SQLCreateSequenceStatement): Boolean

  def endVisit(x: SQLCreateSequenceStatement): Unit

  def visit(x: SQLDateExpr): Boolean

  def endVisit(x: SQLDateExpr): Unit

  def visit(x: SQLLimit): Boolean

  def endVisit(x: SQLLimit): Unit

  def endVisit(x: SQLStartTransactionStatement): Unit

  def visit(x: SQLStartTransactionStatement): Boolean

  def endVisit(x: SQLDescribeStatement): Unit

  def visit(x: SQLDescribeStatement): Boolean

  /**
    * support procedure
    */
  def visit(x: SQLWhileStatement): Boolean

  def endVisit(x: SQLWhileStatement): Unit

  def visit(x: SQLDeclareStatement): Boolean

  def endVisit(x: SQLDeclareStatement): Unit

  def visit(x: SQLReturnStatement): Boolean

  def endVisit(x: SQLReturnStatement): Unit

  def visit(x: SQLArgument): Boolean

  def endVisit(x: SQLArgument): Unit

  def visit(x: SQLCommitStatement): Boolean

  def endVisit(x: SQLCommitStatement): Unit

  def visit(x: SQLFlashbackExpr): Boolean

  def endVisit(x: SQLFlashbackExpr): Unit

  def visit(x: SQLCreateMaterializedViewStatement): Boolean

  def endVisit(x: SQLCreateMaterializedViewStatement): Unit

  def visit(x: SQLBinaryOpExprGroup): Boolean

  def endVisit(x: SQLBinaryOpExprGroup): Unit

  def visit(x: SQLScriptCommitStatement): Boolean

  def endVisit(x: SQLScriptCommitStatement): Unit

  def visit(x: SQLReplaceStatement): Boolean

  def endVisit(x: SQLReplaceStatement): Unit

  def visit(x: SQLCreateUserStatement): Boolean

  def endVisit(x: SQLCreateUserStatement): Unit

  def visit(x: SQLAlterFunctionStatement): Boolean

  def endVisit(x: SQLAlterFunctionStatement): Unit

  def visit(x: SQLAlterTypeStatement): Boolean

  def endVisit(x: SQLAlterTypeStatement): Unit

  def visit(x: SQLIntervalExpr): Boolean

  def endVisit(x: SQLIntervalExpr): Unit

  def visit(x: SQLLateralViewTableSource): Boolean

  def endVisit(x: SQLLateralViewTableSource): Unit

  def visit(x: SQLShowErrorsStatement): Boolean

  def endVisit(x: SQLShowErrorsStatement): Unit

  def visit(x: SQLAlterCharacter): Boolean

  def endVisit(x: SQLAlterCharacter): Unit

  def visit(x: SQLExprStatement): Boolean

  def endVisit(x: SQLExprStatement): Unit

  def visit(x: SQLAlterProcedureStatement): Boolean

  def endVisit(x: SQLAlterProcedureStatement): Unit

  def visit(x: SQLAlterViewStatement): Boolean

  def endVisit(x: SQLAlterViewStatement): Unit

  def visit(x: SQLDropEventStatement): Boolean

  def endVisit(x: SQLDropEventStatement): Unit

  def visit(x: SQLDropLogFileGroupStatement): Boolean

  def endVisit(x: SQLDropLogFileGroupStatement): Unit

  def visit(x: SQLDropServerStatement): Boolean

  def endVisit(x: SQLDropServerStatement): Unit

  def visit(x: SQLDropSynonymStatement): Boolean

  def endVisit(x: SQLDropSynonymStatement): Unit

  def visit(x: SQLRecordDataType): Boolean

  def endVisit(x: SQLRecordDataType): Unit

  def visit(x: SQLDropTypeStatement): Boolean

  def endVisit(x: SQLDropTypeStatement): Unit

  def visit(x: SQLExternalRecordFormat): Boolean

  def endVisit(x: SQLExternalRecordFormat): Unit

  def visit(x: SQLArrayDataType): Boolean

  def endVisit(x: SQLArrayDataType): Unit

  def visit(x: SQLMapDataType): Boolean

  def endVisit(x: SQLMapDataType): Unit

  def visit(x: SQLStructDataType): Boolean

  def endVisit(x: SQLStructDataType): Unit

  def visit(x: SQLStructDataType.Field): Boolean

  def endVisit(x: SQLStructDataType.Field): Unit

  def visit(x: SQLDropMaterializedViewStatement): Boolean

  def endVisit(x: SQLDropMaterializedViewStatement): Unit

  def visit(x: SQLAlterTableRenameIndex): Boolean

  def endVisit(x: SQLAlterTableRenameIndex): Unit

  def visit(x: SQLAlterSequenceStatement): Boolean

  def endVisit(x: SQLAlterSequenceStatement): Unit

  def visit(x: SQLAlterTableExchangePartition): Boolean

  def endVisit(x: SQLAlterTableExchangePartition): Unit
}

