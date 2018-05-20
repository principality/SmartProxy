package me.principality.server

import com.typesafe.config.ConfigFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import me.principality.session.HandlerFactory

class ProxyServer {
  def run(host: String, port: Int): Unit = {
    val masterGroup = new NioEventLoopGroup()
    val workerGroup = new NioEventLoopGroup()

    try {
      val bootstrap = new ServerBootstrap()

      bootstrap.group(masterGroup, workerGroup)
        .channel(classOf[NioServerSocketChannel])
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = ch.pipeline().addLast(HandlerFactory.createHandler())
        })

      val future = bootstrap.bind(host, port).sync()
      future.channel().closeFuture().sync()

    } finally {
      masterGroup.shutdownGracefully()
      workerGroup.shutdownGracefully()
    }
  }
}

object ProxyServer {
  def apply(): Unit = {
    val conf = ConfigFactory.load()
    val host = conf.getString("proxy.front.host")
    val port = conf.getInt("proxy.front.port")
    val server = new ProxyServer
    server.run(host, port)
  }
}