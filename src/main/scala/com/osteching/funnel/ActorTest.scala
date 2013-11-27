/**
 *
 */
package com.osteching.funnel

import akka.actor._

/**
 * @author Zhenxing Zhu
 *
 */
object ActorTest {

  class Ping(pong: ActorRef) extends Actor {

    var count = 0
    def incrementCounter = count += 1

    val MAX_NUM_PINGS = 5

    def receive = {
      case StartMessage =>
        incrementCounter
        pong ! PingMessage
      case PongMessage =>
        incrementCounter
        if (count > MAX_NUM_PINGS) {
          sender ! StopMessage
          println("ping stopped")
          context.stop(self)
        } else {
          sender ! PingMessage
          Thread.sleep(100) // blocking! bad!
        }
    }
  }

  class Pong extends Actor {
    def receive = {
      case PingMessage =>
        sender ! PongMessage
        Thread.sleep(100) // blocking! bad!
      case StopMessage =>
        println("pong stopped")
        context.stop(self)
    }

  }

  def main(args: Array[String]): Unit = {
    // create the actor system
    val actorSystem = ActorSystem("PingPongSystem")

    val pong = actorSystem.actorOf(Props[Pong], name = "pong")
    val ping = actorSystem.actorOf(Props(new Ping(pong)), name = "ping")

    Thread.sleep(5 * 1000)

    ping ! StartMessage

  }

  case class Speak(who: String, line: String)

  case class PingMessage

  case class PongMessage

  case class StopMessage

  case class StartMessage

}
