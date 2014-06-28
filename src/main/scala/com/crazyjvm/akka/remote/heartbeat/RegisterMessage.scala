package com.crazyjvm.akka.remote.heartbeat

/**
 * Created by chenchao on 14-6-28.
 */
trait RegisterMessage extends Serializable

case object RegisterWorker extends RegisterMessage

case object RegisteredWorker extends RegisterMessage