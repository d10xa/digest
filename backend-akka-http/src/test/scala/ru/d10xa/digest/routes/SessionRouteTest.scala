package ru.d10xa.digest.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}
import ru.d10xa.digest.routes.SessionRoute.SessionIdDto

class SessionRouteTest extends FlatSpec with Matchers with ScalatestRouteTest {

  import org.json4s.DefaultFormats
  import org.json4s.native.Serialization.read

  implicit val formats = DefaultFormats

  it should "return uuid" in {
    Get("/session/id") ~> SessionRoute.route ~> check {
      val s = read[SessionIdDto](responseAs[String])
      s.id should not be null
    }
  }

}
