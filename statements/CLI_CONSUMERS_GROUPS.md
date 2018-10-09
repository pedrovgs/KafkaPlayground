# Consuming messages in group.

Learning how to use the CLI tools Kafka provides is really important and it will help you to interact with a Kafka cluster and understand what's going on in real time. For this exercise, there are some tasks you should know how to perform using Kafka CLI tools in order to use consumer groups.

Using just the command line could you perform the following tasks?

* Create the named "new-users" with at least 3 partitions.
* From one terminal start consuming the topic using the group with id "my-app".
* From another terminal start consuming the topic using the group with id "my-app".
* Produce 10 messages for the topic "users".
* Can you see all the messages in both consumers?
* Are the messages received in order?
* What happens if you start a third terminal and using a console consumer start listening the topic but without specify the group?
* What happens if you start a other three terminals and using a console consumer start listening the topic using the group id "my-app"?
* Start a consumer for that topic in a new group, using the param ``--from-beginning``, close it and start it again. Do you see something weird?