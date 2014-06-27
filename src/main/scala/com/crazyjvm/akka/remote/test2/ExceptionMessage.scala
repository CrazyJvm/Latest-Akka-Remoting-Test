package com.crazyjvm.akka.remote.test2

/**
 * Created by chenchao on 14-6-27.
 */
sealed trait ExceptionMessage extends Serializable

case class MyException(content : String) extends ExceptionMessage


