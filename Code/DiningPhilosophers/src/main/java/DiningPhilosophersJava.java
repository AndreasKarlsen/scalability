/**
 * Created by Kasper on 12/23/2014.
 */
public class DiningPhilosophersJava {

    public static void main(String... args){
        Fork f1 = new Fork();
        Fork f2 = new Fork();
        Fork f3 = new Fork();
        Fork f4 = new Fork();
        Fork f5 = new Fork();

        Thread t1 = new Thread(new Philosopher(f1,f2));
        Thread t2 = new Thread(new Philosopher(f2,f3));
        Thread t3 = new Thread(new Philosopher(f3,f4));
        Thread t4 = new Thread(new Philosopher(f4,f5));
        Thread t5 = new Thread(new Philosopher(f5,f1));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
