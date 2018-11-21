package com.github.pedrovgs.kafkaplayground.flash

import cakesolutions.kafka.KafkaProducer.Conf
import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import com.danielasfregola.twitter4s.entities.{Geo, Tweet}
import org.apache.commons.lang.StringEscapeUtils
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.{ExecutionContext, Future}

object TheFlashTweetsProducer {
  private val unknownLocationFlashTopic = "the-flash-tweets"
  private val locatedFlashTopic         = "the-flash-tweets-with-location"
}

class TheFlashTweetsProducer(private val brokerAddress: String,
                             implicit val ec: ExecutionContext = ExecutionContext.global) {

  import TheFlashTweetsProducer._

  private val flashProducer = KafkaProducer(
    Conf(
      keySerializer = new StringSerializer(),
      valueSerializer = new StringSerializer(),
      bootstrapServers = brokerAddress,
      enableIdempotence = true,
      lingerMs = 20,
      batchSize = 32 * 1024
    ).withProperty("compression.type", "snappy")
  )

  def apply(tweet: Tweet): Future[Tweet] = ???

  private def sendGeoLocatedFlashAdvertisement(tweet: Tweet, coordinates: Geo): Future[Tweet] = ???

  private def sendUnknownLocationFlashAdvertisement(tweet: Tweet): Future[Tweet] = ???

}
