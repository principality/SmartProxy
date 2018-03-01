package me.principality.protocol.sql.ast

import me.principality.protocol.sql.visitor.SQLASTVisitor

class SQLCommentHint() extends SQLObjectImpl with SQLHint {
  private var text = null

  def this(text: String) {
    this()
    this.text = text
  }

  def getText: String = this.text

  def setText(text: String): Unit = {
    this.text = text
  }

  override protected def accept0(visitor: SQLASTVisitor): Unit = {
    visitor.visit(this)
    visitor.endVisit(this)
  }

  override def clone = new SQLCommentHint(text)

  override def output(buf: StringBuffer): Unit = {
    new SQLASTOutputVisitor(buf).visit(this)
  }
}
