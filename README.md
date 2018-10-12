# Apache Kafka Playground [![Build Status](https://travis-ci.com/pedrovgs/KafkaPlayground.svg?branch=master)](https://travis-ci.com/pedrovgs/KafkaPlayground)

Playground used to learn and experiment with [Apache Kafka](https://kafka.apache.org/) using [Scala](https://www.scala-lang.org/). Do you want to learn Apache Kafka? Try to resolve the proposed exercises :smiley:

![Kafka Logo](./art/kafkaLogo.png)

Apache Kafka is used for building real-time data pipelines and streaming apps. It is horizontally scalable, fault-tolerant, wicked fast, and runs in production in thousands of companies.

![Kafka Diagram](./art/kafkaDiagram.png)

This highly-scalable publish-subscribe messaging system that can serve as the data backbone in distributed applications. With Kafka’s Producer-Consumer model it becomes easy to implement multiple data consumers that do live monitoring as well persistent data storage for later analysis.

## Exercises

This table contains all the exercises resolved in this repository sorted by goals with links for the solution and the specs.

| # | Goal | Statement | Code | Tests |
| - | ---- | --------- | ---- | ----- |
| 1 | Learn how to handle topics using ``kafka-cli`` tools. | [CLI TOPICS 101.](./statements/CLI_TOPICS_101.md) | [CLI TOPICS 101.](./statements/cli_statements_solutions/CLI_TOPICS_101.md) | - |
| 2 | Learn how to produce and cosume messages using ``kafka-cli`` tools. | [CLI PRODUCER AND CONSUMER.](./statements/CLI_PRODUCER_CONSUMER.md) | [CLI PRODUCER AND CONSUMER.](./statements/cli_statements_solutions/CLI_PRODUCER_CONSUMER.md) | - |
| 3 | Learn how to use consumers in group using ``kafka-cli`` tools. | [CLI CONSUMERS GROUP.](./statements/CLI_CONSUMERS_GROUPS.md) | [CLI CONSUMERS GROUP.](./statements/cli_statements_solutions/CLI_CONSUMERS_GROUPS.md) | - |
| 4 | Learn how to use kafka consumer groups ``kafka-cli`` tools. | [CLI CONSUMERS GROUPS 2.](./statements/CLI_CONSUMERS_GROUPS_2.md) | [CLI CONSUMERS GROUPS 2.](./statements/cli_statements_solutions/CLI_CONSUMERS_GROUPS_2.md) | - |
| 5 | Learn how to use Kafka Tool app. | [KAFKA TOOL.](./statements/KAFKA_TOOL.md) | - | - |


## Install Apache Kafka with Docker

Thanks to the usage of docker we can simplify the usage of Kafka for this playground. You just need to move to the folder named ``docker`` and start all the required instances.

```
cd docker
docker-compose up
```

This might take a while, so I'd recommend you to grab a :coffee:.

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

## Running Scala code

Inside the scala folder you'll find a sbt project using Kafka intensively. This project uses a Twitter client as data source so configuring the access to this platform throug this API is needed. To do this you only need to create a Twitter app from [this page](https://apps.twitter.com) and add a ``src/main/resources/application.conf`` file to your ``scala`` folder with the following content:

```
twitter {
  consumer {
    key = "my-consumer-key"
    secret = "my-consumer-secret"
  }
  access {
    key = "my-access-key"
    secret = "my-access-secret"
  }
}
```

Developed By
------------

* Pedro Vicente Gómez Sánchez - <pedrovicente.gomez@gmail.com>

<a href="https://twitter.com/pedro_g_s">
  <img alt="Follow me on Twitter" src="https://image.freepik.com/iconos-gratis/twitter-logo_318-40209.jpg" height="60" width="60"/>
</a>
<a href="https://es.linkedin.com/in/pedrovgs">
  <img alt="Add me to Linkedin" src="https://image.freepik.com/iconos-gratis/boton-del-logotipo-linkedin_318-84979.png" height="60" width="60"/>
</a>

License
-------

    Copyright 2017 Pedro Vicente Gómez Sánchez

    Licensed under the GNU General Public License, Version 3 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.gnu.org/licenses/gpl-3.0.en.html

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
