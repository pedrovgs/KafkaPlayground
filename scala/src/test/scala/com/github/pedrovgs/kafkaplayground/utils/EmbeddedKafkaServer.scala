package com.github.pedrovgs.kafkaplayground.utils

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.testkit.KafkaServer
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._
import scala.concurrent.duration._

trait EmbeddedKafkaServer {

  private val kafkaServer = new KafkaServer

  def startKafkaServer(): Unit = kafkaServer.startup()

  def stopKafkaServer(): Unit = kafkaServer.close()

  def kafkaServerAddress(): String = s"localhost:${kafkaServer.kafkaPort}"

  def zookeeperServerAddress(): String = s"localhost:${kafkaServer.zookeeperPort}"

  def recordsForTopic(topic: String): Iterable[ConsumerRecord[String, String]] = {
    val baseConfig = ConfigFactory.parseString(
      s"""
         |{
         |  bootstrap.servers = "localhost:${kafkaServer.kafkaPort}"
         |}
       """.stripMargin)
    val consumer = KafkaConsumer(KafkaConsumer.Conf(
      ConfigFactory.parseString(
        s"""
           |{
           |  topics = ["$topic"]
           |  group.id = "testing-consumer"
           |  auto.offset.reset = "earliest"
           |}
          """.stripMargin).withFallback(baseConfig),
      keyDeserializer = new StringDeserializer(),
      valueDeserializer = new StringDeserializer()
    ))
    consumer.subscribe(List(topic).asJava)
    val records = consumer.poll(10.seconds.toMillis).asScala
    consumer.close()
    records
  }

}
