package me.principality.protocol.sql.ast

object SQLOrderingSpecification extends Enumeration {
  type SQLOrderingSpecification = Value
  val ASC, DESC = Value

  var name: String = _
  var nameClass: String = _

  this (name: String) {
    this ()
    this.name = name
    this.nameClass = name.toLowerCase
  }
}
