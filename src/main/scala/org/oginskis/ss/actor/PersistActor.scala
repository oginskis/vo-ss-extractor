package org.oginskis.ss.actor

import akka.actor.{ActorRef, Actor, ActorLogging}
import org.oginskis.ss.FlatStatus
import org.oginskis.ss.model.Flat
import org.oginskis.ss.repo.FlatRepo

/**
  * Created by oginskis on 31/12/2016.
  */
class PersistActor(notificationActor: ActorRef) extends Actor with ActorLogging {
  override def receive: Receive = {
    case flat:Flat => {
      val flatStatus = FlatRepo.addOrUpdateFlat(flat)
      if (FlatStatus.Added == flatStatus) {
        notificationActor ! flat
      }
    }
  }
}
