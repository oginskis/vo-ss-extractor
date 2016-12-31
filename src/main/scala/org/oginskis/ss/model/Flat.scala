package org.oginskis.ss.model

/**
  * Created by oginskis on 30/12/2016.
  */
class Flat(
            var address: String,
            var rooms: String,
            var size: Int,
            var floor: String,
            var price: Int,
            var link: String
          ) {
  override def toString: String =
      "address: " + address + ", "+
      "rooms: " + rooms + ", "+
      "size: " + size + ", "+
      "floor: " + floor + ", "+
      "price: " + price + ", "+
      "link: https://www.ss.lv" + link
}