/**
 * Created by Tobias on 17-10-2014.
 */

import java.util

import akka.actor._
import akka.routing.RoundRobinRouter
import com.google.common.base.Stopwatch
import kmeans.clustering.{Cluster, Clustering, ClusteringService}
import kmeans.datageneration.DataGenerator
import kmeans.parsing.DataParser
import kmeans.partitioning.{Partition, Partitioning, Partitioner}
import kmeans.model.Vector
import collection.JavaConversions._ //Required for the for loops over Java collections

object KMeansScala extends App {
  val _nrClusters: Int = 5
  val _nrIterations: Int = 1
  val vectors: java.util.List[Vector] = DataGenerator.generateRandomVectors(500000)

  RunStaticTest();
  //RunClustering(vectors, _nrClusters, 2, _nrIterations)

  //Message types
  case class Calculate(means: java.util.List[Vector])
  case class MapWork(data: java.util.List[Vector], means: java.util.List[Vector], reducer: ActorRef)
  case class MapperResult(intermediateClustering: Clustering)
  case class ReducerResult(finalClustering: Clustering)

  //Both master and listener for final result
  class Master(nrActors: Int, partitioning: Partitioning[Vector], nrClusters: Int, nrIterations: Int, printMeans: Boolean) extends Actor {
    var itrCount: Int = 0
    val mapperRouter = context.actorOf(Props[Mapper].withRouter(RoundRobinRouter(nrActors)), name = "mapperRouter")
    val sw: Stopwatch = Stopwatch.createStarted
    val reducer = context.actorOf(Props(new Reducer(nrActors, nrClusters)), name = "reducer")

    def receive = {
      case Calculate(means: java.util.List[Vector]) =>
        System.out.println("Starting iteration: " + (itrCount + 1))
        for (p : Partition[Vector] <- partitioning.getPartitions()) {
          mapperRouter ! MapWork(p.getData(), means, reducer) //Evenly distributes mapperwork across all the mapper actors
        }
      case ReducerResult(finalClustering: Clustering) =>
        System.out.println("Finishing iteration: " + (itrCount + 1))
        itrCount += 1

        val newMeans: java.util.List[Vector] = new util.ArrayList[Vector]()
        for (c : Cluster <- finalClustering.getClusters()) {
          newMeans.add(c.getMean)
        }

        if(itrCount != nrIterations){
          self ! Calculate(newMeans)  //Initiating new iteration by sending message to self (Master)
        }
        else{
          sw.stop();
          ResultWriter.PrintResult(sw, nrIterations, nrClusters, nrActors, "Akka", "")
          context.system.shutdown() // Stop the actor system
          ResultWriter.printVectors(newMeans)
          ResultWriter.writeVectorsToDisk(newMeans,"Akka")
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

  class Reducer(nrActors: Int, nrClusters: Int) extends Actor {
    var consumedMessages: Int = 0
    var clustering: Clustering = new Clustering(nrClusters) //intermediate clustering result

    def receive = {
      case MapperResult(c: Clustering) =>
        System.out.println("Reducer consumed actor message: " + (consumedMessages + 1))


        clustering.mergeWith(c);
        clustering.calcMeansUsingMeanSum();

        /*
        var i: Int = 0;

        //Merge clusters
        while (i < nrClusters) {
          val c1: Cluster = clustering.getClusters.get(i)
          val c2: Cluster = c.getClusters.get(i)
          c1.mergeWith(c2)
          i += 1
        }

        //Calculate new means
        for (c: Cluster <- clustering.getClusters()) {
          c.calcMean(c.getMeanSums)
        }
        */

        consumedMessages += 1

        if (nrActors == consumedMessages) { //Barrier ensuring all messages are consumed before sending final result
          System.out.println("Reducer finished")
          context.parent ! ReducerResult(clustering) //Send final result to parent (which is the Master actor)

          //Reset the reducer for next iteration
          consumedMessages = 0
          clustering = new Clustering(nrClusters)
        }
    }
  }

  def RunClustering(vectors: java.util.List[Vector], nrClusters: Int, nrActors: Int, nrIterations: Int) : Unit = {
    val means: java.util.List[Vector] = DataGenerator.generateRandomVectors(nrClusters) // Generate initial means
    RunClustering(vectors,means,nrActors,nrIterations,false)
  }

  def RunClustering(vectors: java.util.List[Vector], means : java.util.List[Vector],  nrActors: Int, nrIterations: Int, printMeans: Boolean) : Unit = {
    val partitioning: Partitioning[Vector] = new Partitioner[Vector]().partition(vectors, nrActors) //Paritionining of data to given number of mapper actors

    // Create an Akka system
    val system = ActorSystem("KMeansSystem")

    // create the master
    val master = system.actorOf(Props(new Master(nrActors, partitioning, means.size, nrIterations, printMeans)), name = "master")

    // start the calculation
     master ! Calculate(means)


  }

  def RunStaticTest(): Unit ={
    val staticVectors = DataParser.parseStaticData
    val staticMeans = DataParser.parseStaticDataMeans
    RunClustering(staticVectors, staticMeans,5,2,true)
  }

}
