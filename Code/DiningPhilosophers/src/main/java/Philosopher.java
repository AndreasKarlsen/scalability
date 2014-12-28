/**
 * Created by Kasper on 12/23/2014.
 */
public class Philosopher implements Runnable{

    private final TimeoutTimer timeoutTimer = new TimeoutTimer(10);

    private Fork leftFork;
    private Fork rightFork;

    public Philosopher(Fork leftFork, Fork rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }


    @Override
    public void run() {

        try{
            while(true){
                int timeout = timeoutTimer.getTimeout();
                System.out.println("Timeout: "+timeout);
                if (leftFork.pickUp(timeout)){
                    if(rightFork.pickUp(timeout)){
                        Thread.sleep(100);
                        System.out.println(this.toString()+"  Eating");
                        rightFork.putDown();
                        leftFork.putDown();
                        timeoutTimer.reset();
                    }else{
                        leftFork.putDown();
                    }
                }
            }

        }catch (InterruptedException ex){

        }
    }
}
