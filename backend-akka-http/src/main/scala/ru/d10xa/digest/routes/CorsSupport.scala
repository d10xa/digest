package ru.d10xa.digest.routes

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

object CorsSupport {

  private val RESPONSE_STATUS_OK = 200

  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }

  private def addAccessControlHeaders: Directive0 = {
    mapResponseHeaders { headers =>
      `Access-Control-Allow-Origin`("http://localhost:8080") +:
        `Access-Control-Allow-Credentials`(true) +:
        `Access-Control-Allow-Headers`("*") +:
        headers
    }
  }

  private def preflightRequestHandler: Route = options {
    complete(
      HttpResponse(RESPONSE_STATUS_OK).withHeaders(
        `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }

}
