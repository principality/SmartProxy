package me.principality.protocol.sql.visitor

object VisitorFeature extends Enumeration {
  type VisitorFeature = Value
  val OutputUCase, OutputPrettyFormat, OutputParameterized, OutputDesensitize, OutputUseInsertValueClauseOriginalString, OutputSkipSelectListCacheString, OutputSkipInsertColumnsString, OutputParameterizedQuesUnMergeInList,

  /**
    * @deprecated
    */
  OutputKeepParenthesisWhenNotExpr = Value

  def this {
    this()
    mask = 1 << ordinal
  }

  var mask: Int
  = 0d ef isEnabled(features: Int, feature: VisitorFeature): Boolean
  =
  {
    return (features & feature.mask) != 0
  }
  def config(features: Int, feature: VisitorFeature, state: Boolean): Int
  =
  {
    if (state) features |= feature.mask
    else features &= ~feature.mask
    features
  }
  def of(features: VisitorFeature *
  ): Int = {
    if (features == null) return 0
    var value = 0
    for (feature <- features) {
      value |= feature.mask
    }
    value
  }
}
