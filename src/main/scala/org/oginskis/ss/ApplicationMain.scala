package org.oginskis.ss

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import com.sun.net.httpserver.{HttpHandler, HttpExchange, HttpServer}
import org.oginskis.ss.actor.{NotificationActor, PersistActor, ExtractingActor}
import org.oginskis.ss.tool.Properties
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ApplicationMain extends App {

  val FLAT_CHECK_SCHEDULE = "flatCheckSchedule"

  val system = ActorSystem("flat-extractor-system")
  val notification = system.actorOf(Props(new NotificationActor), name = "notification")
  val persist = system.actorOf(Props(new PersistActor(notification)), name = "persist")
  val extracting = system.actorOf(Props(new ExtractingActor(persist)), name = "extracting")
  system.scheduler.schedule(0 seconds, Properties.getProperty(FLAT_CHECK_SCHEDULE).toInt seconds,
    extracting, ExtractingActor.Extract)
  val server = HttpServer.create(new InetSocketAddress(System.getProperty("httpport").toInt), 0);
  server.createContext("/", new HttpHandler {
    @Override
    def handle(t:HttpExchange) {
      val response = "This is the response";
      t.sendResponseHeaders(200, response.length());
      val os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }})
  server.setExecutor(null);
  server.start();
}

