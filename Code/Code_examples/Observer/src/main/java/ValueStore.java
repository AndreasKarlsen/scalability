import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Kasper on 10/27/2014.
 */
public class ValueStore implements IObservable{

    private int value = 0;
    private List<IObserver> observers = new ArrayList<>();
    private Semaphore sem = new Semaphore(1);

    public void setValue(int newValue) throws InterruptedException {
        sem.acquire();
        value = newValue;
        for (IObserver o : observers) {
            o.notify(value);
        }
        sem.release();
    }

    @Override
    public void register(IObserver observer) throws InterruptedException {
        sem.acquire();
        observers.add(observer);
        sem.release();
    }

    @Override
    public void unregister(IObserver observer) throws InterruptedException {
        sem.acquire();
        observers.remove(observer);
        sem.release();
    }
}
