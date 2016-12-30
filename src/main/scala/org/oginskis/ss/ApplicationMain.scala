package org.oginskis.ss

import akka.actor.{ActorSystem, Props}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ApplicationMain extends App {
  val system = ActorSystem("MySystem")
  val actor = system.actorOf(Props(new ExtractingActor), name = "actor")
  system.scheduler.schedule(0 seconds, 60 seconds, actor, ExtractingActor.Extract)
}