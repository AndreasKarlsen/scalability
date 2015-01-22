import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kasper on 12/23/2014.
 */
public class Fork {

    private final Lock lock = new ReentrantLock();

    public boolean pickUp() throws InterruptedException {
        lock.lock();
        return true;
    }

    public boolean pickUp(int waitTime) throws InterruptedException {
        return lock.tryLock(waitTime, TimeUnit.MILLISECONDS);
    }

    public void putDown(){
        lock.unlock();
    }
}
