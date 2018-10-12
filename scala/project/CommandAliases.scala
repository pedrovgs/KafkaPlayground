import sbt.addCommandAlias

object CommandAliases {

  def addCommandAliases() = {
    addCommandAlias("format", ";scalafmt;test:scalafmt")
  }

}