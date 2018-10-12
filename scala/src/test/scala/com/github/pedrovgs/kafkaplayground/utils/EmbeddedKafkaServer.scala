package com.github.pedrovgs.kafkaplayground.utils

import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.testkit.KafkaServer
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Suite}

import scala.collection.JavaConverters._
import scala.concurrent.duration._

trait EmbeddedKafkaServer extends BeforeAndAfter with BeforeAndAfterAll {
  this: Suite =>

  val topicsToClearAfterEach: Seq[String] = Seq()

  private val kafkaServer = new KafkaServer

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    startKafkaServer()
  }

  before {
    clearTopics()
  }

  after {
    clearTopics()
  }

  override protected def afterAll(): Unit = {
    stopKafkaServer()
    super.afterAll()
  }

  def startKafkaServer(): Unit = kafkaServer.startup()

  def stopKafkaServer(): Unit = {
    kafkaServer.close()
  }

  def kafkaServerAddress(): String = s"localhost:${kafkaServer.kafkaPort}"

  def zookeeperServerAddress(): String = s"localhost:${kafkaServer.zookeeperPort}"

  def clearTopics(topics: Seq[String] = topicsToClearAfterEach): Unit =
    topics.foreach(topic => recordsForTopic(topic))

  def recordsForTopic(topic: String): Iterable[ConsumerRecord[String, String]] = {
    val baseConfig = ConfigFactory.parseString(s"""
         |{
         |  bootstrap.servers = "localhost:${kafkaServer.kafkaPort}"
         |}
       """.stripMargin)
    val consumer = KafkaConsumer(
      KafkaConsumer.Conf(
        ConfigFactory.parseString(s"""
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
    val records = consumer.poll(10.second.toMillis).asScala
    consumer.commitSync()
    consumer.close()
    records
  }

}
