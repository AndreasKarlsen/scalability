/**
 * Created by Kasper on 10/28/2014.
 */
public class ValueObserver implements Observer {

    @Override
    public void notify(Observable sender, int value) throws InterruptedException {
        sender.unregister(this);
    }
}
