package com.github.pedrovgs.kafkaplayground.utils

import cakesolutions.kafka.KafkaProducerRecord
import cakesolutions.kafka.testkit.KafkaServer
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.scalatest.{BeforeAndAfter, Suite}

import scala.concurrent.duration._

trait EmbeddedKafkaServer extends BeforeAndAfter {
  this: Suite =>

  private var kafkaServer: KafkaServer = _

  before {
    kafkaServer = new KafkaServer
    startKafkaServer()
  }

  after {
    stopKafkaServer()
  }

  def startKafkaServer(): Unit = kafkaServer.startup()

  def stopKafkaServer(): Unit = kafkaServer.close()

  def kafkaServerAddress(): String = s"localhost:${kafkaServer.kafkaPort}"

  def zookeeperServerAddress(): String = s"localhost:${kafkaServer.zookeeperPort}"

  def recordsForTopic(topic: String, expectedNumberOfRecords: Int = 1): Iterable[String] =
    kafkaServer
      .consume[String, String](
        topic = topic,
        keyDeserializer = new StringDeserializer,
        valueDeserializer = new StringDeserializer,
        expectedNumOfRecords = expectedNumberOfRecords,
        timeout = 10.seconds.toMillis
      )
      .map(_._2)

  def produceMessage(topic: String, content: String): Unit =
    kafkaServer.produce(
      topic = topic,
      records = Seq(KafkaProducerRecord[String, String](topic = topic, value = content)),
      keySerializer = new StringSerializer(),
      valueSerializer = new StringSerializer()
    )

}
