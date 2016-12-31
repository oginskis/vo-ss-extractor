package org.oginskis.ss.actor

import akka.actor.{Actor, ActorLogging}
import org.oginskis.ss.model.Flat
import org.oginskis.ss.repo.FlatRepo

/**
  * Created by oginskis on 31/12/2016.
  */
class PersistActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case flat:Flat => {
      FlatRepo.addOrUpdateFlat(flat)
    }
  }
}
