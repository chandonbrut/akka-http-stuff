name := "akka-http-example"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.vividsolutions" % "jts" % "1.13",
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9"
)
