package me.principality.server

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.apache.calcite.avatica.jdbc.JdbcMeta
import org.apache.calcite.avatica.remote.Driver.Serialization
import org.apache.calcite.avatica.remote.LocalService
import org.apache.calcite.avatica.server.HttpServer
import org.apache.calcite.avatica.util.Unsafe

class AvaticaServer {
  private val configure: Config = ConfigFactory.load()
  private val jdbcUrl = configure.getString("avatica.jdbcurl")
  private val port = configure.getInt("avatica.port")
  private val serialization = configure.getString("avatica.serialization") match {
    case "PROTOBUF" => Serialization.PROTOBUF
    case "JSON" => Serialization.JSON
  }
  private val logger: Logger = Logger[AvaticaServer]
  private var server: HttpServer = _

  def start(): Unit = {
    if (null != server) {
      logger.error("The server was already started")
      Unsafe.systemExit(ExitCodes.ALREADY_STARTED.id)
      return
    }
    try {
      val meta = new JdbcMeta(jdbcUrl)
      val service = new LocalService(meta)
      // Construct the server
      this.server = new HttpServer.Builder().withHandler(service, serialization).withPort(port).build()
      // Then start it
      server.start()
      logger.info("Started Avatica server on port {} with serialization {}", server.getPort, serialization)
    } catch {
      case e: Exception =>
        logger.error("Failed to start Avatica server", e)
        Unsafe.systemExit(ExitCodes.START_FAILED.id)
    }
  }

  def stop(): Unit = {
    if (null != server) {
      server.stop()
      server = null
    }
  }

  @throws[InterruptedException]
  def join(): Unit = {
    server.join()
  }

  def info(log: String): Unit = {
    logger.info(log)
  }

  private object ExitCodes extends Enumeration {
    type ExitCodes = Value
    val NORMAL, ALREADY_STARTED, START_FAILED, USAGE = Value
  }

}

object AvaticaServer {
  def apply(): Unit = {
    val server = new AvaticaServer

    server.start()
    // Try to clean up when the server is stopped.
    Runtime.getRuntime.addShutdownHook(new Thread(() => {
      server.info("Stopping server")
      server.stop()
      server.info("Server stopped")
    }))
    try
      server.join()
    catch {
      case e: InterruptedException =>
        Thread.currentThread.interrupt()
    }
  }
}