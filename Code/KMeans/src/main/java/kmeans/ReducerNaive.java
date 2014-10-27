package kmeans;

import kmeans.clustering.Clustering;

import java.util.concurrent.Semaphore;

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

            clustering.calcMeansUsingMeanSum();

        }catch (InterruptedException ex){

        }

        System.out.println("Reducer finished");
    }
}
