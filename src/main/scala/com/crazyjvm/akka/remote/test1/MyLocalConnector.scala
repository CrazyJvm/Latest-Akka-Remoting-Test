package com.crazyjvm.akka.remote.test1

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Created by chenchao on 14-6-26.
 */
class MyLocalActor extends Actor{
  val selection : ActorSelection = context.actorSelection("akka.tcp://MySystem@127.0.0.1:2050/user/MyRemotingActor")

  def receive = {
    case "Success" => {println("Success" + selection); selection ! "name" }
    case "cc" => {println("Got cc"); sender ! "thanks"}
    case _ => println("unknown information")
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
    actor ! "Success"

  }
}
