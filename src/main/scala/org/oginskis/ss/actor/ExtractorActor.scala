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
      val minSize = 0
      val maxPrice = 1000000
      val minPrice = 10
      val flats = FlatExtractor.extractFlats.filter(f=>f.size >= minSize)
        .sortBy(f=>f.price).filter(a=>a.price>=minPrice).filter(a=>a.price<maxPrice)
      println("We found "+ flats.size +" flats:")
      flats.foreach(flat=>println(flat))
      flats.foreach(flat=> {
        persistActor ! flat
      })
    }
  }
}

object ExtractingActor {
  val Extract = "extract"
}
