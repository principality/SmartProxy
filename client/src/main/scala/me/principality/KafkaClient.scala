package me.principality

import java.io.FileInputStream
import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.util
import java.util.Properties

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.KafkaConsumer.Conf
import com.typesafe.config.ConfigFactory
import org.apache.calcite.adapter.java.ReflectiveSchema
import org.apache.calcite.avatica.ConnectStringParser
import org.apache.calcite.jdbc.CalciteConnection
import org.apache.calcite.schema.SchemaPlus
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords}
import org.apache.kafka.common.serialization.StringDeserializer

object KafkaClient {
  def main(args: Array[String]): Unit = {
    // 初始化jdbc driver
    Class.forName("org.apache.calcite.jdbc.Driver")
    val info: Properties = new Properties()
    val model = "inline:" +
      """
        |{
        |  version: "1.0",
        |    schemas: [
        |         {
        |                type: "custom",
        |                name: "elasticsearch",
        |                factory: "org.apache.calcite.adapter.elasticsearch5.Elasticsearch5SchemaFactory",
        |                operand: {
        |                     coordinates: "{'127.0.0.1': 9300}",
        |                     userConfig: "{}",
        |                     index: "customer"
        |                }
        |         }
        |    ]
        |}
      """.stripMargin
    info.put("model", model)

    val connection = DriverManager.getConnection("jdbc:avatica:remote:url=http://localhost:28000", info)
    val calciteConnection = connection.unwrap(classOf[CalciteConnection])
    val rootSchema = calciteConnection.getRootSchema
    val statement = connection.createStatement
    val resultSet = statement.executeQuery("select * from external")
    PrintResultSet(resultSet)
    resultSet.close()
    return

    val conf = ConfigFactory.load()
    val consumer = KafkaConsumer(
      Conf(conf, new StringDeserializer(), new StringDeserializer())
    )
    consumer.subscribe(util.Arrays.asList("customer_external"))

    while (true) {
      val records = consumer.poll(100)
      records.forEach({ case record: ConsumerRecord[String, String] =>
        System.out.printf("key = %s, value = %s\n", record.key(), record.value())

        // write to jdbc

      })
    }
    statement.close()
    connection.close()
  }

  private def PrintResultSet(resultSet: ResultSet): Unit = {
    val buf = new StringBuilder
    while ( {
      resultSet.next
    }) {
      val n = resultSet.getMetaData.getColumnCount
      var i = 1
      while ( {
        i <= n
      }) {
        buf.append(
          if (i > 1) "; " else "")
          .append(resultSet.getMetaData.getColumnLabel(i))
          .append("=").append(resultSet.getObject(i)) {
          i += 1
          i - 1
        }
      }
      System.out.println(buf.toString)
      buf.setLength(0)
    }
  }
}