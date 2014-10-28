import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 10/28/2014.
 */
public class ValueStore2 extends ValueStore{

    @Override
    public void setValue(int newValue) throws InterruptedException {
        List<Observer> shallowCopy;
        sem.acquire();
        value = newValue;
        shallowCopy = new ArrayList<>(observers);
        sem.release();

        for (Observer o : shallowCopy) {
            o.notify(this,value);
        }
    }
}
