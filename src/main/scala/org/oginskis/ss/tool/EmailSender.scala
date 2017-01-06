package org.oginskis.ss.tool

import javax.mail._
import javax.mail.internet.{MimeMultipart, MimeBodyPart, InternetAddress, MimeMessage}

import org.oginskis.ss.model.Flat
import org.oginskis.ss.repo.FlatRepo

/**
  * Created by oginskis on 01/01/2017.
  */
object EmailSender {

  val SMTP_HOST = "smtp.host"
  val SMTP_PORT = "smtp.port"
  val SMTP_USERNAME = "smtp.username"
  val SMTP_PASSWORD = "smtp.password"
  val SENT_TO_LIST = "sendto.emails"
  val SENT_FROM = "smtp.sentfrom"
  val props = new java.util.Properties()
  props.put("mail.smtp.starttls.enable", "true")
  props.put("mail.smtp.auth", "true")
  props.put("mail.smtp.host", Properties.getProperty(SMTP_HOST))
  props.put("mail.smtp.port", Properties.getProperty(SMTP_PORT))

  val session = Session.getInstance(props,
    new javax.mail.Authenticator() {
      override protected def getPasswordAuthentication(): javax.mail.PasswordAuthentication = {
        return new PasswordAuthentication(Properties.getProperty(SMTP_USERNAME),
          Properties.getProperty(SMTP_PASSWORD))
      }
    });

  def sendEmail(flat: Flat) = {
    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(Properties.getProperty(SENT_FROM)))
      message.setRecipients(Message.RecipientType.TO,
        Properties.getProperty(SENT_TO_LIST).split(",")
          .map(email=> {
            val address: Address = new InternetAddress(email)
            address
          }
          ).array
      )
      var historicFlatStr = FlatRepo.findHistoricAdds(new Flat(flat.address,flat.rooms,flat.size,flat.floor))
          .filterNot(incomingFlat=>(incomingFlat.link == flat.link))
          .map(flat=>flat.price.get+" EUR")
          .mkString("<br />")
      if (historicFlatStr.isEmpty){
        historicFlatStr = "Nothing has been found"
      }
      val textPart = new MimeBodyPart()
      textPart.setContent("<html><head></head><body>New flat posted or updated:"
        + "<br />"
        + "<br /><b>Address:</b> " + flat.address.get
        + "<br /><b>Floor:</b> " + flat.floor.get
        + "<br /><b>Rooms:</b> " + flat.rooms.get
        + "<br /><b>Size:</b> " + flat.size.get
        + "<br /><b>Price:</b> " + flat.price.get + " EUR"
        + "<br /><b>Link:</b> http://www.ss.lv" + flat.link.get
        + "<br />"
        + "<br /><b>Historic prices (for flats with the same address, floor, " +
        "number of rooms and size):</b> <br />"
        + historicFlatStr
        + "<br />"
        +  "<br />--Viktors</body></html>", "text/html; charset=UTF-8")
      message.setSubject(flat.address.get+ ", "+flat.price.get+" EUR")
      val mp = new MimeMultipart()
      mp.addBodyPart(textPart)
      message.setContent(mp)
      Transport.send(message)
    } catch {
      case ex:Exception =>
        throw new RuntimeException(ex);
    }
  }
}
