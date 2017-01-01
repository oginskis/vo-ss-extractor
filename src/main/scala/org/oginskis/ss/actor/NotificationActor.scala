package org.oginskis.ss.actor
import akka.actor.{Actor, ActorLogging}
import org.oginskis.ss.model.Flat
import org.oginskis.ss.tool.EmailSender

/**
  * Created by oginskis on 01/01/2017.
  */
class NotificationActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case flat: Flat => {
      EmailSender.sendEmail(flat)
    }
  }
}
