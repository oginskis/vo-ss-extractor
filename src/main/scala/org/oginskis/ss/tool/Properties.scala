package org.oginskis.ss.tool

import java.util.Properties

import scala.io.Source

/**
  * Created by oginskis on 01/01/2017.
  */
object Properties {

  var res = getClass.getResourceAsStream("/app.properties")
  val source = Source.fromInputStream(res)
  val prop = new Properties()
  prop.load(source.bufferedReader())

  def getProperty(key:String): String ={
    prop.getProperty(key,null)
  }
}
