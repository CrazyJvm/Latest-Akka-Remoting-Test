package com.crazyjvm.akka.remote.test1

import akka.actor.{Props, ActorSystem, Actor}
import com.typesafe.config.ConfigFactory
import akka.remote.{AssociatedEvent, RemotingLifecycleEvent}

/**
 * Created by chenchao on 14-6-25.
 */
class MyRemotingActor extends Actor{

  context.system.eventStream.subscribe(self, classOf[RemotingLifecycleEvent])

  def receive = {
    case AssociatedEvent(localAddress,remoteAddress,inbound) =>
      println(s"info : local address is $localAddress, remote address is $remoteAddress," +
        s"inbound is $inbound")
    case "name" => {println("----------------this is cc-------------");sender ! "cc"}
    case "thanks" => println("You are welcome")
    case _ =>
  }
}

object MyRemotingActor{
  def main(args : Array[String]){
    //println(ConfigFactory.load())
    //val system = ActorSystem("MySystem" , ConfigFactory.load())
    val system = ActorSystem("MySystem" ,
      ConfigFactory.parseString(
        """
          |akka {
          |  actor {
          |    provider = "akka.remote.RemoteActorRefProvider"
          |  }
          |  remote {
          |    enabled-transports = ["akka.remote.netty.tcp"]
          |    netty.tcp {
          |      hostname = "127.0.0.1"
          |      port = 2050
          |    }
          | }
          |}
        """.stripMargin)
    )
    val actor = system.actorOf(Props[MyRemotingActor],"MyRemotingActor")
  }
}