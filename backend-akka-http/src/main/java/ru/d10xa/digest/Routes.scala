package ru.d10xa.digest

import java.util.UUID

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s
import org.json4s.native.Serialization.write

object Routes extends Json4sSupport {

  private val okResponse = 200

  implicit val formats = json4s.DefaultFormats

  val SESSION_COOKIE_NAME = "JSESSIONID"

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
      HttpResponse(okResponse).withHeaders(
        `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }

  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }

  val route: Route = corsHandler {
    path("session" / "id") {
      optionalSessionCookie { cookie =>
        var uuid = ""
        cookie match {
          case Some(b) =>
            uuid = b.value
          case None =>
            uuid = UUID.randomUUID().toString
        }
        setSessionCookie(uuid) {
          complete(write(SessionIdDto(uuid)))
        }
      }
    }
  }

  def session(r: Route): Route = optionalSessionCookie {
    case Some(b) =>
      setSessionCookie(b.value) {
        r
      }
    case None =>
      setSessionCookie(UUID.randomUUID().toString) {
        r
      }
  }

  val notAvailable: Route = get {
    complete("***404***")
  }

  private def optionalSessionCookie: Directive1[Option[HttpCookiePair]] =
    optionalCookie(SESSION_COOKIE_NAME)

  private def setSessionCookie(sessionId: String): Directive0 =
    setCookie(HttpCookie(SESSION_COOKIE_NAME, sessionId))

}

case class SessionIdDto(id: String)
