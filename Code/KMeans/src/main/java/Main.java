import kmeans.datageneration.DataGenerator;
import kmeans.model.Vector;
import kmeans.parsing.DataParser;
import kmeans.partitioning.Partitioner;
import kmeans.partitioning.Partitioning;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Main {

    public static void main(String[] args) {
        try {
            ArrayList<Integer> a1 = new ArrayList<Integer>();
            a1.add(87);
            a1.add(67);
            a1.add(17);
            a1.add(35);
            a1.add(42);
            ArrayList<Integer> a2 = new ArrayList<Integer>();
            a2.add(15);
            a2.add(44 );
            a2.add(57);
            a2.add(23);
            a2.add(76);
            Vector v1 = new Vector(a1);
            Vector v2 = new Vector(a2);
            double cosineSimilarity = v1.vectorSimilarityWith(v2);
            double manDistance = v1.manhattanDistanceTo(v2);
            double euDistance = v1.euclideanDistanceTo(v2);
            double covariance = v1.covarianceWith(v2);
            double sdv = v1.standardDeviation();
            double pearsons = v1.pearsonCorrelationWith(v2);


            List<Vector> vectors = DataGenerator.generateData();
            Partitioning<Vector> partitioning = new Partitioner<Vector>().partition(vectors, 5);
            String breakString = "";

        }
        catch (Exception ex) {
            System.out.println("Error:");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }
}
