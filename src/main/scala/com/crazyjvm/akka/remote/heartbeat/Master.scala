package com.crazyjvm.akka.remote.heartbeat

import akka.actor.{Props, ActorSystem, Actor}
import com.typesafe.config.ConfigFactory

/**
 * Created by chenchao on 14-6-28.
 */
class Master extends Actor{

  def receive = {
    case RegisterWorker => sender ! RegisteredWorker
  }
}

object Master{
  def main(args : Array[String]){
    val system = ActorSystem("MasterSystem" ,
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
    val actor = system.actorOf(Props[Master],"MasterActor")
  }
}
