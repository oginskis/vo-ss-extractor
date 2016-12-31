package org.oginskis.ss

import akka.actor.{ActorSystem, Props}
import org.oginskis.ss.actor.{PersistActor, ExtractingActor}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ApplicationMain extends App {
  val system = ActorSystem("MySystem")
  val persist = system.actorOf(Props(new PersistActor), name = "persist")
  val extracting = system.actorOf(Props(new ExtractingActor(persist)), name = "extracting")
  system.scheduler.schedule(0 seconds, 60 seconds, extracting, ExtractingActor.Extract)
}