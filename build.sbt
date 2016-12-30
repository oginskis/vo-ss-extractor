name := """vo-ss-extractor"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
  "net.ruippeixotog" %% "scala-scraper" % "1.2.0")


fork in run := true