import com.google.common.base.Stopwatch;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
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
import kmeans.partitioning.Partition;
import kmeans.partitioning.Partitioner;
import kmeans.partitioning.Partitioning;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Main {

    public static void main(String[] args) {
        try {

            List<Vector> vectors = DataGenerator.generateData();
            RunClustering(vectors,5,7,1);
            RunClusteringNaive(vectors,5,7,1);

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


    public static void RunClustering(List<Vector> vectors, int nrClusters,  int maxIterationCount) {
        RunClustering(vectors,nrClusters,nrClusters,maxIterationCount);
    }

    public static void RunClustering(List<Vector> vectors, int nrClusters, int nrThreads, int maxIterationCount) {


        ExecutorService executor = Executors.newFixedThreadPool(nrThreads);
        Partitioning<Vector> partitioning = new Partitioner<Vector>().partition(vectors, nrThreads);
        List<Vector> means = DataGenerator.generateRandomVectors(100, nrClusters, 100);
        Semaphore sem = new Semaphore(0);
        ReentrantLock lock = new ReentrantLock();
        Queue<Clustering> queue = new LinkedList<Clustering>();
        int itrCount = 0;
        Stopwatch sw = Stopwatch.createStarted();
        while (itrCount < maxIterationCount) {
            System.out.println("Starting iteration: " + (itrCount + 1));
            for (Partition<Vector> p : partitioning.getPartitions()) {
                executor.execute(new Mapper(sem, lock, p.getData(), means, queue));
            }
            Reducer reducer = new Reducer(sem, lock, queue, nrClusters);
            reducer.run();
            queue.clear();
            means.clear();
            Clustering clustering = reducer.getClustering();
            for (Cluster c : clustering.getClusters()) {
                means.add(c.getMean());
            }
            System.out.println("Finishing iteration: " + (itrCount + 1));
            itrCount++;
        }
        sw.stop();
        PrintResult(sw,maxIterationCount,nrClusters, "Java");
        executor.shutdown();
    }

    public static void RunClusteringSingleThread(List<Vector> vectors, int nrClusters, int maxIterationCount){
        List<Vector> means = DataGenerator.generateRandomVectors(100, nrClusters, 100);
        int itrCount = 0;
        Stopwatch sw = Stopwatch.createStarted();
        while (itrCount < maxIterationCount) {
            System.out.println("Starting iteration: " + (itrCount + 1));
            Clustering clustering = ClusteringService.ClusterKMeans(vectors,means);
            means.clear();
            for (Cluster c : clustering.getClusters()) {
                means.add(c.getMean());
            }
            System.out.println("Finishing iteration: " + (itrCount + 1));
            itrCount++;
        }
        sw.stop();
        PrintResult(sw,maxIterationCount,nrClusters, "Java");

    }

    public static void RunClusteringNaive(List<Vector> vectors, int nrClusters, int maxIterationCount) {
        RunClusteringNaive(vectors,nrClusters,nrClusters,maxIterationCount);
    }

    public static void RunClusteringNaive(List<Vector> vectors, int nrClusters, int nrThreads, int maxIterationCount) {

        ExecutorService executor = Executors.newFixedThreadPool(nrThreads);
        Partitioning<Vector> partitioning = new Partitioner<Vector>().partition(vectors, nrThreads);
        List<Vector> means = DataGenerator.generateRandomVectors(100, nrClusters, 100);
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
            ReducerNaive reducer = new ReducerNaive(sem, lock, nrThreads, clustering);
            reducer.run();
            means.clear();
            System.out.println("Finishing iteration: " + (itrCount + 1));
            itrCount++;
        }
        sw.stop();
        PrintResult(sw,maxIterationCount,nrClusters, "JavaNaive");
        executor.shutdown();
    }


    private static void PrintResult(Stopwatch sw, int maxIterationCount, int nrClusters, String implementation){
        long elapsedSeconds = sw.elapsed(TimeUnit.MILLISECONDS);

        try {
            ResultWriter.WriteResult(elapsedSeconds,TimeUnit.MILLISECONDS,maxIterationCount,nrClusters,implementation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(elapsedSeconds);
    }
}
