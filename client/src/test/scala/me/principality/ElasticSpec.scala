package me.principality

import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.{InetSocketTransportAddress, TransportAddress}
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.specs2.Specification
import org.specs2.specification.core.SpecStructure

class ElasticSpec extends Specification {

  override def is: SpecStructure =
    s2"""
      elastic api check $open
    """

  def open = {
    import java.net.InetAddress
    import org.elasticsearch.action.get.GetResponse

    val settings = Settings.builder()
      .put("cluster.name", "elasticsearch_jianfeng")
      .put("client.transport.sniff", true)
      .build();

    val transportClient = new PreBuiltTransportClient(settings)
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300))

    val response = transportClient.prepareGet("customer", "external", "1").get
    System.out.println(response.toString())

    transportClient.connectedNodes().size() mustEqual 1
  }
}
