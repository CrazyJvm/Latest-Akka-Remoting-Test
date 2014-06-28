package com.crazyjvm.akka.remote.heartbeat

import akka.actor.{Props, ActorSystem, Actor}
import scala.concurrent.duration._
import java.util.Date

/**
 * Created by chenchao on 14-6-28.
 */
case object SendHeartbeat

class Worker extends Actor{
  import context.dispatcher
  override def preStart(){
    context.system.scheduler.schedule(0 millis, 3000 millis, self, SendHeartbeat)
  }

  def receive = {
    case SendHeartbeat =>
      println("receive heartbeat at : " + new Date )
    case _ =>
  }
}

object Worker {
  def main(args : Array[String]){
    val actorSystem = ActorSystem("WorkerSystem")
    actorSystem.actorOf(Props[Worker],"WorkerActor")
  }
}
