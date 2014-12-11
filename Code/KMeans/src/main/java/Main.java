import com.google.common.base.Stopwatch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import kmeans.Mapper;
import kmeans.MapperNaive;
import kmeans.Reducer;
import kmeans.ReducerNaive;
import kmeans.clustering.Cluster;
import kmeans.clustering.Clustering;
import kmeans.clustering.ClusteringService;
import kmeans.datageneration.DataGenerator;
import kmeans.model.Vector;
import kmeans.parsing.DataParser;
import kmeans.partitioning.Partition;
import kmeans.partitioning.Partitioner;
import kmeans.partitioning.Partitioning;

import javax.xml.crypto.Data;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Main {

    public static int _nrClusters = 5;
    public static int _nrIterations = 1;
    public static boolean useArgs = true;

    public static void main(String[] args) {
        try {
            if (useArgs){
                if(args.length != 2) {
                    System.out.println("Invalid number of arguments");
                    System.exit(0);
                }

                String arg1 = args[0];
                String arg2 = args[1];

                int nrVectors = 0;
                int nrMappers = 0;
                try {
                    nrVectors = Integer.parseInt(arg1);
                    nrMappers = Integer.parseInt(arg2);

                }catch (NumberFormatException e){
                    System.out.println("Parameters are not valid integers");
                    System.exit(0);
                }

                List<Vector> vectors = DataGenerator.generateRandomVectors(nrVectors);
                RunClustering(vectors,5,nrMappers,10);
            }else{
                List<Vector> vectors = DataGenerator.generateData();
                RunClustering(vectors,5,5,1);
            }



            //List<Vector> vectors = DataGenerator.generateData();
            //RunClusteringSingleThread(vectors,5,1);
            //RunClusteringNaive(vectors, 5, 7, 1);
            //RunClustering(vectors, 5, 4, 1);

            //"Best" implementation
            /*
            RunClusteringThreadScale();
            RunClusteringDataScale();
            */

            //Naive implementation
            /*
            RunClusteringNaiveThreadScale();
            RunClusteringDataScale();
            */
            /*
            ArrayList<Integer> a1 = new ArrayList<Integer>();
            a1.add(87);
            a1.add(67);
            a1.add(17);
            a1.add(35);
            a1.add(42);
            ArrayList<Integer> a2 = new ArrayList<Integer>();
            a2.add(15);
            a2.add(44 );
            a2.add(57);
            a2.add(23);
            a2.add(76);
            Vector v1 = new Vector(a1);
            Vector v2 = new Vector(a2);
            double cosineSimilarity = v1.vectorSimilarityWith(v2);
            double manDistance = v1.manhattanDistanceTo(v2);
            double euDistance = v1.euclideanDistanceTo(v2);
            double covariance = v1.covarianceWith(v2);
            double sdv = v1.standardDeviation();
            double pearsons = v1.pearsonCorrelationWith(v2);
            */


            /*
            Partitioning<Vector> partitioning = new Partitioner<Vector>().partition(vectors, 5);
            List<Vector> randomMeans = DataGenerator.generateRandomVectors(100,5,100);
            Clustering clustering = ClusteringService.ClusterKMeans(partitioning.getPartitions().get(0).getData(),randomMeans);
            for(Cluster c : clustering.getClusters()){
                Vector mean = c.calcMean();
                String s = "";
            }
            */
            String breakString = "";

        }
        catch (Exception ex) {
            System.out.println("Error:");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }


    }

    public static void RunStaticTest() throws IOException {

        List<Vector> staticVectors = DataParser.parseStaticData();
        List<Vector> staticMeans = DataParser.parseStaticDataMeans();
        RunClustering(staticVectors,staticMeans,5,2,"");
        ResultWriter.printVectors(staticMeans);
        ResultWriter.writeVectorsToDisk(staticMeans, "Java");
    }


    public static void RunClustering(List<Vector> vectors, int nrClusters,  int maxIterationCount) {
        RunClustering(vectors,nrClusters,nrClusters,maxIterationCount);
    }

    public static void RunClusteringThreadScale(){

        String type = "ThreadScale";
        List<Vector> vectors = DataGenerator.generateRandomVectors(1000000);
        //RunClusteringSingleThread(vectors,_nrClusters,_nrIterations);
        RunClustering(vectors,_nrClusters,2,_nrIterations, type);
        RunClustering(vectors,_nrClusters,3,_nrIterations, type);
        RunClustering(vectors,_nrClusters,4,_nrIterations, type);
        RunClustering(vectors,_nrClusters,5,_nrIterations, type);
        RunClustering(vectors,_nrClusters,6,_nrIterations, type);
        RunClustering(vectors,_nrClusters,7,_nrIterations, type);
    }

    public static void RunClusteringDataScale(){
        String type = "DataScale";
        List<Vector> vectors100k = DataGenerator.generateRandomVectors(100000);
        RunClustering(vectors100k,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors200k = DataGenerator.generateRandomVectors(200000);
        RunClustering(vectors200k,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors500k = DataGenerator.generateRandomVectors(500000);
        RunClustering(vectors500k,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors1m = DataGenerator.generateRandomVectors(1000000);
        RunClustering(vectors1m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors15m = DataGenerator.generateRandomVectors(1500000);
        RunClustering(vectors15m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors2m = DataGenerator.generateRandomVectors(2000000);
        RunClustering(vectors2m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors25m = DataGenerator.generateRandomVectors(2500000);
        RunClustering(vectors25m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors3m = DataGenerator.generateRandomVectors(3000000);
        RunClustering(vectors3m,_nrClusters,3,_nrIterations, type);
    }

    public static void RunClusteringNaiveThreadScale(){
        String type = "ThreadScale";
        List<Vector> vectors = DataGenerator.generateRandomVectors(1000000);
        //RunClusteringSingleThread(vectors,_nrClusters,_nrIterations);
        RunClusteringNaive(vectors,_nrClusters,2,_nrIterations, type);
        RunClusteringNaive(vectors,_nrClusters,3,_nrIterations, type);
        RunClusteringNaive(vectors,_nrClusters,4,_nrIterations, type);
        RunClusteringNaive(vectors,_nrClusters,5,_nrIterations, type);
        RunClusteringNaive(vectors,_nrClusters,6,_nrIterations, type);
        RunClusteringNaive(vectors,_nrClusters,7,_nrIterations, type);
        RunClusteringNaive(vectors,_nrClusters,8,_nrIterations, type);
    }

    public static void RunClusteringNaiveDataScale(){
        String type = "DataScale";
        List<Vector> vectors100k = DataGenerator.generateRandomVectors(100000);
        RunClusteringNaive(vectors100k,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors200k = DataGenerator.generateRandomVectors(200000);
        RunClusteringNaive(vectors200k,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors500k = DataGenerator.generateRandomVectors(500000);
        RunClusteringNaive(vectors500k,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors1m = DataGenerator.generateRandomVectors(1000000);
        RunClusteringNaive(vectors1m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors15m = DataGenerator.generateRandomVectors(1500000);
        RunClusteringNaive(vectors15m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors2m = DataGenerator.generateRandomVectors(2000000);
        RunClusteringNaive(vectors2m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors25m = DataGenerator.generateRandomVectors(2500000);
        RunClusteringNaive(vectors25m,_nrClusters,3,_nrIterations, type);
        List<Vector> vectors3m = DataGenerator.generateRandomVectors(3000000);
        RunClusteringNaive(vectors3m,_nrClusters,3,_nrIterations, type);
    }

    public static void RunClustering(List<Vector> vectors, int nrClusters, int nrThreads, int maxIterationCount){
        RunClustering(vectors,nrClusters,nrThreads,maxIterationCount,"");
    }

    public static void RunClustering(List<Vector> vectors, int nrClusters, int nrThreads, int maxIterationCount, String outputFolderName) {

        List<Vector> means = DataGenerator.generateRandomVectors(nrClusters);
        RunClustering(vectors,means,nrThreads,maxIterationCount,outputFolderName);
    }

    public static void RunClustering(List<Vector> vectors, List<Vector> means, int nrThreads, int maxIterationCount, String outputFolderName) {
        int nrClusters = means.size();
        ExecutorService executor = Executors.newFixedThreadPool(nrThreads);
        Partitioning<Vector> partitioning = new Partitioner<Vector>().partition(vectors, nrThreads);

        Semaphore sem = new Semaphore(0);
        ReentrantLock lock = new ReentrantLock();
        Queue<Clustering> queue = new LinkedList<>();
        int itrCount = 0;
        System.out.println("Starting clustering...");
        Stopwatch sw = Stopwatch.createStarted();
        while (itrCount < maxIterationCount) {
            //System.out.println("Starting iteration: " + (itrCount + 1));
            for (Partition<Vector> p : partitioning.getPartitions()) {
                executor.execute(new Mapper(sem, lock, p.getData(), means, queue));
            }
            Reducer reducer = new Reducer(sem, lock, queue, nrThreads, nrClusters);
            reducer.run();
            queue.clear();
            means.clear();
            Clustering clustering = reducer.getClustering();
            for (Cluster c : clustering.getClusters()) {
                means.add(c.getMean());
            }
            //System.out.println("Finishing iteration: " + (itrCount + 1));
            itrCount++;
        }
        sw.stop();
        ResultWriter.PrintResult(sw,vectors.size(),maxIterationCount,nrClusters,nrThreads, "Java", outputFolderName);
        executor.shutdown();
    }

    public static void RunClusteringSingleThread(List<Vector> vectors, int nrClusters, int maxIterationCount){
        List<Vector> means = DataGenerator.generateRandomVectors(nrClusters);
        int itrCount = 0;
        Stopwatch sw = Stopwatch.createStarted();
        while (itrCount < maxIterationCount) {
            System.out.println("Starting iteration: " + (itrCount + 1));
            Clustering clustering = ClusteringService.ClusterKMeansMSIncremental(vectors,means);
            means.clear();
            for (Cluster c : clustering.getClusters()) {
                means.add(c.calcMean(c.getMeanSums()));
            }
            System.out.println("Finishing iteration: " + (itrCount + 1));
            itrCount++;
        }
        sw.stop();
        ResultWriter.PrintResult(sw, vectors.size(),maxIterationCount,nrClusters,0, "Java", "");

    }

    public static void RunClusteringNaive(List<Vector> vectors, int nrClusters, int maxIterationCount) {
        RunClusteringNaive(vectors,nrClusters,nrClusters,maxIterationCount);
    }

    public static void RunClusteringNaive(List<Vector> vectors, int nrClusters, int nrThreads, int maxIterationCount){
        RunClusteringNaive(vectors,nrClusters,nrThreads,maxIterationCount,"");
    }

    public static void RunClusteringNaive(List<Vector> vectors, int nrClusters, int nrThreads, int maxIterationCount, String outputFolderName) {

        ExecutorService executor = Executors.newFixedThreadPool(nrThreads);
        Partitioning<Vector> partitioning = new Partitioner<Vector>().partition(vectors, nrThreads);
        List<Vector> means = DataGenerator.generateRandomVectors(nrClusters);
        Clustering clustering = new Clustering(means);
        Semaphore sem = new Semaphore(0);
        ReentrantLock lock = new ReentrantLock();
        int itrCount = 0;
        Stopwatch sw = Stopwatch.createStarted();
        while (itrCount < maxIterationCount) {
            System.out.println("Starting iteration: " + (itrCount + 1));
            for (Partition<Vector> p : partitioning.getPartitions()) {
                executor.execute(new MapperNaive(sem,lock,p.getData(),clustering));
            }
            ReducerNaive reducer = new ReducerNaive(sem, nrThreads, clustering);
            reducer.run();
            means.clear();
            System.out.println("Finishing iteration: " + (itrCount + 1));
            itrCount++;
        }
        sw.stop();
        ResultWriter.PrintResult(sw,vectors.size(), maxIterationCount, nrClusters, nrThreads, "JavaNaive", outputFolderName);
        executor.shutdown();
    }
}
