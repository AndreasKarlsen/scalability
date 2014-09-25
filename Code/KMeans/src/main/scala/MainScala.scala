import kmeans.datageneration.DataGenerator
import kmeans.datageneration.DataGenerator
import kmeans.model.Vector
import kmeans.parsing.DataParser
import kmeans.partitioning.Partitioner
import kmeans.partitioning.Partitioning
import java.util.ArrayList
import java.util.List

/**
 * Created by Kasper on 23-09-2014.
 */
object MainScala {

  def main(args: Array[String]) {
    val vectors = DataGenerator.generateData()
    val partitioning = new Partitioner[Vector].partition(vectors,5);

    println("Hello, world!")
  }
}
