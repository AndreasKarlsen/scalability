/**
 * Created by Tobias on 17-10-2014.
 */
import akka.actor._
import akka.routing.RoundRobinRouter
import com.google.common.base.Stopwatch
import kmeans.clustering.{Cluster, Clustering, ClusteringService}
import kmeans.datageneration.DataGenerator
import kmeans.partitioning.{Partition, Partitioning, Partitioner}
import kmeans.model.Vector

object KMeans extends App {

  val _nrClusters: Int = 5
  val _nrIterations: Int = 1

  val vectors: java.util.List[Vector] = DataGenerator.generateRandomVectors(1000000)
  RunClustering(vectors, _nrClusters, 2, _nrIterations)

  //Message types
  case class Calculate(means: java.util.List[Vector]) //TODO: Add needed parameters - (vectors: java.util.List[Vector], _nrClusters: Int, nrActors: Int, _nrIterations: Int)
  case class MapWork(data: java.util.List[Vector], means: java.util.List[Vector], reducer: ActorRef)
  case class MapperResult(intermediateClustering: Clustering)
  case class ReducerResult(finalClustering: Clustering)

  //Both master and listener for final result
  class Master(nrActors: Int, partitioning: Partitioning[Vector], _nrClusters: Int) extends Actor {
    var itrCount: Int = 0
    val mapperRouter = context.actorOf(Props[Mapper].withRouter(RoundRobinRouter(nrActors)), name = "mapperRouter")
    val sw: Stopwatch = Stopwatch.createStarted
    val reducer = context.actorOf(Props(new Reducer(nrActors, _nrClusters)), name = "reducer")

    def receive = {
      case Calculate(means: java.util.List[Vector]) =>
        System.out.println("Starting iteration: " + (itrCount + 1))
        for (p : Partition[Vector] <- partitioning.getPartitions) {
          mapperRouter ! MapWork(p.getData, means, reducer) //Evenly distributes mapperwork across all the mapper actors
        }
      case ReducerResult(finalClustering: Clustering) =>
        System.out.println("Finishing iteration: " + (itrCount + 1))
        itrCount += 1
        if(itrCount == _nrIterations){
          //End computation
          sw.stop();
          // Print result
          // Stop actor system
        }
        else{
          //Send a message to start master again becuase we have to do another iteration
        }
    }
  }

  class Mapper extends Actor {

    def receive = {
      case MapWork(data: java.util.List[Vector], means: java.util.List[Vector], reducer: ActorRef) =>
        System.out.println("Actor: " + self.path.name + " started.")
        val clustering: Clustering = ClusteringService.ClusterKMeansMSIncremental(data, means)
        System.out.println("Actor: " + self.path.name + " done clustering.")
        reducer ! MapperResult(clustering)
        System.out.println("Actor: " + self.path.name + " sent MapperResult message to Reducer.")
    }
  }

  class Reducer(nrActors: Int, _nrClusters: Int) extends Actor {
    var consumedMessages: Int = 0
    var clustering: Clustering = new Clustering(_nrClusters) //intermediate clustering result

    def receive = {
      case MapperResult(c: Clustering) =>
        System.out.println("Reducer consumed actor message: " + consumedMessages + 1)

        if(nrActors != consumedMessages){ //Barrier ensuring all messages are consumed before sending final result
          //Merge clusters
          if (clustering == null) {
            clustering = c
          }

          else {
            {
              var i: Int = 0
              val size: Int = clustering.getClusters.size
              while (i < size) {
                {
                  val c1: Cluster = clustering.getClusters.get(i)
                  val c2: Cluster = c.getClusters.get(i)
                  c1.mergeWith(c2)
                }
                ({
                  i += 1; i - 1
                })
              }
            }
          }
          consumedMessages += 1
        }
        else{
          System.out.println("Reducer finished")
          //Last clustering to merge and then send final result to Master (parent node)
        }
    }
  }


  def RunClustering(vectors: java.util.List[Vector], _nrClusters: Int, nrActors: Int, _nrIterations: Int) = {
    val partitioning: Partitioning[Vector] = new Partitioner[Vector]().partition(vectors, nrActors) //Paritionining of data to given number of mapper actors
    val means: java.util.List[Vector] = DataGenerator.generateRandomVectors(_nrClusters) // Generate initial means
    //val clustering: Clustering = new Clustering(means) //Add the inital means as initial clusters TODO: Skal den også være her sendes med?

    // Create an Akka system
    val system = ActorSystem("KMeansSystem")

    // create the master
    val master = system.actorOf(Props(new Master(nrActors, partitioning, _nrClusters)), name = "master")

    // start the calculation
    master ! Calculate(means) //TODO: Insert
  }

}
