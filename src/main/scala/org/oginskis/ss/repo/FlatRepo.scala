package org.oginskis.ss.repo

import java.util
import java.util.Date

import com.mongodb._
import com.mongodb.client.FindIterable
import org.bson.Document
import org.oginskis.ss.FlatStatus
import org.oginskis.ss.model.Flat
import org.oginskis.ss.tool.Properties

/**
  * Created by oginskis on 30/12/2016.
  */
object FlatRepo {

 val MONGODB_USER = "mongodb.user"
 val MONGODB_ORGANIZATION = "mongodb.organization"
 val MONGODB_PASSWORD = "mongodb.password"
 val MONGODB_DB = "mongodb.db"
 val MONGODB_HOST = "mongodb.host"
 val MONGODB_PORT = "mongodb.port"
 val COLL_NAME= "flats"

 val testAuth = MongoCredential.createCredential(Properties.getProperty(MONGODB_USER),
   Properties.getProperty(MONGODB_ORGANIZATION),
   Properties.getProperty(MONGODB_PASSWORD).toCharArray())
 val auths = new util.ArrayList[MongoCredential]()
 auths.add(testAuth)
 val serverAddress = new ServerAddress(Properties.getProperty(MONGODB_HOST),
   Properties.getProperty(MONGODB_PORT).toInt)
 val mongo = new MongoClient(serverAddress, auths)
 val db = mongo.getDatabase(Properties.getProperty(MONGODB_DB))
 db.getCollection(COLL_NAME).createIndex(new BasicDBObject("address",1))
 db.getCollection(COLL_NAME).createIndex(new BasicDBObject("floor",1))
 db.getCollection(COLL_NAME).createIndex(new BasicDBObject("rooms",1))

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
}
