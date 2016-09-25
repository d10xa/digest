package ru.d10xa.digest

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import ru.d10xa.digest.SimpleActor.Greet
import akka.pattern.ask
import akka.util.Timeout

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object Main {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("digest-system")
    implicit val materializer = ActorMaterializer()
    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    implicit val executionContext = system.dispatcher
    val greetActor = system.actorOf(Props[SimpleActor])

    val r: Route = extractRequest(request =>
      complete("Request method is " + request.method.name() +
        " and content-type is " + request.entity.getContentType())
    )

    val route =
      path("hello") {
        get {
          onComplete(greetActor ? Greet("User")) { case t:Try[String] =>
            t match {
              case Success(s) =>
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s))
              case Failure(e) =>
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<p>$e</p>"))
            }
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}

object SimpleActor {
  case class Greet(message:String)
}

class SimpleActor extends Actor {
  override def receive: Receive = {
    case g:Greet => Thread.sleep(100); sender ! s"<p>greeting $g</p>"
  }
}
