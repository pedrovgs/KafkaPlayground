package com.github.pedrovgs.kafkaplayground.flash

import cakesolutions.kafka.KafkaConsumer.Conf
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.core.Index
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ElasticsearchConsumer {
  private val groupId = "kafka-elasticsearch-consumer"
}

class ElasticsearchConsumer(private val brokerAddress: String,
                            private val topic: String,
                            private val elasticsearchHost: String,
                            private val elasticsearchUser: String,
                            private val elasticsearchPass: String,
                            private val elasticIndex: String,
                            implicit val ec: ExecutionContext = ExecutionContext.global) {

  import ElasticsearchConsumer._

  private val consumer = cakesolutions.kafka.KafkaConsumer(
    Conf(
      bootstrapServers = brokerAddress,
      keyDeserializer = new StringDeserializer(),
      valueDeserializer = new StringDeserializer(),
      groupId = groupId
    )
  )

  private val client: JestClient = {
    val factory  = new JestClientFactory()
    val provider = new BasicCredentialsProvider()
    provider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(elasticsearchUser, elasticsearchPass))
    factory.setHttpClientConfig(
      new HttpClientConfig.Builder(elasticsearchHost)
        .credentialsProvider(provider)
        .build())
    factory.getObject
  }

  def start(): Unit = {
    consumer.subscribe(List(topic).asJava)
    sendTweetsInfoToElasticsearch(consumer)
  }

  @tailrec
  private def sendTweetsInfoToElasticsearch(consumer: KafkaConsumer[String, String]): Unit = {
    val records = consumer.poll(10.seconds.toMillis)
    records.forEach { record =>
      val id = s"${record.topic()}_${record.partition()}_${record.offset()}"
      val content = record.value()
      println(s"Saving topic content into elastic: $content")
      saveContentIntoElasticsearch(id, content)
    }
    sendTweetsInfoToElasticsearch(consumer)
  }

  private def saveContentIntoElasticsearch(id: String, content: String) = {
    try {
      val index  = new Index.Builder(content).index(elasticIndex).`type`("tweets").build
      val result = client.execute(index)
      println(s"Elasticsearch new document id: ${result.getId}")
    } catch {
      case e: Exception =>
        println(e.getMessage)
    }
  }
}
