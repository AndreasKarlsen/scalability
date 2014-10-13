package kmeans;

import kmeans.clustering.Cluster;
import kmeans.clustering.Clustering;
import kmeans.model.Vector;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kasper on 29-09-2014.
 */
public class ReducerNaive implements Runnable{

    private Semaphore semaphore;
    private int nrMappers;
    private Clustering clustering;



    public ReducerNaive(Semaphore semaphore, int nrMappers, Clustering clustering) {
        this.semaphore = semaphore;
        this.nrMappers = nrMappers;
        this.clustering = clustering;
    }

    public Clustering getClustering() {
        return clustering;
    }

    @Override
    public void run() {
        System.out.println("Reducer started");

        int recieved = 0;
        try {
            while (recieved != nrMappers) {
                semaphore.acquire(1);
                recieved++;
                System.out.println("Reducer recieved nr: "+recieved);
            }

            for (Cluster c :clustering.getClusters()){
                Vector mean = c.calcMean(c.getMeanSums());
                String breakString = "";
            }

        }catch (InterruptedException ex){

        }

        System.out.println("Reducer finished");
    }
}
