package me.principality.protocol.sql.builder

object SQLBuilderFactory {
  def createSelectSQLBuilder(dbType: String) = new SQLSelectBuilderImpl(dbType)

  def createSelectSQLBuilder(sql: String, dbType: String) = new SQLSelectBuilderImpl(sql, dbType)

  def createDeleteBuilder(dbType: String) = new SQLDeleteBuilderImpl(dbType)

  def createDeleteBuilder(sql: String, dbType: String) = new SQLDeleteBuilderImpl(sql, dbType)

  def createUpdateBuilder(dbType: String) = new SQLUpdateBuilderImpl(dbType)

  def createUpdateBuilder(sql: String, dbType: String) = new SQLUpdateBuilderImpl(sql, dbType)
}
