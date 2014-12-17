package kmeans.clustering;

import java.util.List;
import java.util.concurrent.locks.Lock;

import kmeans.model.Vector;

/**
 * Created by Kasper on 23-09-2014.
 */
public class ClusteringService {

    public static Clustering ClusterKMeans(List<Vector> items, List<Vector> means){

        Clustering clustering = new Clustering(means);

        for (Vector v : items){
            Cluster cluster = ClusteringInternal(clustering,v);
            cluster.addVector(v);
            cluster.updateMeanSums(v);
        }

        return clustering;
    }

    public static Clustering ClusterKMeansMSIncremental(List<Vector> items, List<Vector> means){

        Clustering clustering = new Clustering(means);

        for (Vector v : items){
            Cluster cluster = ClusteringInternal(clustering,v);
            cluster.addVector(v);
            cluster.updateMeanSums(v);
        }

        return clustering;
    }

    private static Cluster ClusteringInternal(Clustering clustering, Vector v){
        Cluster cluster = null;
        double distance = 999999999999999.0;
        for (Cluster c : clustering.getClusters()){
            double manhattanDistance = v.manhattanDistanceTo(c.getMean());
            //double pearsons = v.pearsonCorrelationWith(c.getMean());
            if (manhattanDistance < distance){
                cluster = c;
                distance = manhattanDistance;
            }
        }

        return cluster;
    }

    public static Clustering ClusterKMeansNaive(Clustering clustering, List<Vector> items, Lock lock){

        for (Vector v : items){
            Cluster cluster = ClusteringInternal(clustering,v);

            lock.lock();
            cluster.addVector(v);
            cluster.updateMeanSums(v);
            lock.unlock();
        }

        return clustering;
    }
}
