package com.crazyjvm.become

import akka.actor._
import com.crazyjvm.become.Greeting
import com.crazyjvm.become.Happy
import com.crazyjvm.become.Angry

/**
 * Created by chenchao on 14-11-16.
 */

case class Greeting(who : String)
case class Angry
case class Happy

class GreetingActor extends Actor with ActorLogging{

  def receive = happy

  val happy : Receive = {
    case Greeting(who) => log.info(s"Hello : ${who}")
    case Angry => context.become(angry)
  }

  val angry : Receive = {
    case Greeting(_) => log.info("Go away")
    case Happy  => context.become(happy)
  }

}

object GreetingActor extends App{
  val system : ActorSystem = ActorSystem.create("MySystem")
  val greeter : ActorRef = system.actorOf(Props[GreetingActor],"greeter")

  greeter ! Angry
  greeter ! Greeting("CC")
  greeter ! Happy
  greeter ! Greeting("CC")


}
