package kmeans.parsing;

import kmeans.model.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;
import java.util.List;


/**
 * Created by Kasper on 22-09-2014.
 */
public class DataParser {

    private static String path = null;

    public static String getPath(){
        if(path == null){
            File f = new File(".");
            path = f.getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath();
            path = path + "\\Data\\Data.testdata";
        }

        return path;
    }


    public static List<Vector> ParseData() throws IOException {

        Path p = Paths.get(getPath());
        File f = new File(getPath());
        if(!f.exists())
        {
            throw new IOException("The data is not present");
        }

        List<String> lines = Files.readAllLines(p, Charset.defaultCharset());
        List<Vector> vectors = new ArrayList<Vector>();

        for (String line: lines){
            List<Integer> items = new ArrayList<Integer>();
            String[] splits = line.split(",");
            for (String item : splits)
            {
                items.add(Integer.parseInt(item));
            }
            vectors.add(new Vector(items));
        }

        return vectors;
    }
}
