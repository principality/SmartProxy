package me.principality.protocol.sql.ast

import java.util

abstract class SQLObjectImpl() extends SQLObject {
  protected var parent: SQLObject = null
  protected var attributes: util.Map[String, AnyRef] = null

  override final def accept(visitor: SQLASTVisitor): Unit = {
    if (visitor == null) throw new IllegalArgumentException
    visitor.preVisit(this)
    accept0(visitor)
    visitor.postVisit(this)
  }

  protected def accept0(visitor: SQLASTVisitor): Unit

  final protected def acceptChild(visitor: SQLASTVisitor, children: util.List[_ <: SQLObject]): Unit = {
    if (children == null) return
    import scala.collection.JavaConversions._
    for (child <- children) {
      acceptChild(visitor, child)
    }
  }

  final protected def acceptChild(visitor: SQLASTVisitor, child: SQLObject): Unit = {
    if (child == null) return
    child.accept(visitor)
  }

  override def output(buf: StringBuffer): Unit = {
    buf.append(super.toString)
  }

  override def toString: String = {
    val buf = new StringBuffer
    output(buf)
    buf.toString
  }

  override def getParent: SQLObject = parent

  override def setParent(parent: SQLObject): Unit = {
    this.parent = parent
  }

  override def getAttributes: util.Map[String, AnyRef] = {
    if (attributes == null) attributes = new util.HashMap[String, AnyRef](1)
    attributes
  }

  override def getAttribute(name: String): Any = {
    if (attributes == null) return null
    attributes.get(name)
  }

  override def putAttribute(name: String, value: Any): Unit = {
    if (attributes == null) attributes = new util.HashMap[String, AnyRef](1)
    attributes.put(name, value)
  }

  override def getAttributesDirect: util.Map[String, AnyRef] = attributes

  @SuppressWarnings(Array("unchecked")) override def addBeforeComment(comment: String): Unit = {
    if (comment == null) return
    if (attributes == null) attributes = new util.HashMap[String, AnyRef](1)
    var comments = attributes.get("format.before_comment").asInstanceOf[util.List[String]]
    if (comments == null) {
      comments = new util.ArrayList[String](2)
      attributes.put("format.before_comment", comments)
    }
    comments.add(comment)
  }

  @SuppressWarnings(Array("unchecked")) override def addBeforeComment(comments: util.List[String]): Unit = {
    if (attributes == null) attributes = new util.HashMap[String, AnyRef](1)
    val attrComments = attributes.get("format.before_comment").asInstanceOf[util.List[String]]
    if (attrComments == null) attributes.put("format.before_comment", comments)
    else attrComments.addAll(comments)
  }

  @SuppressWarnings(Array("unchecked")) override def getBeforeCommentsDirect: util.List[String] = {
    if (attributes == null) return null
    attributes.get("format.before_comment").asInstanceOf[util.List[String]]
  }

  @SuppressWarnings(Array("unchecked")) override def addAfterComment(comment: String): Unit = {
    if (attributes == null) attributes = new util.HashMap[String, AnyRef](1)
    var comments = attributes.get("format.after_comment").asInstanceOf[util.List[String]]
    if (comments == null) {
      comments = new util.ArrayList[String](2)
      attributes.put("format.after_comment", comments)
    }
    comments.add(comment)
  }

  @SuppressWarnings(Array("unchecked")) override def addAfterComment(comments: util.List[String]): Unit = {
    if (comments == null) return
    if (attributes == null) attributes = new util.HashMap[String, AnyRef](1)
    val attrComments = attributes.get("format.after_comment").asInstanceOf[util.List[String]]
    if (attrComments == null) attributes.put("format.after_comment", comments)
    else attrComments.addAll(comments)
  }

  @SuppressWarnings(Array("unchecked")) override def getAfterCommentsDirect: util.List[String] = {
    if (attributes == null) return null
    attributes.get("format.after_comment").asInstanceOf[util.List[String]]
  }

  override def hasBeforeComment: Boolean = {
    if (attributes == null) return false
    val comments = attributes.get("format.before_comment").asInstanceOf[util.List[String]]
    if (comments == null) return false
    !comments.isEmpty
  }

  override def hasAfterComment: Boolean = {
    if (attributes == null) return false
    val comments = attributes.get("format.after_comment").asInstanceOf[util.List[String]]
    if (comments == null) return false
    !comments.isEmpty
  }

  override def clone = throw new UnsupportedOperationException(this.getClass.getName)

  def computeDataType: SQLDataType = null

}
