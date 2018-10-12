package com.github.pedrovgs.kafkaplayground

import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.Tweet

object TheFlashSeeker {

  private val producer = new GeolocatedTweetsProducer()

  def main(args: Array[String]): Unit = {
    val streamingClient = TwitterStreamingClient()
    streamingClient.filterStatuses(tracks = Seq("flash")) {
      case tweet: Tweet => producer(tweet)
    }
  }

}