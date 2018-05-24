import sbt.Keys.{resolvers, scalaVersion}

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.2",
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  resolvers += Resolver.bintrayRepo("cakesolutions", "maven")
)

lazy val main = (project in file("main"))
  .settings(
    name := "Calcite4s",
    commonSettings,
    libraryDependencies ++= Seq(
      "io.netty" % "netty-all" % "4.1.20.Final",
      "org.apache.calcite.avatica" % "avatica-core" % "1.11.0",
      "org.apache.calcite.avatica" % "avatica-server" % "1.11.0",
      "org.apache.calcite" % "calcite-server" % "1.16.0", // 目前的实现不支持创建表，采用手工创建的方式，以后实现支持
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
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-simple" % "1.7.25",
      "net.cakesolutions" %% "scala-kafka-client" % "1.1.0",
      "org.apache.calcite" % "calcite-core" % "1.16.0",
      "org.specs2" %% "specs2-core" % "4.0.2" % Test,
      "org.elasticsearch.client" % "transport" % "5.5.2" % Test
    )
  )

// lazy val optimizer = (project in file("optimizer")) // 考虑实现elastic的查询优化

lazy val root = (project in file("."))
  .settings(name := "SmartProxy")
  .aggregate(client, main)
