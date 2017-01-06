package org.oginskis.ss.model


/**
  * Created by oginskis on 30/12/2016.
  */
class Flat(
            val address: Option[String],
            val rooms: Option[String],
            val size: Option[Int],
            val floor: Option[String],
            val price: Option[Int],
            val link: Option[String]
          ) {

  def this(address: Option[String],
    rooms: Option[String],
    size: Option[Int],
    floor: Option[String]) = {
    this(address,rooms,size,floor,None,None)
  }

  override def toString: String = {
    if (price == None && link == None){
      "address: " + address.get + ", " +
        "rooms: " + rooms.get + ", " +
        "size: " + size.get + ", " +
        "floor: " + floor.get
    } else {
      "address: " + address.get + ", " +
        "rooms: " + rooms.get + ", " +
        "size: " + size.get + ", " +
        "floor: " + floor.get + ", " +
        "price: " + price.get + ", " +
        "link: https://www.ss.lv" + link.get
    }
  }
}