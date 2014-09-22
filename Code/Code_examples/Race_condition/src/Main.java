public class Main {
    public static int number = 10;
    public static int result = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (number == 10){
                    Thread.yield();
                    result = number * 3;
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
               number = 20;
            }
        });
        t1.start(); t2.start();
        t1.join(); t2.join();
        System.out.println("Result is: "+result);
    }
}




