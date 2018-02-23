name := "SmartProxy"

version := "0.1"

scalaVersion := "2.12.2"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "io.netty" % "netty-all" % "4.1.20.Final",
  "com.typesafe" % "config" % "1.3.1"
)