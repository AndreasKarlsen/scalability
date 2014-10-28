import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Kasper on 10/27/2014.
 */
public class ValueStore implements Observable {

    protected int value = 0;
    protected List<Observer> observers = new ArrayList<>();
    protected Semaphore sem = new Semaphore(1);

    public void setValue(int newValue) throws InterruptedException {
        sem.acquire();
        value = newValue;
        for (Observer o : observers) {
            o.notify(this,value);
        }
        sem.release();
    }

    @Override
    public void register(Observer observer) throws InterruptedException {
        sem.acquire();
        observers.add(observer);
        sem.release();
    }

    @Override
    public void unregister(Observer observer) throws InterruptedException {
        sem.acquire();
        observers.remove(observer);
        sem.release();
    }
}
