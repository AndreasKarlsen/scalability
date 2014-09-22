package kmeans.partitioning;

import kmeans.model.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Partition<T> {

    private List<T> vectors;

    public Partition() {
        vectors = new ArrayList<T>();
    }

    public List<T> getData() {
        return vectors;
    }
}
