package com.github.pedrovgs.kafkaplayground.flash

import cakesolutions.kafka.KafkaConsumer.Conf
import com.github.pedrovgs.kafkaplayground.flash.elasticsearch.ElasticClient
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._
import scala.concurrent.duration._

object TheFlashTweetsConsumer {
  private val groupId = "kafka-elasticsearch-consumer"
}

class TheFlashTweetsConsumer(private val brokerAddress: String,
                             private val topic: String,
                             private val elasticClient: ElasticClient) {

  import TheFlashTweetsConsumer._

  private val consumer = cakesolutions.kafka.KafkaConsumer(
    Conf(
      bootstrapServers = brokerAddress,
      keyDeserializer = new StringDeserializer(),
      valueDeserializer = new StringDeserializer(),
      groupId = s"$topic-$groupId",
      autoOffsetReset = OffsetResetStrategy.EARLIEST
    )
  )
  consumer.subscribe(List(topic).asJava)

  def poll(): Unit = ???

}
