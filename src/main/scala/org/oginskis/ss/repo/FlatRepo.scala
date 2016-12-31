package org.oginskis.ss.repo

import java.util
import java.util.Date

import com.mongodb._
import com.mongodb.client.FindIterable
import org.bson.Document
import org.oginskis.ss.FlatStatus
import org.oginskis.ss.model.Flat

/**
  * Created by oginskis on 30/12/2016.
  */
object FlatRepo {

 val testAuth = MongoCredential.createCredential("admin", "admin", "viktors".toCharArray())
 val auths = new util.ArrayList[MongoCredential]()
 auths.add(testAuth)
 val serverAddress = new ServerAddress("localhost", 27017)
 val mongo = new MongoClient(serverAddress, auths)
 val db = mongo.getDatabase("ss")
 db.getCollection("flats").createIndex(new BasicDBObject("address",1))
 db.getCollection("flats").createIndex(new BasicDBObject("floor",1))
 db.getCollection("flats").createIndex(new BasicDBObject("rooms",1))

 def addOrUpdateFlat(flat: Flat): FlatStatus.Value = {
   def createDocument(flat: Flat): org.bson.Document = {
     val params = new java.util.HashMap[String,Object]()
     params.put("address",flat.address)
     params.put("floor",flat.floor)
     params.put("link",flat.link)
     params.put("price",flat.price.toString)
     params.put("rooms",flat.rooms)
     params.put("size",flat.size.toString)
     params.put("lastSeenAt",new Date())
     new org.bson.Document(params)
   }
   def findFilter(flat: Flat): org.bson.Document = {
     val params = new java.util.HashMap[String,Object]()
     params.put("address",flat.address)
     params.put("floor",flat.floor)
     params.put("rooms",flat.rooms)
     params.put("size",flat.size.toString)
     params.put("price",flat.price.toString)
     params.put("link",flat.link)
     new org.bson.Document(params)
   }
   val docs: FindIterable[Document] = db.getCollection("flats").find(findFilter(flat))
   if (db.getCollection("flats").findOneAndReplace(findFilter(flat)
     ,createDocument(flat)) == null){
     db.getCollection("flats").insertOne(createDocument(flat))
     FlatStatus.Added
   }
   else {
     FlatStatus.Updated
   }
 }
}
