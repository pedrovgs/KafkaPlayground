package com.github.pedrovgs.kafkaplayground.flash

object TheFlashTweetsSaver {

  private val notLocatedTweetsConsumer = new ElasticsearchConsumer(
    brokerAddress = "localhost:29092",
    topic = "the-flash-tweets"
  )

  def main(args: Array[String]): Unit = {
    notLocatedTweetsConsumer.start()
  }

}
