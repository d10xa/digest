package ru.d10xa.digest

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RouteResult.route2HandlerFlow
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ru.d10xa.digest.actors.DigestDispatcherActor
import ru.d10xa.digest.routes.Routes

import scala.concurrent.ExecutionContextExecutor

object Main extends App
  with RequestTimeout {

  implicit val system = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  system.actorOf(DigestDispatcherActor.props(), DigestDispatcherActor.name)

  val port: Int = sys.props.getOrElse("port", "8080").toInt
  val host: String = sys.props.getOrElse("host", "localhost")

  private val route = Routes(system).route

  Http().bindAndHandle(route2HandlerFlow(route), host, port)

  println(s"server start at $host $port")
}

trait RequestTimeout {

  import scala.concurrent.duration.FiniteDuration

  def requestTimeout(): Timeout =
    FiniteDuration(5, TimeUnit.SECONDS)
}
