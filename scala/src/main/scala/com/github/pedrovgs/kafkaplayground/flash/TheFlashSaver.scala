package com.github.pedrovgs.kafkaplayground.flash

import com.github.pedrovgs.kafkaplayground.flash.elasticsearch.ElasticClient
import com.typesafe.config.ConfigFactory

import scala.annotation.tailrec

object TheFlashSaver {

  private val config            = ConfigFactory.load()
  private val elasticsearchHost = config.getString("elasticsearch.host")
  private val elasticsearchUser = config.getString("elasticsearch.user")
  private val elasticsearchPass = config.getString("elasticsearch.pass")

  private val notLocatedTweetsConsumer = new TheFlashTweetsConsumer(
    brokerAddress = "localhost:29092",
    topic = "the-flash-tweets",
    elasticClient = new ElasticClient(elasticsearchHost = elasticsearchHost,
                                      elasticsearchUser = elasticsearchUser,
                                      elasticsearchPass = elasticsearchPass,
                                      elasticIndex = "unknown_location_tweets")
  )

  private val locatedTweetsConsumer = new TheFlashTweetsConsumer(
    brokerAddress = "localhost:29092",
    topic = "the-flash-tweets-with-location",
    elasticClient = new ElasticClient(elasticsearchHost = elasticsearchHost,
                                      elasticsearchUser = elasticsearchUser,
                                      elasticsearchPass = elasticsearchPass,
                                      elasticIndex = "located_tweets")
  )

  def main(args: Array[String]): Unit = ???
}
