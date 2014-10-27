/**
 * Created by Kasper on 10/27/2014.
 */



public class Main {

    private static final ValueStore observable = new ValueStore();

    public static class Observer implements IObserver{

        @Override
        public void notify(int value) throws InterruptedException {
            observable.unregister(this);
        }
    }
    public static void main(String [ ] args) throws InterruptedException {
        System.out.println("Started");
        observable.register(new Observer());
        System.out.println("Setting value");
        observable.setValue(5);
        System.out.println("Done");
    }
}
