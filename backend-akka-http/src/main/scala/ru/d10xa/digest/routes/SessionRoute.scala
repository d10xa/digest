package ru.d10xa.digest.routes

import java.util.UUID

import akka.http.scaladsl.model.headers.{HttpCookie, HttpCookiePair}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

object SessionRoute {

  val SESSION_COOKIE_NAME = "JSESSIONID"

  implicit val formats =
    DefaultFormats

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

  private def optionalSessionCookie: Directive1[Option[HttpCookiePair]] =
    optionalCookie(SESSION_COOKIE_NAME)

  private def setSessionCookie(sessionId: String): Directive0 =
    setCookie(HttpCookie(SESSION_COOKIE_NAME, sessionId))

  val route: Route = path("session" / "id") {
    get {
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

  case class SessionIdDto(id: String)

}
