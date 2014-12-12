package kmeans;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Kasper on 03-11-2014.
 */
public class Vars {

    private static String path = null;

    public static String getPath(){
        if(path == null){
            path = "Data";
            /*
            File f = new File(".");
            path = f.getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath();
            path = Paths.get(path, "Data").toString();
            */
        }

        return path;
    }
}
