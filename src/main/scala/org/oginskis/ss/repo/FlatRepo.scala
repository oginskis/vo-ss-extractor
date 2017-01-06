package org.oginskis.ss.repo

import java.util.Date

import com.mongodb._
import com.mongodb.client.FindIterable
import org.bson.Document
import org.oginskis.ss.FlatStatus
import org.oginskis.ss.model.Flat
import org.oginskis.ss.tool.Properties

import scala.collection.mutable.ListBuffer

/**
  * Created by oginskis on 30/12/2016.
  */
object FlatRepo {

 val MONGODB_USER = "mongodb.user"
 val MONGODB_PASSWORD = "mongodb.password"
 val MONGODB_DB = "mongodb.db"
 val MONGODB_HOST = "mongodb.host"
 val MONGODB_PORT = "mongodb.port"
 val MONGODB_SSL = "mongodb.ssl"
 val COLL_NAME= "flats"

 val mongo = new MongoClient(new MongoClientURI("mongodb://"+Properties.getProperty(MONGODB_USER)+":"
   +Properties.getProperty(MONGODB_PASSWORD)+"@"+Properties.getProperty(MONGODB_HOST)
   +":"+Properties.getProperty(MONGODB_PORT)+"/?ssl="+Properties.getProperty(MONGODB_SSL)))
 val db = mongo.getDatabase(Properties.getProperty(MONGODB_DB))
 db.getCollection(COLL_NAME).createIndex(new BasicDBObject("address",1))
 db.getCollection(COLL_NAME).createIndex(new BasicDBObject("floor",1))
 db.getCollection(COLL_NAME).createIndex(new BasicDBObject("rooms",1))

 def addOrUpdateFlat(flat: Flat): FlatStatus.Value = {
   def createDocument(flat: Flat): org.bson.Document = {
     val params = new java.util.HashMap[String,Object]()
     params.put("address",flat.address.get)
     params.put("floor",flat.floor.get)
     params.put("link",flat.link.get)
     params.put("price",flat.price.get.toString)
     params.put("rooms",flat.rooms.get)
     params.put("size",flat.size.get.toString)
     params.put("lastSeenAt",new Date())
     new org.bson.Document(params)
   }
   val docs: FindIterable[Document] = db.getCollection(COLL_NAME).find(findFilter(flat))
   if (db.getCollection(COLL_NAME).findOneAndReplace(findFilter(flat)
     ,createDocument(flat)) == null){
     db.getCollection(COLL_NAME).insertOne(createDocument(flat))
     FlatStatus.Added
   }
   else {
     FlatStatus.Updated
   }
 }

 def findHistoricAdds(flat: Flat): List[Flat] = {
   val documents = db.getCollection(COLL_NAME).find(findFilter(flat))
   val cursor = documents.iterator()
   val flats = ListBuffer[Flat]()
   while (cursor.hasNext){
     val doc = cursor.next()
     flats += new Flat(Option(doc.get("address").toString),
       Option(doc.get("rooms").toString),
       Option(doc.get("size").toString.toInt),
       Option(doc.get("floor").toString),
       Option(doc.get("price").toString.toInt),
       Option(doc.get("link").toString))
   }
   flats.toList
 }

 def findFilter(flat: Flat): org.bson.Document = {
   val params = new java.util.HashMap[String,Object]()
   params.put("address",flat.address.get)
   params.put("floor",flat.floor.get)
   params.put("rooms",flat.rooms.get)
   params.put("size",flat.size.get.toString)
   if (flat.price != None) params.put("price",flat.price.get.toString)
   if (flat.link != None) params.put("link",flat.link.get)
   new org.bson.Document(params)
 }


}
