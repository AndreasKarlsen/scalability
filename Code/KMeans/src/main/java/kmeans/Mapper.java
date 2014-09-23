package kmeans;

import kmeans.clustering.Clustering;
import kmeans.clustering.ClusteringService;
import kmeans.model.Vector;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kasper on 23-09-2014.
 */
public class Mapper implements  Runnable{

    private Semaphore semaphore;
    private ReentrantLock lock;
    private List<Vector> data;
    private List<Vector> means;
    private Queue<Clustering> queue;

    public Mapper(Semaphore semaphore, ReentrantLock lock, List<Vector> data, List<Vector> means, Queue<Clustering> queue) {
        this.semaphore = semaphore;
        this.lock = lock;
        this.data = data;
        this.means = means;
        this.queue = queue;
    }

    @Override
    public void run() {

        System.out.println("Thread: "+Thread.currentThread().getId()+" started.");
        Clustering clustering = ClusteringService.ClusterKMeans(data, means);
        System.out.println("Thread: "+Thread.currentThread().getId()+" done clustering.");
        //Hand off data
        lock.lock();
        queue.add(clustering);
        lock.unlock();
        semaphore.release(1);
        System.out.println("Thread: "+Thread.currentThread().getId()+" done delivering.");
    }
}
