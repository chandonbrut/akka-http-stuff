package com.github.chandonbrut


import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import akka.util.ByteString

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import spray.json._
import akka.stream._
import akka.stream.scaladsl._

import akka.Done

import scala.util.Random

import scala.concurrent.Future

case class GeoJSONPoint(`type`:String = "Point", coordinates:Seq[Double])
case class GeoJSONMultipolygon(`type`:String = "Multipolygon", coordinates:Seq[Seq[Seq[Double]]])
case class Report(messageId:String,imoNumber:String,position:GeoJSONPoint,timestamp:Long)
case class SURPICRequest(messageId:String,timestamp:Long,area:GeoJSONMultipolygon)

trait JsonSupport extends SprayJsonSupport {
	implicit val geoJSONPointFormat = jsonFormat2(GeoJSONPoint.apply)
	implicit val geoJSONMultipolygonFormat = jsonFormat2(GeoJSONMultipolygon.apply)
	implicit val reportFormat = jsonFormat4(Report.apply)
	implicit val surpicRequestFormat = jsonFormat3(SURPICRequest.apply)
}


class Service extends Directives with JsonSupport {

	implicit val system = ActorSystem("my-cluster")
	implicit val materializer = ActorMaterializer()
	implicit val executionContext = system.dispatcher

	val intIterator = Stream.from(1).toIterator

	val numbers = Source.fromIterator(() => Iterator.continually(intIterator.next))

	val route = {
		get {
			path("") {
				complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,"<h1>Hello</h1>"))
			} ~
			path("numbers") {
				complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, numbers.map(n => ByteString(s"$n\n"))))
			}
		} ~ 
		post {
			path("surpic") {
				entity(as[SURPICRequest]) { request => 
					val done:Future[Done] = Future { Done }
					onComplete(done) { d => complete(request) }
				}
			} ~
			path("report") {
				entity(as[Report]) { request => 
					val done:Future[Done] = Future { Done }
					onComplete(done) { d => complete(request) }
				}
			}	

		}
	}
}
