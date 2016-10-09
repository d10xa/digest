package ru.d10xa.digest.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s

object Routes extends Json4sSupport {

  implicit val formats = json4s.DefaultFormats

  val route: Route =
    CorsSupport
      .corsHandler(Session.route)

  val notAvailable: Route = get {
    complete("***404***")
  }
}

