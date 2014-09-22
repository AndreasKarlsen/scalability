package kmeans.model;
import java.util.List;

/**
 * Created by Kasper on 22-09-2014.
 */
public class Vector {

    private List<Integer> items;

    public Vector(List<Integer> items) {
        this.items = items;
    }

    public int Size() {
        return items.size();
    }

    public int ItemAt(int index) {
        return items.get(index);
    }
}
