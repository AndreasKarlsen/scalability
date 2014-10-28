/**
 * Created by Kasper on 10/27/2014.
 */
public interface Observable {
    void register(Observer observer) throws InterruptedException;
    void unregister(Observer observer) throws InterruptedException;
}
