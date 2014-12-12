import java.util

import akka.actor._

/**
* Created by Toby on 11/12/2014.
*/
object RefTest extends App {

  val system = ActorSystem("MySystem")

  val myOne = system.actorOf(Props(new ActorOne(1)), name = "myOne")
  val myTwo = system.actorOf(Props(new ActorTwo(myOne)), name = "myTwo")

  myOne ! Start(myTwo)

  //*****Follwoing is for testing calling method inside actor directly is not possbile (only through message)
  //var actOne = new ActorOne(1)
  //val myOne = system.actorOf(Props(actOne), name = "myOne")
  //val myTwo = system.actorOf(Props(new ActorTwo(myOne)), name = "myTwo")
  //myOne ! Start(myTwo)
  //myOne.myAdd()
  //actOne.myAdd

  case class Start(actTwo: ActorRef)
  case object Print
  case class Msg(data: java.util.List[String], first: ActorRef)

  class ActorOne(myStr: Int) extends Actor {
    var data: java.util.List[String] = new java.util.ArrayList[String]()

    def myAdd = {
      println("hej med dig")
    }

    def receive = {
      case Start(actTwo: ActorRef) =>
        println(data.size())
        actTwo ! Msg(data, self)
      case Print =>
        println(data.size())
        context.system.shutdown()
    }
  }

  class ActorTwo(actOne: ActorRef) extends Actor {
    def receive = {
      case Msg(data: java.util.List[String], first: ActorRef) =>
        data.add("hej")
        first ! Print
    }

  }
}
