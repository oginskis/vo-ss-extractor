package org.oginskis.ss

import akka.actor.{ActorSystem, Props}
import org.oginskis.ss.actor.{NotificationActor, PersistActor, ExtractingActor}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ApplicationMain extends App {
  val system = ActorSystem("flat-extractor-system")
  val notification = system.actorOf(Props(new NotificationActor), name = "notification")
  val persist = system.actorOf(Props(new PersistActor(notification)), name = "persist")
  val extracting = system.actorOf(Props(new ExtractingActor(persist)), name = "extracting")
  system.scheduler.schedule(0 seconds, 60 seconds, extracting, ExtractingActor.Extract)
}