name := "scala"

version := "0.1"

scalaVersion := "2.12.7"

CommandAliases.addCommandAliases()

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies += "com.danielasfregola" %% "twitter4s" % "5.5"
libraryDependencies += "net.cakesolutions" %% "scala-kafka-client" % "1.1.1"
libraryDependencies += "io.searchbox" % "jest" % "5.3.3"
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"
libraryDependencies += "com.typesafe" % "config" % "1.3.3"
libraryDependencies += "net.cakesolutions" %% "scala-kafka-client-testkit" % "1.1.1" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.23.0" % "test"

parallelExecution in Test := false
