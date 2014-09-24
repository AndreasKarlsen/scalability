import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kasper on 24-09-2014.
 */
public class ResultWriter {

    private static String path = null;

    public static String getPath(){
        if(path == null){
            File f = new File(".");
            path = f.getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath();
            path = path + "\\Data";
        }

        return path;
    }


    public static void WriteResult(long time, TimeUnit unit, int nrIterations, int nrClusters, String implementation) throws IOException {
        PrintWriter writer = null;
        String path = getPath();
        Path p = Paths.get(path);
        if (Files.notExists(p)){
            Files.createDirectory(p);
        }

        p = Paths.get(path,implementation);
        if (Files.notExists(p)){
            Files.createDirectory(p);
        }

        String fileName = "I"+nrIterations+"C"+nrClusters+".txt";
        path = Paths.get(path,implementation,fileName).toString();
        try {
            writer = new PrintWriter(path);
            writer.println("Implementation: "+implementation);
            writer.println("Clusters: "+nrClusters);
            writer.println("Iterations: "+nrIterations);
            writer.println("Time: "+time+" "+unit.name());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }


    }
}
