package com.github.pedrovgs.kafkaplayground.flash

import java.util.Date

import com.danielasfregola.twitter4s.entities.{Geo, Tweet}
import com.github.pedrovgs.kafkaplayground.utils.EmbeddedKafkaServer
import org.scalatest.concurrent.{PatienceConfiguration, ScalaFutures}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.concurrent.duration._

object TheFlashTweetsProducerSpec {
  private val unknownLocationFlashTopic = "the-flash-tweets"
  private val locatedFlashTopic         = "the-flash-tweets-with-location"
  private val anyNotGeoLocatedTweet = Tweet(
    created_at = new Date(),
    id = 1L,
    id_str = "1",
    source = "source",
    text = "I've seen the fastest man alive!"
  )

  private val anyGeoLocatedTweet = anyNotGeoLocatedTweet.copy(
    geo = Some(Geo(Seq(12.0, 11.0), "lat-long"))
  )
}

class TheFlashTweetsProducerSpec
    extends FlatSpec
    with Matchers
    with EmbeddedKafkaServer
    with ScalaFutures
    with BeforeAndAfter {

  import TheFlashTweetsProducerSpec._

  "TheFlashTweetsProducer" should "return the tweet passed as param if the tweet has no geo location info" in {
    val result = produceTweet(anyNotGeoLocatedTweet)

    result shouldBe anyNotGeoLocatedTweet
  }

  it should "send a record with just the text of the tweet to the the-flash-tweets topic if the tweet has no geo location info" in {
    produceTweet(anyNotGeoLocatedTweet)

    val records = recordsForTopic(unknownLocationFlashTopic)

    val expectedMessage =
      s"""
         |{
         |  "message": "I've seen the fastest man alive!"
         |}
        """.stripMargin
    records.size shouldBe 1
    records.head shouldBe expectedMessage
  }

  it should "return the tweet passed as param if the tweet has geo location info" in {
    val result = produceTweet(anyGeoLocatedTweet)

    result shouldBe anyGeoLocatedTweet
  }

  it should "send a record with just the text of the tweet to the the-flash-tweets-with-location topic if the tweet has geo location info" in {
    produceTweet(anyGeoLocatedTweet)

    val records = recordsForTopic(locatedFlashTopic)

    val expectedMessage =
      s"""
         |{
         |  "latitude": 12.0,
         |  "longitude": 11.0,
         |  "id": "1",
         |  "message": "I've seen the fastest man alive!"
         |}
       """.stripMargin
    records.size shouldBe 1
    records.head shouldBe expectedMessage
  }

  it should "send a not geo-located tweet to a topic and another geo-located to the other topic configured" in {
    produceTweet(anyNotGeoLocatedTweet)
    produceTweet(anyGeoLocatedTweet)

    val locatedTopicRecords         = recordsForTopic(locatedFlashTopic)
    val unknownLocationTopicRecords = recordsForTopic(unknownLocationFlashTopic)

    locatedTopicRecords.size shouldBe 1
    unknownLocationTopicRecords.size shouldBe 1
  }

  private def produceTweet(tweet: Tweet) =
    new TheFlashTweetsProducer(kafkaServerAddress())(tweet)
      .futureValue(timeout = PatienceConfiguration.Timeout(1.seconds))

}
