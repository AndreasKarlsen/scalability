/**
 * Created by Kasper on 10/27/2014.
 */
public interface IObserver {
     void notify(int value) throws InterruptedException;
}
