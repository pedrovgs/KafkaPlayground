package com.github.pedrovgs.kafkaplayground.flash

import com.github.pedrovgs.kafkaplayground.flash.elasticsearch.ElasticClient
import com.github.pedrovgs.kafkaplayground.utils.EmbeddedKafkaServer
import org.scalatest.concurrent.{PatienceConfiguration, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import org.mockito.Mockito.verify
import org.mockito.Mockito.never
import org.mockito.ArgumentMatchers.any
import scala.concurrent.duration._
object ElasticsearchConsumerSpec {
  private val anyTopic   = "topic"
  private val anyContent = "content"
}

class ElasticsearchConsumerSpec
    extends FlatSpec
    with Matchers
    with EmbeddedKafkaServer
    with ScalaFutures
    with MockitoSugar {

  import ElasticsearchConsumerSpec._

  private val elasticClient = mock[ElasticClient]

  override val topicsToClearAfterEach: Seq[String] = Seq(anyTopic)

  "ElsticsearchConsumer" should "create a new document for the configured index using the messages polled from the kafka cluster" in {
    val record = produceMessage(anyTopic, anyContent).futureValue(
      timeout = PatienceConfiguration.Timeout(10.seconds))

    givenAElasticConsumer().poll()

    val expectedId = s"${record.topic()}_${record.partition()}_${record.offset()}"
    verify(elasticClient).insertOrUpdate(expectedId, anyContent)
  }

  it should "not send any message to elastic if there are no records" in {
    givenAElasticConsumer().poll()

    verify(elasticClient, never()).insertOrUpdate(any(), any())
  }

  private def givenAElasticConsumer() =
    new ElasticsearchConsumer(kafkaServerAddress(), anyTopic, elasticClient)

}
