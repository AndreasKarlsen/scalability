package kmeans.partitioning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Partitioning<T>  {

    private List<Partition<T> > partitions;

    public Partitioning(int nrPartitions) {
        partitions = new ArrayList<Partition<T> >();
        for (int i = 0; i < nrPartitions; i++)
        {
            partitions.add(new Partition<T>());
        }
    }

    public List<Partition<T>> getPartitions() {
        return partitions;
    }
}
