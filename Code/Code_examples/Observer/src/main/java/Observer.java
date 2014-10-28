/**
 * Created by Kasper on 10/27/2014.
 */
public interface Observer {
     void notify(Observable sender, int value) throws InterruptedException;
}
