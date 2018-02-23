package me.principality.session.receiver

import io.netty.buffer.Unpooled
import io.netty.channel.{Channel, ChannelInboundHandlerAdapter}

class BackHandler extends ChannelInboundHandlerAdapter with Receiver {

  import io.netty.channel.ChannelFuture
  import io.netty.channel.ChannelFutureListener
  import io.netty.channel.ChannelHandlerContext

  private var inboundChannel: Channel = _
  private var outboundChannel: Channel = _

  def this(inboundChannel: Channel) {
    this()
    this.inboundChannel = inboundChannel
  }

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    outboundChannel = ctx.channel()
    ctx.read
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    inboundChannel.writeAndFlush(msg).addListener((future: ChannelFuture) => {
      if (future.isSuccess) ctx.channel.read
      else future.channel.close
    })
  }

  override def channelInactive(ctx: ChannelHandlerContext): Unit = {
    closeOnFlush(inboundChannel)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace() // TODO: write to log
    closeOnFlush(ctx.channel)
  }

  private def closeOnFlush(ch: Channel): Unit = {
    if (ch.isActive) ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
  }

  override def writeAndFlush(msg: Object, listener: ChannelFutureListener): Unit = this.writeAndFlush(msg, listener)

  override def isActive: Boolean = this.outboundChannel.isActive
}
