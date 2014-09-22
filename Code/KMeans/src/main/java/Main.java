import kmeans.model.Vector;
import kmeans.parsing.DataParser;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Main {

    public static void main(String [] args) {
        String s;
        try {
            List<Vector> vectors = DataParser.ParseData();
            s = "";
        }catch (Exception ex){
            s = ex.getMessage();
        }

    }
}
