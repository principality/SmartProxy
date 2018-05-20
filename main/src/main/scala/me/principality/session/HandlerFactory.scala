package me.principality.session

import io.netty.channel.ChannelInboundHandlerAdapter
import me.principality.session.proxy.FrontHandler

object HandlerFactory {
  def createHandler(): ChannelInboundHandlerAdapter = new FrontHandler

}
