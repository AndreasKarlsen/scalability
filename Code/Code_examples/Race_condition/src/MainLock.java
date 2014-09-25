import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kasper on 25-09-2014.
 */
public class MainLock {
    private static int number = 10;
    private static int result = 0;
    private final static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                if (number == 10){
                    Thread.yield();
                    result = number * 3;
                }
                lock.unlock();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                number = 20;
                lock.unlock();
            }
        });
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Result is: "+result);
    }
}
