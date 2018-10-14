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

  def apply(tweet: Tweet): Future[Tweet] = {
    println(s"Sending tweet to the associated topic: ${tweet.text}")
    tweet.geo match {
      case Some(coordinates) => sendGeoLocatedFlashAdvertisement(tweet, coordinates)
      case _                 => sendUnknownLocationFlashAdvertisement(tweet)
    }
  }

  private def sendGeoLocatedFlashAdvertisement(tweet: Tweet, coordinates: Geo): Future[Tweet] =
    sendRecordToProducer(
      topic = locatedFlashTopic,
      message = s"""
           |{
           |  "latitude": ${coordinates.coordinates.head},
           |  "longitude": ${coordinates.coordinates.last},
           |  "id": "${tweet.id}",
           |  "message": "${StringEscapeUtils.escapeJava(tweet.text)}"
           |}
       """.stripMargin
    ).map(_ => tweet)

  private def sendUnknownLocationFlashAdvertisement(tweet: Tweet): Future[Tweet] =
    sendRecordToProducer(
      topic = unknownLocationFlashTopic,
      message = s"""
           |{
           |  "message": "${StringEscapeUtils.escapeJava(tweet.text)}"
           |}
        """.stripMargin
    ).map(_ => tweet)

  private def sendRecordToProducer(topic: String, message: String) =
    flashProducer.send(
      KafkaProducerRecord[String, String](topic = topic, value = message)
    )
}
