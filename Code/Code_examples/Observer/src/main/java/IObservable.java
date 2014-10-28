/**
 * Created by Kasper on 10/27/2014.
 */
public interface IObservable {
    void register(IObserver observer) throws InterruptedException;
    void unregister(IObserver observer) throws InterruptedException;
}
