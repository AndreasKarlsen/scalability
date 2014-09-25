package kmeans.parsing;

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

    private static String path = null;

    public static String getPath(){
        if(path == null){
            File f = new File(".");
            path = f.getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath();
            path = path + "\\Data\\Data.testdata";
        }

        return path;
    }


    public static List<Vector> parseData() throws IOException {

        File f = new File(getPath());
        if(!f.exists())
        {
            throw new IOException("The data is not present");
        }

        List<Vector> vectors = new ArrayList<Vector>();

        InputStream fis;
        BufferedReader br;
        String         line;

        fis = new FileInputStream(f);
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        while ((line = br.readLine()) != null) {

            int[] items = new int[100];
            String[] splits = line.split(",");
            for (int i = 0; i < 100; i++) {
                items[i] = Integer.parseInt(splits[i]);
            }
            vectors.add(new Vector(items));
        }

// Done with the file
        br.close();
        br = null;
        fis = null;

        return vectors;
    }
}
