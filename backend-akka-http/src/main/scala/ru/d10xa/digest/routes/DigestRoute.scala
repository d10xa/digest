package ru.d10xa.digest.routes

import java.net.URI

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.JsonAST.JString
import org.json4s.{DefaultFormats, _}
import ru.d10xa.digest.routes.DigestRoute.DigestScheduleRequestDto

class DigestRoute(system: ActorSystem) extends Json4sSupport {

  implicit val formats: Formats = DefaultFormats + UriSerializer
  implicit val serialization = native.Serialization

  val route: Route = path("digest" / "schedule") {
    (post & entity(as[DigestScheduleRequestDto])) { a: DigestScheduleRequestDto =>
      println(s"schedule $a")
      complete(StatusCodes.OK)
    }
  }

}

object DigestRoute {

  def apply(system: ActorSystem): DigestRoute = new DigestRoute(system)
  case class DigestScheduleRequestDto(algo: String, src: URI)

}

case object UriSerializer
  extends CustomSerializer[URI](_ =>
    ( {
      case JString(u) => new URI(u)
    }, {
      case u: URI => JString(u.toString)
    })
  )
