package com.crazyjvm.create

import akka.actor._

/**
 * Created by chenchao on 14-11-14.
 */
class CreateActor {

}

case class Greeting(who : String)

class GreetingActor extends Actor with ActorLogging{
  def receive = {
    case Greeting(who) => log.info("Hello " + who)
  }
}

object CreateActor extends App{
  val system : ActorSystem = ActorSystem.create("MySystem")
  val greeter : ActorRef = system.actorOf(Props[GreetingActor],"greeter")

  greeter ! Greeting("CC")

  system.stop(greeter)

  system.shutdown()
}
