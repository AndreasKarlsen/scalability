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

    public Clustering(int nrClusters){
        clusters = new ArrayList<Cluster>();
        for (int i = 0; i < nrClusters; i++) {
            clusters.add(new Cluster());
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

    public void CalcMeanSums(){
        for (Cluster c : clusters){
            c.calcMeanSums();
        }
    }

    public List<int[]> GetMeanSums(){
        List<int[]> meanSums = new ArrayList<>();
        for (Cluster c : clusters){
            int[] meanSum = c.getMeanSums();
            if (meanSum == null){
                meanSum = c.calcMeanSums();
            }
            meanSums.add(meanSum);
        }

        return meanSums;
    }

    public void mergeWith(Clustering other){

        if (this.clusters.size() != other.clusters.size()){
            throw new  IllegalArgumentException("Clusterings do not have the same amount of clusters");
        }

        for (int i = 0; i < this.clusters.size(); i++) {
            Cluster c1 = this.getClusters().get(i);
            Cluster c2 = other.getClusters().get(i);
            c1.mergeWith(c2);
        }
    }

    public void calcMeans(){
        for (Cluster c : clusters){
            c.calcMean();
        }
    }

    public void calcMeansUsingMeanSum(){
        for (Cluster c : clusters){
            c.calcMean(c.getMeanSums());
        }
    }
}
