package me.principality.protocol.sql.builder

trait SQLBuilder {}

class SQLBuilderImpl extends SQLBuilder {}

object SQLBuilderImpl {
  def toSQLExpr(obj: Any, dbType: String): SQLExpr = {
    if (obj == null) return new SQLNullExpr
    if (obj.isInstanceOf[Integer]) return new SQLIntegerExpr(obj.asInstanceOf[Integer])
    if (obj.isInstanceOf[Number]) return new SQLNumberExpr(obj.asInstanceOf[Number])
    if (obj.isInstanceOf[String]) return new SQLCharExpr(obj.asInstanceOf[String])
    if (obj.isInstanceOf[Boolean]) return new SQLBooleanExpr(obj.asInstanceOf[Boolean])
    throw new IllegalArgumentException("not support : " + obj.getClass.getName)
  }
}
