name := "scala"

version := "0.1"

scalaVersion := "2.12.7"

CommandAliases.addCommandAliases()

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies += "com.danielasfregola" %% "twitter4s" % "5.5"
libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "1.1.1"
libraryDependencies += "com.sksamuel.elastic4s" % "elastic4s-core_2.12" % "6.2.10"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-tcp" % "6.2.10"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-http" % "6.2.10"

libraryDependencies += "net.cakesolutions" %% "scala-kafka-client-testkit" % "1.1.1" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "6.2.10" % "test"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-embedded" % "6.2.10" % "test"

parallelExecution in Test := false
