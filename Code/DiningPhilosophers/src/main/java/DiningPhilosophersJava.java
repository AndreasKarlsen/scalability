import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kasper on 12/23/2014.
 */
public class DiningPhilosophersJava {

    public static void main(String... args) throws InterruptedException {
        Fork f1 = new Fork();
        Fork f2 = new Fork();
        Fork f3 = new Fork();
        Fork f4 = new Fork();
        Fork f5 = new Fork();
        Plate plate = new Plate(200);

        Thread t1 = new Thread(new Philosopher(f1,f2, "Plato", plate));
        Thread t2 = new Thread(new Philosopher(f2,f3, "Konfuzius", plate));
        Thread t3 = new Thread(new Philosopher(f3,f4, "Socrates", plate));
        Thread t4 = new Thread(new Philosopher(f4,f5, "Voltaire", plate));
        Thread t5 = new Thread(new Philosopher(f5,f1, "Descartes", plate));

        System.out.println("Started...");
        Stopwatch stopwatch = Stopwatch.createStarted();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}
