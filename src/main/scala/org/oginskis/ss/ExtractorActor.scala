package org.oginskis.ss
import akka.actor.{Actor, ActorLogging}
import org.oginskis.ss.repo.FlatRepo

/**
  * Created by oginskis on 30/12/2016.
  */
class ExtractingActor extends Actor with ActorLogging {
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
      flats.foreach(flat=>FlatRepo.addOrUpdateFlat(flat))
    }
  }
}

object ExtractingActor {
  val Extract = "extract"
}
