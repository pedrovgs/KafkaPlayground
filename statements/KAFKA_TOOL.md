# Kafka Tool

Learning how to use the CLI tools Kafka provides is really important and it will help you to interact with a Kafka cluster and understand what's going on in real time. But sometimes a UI app could be interesting. During this exercise we will review the Kafka Tool UI capabilities.

Using Kafka Tool could you perform the following tasks?

* Download Kafka Tool from the following site [http://kafkatool.com/download.html](http://kafkatool.com/download.html)
* Connect to your cluster using the Zookeeper host and port you've configured ``localhost:2181``.
* Review available brokers.
* Review the topics created.
* List all the messages (without key) of the previously created topics. Remember to configure the messages as strings without keys or you'll only be able to read a bunch of hexadecimal bytes.
* Look at the consumers list and the offsets associated per partition.

There is another tool you could review if you have time named [kafka-manager](https://github.com/yahoo/kafka-manager) which similar. And if you don't like UI tools, you can use another alternative to kafka-cli named [kafkacat](https://github.com/edenhill/kafkacat). Take a look at the usage of these tools if you don't like kafka-cli :smiley: