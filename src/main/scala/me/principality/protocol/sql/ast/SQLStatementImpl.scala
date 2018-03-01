package me.principality.protocol.sql.ast

import java.util
import java.util.List

import me.principality.protocol.sql.util.SQLUtils
import me.principality.protocol.sql.visitor.SQLASTVisitor

abstract class SQLStatementImpl() extends SQLObjectImpl with SQLStatement {
  protected var dbType: String = _
  protected var afterSemi: Boolean = false
  protected var headHints: util.List[SQLCommentHint] = _

  def this(dbType: String) {
    this()
    this.dbType = dbType
  }

  override def getDbType: String = dbType

  def setDbType(dbType: String): Unit = {
    this.dbType = dbType
  }

  override def toString: String = SQLUtils.toSQLString(this, dbType)

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    throw new UnsupportedOperationException(this.getClass.getName)
  }

  override def getChildren: util.List[SQLObject] = throw new UnsupportedOperationException(this.getClass.getName)

  override def isAfterSemi: Boolean = afterSemi

  override def setAfterSemi(afterSemi: Boolean): Unit = {
    this.afterSemi = afterSemi
  }

  override def clone: SQLStatement = throw new UnsupportedOperationException(this.getClass.getName)

  def getHeadHintsDirect: util.List[SQLCommentHint] = headHints

  def setHeadHints(headHints: util.List[SQLCommentHint]): Unit = {
    this.headHints = headHints
  }
}
