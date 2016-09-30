package ru.d10xa.digest

import java.util.UUID

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.headers.{HttpCookie, HttpCookiePair}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

object Routes extends {

  val SESSION_COOKIE_NAME = "JSESSIONID"

  val route: Route = session(
    path("session" / "id") {
      optionalSessionCookie { session: Option[HttpCookiePair] =>
        complete( ToResponseMarshallable(
          HttpEntity(ContentTypes.`application/json`,
            s"""{"id":"${session.get.value}"}"""
          )))
      }
    }
  )

  val sessionIdPath: Directive[Unit] = path("session" / "id")

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
