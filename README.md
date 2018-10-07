# Apache Kafka Playground [![Build Status](https://travis-ci.com/pedrovgs/KafkaPlayground.svg?branch=master)](https://travis-ci.com/pedrovgs/KafkaPlayground)

Playground used to learn and experiment with [Apache Kafka](https://kafka.apache.org/) using [Scala](https://www.scala-lang.org/). Do you want to learn Apache Kafka? Try to resolve the exercised proposed :smiley:

![Kafka Logo](./art/kafkaLogo.png)

Apache Kafka is used for building real-time data pipelines and streaming apps. It is horizontally scalable, fault-tolerant, wicked fast, and runs in production in thousands of companies.

![Kafka Diagram](./art/kafkaDiagram.png)

This highly-scalable publish-subscribe messaging system that can serve as the data backbone in distributed applications. With Kafka’s Producer-Consumer model it becomes easy to implement multiple data consumers that do live monitoring as well persistent data storage for later analysis.

## Install Apache Kafka with Docker

Thanks to the usage of docker we can simplify the usage of Kafka for this playground. You just need to move to the folder named ``docker`` and start all the required instances.

```
cd docker
docker-compose up
```

This might take a while, so I'd recommend you to grab a :cofee:.

After that, a Zookeeper instance and a Kafka broker will be initialized. Once the instances are up and running you can test the execution by running the following comands:

```
# Create topic
docker-compose exec broker kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic users 

# Start posting messages to the already created topic
docker-compose exec broker kafka-console-producer --topic=users --broker-list localhost:9092

# Start listening messages from the already created tpic
docker-compose exec broker kafka-console-consumer --bootstrap-server localhost:9092 --topic users --from-beginning
```

**If everything goes ok, you'll see how the consumer shows every message the producer posted.**

If you want to access this information from outside the Docker container you can use the ip and port listed when executing ``docker-compose ps``.

```
  Name               Command            State                        Ports
------------------------------------------------------------------------------------------------
broker      /etc/confluent/docker/run   Up      0.0.0.0:29092->29092/tcp, 0.0.0.0:9092->9092/tcp
zookeeper   /etc/confluent/docker/run   Up      0.0.0.0:2181->2181/tcp, 2888/tcp, 3888/tcp
```

The broker is accessible from the ip address ``localhost:29092``. If you install the Kafka binaries locally (you can read the following section) or you want to connect from any code you wrote, you can use that ip address and port. This is an example of how to use a console producer and consumer from outside the Docker container.

```
# Start posting messages to the already created topic
kafka-console-producer --topic=users --broker-list localhost:29092

# Start listening messages from the already created tpic
kafka-console-consumer --bootstrap-server localhost:29092 --topic users --from-beginning
```

## Install Apache Kafka (local)

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


    
