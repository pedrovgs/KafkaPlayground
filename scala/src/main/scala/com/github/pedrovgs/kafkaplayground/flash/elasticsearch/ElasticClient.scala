package com.github.pedrovgs.kafkaplayground.flash.elasticsearch

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.core.Index
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.impl.client.BasicCredentialsProvider

class ElasticClient(private val elasticsearchHost: String,
                    private val elasticsearchUser: String,
                    private val elasticsearchPass: String,
                    private val elasticIndex: String) {

  private val client: JestClient = {
    val factory  = new JestClientFactory()
    val provider = new BasicCredentialsProvider()
    provider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(elasticsearchUser, elasticsearchPass))
    factory.setHttpClientConfig(
      new HttpClientConfig.Builder(elasticsearchHost)
        .credentialsProvider(provider)
        .build())
    factory.getObject
  }

  def insertOrUpdate(id: String, content: String) =
    try {
      val index  = new Index.Builder(content).index(elasticIndex).`type`("tweets").build
      val result = client.execute(index)
      println(s"Elasticsearch new document id: ${result.getId}")
    } catch {
      case e: Exception =>
        println(e.getMessage)
    }

}
