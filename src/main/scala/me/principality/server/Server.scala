package me.principality.server

import java.nio.channels.SocketChannel

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import me.principality.session.FrontHandler

class Server {
  def run(host: String, port: Int): Unit = {
    val bossGroup = new NioEventLoopGroup()
    val workGroup = new NioEventLoopGroup()

    try {
      val bootstrap = new ServerBootstrap()

      bootstrap.group(bossGroup, workGroup)
        .channel(classOf[NioServerSocketChannel])
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = ch.pipeline().addLast(new FrontHandler)
        })

      val future = bootstrap.bind(host, port).sync()
      future.channel().closeFuture().sync()

    } finally {
      bossGroup.shutdownGracefully()
      workGroup.shutdownGracefully()
    }
  }
}
