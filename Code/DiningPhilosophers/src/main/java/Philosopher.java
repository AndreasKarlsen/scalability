/**
 * Created by Kasper on 12/23/2014.
 */
public class Philosopher implements Runnable {


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

    private void eat() throws InterruptedException {
        System.out.println(name.concat("  Eating"));
        plate.takeFood();

    }

    private void Think() throws InterruptedException {
        Thread.sleep(100);
    }

    @Override
    public void run() {
        try {
            while (plate.hasFood()) {
                if (leftFork.pickUp(10)) {
                    if (rightFork.pickUp(10)) {
                        eat();
                        rightFork.putDown();
                        leftFork.putDown();
                    } else {
                        leftFork.putDown();
                    }
                }
                Think();
            }
        } catch (InterruptedException ex) {

        }
    }
}
