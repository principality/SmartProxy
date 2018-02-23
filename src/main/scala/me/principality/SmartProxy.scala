package me.principality

import com.typesafe.config.ConfigFactory
import me.principality.server.Server

object SmartProxy {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load()
    val host = conf.getString("front.host")
    val port = conf.getInt("front.port")
    val server = new Server
    server.run(host, port)
  }

}
