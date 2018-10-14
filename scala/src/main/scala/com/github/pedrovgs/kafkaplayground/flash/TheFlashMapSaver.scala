package com.github.pedrovgs.kafkaplayground.flash

import com.typesafe.config.ConfigFactory

object TheFlashMapSaver {

  private val config            = ConfigFactory.load()
  private val elasticsearchHost = config.getString("elasticsearch.host")
  private val elasticsearchUser = config.getString("elasticsearch.user")
  private val elasticsearchPass = config.getString("elasticsearch.pass")

  private val notLocatedTweetsConsumer = new ElasticsearchConsumer(
    brokerAddress = "localhost:29092",
    topic = "the-flash-tweets",
    elasticsearchHost = elasticsearchHost,
    elasticsearchUser = elasticsearchUser,
    elasticsearchPass = elasticsearchPass,
    elasticIndex = "unknown_location_tweets"
  )

  private val locatedTweetsConsumer = new ElasticsearchConsumer(
    brokerAddress = "localhost:29092",
    topic = "the-flash-tweets",
    elasticsearchHost = "https://kafka-testing-3470068779.eu-west-1.bonsaisearch.net",
    elasticsearchUser = "6zuds9wifz",
    elasticsearchPass = "8htlatmiod",
    elasticIndex = "located_tweets"
  )

  def main(args: Array[String]): Unit = {
    notLocatedTweetsConsumer.start()
    locatedTweetsConsumer.start()
  }

}
