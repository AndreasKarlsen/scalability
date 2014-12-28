/**
 * Created by Kasper on 12/23/2014.
 */
public class TimeoutTimer {

    private int defaultValue;
    private int nextTimeOut;

    public TimeoutTimer(int defaultValue) {
        this.defaultValue = defaultValue;
        this.nextTimeOut = defaultValue;
    }

    public int getTimeout(){
        int temp = nextTimeOut;
        nextTimeOut = nextTimeOut * defaultValue;
        return temp;
    }

    public void reset(){
        nextTimeOut = defaultValue;
    }
}
