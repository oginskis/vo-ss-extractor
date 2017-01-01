package org.oginskis.ss.actor

import akka.actor.{Actor, ActorLogging, ActorRef}
import org.oginskis.ss.tool.FlatExtractor

/**
  * Created by oginskis on 30/12/2016.
  */
class ExtractingActor(persistActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case ExtractingActor.Extract =>
    {
      FlatExtractor.extractFlats.foreach(flat=> { persistActor ! flat
      })
    }
  }
}

object ExtractingActor {
  val Extract = "extract"
}
