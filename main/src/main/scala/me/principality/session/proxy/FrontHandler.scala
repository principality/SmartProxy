package me.principality.session.proxy

import com.typesafe.config.{Config, ConfigFactory}
import io.netty.bootstrap.Bootstrap
import io.netty.channel._

class FrontHandler extends ChannelInboundHandlerAdapter {

  private val conf: Config = ConfigFactory.load()
  private val remoteHost: String = conf.getString("proxy.back.host")
  private val remotePort: Int = conf.getInt("proxy.back.port")
  private var receiver: BackHandler = _

  override def channelActive(ctx: ChannelHandlerContext): Unit = {

    val inboundChannel = ctx.channel

    // Start the connection attempt.
    val b = new Bootstrap
    b.group(inboundChannel.eventLoop)
      .channel(ctx.channel.getClass)
      .handler(new BackHandler(inboundChannel))
      .option[java.lang.Boolean](ChannelOption.AUTO_READ, false)

    val f = b.connect(remoteHost, remotePort)
    receiver = new BackHandler(f.channel)
    f.addListener((future: ChannelFuture) => {
      if (future.isSuccess) { // connection complete start to read first data
        inboundChannel.read
      }
      else { // Close the connection if the connection attempt has failed.
        inboundChannel.close
      }
    })
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Object): Unit = {
    if (receiver.isActive) receiver.writeAndFlush(msg, new ChannelFutureListener() {
      override def operationComplete(future: ChannelFuture): Unit = {
        if (future.isSuccess) { // was able to flush out data, start to read the next chunk
          ctx.channel.read
        }
        else future.channel.close
      }
    })
  }

  override def channelReadComplete(context: ChannelHandlerContext): Unit = {
    context.flush()
  }

  override def channelInactive(ctx: ChannelHandlerContext): Unit = {
    if (receiver != null) closeOnFlush(ctx.channel)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    closeOnFlush(ctx.channel)
  }

  import io.netty.buffer.Unpooled
  import io.netty.channel.ChannelFutureListener

  private def closeOnFlush(ch: Channel): Unit = {
    if (ch.isActive) ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
  }
}
