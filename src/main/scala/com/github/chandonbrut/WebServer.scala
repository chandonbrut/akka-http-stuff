package com.github.chandonbrut

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol


import akka.Done

import scala.concurrent.Future


object WebServer {

        implicit val system = ActorSystem("my-cluster")
        implicit val materializer = ActorMaterializer()
        implicit val executionContext = system.dispatcher

	def main(args:Array[String]) {
		val serv = new Service()
		val bindingFuture = Http().bindAndHandle(serv.route, "localhost", 8080)
		
	}
}
