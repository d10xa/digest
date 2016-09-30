package ru.d10xa.digest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import akka.http.scaladsl.server.Directives._

object Main extends App {

  implicit val system = ActorSystem("digest")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(
    Routes.route,
    sys.props.getOrElse("host", "localhost"),
    sys.props.getOrElse("port", "8080").toInt
  )

}
