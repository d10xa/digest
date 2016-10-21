package ru.d10xa.digest.actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}

class DigestDispatcherActor extends Actor with ActorLogging {

  override def receive = {
    case _ => log.info(s"receive ${this.self.path}")
  }

}

object DigestDispatcherActor {

  val name = "DigestDispatcher"

  case class NewTask(id: UUID)

  def props(): Props = Props(new DigestDispatcherActor())
}
