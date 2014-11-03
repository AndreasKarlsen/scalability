import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kasper on 10/28/2014.
 */
public class Account {

    private int balance;
    private final Lock lock = new ReentrantLock();

    public Account(int balance){
       this.balance = balance;
    }

    public void credit(int amount){
        lock.lock();
        balance += amount;
        lock.unlock();
    }

    public void debit(int amount){
        lock.lock();
        balance -= amount;
        lock.unlock();
    }
}
