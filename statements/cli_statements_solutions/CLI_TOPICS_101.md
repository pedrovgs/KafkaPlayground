# Handling topics for the command line interface.

First of all, we need to initialize the Kafka cluster. From the ``docker`` folder we can run this command ``docker-compose up`` and wait until the cluster is initialized. Then, using the following commands we can resolve the proposed tasks:

* Create the named: "topic-1"

```
kafka-topics --zookeeper localhost:2181 --topic topic-1 --create --partitions 4 --replication-factor 1
```

* Create the named: "topic-2"

```
kafka-topics --zookeeper localhost:2181 --topic topic-2 --create --partitions 4 --replication-factor 1
```

* List all the topics created.

```
kafka-topics --zookeeper localhost:2181 --list
```

* Delete the last topic created.

```
kafka-topics --zookeeper localhost:2181 --topic topic-2 --delete
```

* Describe the first topic created.

```
kafka-topics --zookeeper localhost:2181 --topic topic-1 --describe
```

* List all the topics created again.

```
kafka-topics --zookeeper localhost:2181 --list
```

There are some details to keep in mind while resolving this exercise:

 * You'll need to install kafka locally to run the cli tools from your computer or execute these commans from the broker instance itself. This is an example: ``docker-compose exec broker kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic topic-1``.
 * The replication factor can't be greater than the number of brokers initialized in our cluster.