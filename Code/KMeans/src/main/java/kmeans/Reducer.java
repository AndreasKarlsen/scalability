package kmeans;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import kmeans.clustering.Clustering;

/**
 * Created by Kasper on 23-09-2014.
 */
public class Reducer implements Runnable{

    private Semaphore semaphore;
    private ReentrantLock lock;
    private Queue<Clustering> queue;
    private int nrThreads;
    private int nrClusters;
    private Clustering _clustering;



    public Reducer(Semaphore semaphore, ReentrantLock lock, Queue<Clustering> queue, int nrThreads, int nrClusters) {
        this.semaphore = semaphore;
        this.lock = lock;
        this.queue = queue;
        this.nrThreads = nrThreads;
        this.nrClusters = nrClusters;
    }

    public Clustering getClustering() {
        return _clustering;
    }

    @Override
    public void run() {
        System.out.println("Reducer started");

        int recieved = 0;
        Clustering clustering = new Clustering(nrClusters);; //intermediate result
        try {
            while (recieved != nrThreads) {
                Clustering temp;
                semaphore.acquire(1);
                lock.lock();
                temp= queue.poll();
                lock.unlock();
                recieved++;
                System.out.println("Reducer recieved nr: "+recieved);

                clustering.mergeWith(temp);
            }

            clustering.calcMeansUsingMeanSum();

            _clustering = clustering;

        }catch (InterruptedException ex){

        }

        System.out.println("Reducer finished");
    }
}
