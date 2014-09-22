package kmeans.partitioning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Partitioner<T> {

    public Partitioning<T> partition(List<T> items, int nrPartitions){
        Partitioning<T> partitioning = new Partitioning<T>(nrPartitions);

        for (int i = 0; i < items.size(); i++)
        {
            int partitionIndex = i % nrPartitions;
            partitioning.getPartitions().get(partitionIndex).getData().add(items.get(i));
        }

        return partitioning;
    }
}
