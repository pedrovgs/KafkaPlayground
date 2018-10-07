# Apache Kafka Playground [![Build Status](https://travis-ci.com/pedrovgs/KafkaPlayground.svg?branch=master)](https://travis-ci.com/pedrovgs/KafkaPlayground)

Playground used to learn and experiment with [Apache Kafka](https://kafka.apache.org/) using [Scala](https://www.scala-lang.org/). Do you want to learn Apache Kafka? Try to resolve the exercised proposed :smiley:

![Kafka Logo](./art/kafkaLogo.png)

Apache Kafka is used for building real-time data pipelines and streaming apps. It is horizontally scalable, fault-tolerant, wicked fast, and runs in production in thousands of companies.

![Kafka Diagram](./art/kafkaDiagram.png)

## Install Apache Kafka

Apache **Kafka** is a highly-scalable publish-subscribe messaging system that can serve as the data backbone in distributed applications. With Kafka’s Producer-Consumer model it becomes easy to implement multiple data consumers that do live monitoring as well persistent data storage for later analysis.

The best way to install the latest version of the Kafka server on OS X and to keep it up to date is via Homebrew.

```
brew install kafka
```

The above commands will install a dependency called zookeeper which is required to run kafka. Start the zookeeper service and don't close the terminal where the following command is executed.

```
zookeeper-server-start /usr/local/Cellar/kafka/2.0.0/libexec/config/zookeeper.properties
```

While Zookeeper is running, Start the kafka server which handles both producer and consumer parts.

```
kafka-server-start /usr/local/etc/kafka/server.properties
```

In a new terminal, create a new topic.

```
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
```

From another terminal, list the topics already created.

```
kafka-topics --list --zookeeper localhost:2181
```

If you followed the above steps properly you should see how the result of the last command executed prints ``test`` in your terminal. **We are now ready to test the producers and consumers in order to test our installation**

From any terminal execute the following command that will let you send messages to any Kafka cluster you can reach using the already created topic:

```
kafka-console-producer --broker-list localhost:9092 --topic test
This is a message
This is another message
```

From another terminal execute this command and you'll see how the messages sent from the prodcuer in the previous step will be read by the consumer we've just created:

```
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --from-beginning
```


    
