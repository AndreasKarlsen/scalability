import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kasper on 24-09-2014.
 */
public class ResultWriter {

    private static String path = null;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getPath(){
        if(path == null){
            File f = new File(".");
            path = f.getAbsoluteFile().getParentFile().getAbsoluteFile().getParentFile().getAbsolutePath();
            path = Paths.get(path, "Data").toString();
        }

        return path;
    }


    public static void WriteResult(long time, TimeUnit unit, int nrIterations, int nrClusters, int nrMappers, String implementation) throws IOException {
        String path = getPath();
        Path p = Paths.get(path);
        if (Files.notExists(p)){
            Files.createDirectory(p);
        }

        p = Paths.get(path,implementation);
        if (Files.notExists(p)){
            Files.createDirectory(p);
        }

        String fileName = "M"+nrMappers+"C"+nrClusters+"I"+nrIterations+".txt";
        path = Paths.get(path,implementation,fileName).toString();
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path,true)))) {
            writer.println("Timestamp: "+sdf.format(new Date()));
            writer.println("Implementation: " + implementation);
            writer.println("Mappers: " + nrMappers);
            writer.println("Clusters: " + nrClusters);
            writer.println("Iterations: " + nrIterations);
            writer.println("Time: " + time + " " + unit.name());
            writer.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
