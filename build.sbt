name := """vo-ss-extractor"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
  "net.ruippeixotog" %% "scala-scraper" % "1.2.0",
  "org.mongodb" % "mongo-java-driver" % "3.4.1",
  "javax.mail" % "mail" % "1.4",
  "junit" % "junit" % "4.12"
)

mainClass in Compile := Some("org.oginskis.ss.ApplicationMain")
mainClass in assembly := Some("org.oginskis.ss.ApplicationMain")

fork in run := true