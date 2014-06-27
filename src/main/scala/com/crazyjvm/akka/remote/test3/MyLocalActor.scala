package com.crazyjvm.akka.remote.test3

import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory
import akka.remote.{DisassociatedEvent, AssociatedEvent}
import scala.concurrent.duration.Duration
import akka.util.Timeout
import scala.concurrent.Await

/**
 * Created by chenchao on 14-6-27.
 */
class MyLocalActor extends Actor{

  val selection = context.actorSelection("akka.tcp://FutureSystem@127.0.0.1:2050/user/FutureActor")

  override def preStart() {
    import akka.pattern.ask
    val timeoutDuration = Duration.create(3,"sec")
    implicit val timeout = Timeout(timeoutDuration)
    val future = selection.ask("Timeout")
    val result = Await.result(future, timeoutDuration).asInstanceOf[String]
    println(result)
  }

  def receive = {
    case AssociatedEvent(localAddress,remoteAddress,inbound) =>
      println(s"AssociatedEvent info : local address is $localAddress, remote address is $remoteAddress," +
        s"inbound is $inbound")

    case DisassociatedEvent(localAddress,remoteAddress,inbound) =>
      println(s"DisassociatedEvent info : local address is $localAddress, remote address is $remoteAddress," +
        s"inbound is $inbound")

    case "NOT_TIMEOUT" =>
      println("Not timeout")

    case _ =>
  }
}

object MyLocalConnector{
  def main(args : Array[String]){
    val system = ActorSystem("LocalSystem" ,
      ConfigFactory.parseString(
        """
          |akka {
          |  actor {
          |    provider = "akka.remote.RemoteActorRefProvider"
          |  }
          |  remote {
          |    enabled-transports = ["akka.remote.netty.tcp"]
          |  }
          |}
        """.stripMargin)
    )
    val actor = system.actorOf(Props[MyLocalActor],"MyLocalActor")
  }
}