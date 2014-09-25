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

    public void mergeWith(Cluster other){
        this.vectors.addAll(other.getVectors());
    }

    public Vector calcMean(){
        int[] items;
        if (this.vectors.size() == 0){
            //Empty vector
            items = new int[0];
        }else{
            int vectorSize = this.vectors.get(0).size();
            items = new int[vectorSize];
            for (int i = 0; i < vectorSize; i++) {
                int sum = 0;
                for (Vector v : this.vectors){
                    sum += v.itemAt(i);
                }
                items[i] = sum/this.vectors.size();
            }
        }

        mean = new Vector(items);
        return mean;
    }
}
