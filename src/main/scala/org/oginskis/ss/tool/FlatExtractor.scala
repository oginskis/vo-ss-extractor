package org.oginskis.ss.tool

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Element
import org.jsoup.Connection
import org.oginskis.ss.model.Flat

/**
  * Created by oginskis on 30/12/2016.
  */
object FlatExtractor {

  val SS_LV_BASE ="ss.lv.base.url"

  class ExtendedJsoupBrowser extends JsoupBrowser {
    override protected[this] def defaultRequestSettings(conn: Connection): Connection = {
      super.defaultRequestSettings(conn)
      conn.followRedirects(false)
    }
  }

  val browser = new ExtendedJsoupBrowser()

  def extractFlats() : List[Flat] = {
    def extractFlats(page: Int) : List[Flat] = {
      try {
        val doc = browser
          .get(Properties.getProperty(SS_LV_BASE)+"/riga/centre/sell/page"
            + page + ".html")
        val rawList: Iterable[Element] = doc.body.select("[id^=\"tr_\"]")
        rawList.init.toList.map(
          entry => {
            val attr: List[Element] = entry.select(".msga2-o").toList
            val link: String = entry.select(".msg2 .d1 .am").head.attr("href")
            new Flat(Option(attr(0).text.replace("\\", "/")),
              Option(attr(1).text.trim.replace("\\", "/")),
              Option(attr(2).text.trim.toInt),
              Option(attr(3).text.replace("\\", "/")),
              Option(attr(6).text.replace(",","").replace(" €","").trim.toInt),
              Option(link))
          }) ::: extractFlats(page + 1)
      }
      catch {
        case unknown: Throwable => {
          List[Flat]()
        }
      }
    }
    extractFlats(1)
  }



}

