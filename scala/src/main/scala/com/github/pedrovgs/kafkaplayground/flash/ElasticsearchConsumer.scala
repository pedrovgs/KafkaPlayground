package com.github.pedrovgs.kafkaplayground.flash

import cakesolutions.kafka.KafkaConsumer.Conf
import com.sksamuel.elastic4s.ElasticsearchClientUri
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.client.config.RequestConfig.Builder
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.kafka.common.serialization.StringDeserializer
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback
import com.sksamuel.elastic4s.http.HttpClient
import org.apache.kafka.clients.consumer.KafkaConsumer
import com.sksamuel.elastic4s.http.ElasticDsl._
import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ElasticsearchConsumer {
  private val groupId = "kafka-elasticsearch-consumer"
}

class ElasticsearchConsumer(private val brokerAddress: String,
                            private val topic: String,
                            private val elasticsearchHost: String = "https://kafka-testing-3470068779.eu-west-1.bonsaisearch.net",
                            private val elasticsearchUser: String = "6zuds9wifz",
                            private val elasticsearchPass: String = "8htlatmiod",
                            private val elasticIndex: String = "unknown_location_tweets",
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

  private val provider = {
    val provider = new BasicCredentialsProvider
    val credentials = new UsernamePasswordCredentials(elasticsearchUser, elasticsearchPass)
    provider.setCredentials(AuthScope.ANY, credentials)
    provider
  }
  private val client = HttpClient(ElasticsearchClientUri(elasticsearchHost, 9300), new RequestConfigCallback {
    override def customizeRequestConfig(requestConfigBuilder: Builder) = {
      requestConfigBuilder
    }
  }, (httpClientBuilder: HttpAsyncClientBuilder) => {
    httpClientBuilder.setDefaultCredentialsProvider(provider)
  })

  def start(): Unit = {
    consumer.subscribe(List(topic).asJava)
    sendTweetsInfoToElasticsearch(consumer)
  }

  @tailrec
  private def sendTweetsInfoToElasticsearch(consumer: KafkaConsumer[String, String]): Unit = {
    val records = consumer.poll(10.seconds.toMillis)
    records.forEach { record =>
      val content = record.value()
      println(content)
      client.execute {
        indexInto(elasticIndex) doc content
      }
    }
    sendTweetsInfoToElasticsearch(consumer)
  }

}
