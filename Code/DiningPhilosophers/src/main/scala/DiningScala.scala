/**
* Created by Toby on 20/1/2015.
*/

import java.util.concurrent.TimeUnit

import akka.actor._
import com.google.common.base.Stopwatch


object DiningScala extends App {
  Run(200) //Staring the computation (with number of start food)

  def Run(startFood: Int): Unit = {
    val system = ActorSystem("DiningPhilSystem")
    val waiter = system.actorOf(Props(new Waiter(startFood)), name = "waiter")
    val phil0 = system.actorOf(Props(new Philosopher(waiter)), name = "0")
    val phil1 = system.actorOf(Props(new Philosopher(waiter)), name = "1")
    val phil2 = system.actorOf(Props(new Philosopher(waiter)), name = "2")
    val phil3 = system.actorOf(Props(new Philosopher(waiter)), name = "3")
    val phil4 = system.actorOf(Props(new Philosopher(waiter)), name = "4")
    phil0 ! PhilBegin
    phil1 ! PhilBegin
    phil2 ! PhilBegin
    phil3 ! PhilBegin
    phil4 ! PhilBegin
  }

  //Message types

  //Waiter messages
  case object TakeFood
  case object PickupLeft
  case object PickupRight
  case object PutDownLeft
  case object PutDownRight
  case object Done

  //Philosopher messages
  case object PhilBegin
  case object AcquiredLeft
  case object AcquiredRight
  case object BusyLeft
  case object BusyRight


  //Actors
  class Waiter(startFood: Int) extends Actor {
    val sw = Stopwatch.createStarted
    var forks = Array(0,0,0,0,0)
    var food = startFood

    def philPos() = {
      sender().path.name.toInt //name of philosopher (which is an index)
    }

    def leftForkPos(phil_pos: Int) : Int = {
      (phil_pos + 1) % 5 //5 = number of forks
    }

    def rightForkPos(phil_pos: Int) = {
      phil_pos
    }

    def receive = {
      case TakeFood =>
        food -= 1
        if(food == 0){
          self ! Done
        }
      case PickupLeft =>
        val l_fork_pos = leftForkPos(philPos())
        if(forks(l_fork_pos)==0){
          forks(l_fork_pos) = 1
          sender ! AcquiredLeft
        } else {
          sender ! BusyLeft
        }
      case PickupRight =>
        val r_fork_pos = rightForkPos(philPos())
        if(forks(r_fork_pos)==0){
          forks(r_fork_pos) = 1
          sender ! AcquiredRight
        } else {
          sender ! BusyRight
        }
      case PutDownLeft =>
        forks(leftForkPos(philPos())) = 0
      case PutDownRight =>
        forks(rightForkPos(philPos())) = 0
      case Done =>
        println("Elapsed time: " + sw.stop().elapsed(TimeUnit.MILLISECONDS))
        println("All the food is gone! :(")
        context.system.shutdown() //stop actor system
    }
  }

  class Philosopher(waiter: ActorRef) extends Actor {

    def eat (): Unit = {
      println("Phil " + self.path.name + " is eating")
      waiter ! TakeFood
      Thread.sleep(100)
      println("Phil " + self.path.name + " is DONE eating")
      waiter ! PutDownRight
      waiter ! PutDownLeft
    }

    def think (): Unit = {
      println("Phil " + self.path.name + " is thinking")
      Thread.sleep(100)
      println("Phil " + self.path.name + " is DONE thinking")
      self ! PhilBegin
    }

    def receive = {
      case PhilBegin =>
        waiter ! PickupLeft
      case AcquiredLeft =>
        waiter ! PickupRight
      case AcquiredRight =>
        eat() //Both acquired
        think()
      case BusyLeft =>
        think()
      case BusyRight =>
        waiter ! PutDownLeft
        think()
    }
  }
}
