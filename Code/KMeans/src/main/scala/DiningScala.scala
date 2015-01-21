/**
* Created by Toby on 20/1/2015.
*/
import akka.actor._
import akka.routing.RoundRobinRouter
import com.google.common.base.Stopwatch


object  DiningScala extends App {

  def Run(nrFood: Int): Unit = {
    val system = ActorSystem("DiningPhilSystem")
    //val waiter
    //val philos
    //Start alle philos
  }

  //Message types
  case object Hungry
  case object Eat
  case object Think
  case object Done

  //Actors
  class Waiter extends Actor {
    var forks = Array(0,0,0,0,0)

    def receive = {
      case Hungry =>
        val phil_pos = sender().path.name.toInt //philosopher name

        val l_fork_pos = phil_pos
        val r_fork_pos = phil_pos + 1

        if(forks(l_fork_pos) == 0 && forks(r_fork_pos) ==0) {
          forks(l_fork_pos) = 1
          forks(r_fork_pos) = 1
          sender ! Eat
        }
        else {
          sender ! Think
        }

      case Done =>
        //bla
    }
  }

  class Philosopher extends Actor {

    def receive = {
      //case
    }
  }


}
