package com.github.pedrovgs.kafkaplayground

import cakesolutions.kafka.KafkaProducer.Conf
import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import com.danielasfregola.twitter4s.entities.{Geo, Tweet}
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.{ExecutionContext, Future}

object GeolocatedTweetsProducer {
  private val unknownLocationFlashTopic = "the-flash-tweets"
  private val locatedFlashTopic         = "the-flash-tweets-with-location"
  private val brokerAddress             = "localhost:29092"
}

class GeolocatedTweetsProducer(implicit val ec: ExecutionContext = ExecutionContext.global) {

  import GeolocatedTweetsProducer._

  private val flashProducer = KafkaProducer(
    Conf(new StringSerializer(), new StringSerializer(), bootstrapServers = brokerAddress)
  )

  def apply(tweet: Tweet): Future[Tweet] = {
    tweet.geo match {
      case Some(coordinates) => sendGeoLocatedFlashAdvertisement(tweet, coordinates)
      case _                 => sendUnknownLocationFlashAdvertisement(tweet)
    }
  }

  private def sendGeoLocatedFlashAdvertisement(tweet: Tweet, coordinates: Geo): Future[Tweet] = {
    val message =
      s"""
         |{
         |  "latitude": ${coordinates.coordinates.headOption},
         |  "longitude": ${coordinates.coordinates.lastOption},
         |  "id": "${tweet.id}",
         |  "message": "${tweet.text}"
         |}
       """
    sendRecordToProducer(locatedFlashTopic, tweet, message)
  }

  private def sendUnknownLocationFlashAdvertisement(tweet: Tweet): Future[Tweet] = {
    val message = tweet.text
    sendRecordToProducer(unknownLocationFlashTopic, tweet, message)
  }

  private def sendRecordToProducer(topic: String, tweet: Tweet, message: String) = {
    val record =
      KafkaProducerRecord[String, String](topic = unknownLocationFlashTopic, value = message)
    flashProducer.send(record).map(_ => tweet)
  }
}
