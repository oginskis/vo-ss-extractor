package org.oginskis.ss.tool

import javax.mail._
import javax.mail.internet.{InternetAddress, MimeMessage}

import org.oginskis.ss.model.Flat

/**
  * Created by oginskis on 01/01/2017.
  */
object EmailSender {

  val SMTP_HOST = "smtp.host"
  val SMTP_PORT = "smtp.port"
  val SMTP_USERNAME = "smtp.username"
  val SMTP_PASSWORD = "smtp.password"
  val SENT_TO_LIST = "sendto.emails"

  val props = new java.util.Properties()
  props.put("mail.smtp.starttls.enable", "true");
  props.put("mail.smtp.auth", "true");
  props.put("mail.smtp.host", Properties.getProperty(SMTP_HOST));
  props.put("mail.smtp.port", Properties.getProperty(SMTP_PORT));
  val session = Session.getInstance(props,
    new javax.mail.Authenticator() {
      override protected def getPasswordAuthentication(): javax.mail.PasswordAuthentication = {
        return new PasswordAuthentication(Properties.getProperty(SMTP_USERNAME),
          Properties.getProperty(SMTP_PASSWORD));
      }
    });

  def sendEmail(flat: Flat) = {
    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(Properties.getProperty(SMTP_USERNAME)))
      message.setRecipients(Message.RecipientType.TO,
        Properties.getProperty(SENT_TO_LIST).split(",")
          .map(email=> {
            val address: Address = new InternetAddress(email)
            address
          }
          ).array
      )
      message.setSubject("new flat posted")
      message.setText("New flat posted:"
                   + "\nAddress: " + flat.address
                   + "\nFloor: " + flat.floor
                   + "\nRooms: " + flat.rooms
                   + "\nSize: " + flat.size
                   + "\nPrice: " + flat.price + " EUR"
                   + "\nLink: http://www.ss.lv" + flat.link
                   +  "\n--Viktors")
      Transport.send(message)
    } catch {
      case ex:RuntimeException =>
        throw new RuntimeException(ex);
    }
  }

}
