package me.principality.protocol.sql.ast

import java.util

trait SQLObject {
  def accept(visitor: SQLASTVisitor): Unit

  override def clone: SQLObject

  def getParent: SQLObject

  def setParent(parent: SQLObject): Unit

  def getAttributes: util.Map[String, AnyRef]

  def getAttribute(name: String): Any

  def putAttribute(name: String, value: Any): Unit

  def getAttributesDirect: util.Map[String, AnyRef]

  def output(buf: StringBuffer): Unit

  def addBeforeComment(comment: String): Unit

  def addBeforeComment(comments: util.List[String]): Unit

  def getBeforeCommentsDirect: util.List[String]

  def addAfterComment(comment: String): Unit

  def addAfterComment(comments: util.List[String]): Unit

  def getAfterCommentsDirect: util.List[String]

  def hasBeforeComment: Boolean

  def hasAfterComment: Boolean
}
