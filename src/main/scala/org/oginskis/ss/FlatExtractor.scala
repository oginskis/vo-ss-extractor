package org.oginskis.ss

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.oginskis.ss.model.Flat
import net.ruippeixotog.scalascraper.model.Element
import org.jsoup.Connection

/**
  * Created by oginskis on 30/12/2016.
  */
object FlatExtractor {


  class ExtendedJsoupBrowser extends JsoupBrowser {
    override protected[this] def defaultRequestSettings(conn: Connection): Connection = {
      super.defaultRequestSettings(conn)
      conn.followRedirects(false)
    }
  }

  def extractFlats() : List[Flat] = {
    extractFlats(1)
  }

  private def extractFlats(page: Int) : List[Flat] = {
    val browser = new ExtendedJsoupBrowser()
    try {
      val doc = browser
        .get("https://www.ss.lv/lv/real-estate/flats/riga/centre/sell/page"
          + page + ".html")
      val rawList: Iterable[Element] = doc.body.select("[id^=\"tr_\"]")
      rawList.init.toList.map(
        entry => {
          val attr: List[Element] = entry.select(".msga2-o").toList
          val link: String = entry.select(".msg2 .d1 .am").head.attr("href")
          new Flat(attr(0).text,
            attr(1).text,
            attr(2).text,
            attr(3).text,
            attr(6).text,
            link)
        }) ::: extractFlats(page + 1)
    }
    catch {
      case unknown: Throwable => List[Flat]()
    }
  }

}

