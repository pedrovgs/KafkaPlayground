name := "scala"

version := "0.1"

scalaVersion := "2.12.7"

CommandAliases.addCommandAliases()

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies += "com.danielasfregola" %% "twitter4s" % "5.5"
libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "1.1.1"
libraryDependencies += "com.sksamuel.elastic4s" % "elastic4s-core_2.12" % "6.3.7"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-http" % "6.3.7"
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

libraryDependencies += "net.cakesolutions" %% "scala-kafka-client-testkit" % "1.1.1" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "6.3.7" % "test"
libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-embedded" % "6.3.7" % "test"

parallelExecution in Test := false
