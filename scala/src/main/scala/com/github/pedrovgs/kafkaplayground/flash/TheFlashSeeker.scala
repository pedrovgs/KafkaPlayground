package com.github.pedrovgs.kafkaplayground.flash

import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.Tweet

object TheFlashSeeker {

  private val producer = new TheFlashTweetsProducer(
    brokerAddress = "localhost:29092"
  )

  def main(args: Array[String]): Unit = ???

}
