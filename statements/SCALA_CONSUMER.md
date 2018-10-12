# The Flash seeker 2.0

Our Star Labs machine learning team is full of awesome mathematicians, but they are not so good at computer scinece. Yesterday, Cisco came to our office and asked if it could be possible for us to create a web-based tool where they can review all the tweets we've sent them using the already implemented the producer and also search some text these tweets contain. They are building the machine learning model based on TensorFlow and other deep learning techniques, but they need to be able to review the corpus during the model training stage.

![starLabs](../art/starLabs.png)

Using a ready to use Elasticsearch cluster you can configure [here](https://bonsai.io) and some Scala code, could you perform these tasks?

* Create [bonsai.io](https://bonsai.io) free account and free cluster.
* Create two Elasticsearch index, one for the located tweet and another one for the unknown location tweets. You can do it from the bonsai.io web if you click on the ``Interactive Console`` tab and you send two ``PUT`` requests at the index names: ``located_tweets`` and ``unknown_location_tweets``.



Remember that based on the ``bonsai.io`` cluster url you can extract the username and password as follows: ``https://username:password@host:port``.
