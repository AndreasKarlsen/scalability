package kmeans.datageneration;

import kmeans.model.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kasper on 22-09-2014.
 */
public class DataGenerator {

    public static List<Vector> generateData(){
        return generateRandomVectors(100,500000,100);
    }

    public static List<Vector> generateRandomVectors(int max, int count, int vectorSize){
        List<Vector> vectors = new ArrayList<Vector>();
        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < count; i++) {
            ArrayList<Integer> items = new ArrayList<Integer>();
            for (int j = 0; j < vectorSize ; j++) {
                items.add(rand.nextInt(max));
            }
            vectors.add(new Vector(items));
        }

        return vectors;
    }
}
