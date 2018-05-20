import sbt.Keys.{resolvers, scalaVersion}

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.2",
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

lazy val main = (project in file("main"))
  .settings(
    name := "Calcite4s",
    commonSettings,
    libraryDependencies ++= Seq(
      "io.netty" % "netty-all" % "4.1.20.Final",
      "org.apache.calcite.avatica" % "avatica-core" % "1.11.0",
      "org.apache.calcite.avatica" % "avatica-server" % "1.11.0",
      "org.apache.calcite" % "calcite-elasticsearch5" % "1.16.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe" % "config" % "1.3.1"
    )
  )

lazy val client = (project in file("client"))
  .settings(
    name := "KafkaClient",
    commonSettings,
  )

lazy val root = (project in file("."))
  .settings(name := "SmartProxy")
  .aggregate(client, main)
