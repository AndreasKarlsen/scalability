package kmeans.clustering;

import kmeans.model.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 23-09-2014.
 */
public class Clustering {
    private List<Cluster> clusters;

    public Clustering(List<Vector> means){
        clusters = new ArrayList<Cluster>();
        for (int i = 0; i < means.size(); i++) {
            clusters.add(new Cluster(means.get(i)));
        }
    }

    public Clustering(){
        this.clusters = new ArrayList<Cluster>();
    }

    public void AddCluster(Cluster cluster){
        this.clusters.add(cluster);
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}
