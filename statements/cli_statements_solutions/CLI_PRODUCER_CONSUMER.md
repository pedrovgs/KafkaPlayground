# Producing and consuming messages.

First of all, we need to initialize the Kafka cluster. From the ``docker`` folder we can run this command ``docker-compose up`` and wait until the cluster is initialized. Then, using the following commands we can resolve the proposed tasks:

* Create the named: "users"

```
kafka-topics --zookeeper localhost:2181 --topic users --create --partitions 4 --replication-factor 1
```

* Produce 10 messages for the topic "users".

```
kafka-console-producer --broker-list 127.0.0.1:29092 --topic users
1
2
3
4
5
6
7
8
9
10
```

Ensure you are using the port ``29092`` and not the ``9092``. The first port is the one your local machine exposes and the other one is forwarded by docker.

* Produce another 10 messages for the topic "users" with the ack property "all".

```
kafka-console-producer --broker-list 127.0.0.1:29092 --topic users --producer-property acks=all
1
2
3
4
5
6
7
8
9
10
```

* Produce another 10 messages for the topic "new-users" without creating the topic before

```
kafka-console-producer --broker-list 127.0.0.1:29092 --topic new-users
1
2
3
4
5
6
7
8
9
10
```

You should see how the topic is created automatically by Kafka and a warning is shown to you.

* Describe the topic information for the topic "new-users".

```
kafka-topics --zookeeper localhost:2181 --topic new-users --describe
```

You should see how the partition count and replication is configured as 1 by default. This is not ideal, we could create the topics before post any message so we can configure these values. You can also configure the number of partitions by default from the ``server.properties`` file.

* Consume the message posted from the beginning.

```
kafka-console-consumer --bootstrap-server localhost:29092 --topic new-users --from-beginning
```

Pay attention to the ``--bootstrap-server`` param, which is equivalent to the ``--brokers-list`` we used in the past. The parameter named ``--from-beginning`` let's you read all messages posted into the topic since it was created. If you don't specify it you'll start consuming only new messages.

**Important detail:** If you review the order the messages are printed in the consumer terminal you could see how the order could not be correct. This is because Kafka only guarantees the order per partition. If you want all the mensages ordered properly you can create your topic with just one partition.
