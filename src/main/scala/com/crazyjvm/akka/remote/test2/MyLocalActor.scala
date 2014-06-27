package com.crazyjvm.akka.remote.test2

import akka.actor.{Props, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory
import akka.remote.{DisassociatedEvent, AssociatedEvent}

/**
 * Created by chenchao on 14-6-27.
 */
class MyLocalActor extends Actor{

  val selection = context.actorSelection("akka.tcp://EventStreamSystem@127.0.0.1:2050/user/RemoteEventStreamActor")


  override def preStart() {
    selection ! MyException
  }

  def receive = {
    case "Exception" => println("---------Exception-------------")

    case AssociatedEvent(localAddress,remoteAddress,inbound) =>
      println(s"AssociatedEvent info : local address is $localAddress, remote address is $remoteAddress," +
        s"inbound is $inbound")

    case DisassociatedEvent(localAddress,remoteAddress,inbound) =>
      println(s"DisassociatedEvent info : local address is $localAddress, remote address is $remoteAddress," +
        s"inbound is $inbound")

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