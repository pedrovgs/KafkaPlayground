# The Flash seeker 3.0

First of all, we need to initialize the Kafka cluster. From the ``docker`` folder we can run this command ``docker-compose up`` and wait until the cluster is initialized. Then, using the following commands we can resolve the proposed tasks:

Using the already solved exercises from the previous sections we will execute the following commands.

* List all the topics we've created previously

```
???
```

We shoudl increase the number of partitions for ``the-flash-tweets`` and ``the-flash-tweets-with-location``.

```
???
```

You can now review the topis configurations as follows:

```
???
```

**Important note:** The warining you see ``WARNING: If partitions are increased for a topic that has a key, the partition logic or ordering of the messages will be affected`` means that the order of the new events might change because the key hashing algorithm will now post messages to new partitions.