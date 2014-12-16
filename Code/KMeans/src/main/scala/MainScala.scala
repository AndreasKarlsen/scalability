import java.util.List

import kmeans.datageneration.DataGenerator
import kmeans.model.Vector

/**
 * Created by Kasper on 23-09-2014.
 */
object MainScala {

  val useArgs = false;


  def main(args: Array[String]) {

    if (useArgs) {
      if (args.length != 3) {
        println("Invalid number of arguments")
        System.out.println()
        System.exit(0)
      }

      val arg1: String = args(0)
      val arg2: String = args(1)
      val arg3: String = args(2)

      var nrVectors: Int = 0
      var nrMappers: Int = 0
      var nrIterations: Int = 0

      try {
        nrVectors = arg1.toInt
        nrMappers = arg2.toInt
        nrIterations = arg3.toInt
      }
      catch {
        case e: NumberFormatException => {
          System.out.println("Parameters are not valid integers")
          System.exit(0)
        }
      }
      val vectors: List[Vector] = DataGenerator.generateRandomVectors(nrVectors)
      KMeansScala.RunClustering(vectors, 5, nrMappers, nrIterations);
    }else{
      val vectors: List[Vector] = DataGenerator.generateRandomVectors(100000)
      KMeansScala.RunClustering(vectors, 5, 5, 10);
    }
  }
}
