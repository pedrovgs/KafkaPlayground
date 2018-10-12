package com.github.pedrovgs.kafkaplayground.flash

import java.util.Date

import com.danielasfregola.twitter4s.entities.{Coordinates, Tweet}
import com.github.pedrovgs.kafkaplayground.utils.EmbeddedKafkaServer
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

object TheFlashTweetsProducerSpec {
  private val unknownLocationFlashTopic = "the-flash-tweets"
  private val locatedFlashTopic = "the-flash-tweets-with-location"
  private val anyNotGeoLocatedTweet = Tweet(
    created_at = new Date(),
    id = 1L,
    id_str = "1",
    source = "source",
    text = "I've seen the fastest man alive!"
  )

  private val anyGeoLocatedTweet = anyNotGeoLocatedTweet.copy(
    coordinates = Some(Coordinates(Seq(12.0, 11.0), "lat-long"))
  )
}

class TheFlashTweetsProducerSpec
  extends FlatSpec
    with Matchers
    with BeforeAndAfter
    with EmbeddedKafkaServer
    with ScalaFutures {

  import TheFlashTweetsProducerSpec._

  before {
    startKafkaServer()
  }

  after {
    stopKafkaServer()
  }

  private val producer = new TheFlashTweetsProducer(kafkaServerAddress())

  "TheFlashTweetsProducer" should "return the tweet passed as param if the tweet has no geo location info" in {
    val result = producer(anyNotGeoLocatedTweet).futureValue

    result shouldBe anyGeoLocatedTweet
  }

  it should "send a record with just the text of the tweet to the the-flash-tweets topic if the tweet has no geo location info" in {
    producer(anyNotGeoLocatedTweet).futureValue

    val records = recordsForTopic(unknownLocationFlashTopic)

    records.size shouldBe 1
    records.head.value shouldBe anyGeoLocatedTweet.text
  }

  it should "return the tweet passed as param if the tweet has geo location info" in {
    val result = producer(anyGeoLocatedTweet).futureValue

    result shouldBe anyGeoLocatedTweet
  }

  it should "send a record with just the text of the tweet to the the-flash-tweets-with-location topic if the tweet has geo location info" in {
    producer(anyGeoLocatedTweet).futureValue

    val records = recordsForTopic(locatedFlashTopic)

    records.size shouldBe 1
    records.head.value shouldBe anyGeoLocatedTweet.text
  }

}
