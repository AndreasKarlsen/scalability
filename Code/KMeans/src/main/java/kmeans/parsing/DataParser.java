package kmeans.parsing;

import kmeans.Vars;
import kmeans.model.Vector;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;
import java.util.List;


/**
 * Created by Kasper on 22-09-2014.
 */
public class DataParser {


    public static List<Vector> parseStaticData()  throws IOException{
        return parseData("staticdata.txt");
    }

    public static List<Vector> parseStaticDataMeans()  throws IOException{
        return parseData("staticdatameans.txt");
    }

    public static List<Vector> parseData(String filename) throws IOException {
        String path = Vars.getPath();
        Path p = Paths.get(path);
        if (Files.notExists(p)){
            Files.createDirectory(p);
        }

        p = Paths.get(path,filename);
        if (Files.notExists(p)){
            throw new IOException("The data is not present");
        }


        List<Vector> vectors = new ArrayList<Vector>();


        List<String> lines = Files.readAllLines(p);
        for (String line : lines){
            int[] items = new int[100];
            String[] splits = line.split(",");
            for (int i = 0; i < 100; i++) {
                items[i] = Integer.parseInt(splits[i]);
            }
            vectors.add(new Vector(items));
        }

        return vectors;
    }
}
