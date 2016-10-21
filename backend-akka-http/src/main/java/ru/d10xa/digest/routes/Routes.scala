package ru.d10xa.digest.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s

object Routes {
  def apply(system: ActorSystem): Routes = new Routes(system)
}

class Routes(system: ActorSystem) extends Json4sSupport {

  implicit val formats = json4s.DefaultFormats

  val digest: Route = DigestRoute(system).route

  val session: Route = SessionRoute.route

  val route: Route =
    CorsSupport
      .corsHandler(session ~ digest)

  val notAvailable: Route = get {
    complete("***404***")
  }

}
