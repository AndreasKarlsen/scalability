import java.io.{File, FileWriter, BufferedWriter, PrintWriter}
import java.nio.file._
import java.util
import scala.collection.JavaConverters._


/**
 * Created by Kasper on 15-12-2014.
 */
object Main {
  def main(args: Array[String]) {
    if(args.length == 1){
      MeanCalculator.Calculate(args(0))
      println("Done")
    }else{
      println("Invalid input...")
    }
  }
}

object MeanCalculator {


  def Calculate(folder: String): Unit ={
    val writer = new PrintWriter(new BufferedWriter(new FileWriter("means.td",true)))
    CalculateInternal(folder,writer)
    writer.flush
    writer.close
  }

  private def CalculateInternal(folder: String, sw: PrintWriter): Unit = {

    val f = new File(folder)
    val p = Paths.get(folder)
    if (Files.notExists(p)) {
      println("Invalid path: " + folder)
      System.exit(0)
    }

    val files = f.list()

    if (files != null) {
      for (path <- files) {
        val fullPath = Paths.get(folder,path).toString
        val subFile = new File(fullPath)
        if (subFile.isDirectory) {
          CalculateInternal(fullPath, sw)
        } else if (path.toString.toLowerCase.endsWith(".td")) {
          val lines = Files.readAllLines(Paths.get(fullPath))
          var milisecs = 0
          var count = 0
          for (line <- lines.asScala) {
            if (line.startsWith("Time:")) {
              val parts = line.split(" ")
              milisecs += parts(1).toInt
              count = count + 1
            }
          }
          if (count > 0) sw.println(folder+ File.separator +path + ": " + (milisecs / count))
        }
      }
    }
  }
}