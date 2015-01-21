public class Plate {
    private int currentFood;

    public Plate(int startFood){
        currentFood = startFood;
    }

    public synchronized void takeFood(){
        currentFood--;
    }

    public synchronized boolean hasFood(){
        return currentFood > 0;
    }
}
