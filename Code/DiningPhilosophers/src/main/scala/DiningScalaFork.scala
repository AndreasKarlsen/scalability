/**
 * Created by Kasper on 1/22/2015.
 */
/**
 * Created by Toby on 20/1/2015.
 */

import java.util.concurrent.TimeUnit

import akka.actor.Actor.Receive
import akka.actor._
import com.google.common.base.Stopwatch


object DiningScalaFork extends App {
  Run(200) //Staring the computation (with number of start food)

  def Run(startFood: Int): Unit = {
    val system = ActorSystem("DiningPhilSystem")
    val waiter = system.actorOf(Props(new Waiter(startFood)), name = "waiter")
    val fork0 = system.actorOf(Props(new Fork()))
    val fork1 = system.actorOf(Props(new Fork()))
    val fork2 = system.actorOf(Props(new Fork()))
    val fork3 = system.actorOf(Props(new Fork()))
    val fork4 = system.actorOf(Props(new Fork()))

    val phil0 = system.actorOf(Props(new Philosopher(waiter, fork0, fork1)), name = "0")
    val phil1 = system.actorOf(Props(new Philosopher(waiter, fork1, fork2)), name = "1")
    val phil2 = system.actorOf(Props(new Philosopher(waiter, fork2, fork3)), name = "2")
    val phil3 = system.actorOf(Props(new Philosopher(waiter, fork3, fork4)), name = "3")
    val phil4 = system.actorOf(Props(new Philosopher(waiter, fork4, fork0)), name = "4")
    phil0 ! PhilBegin
    phil1 ! PhilBegin
    phil2 ! PhilBegin
    phil3 ! PhilBegin
    phil4 ! PhilBegin
  }

  //Message types

  case object Pickup
  case object PutDown
  case object ForkBusy
  case object ForkAquired

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

  class Fork() extends  Actor {

    var state = true;

    override def receive: Receive = {
      case Pickup =>
        if(state){
          state = false;
          sender ! ForkAquired
        }else{
          sender ! ForkBusy
        }
      case PutDown =>
        state = true;
    }
  }

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
      case Done =>
        println("Elapsed time: " + sw.stop().elapsed(TimeUnit.MILLISECONDS))
        println("All the food is gone! :(")
        context.system.shutdown() //stop actor system
    }
  }

  class Philosopher(waiter: ActorRef, left: ActorRef, right: ActorRef) extends Actor {

    var hasLeft: Boolean = false

    def eat (): Unit = {
      //println("Phil " + self.path.name + " is eating")
      waiter ! TakeFood
      Thread.sleep(100)
      //println("Phil " + self.path.name + " is DONE eating")
      left ! PutDown
      right ! PutDown
      hasLeft = false
    }

    def think (): Unit = {
      //println("Phil " + self.path.name + " is thinking")
      Thread.sleep(100)
      //println("Phil " + self.path.name + " is DONE thinking")
      self ! PhilBegin
    }

    def receive = {
      case PhilBegin =>
        left ! Pickup
      case ForkAquired =>
        if(hasLeft){
          eat()
          think()
        }else{
          hasLeft = true
          right ! Pickup
        }
      case ForkBusy =>
        if(hasLeft){
          left ! PutDown
        }
        think()
    }
  }
}

