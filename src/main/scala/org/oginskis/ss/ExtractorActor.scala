package org.oginskis.ss

/**
  * Created by oginskis on 30/12/2016.
  */
import akka.actor.{Actor, ActorLogging}

/**
  * Created by oginskis on 30/12/2016.
  */
class ExtractingActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case ExtractingActor.Extract =>
    {
      val minSize = 45
      val maxPrice = 85000
      val minPrice = 40000
      val flats = FlatExtractor.extractFlats.filter(f=>f.size.toInt >= minSize).sortBy(f=>f.price.replace(",","")
        .replace(" €","").trim.toInt)
        .filter(f=>f.price.replace(",","").replace(" €","").trim.toInt <= maxPrice)
        .filter(f=>f.price.replace(",","").replace(" €","").trim.toInt > minPrice)
      println("We found "+ flats.size +" flats:")
      flats.foreach(flat=>println(flat))
    }
  }
}

object ExtractingActor {
  val Extract = "extract"
}
