# Consumer groups CLI

First of all, we need to initialize the Kafka cluster. From the ``docker`` folder we can run this command ``docker-compose up`` and wait until the cluster is initialized. Then, using the following commands we can resolve the proposed tasks:

Using the already solved exercises from the previous sections we will execute the following commands.

* Get all the consumer groups created in the Kafka environment.

```
kafka-consumer-groups --bootstrap-server localhost:29092 --list
```

**Important note:** You'll see how there are groups you dind't created. This is because Kafka creates the group using a random id when you start a consumer without a group.

* Describe the information of the consumer group "my-app" you created in the previous exercise.

```
kafka-consumer-groups --bootstrap-server localhost:29092 --group my-app --describe
```

**Important note:** The result of this command execution let you identify which consumer is reading from which partition and the host IP.

**Important note:** If you see there are no active members, that's because there are no consumers listening.

**Important note:** In the table shown you can see information per partition and topic.

**Important note:** The ``LAG`` column is the difference between the ``LOG-END-OFFSET`` and ``CURRENT_OFFSET`` columns. That represents the number of messages pending to be consumed. A `LAG`` column equal to 0 means there are no messages pending to be processed.

* Delete the group "my-app-2".

```
kafka-consumer-groups --bootstrap-server localhost:29092 --group my-app --delete
```

* Reset the group "my-app" offset.

```
kafka-consumer-groups --bootstrap-server localhost:29092 --group my-app --all-topics --reset-offsets --to-earliest --execute
```

The output shows the new offsets per topci and partitions. If we start consuming topics again using the param ``--from-beginning`` we'll se messages already consumed before.

**Important note:** It could be useful for consuming messages again that other consumers read but didn't process properly. Kafka keeps the messages for 7 days by default.

**Important note:** ``--execute`` without this param the command is executed as a dry run command. There are several options to choose where to move the new offset. We could also pick the topic to reset by name or just reset ``--all-topics``.

**Important note:** If instead of using ``--to-earliest`` param we use ``--shift-by 2`` we will move 2 steps the offset forwards. ``--shift-by -2`` moves the offset backwards.