package kmeans;

import kmeans.clustering.Clustering;
import kmeans.clustering.ClusteringService;
import kmeans.model.Vector;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kasper on 29-09-2014.
 */
public class MapperNaive implements  Runnable{

    private Semaphore semaphore;
    private Lock lock;
    private List<Vector> data;
    private Clustering clustering;

    public MapperNaive(Semaphore semaphore, ReentrantLock lock, List<Vector> data, Clustering clustering) {
        this.semaphore = semaphore;
        this.lock = lock;
        this.data = data;
        this.clustering = clustering;
    }

    @Override
    public void run() {

        System.out.println("Thread: "+Thread.currentThread().getId()+" started.");
        ClusteringService.ClusterKMeansNaive(clustering,data,lock);
        System.out.println("Thread: "+Thread.currentThread().getId()+" done clustering.");
        //Hand off data
        semaphore.release(1);
        System.out.println("Thread: "+Thread.currentThread().getId()+" done delivering.");
    }
}
