/**
 * Created by Kasper on 12/23/2014.
 */
public class Philosopher implements Runnable {

    private final TimeoutTimer timeoutTimer = new TimeoutTimer(10);

    private Fork leftFork;
    private Fork rightFork;
    private String name;
    private Plate plate;

    public Philosopher(Fork leftFork, Fork rightFork, String name, Plate plate) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.name = name;
        this.plate = plate;
    }

    private void eat() {
        System.out.println(name.concat("  Eating"));
        plate.takeFood();
    }


    @Override
    public void run() {
        try {
            while (plate.hasFood()) {
                int timeout = timeoutTimer.getTimeout();
                System.out.println("Timeout: " + timeout);
                if (leftFork.pickUp(timeout)) {
                    if (rightFork.pickUp(timeout)) {
                        Thread.sleep(100);
                        eat();
                        rightFork.putDown();
                        leftFork.putDown();
                        timeoutTimer.reset();
                    } else {
                        leftFork.putDown();
                    }
                }
            }
        } catch (InterruptedException ex) {

        }
    }
}
