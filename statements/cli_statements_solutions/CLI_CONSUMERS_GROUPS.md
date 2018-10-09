# Producing and consuming messages.

First of all, we need to initialize the Kafka cluster. From the ``docker`` folder we can run this command ``docker-compose up`` and wait until the cluster is initialized. Then, using the following commands we can resolve the proposed tasks:

* Create the named "new-users" with at least 3 partitions.

```
kafka-topics --zookeeper localhost:2181 --topic new-users --create --partitions 3 --replication-factor 1
```
* From one terminal start consuming the topic using the group with id "my-app".

```
kafka-console-consumer --bootstrap-server localhost:29092 --topic new-users --group my-app
```

* From another terminal start consuming the topic using the group with id "my-app".

```
kafka-console-consumer --bootstrap-server localhost:29092 --topic new-users --group my-app
```

* Produce 10 messages for the topic "users".

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

* Can you see all the messages in both consumers?

Nope. Just one consumer shows the message.

* Are the messages received in order?

Nope.

**Important detail:** As you can see, just one consumer per group receives the message.

* What happens if you start a third terminal and using a console consumer start listening the topic but without specify the group?

This is because the consumers not being part of a group will receive every message posted for that topic.

**Important detail:** If we close one of the consumers the load is balanced and the rest of the consumers will assume the messages.

* What happens if you start a other three terminals and using a console consumer start listening the topic using the group id "my-app"?

There are some consumers that doesn't receive any message.

**Important detail:** If we have more consumers than partitions, we will have idle consumers.

* Start a consumer for that topic in a new group, using the param ``--from-beginning``, close it and start it again. Do you see something weird?

```
kafka-console-consumer --bootstrap-server localhost:29092 --topic new-users --group my-app-2 --from-beginning
CNTRL + C
kafka-console-consumer --bootstrap-server localhost:29092 --topic new-users --group my-app-2 --from-beginning
```

The second time the consumer is started does not show any message.

**Important detail:** The group ensures the offset is commited so the partition does not deliver messages which offset has been already consumed. So if your consumer is in a group and you read some messages, that won't be consumed again.