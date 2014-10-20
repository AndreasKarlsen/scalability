package kmeans.clustering;


import kmeans.model.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 23-09-2014.
 */
public class Cluster {

    private List<Vector>  vectors;
    private Vector mean;
    private int[] meanSums;

    public Cluster(){
        vectors = new ArrayList<Vector>();
    }

    public Cluster(Vector mean){
        vectors = new ArrayList<Vector>();
        this.mean = mean;
    }

    public Cluster(List<Vector> vectors){
        this.vectors = vectors;
        calcMean();
    }

    public void addVector(Vector v){
        this.vectors.add(v);
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public Vector getMean() {
        return mean;
    }

    public int[] getMeanSums() {
        return meanSums;
    }

    public void mergeWith(Cluster other){

        if (meanSums == null && other.meanSums != null){
            meanSums = other.meanSums;
        }else if (meanSums != null && other.meanSums != null){
            for (int i = 0; i < meanSums.length; i++) {
                meanSums[i] += other.meanSums[i];
            }
        }

        this.vectors.addAll(other.getVectors());
    }

    public Vector calcMean() {
        return calcMean(calcMeanSums());
    }

    public Vector calcMean(int[] sums) {
        int vectorSize = this.vectors.get(0).size();
        int[] items = new int[sums.length];
        for (int i = 0; i < sums.length; i++) {
            items[i] = sums[i] / this.vectors.size();
        }

        mean = new Vector(items);
        return mean;
    }

    public int[] calcMeanSums(){
        int vectorSize = this.vectors.get(0).size();
        int[] items = new int[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            int sum = 0;
            for (Vector v : this.vectors) {
                sum += v.itemAt(i);
            }
            items[i] = sum;
        }
        meanSums = items;
        return items;
    }

    public void updateMeanSums(Vector v){
        if (meanSums == null ){
            int vectorSize = this.vectors.get(0).size();
            meanSums = new int[vectorSize];
        }

        for (int i = 0; i < meanSums.length; i++) {
            meanSums[i] += v.itemAt(i);
        }
    }
}
