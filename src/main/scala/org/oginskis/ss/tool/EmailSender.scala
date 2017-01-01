package org.oginskis.ss.tool

import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Transport, Message, PasswordAuthentication, Session}

import org.oginskis.ss.model.Flat

/**
  * Created by oginskis on 01/01/2017.
  */
object EmailSender {

  val username = "viktors.oginskis@gmail.com"
  val password = "Mamadaba22"
  val props = new java.util.Properties()
  props.put("mail.smtp.starttls.enable", "true");
  props.put("mail.smtp.auth", "true");
  props.put("mail.smtp.host", "smtp.gmail.com");
  props.put("mail.smtp.port", "587");
  val session = Session.getInstance(props,
    new javax.mail.Authenticator() {
      override protected def getPasswordAuthentication(): javax.mail.PasswordAuthentication = {
        return new PasswordAuthentication(username, password);
      }
    });

  def sendEmail(flat: Flat) = {
    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress("viktors.oginskis@gmail.com"))
      message.setRecipients(Message.RecipientType.TO,
        "viktors.oginskis@gmail.com")
      message.setSubject("new flat posted")
      message.setText("New flat posted:"
                   + "\nAddress: " + flat.address
                   + "\nFloor: " + flat.floor
                   + "\nRooms: " + flat.rooms
                   + "\nSize: " + flat.size
                   + "\nPrice: " + flat.price + " EUR"
                   + "\nLink: http://www.ss.lv" + flat.link
                   +  "\n\n--Viktors")
      Transport.send(message)
    } catch {
      case ex:RuntimeException =>
        throw new RuntimeException(ex);
    }
  }

}
