package com.github.pedrovgs.kafkaplayground.flash

import com.github.pedrovgs.kafkaplayground.flash.elasticsearch.ElasticClient
import com.github.pedrovgs.kafkaplayground.utils.EmbeddedKafkaServer
import org.mockito.Mockito.verify
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

object TheFlashTweetsConsumerSpec {
  private val anyTopic        = "topic"
  private val anyContent      = "content"
  private val anyOtherContent = "anyOtherContent"
}

class TheFlashTweetsConsumerSpec
    extends FlatSpec
    with Matchers
    with EmbeddedKafkaServer
    with ScalaFutures
    with MockitoSugar
    with BeforeAndAfterEach {

  import TheFlashTweetsConsumerSpec._

  private var elasticClient: ElasticClient = _

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    elasticClient = mock[ElasticClient]
  }

  "TheFlashTweetsConsumer" should "create a new document for the configured index using the messages polled from the kafka cluster" in {
    produceMessage(anyTopic, anyContent)

    givenAElasticConsumer().poll()

    val expectedId = s"topic_0_0"
    verify(elasticClient).insertOrUpdate(expectedId, anyContent)
  }

  it should "send more than a message to elasticsearch" in {
    produceMessage(anyTopic, anyContent)
    produceMessage(anyTopic, anyOtherContent)

    givenAElasticConsumer().poll()

    verify(elasticClient).insertOrUpdate("topic_0_0", anyContent)
    verify(elasticClient).insertOrUpdate("topic_0_1", anyOtherContent)
  }

  private def givenAElasticConsumer() =
    new TheFlashTweetsConsumer(kafkaServerAddress(), anyTopic, elasticClient)

}
