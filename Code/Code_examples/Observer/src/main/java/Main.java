/**
 * Created by Kasper on 10/27/2014.
 */



public class Main {

    public static void main(String [ ] args) throws InterruptedException {
        ValueStore observable = new ValueStore();
        observable.register(new ValueObserver());
        observable.setValue(5);
        System.out.println("Done");
    }
}
