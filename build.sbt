import sbt.Keys.{resolvers, scalaVersion}

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.2",
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  resolvers += Resolver.bintrayRepo("cakesolutions", "maven")
)

lazy val root = (project in file("."))
  .settings(name := "SmartProxy")
  .aggregate()
