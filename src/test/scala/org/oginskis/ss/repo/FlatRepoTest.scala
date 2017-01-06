package org.oginskis.ss.repo

import org.bson.Document
import org.junit.runner.RunWith
import org.oginskis.ss.model.Flat
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
  * Created by oginskis on 06/01/2017.
  */
@RunWith(classOf[JUnitRunner])
class FlatRepoTest extends FunSuite{

  test("flat find filter test: All fields are set") {
    val flat = new Flat(Option("Caka 95"),Option("4"),Option(55),
      Option("1/5"),Option(30000),Option("link"))
    val doc: Document = FlatRepo.findFilter(flat)
    assert("Caka 95" === doc.get("address"))
    assert("4" === doc.get("rooms"))
    assert("55" === doc.get("size"))
    assert("1/5" === doc.get("floor"))
    assert("30000" === doc.get("price"))
    assert("link" === doc.get("link"))
  }

  test("flat find filter test: price and link are missing") {
    val flat = new Flat(Option("Caka 95"),Option("4"),Option(55),
      Option("1/5"))
    val doc: Document = FlatRepo.findFilter(flat)
    assert("Caka 95" === doc.get("address"))
    assert("4" === doc.get("rooms"))
    assert("55" === doc.get("size"))
    assert("1/5" === doc.get("floor"))
    assert(doc.get("price") === null)
    assert(doc.get("link") === null)
  }

}
