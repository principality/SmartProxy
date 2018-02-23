package me.principality.session.receiver

import io.netty.channel.ChannelFutureListener

trait Receiver {

  def writeAndFlush(msg: Object, listener: ChannelFutureListener)

  def isActive: Boolean
}
