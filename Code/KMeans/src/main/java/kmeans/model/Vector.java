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

    public int size() {
        return items.size();
    }

    public int itemAt(int index) {
        return items.get(index);
    }

    public double average(){
        return ((double)sum()/(double)size());
    }

    public int sum(){
        return (int) sum(new ITransformation() {
            @Override
            public double Transform(int i) {
                return i;
            }
        });
    }

    public int squaredSum(){
        return (int) sum(new ITransformation() {
            @Override
            public double Transform(int i) {
                return i*i;
            }
        });
    }

    public double sum(ITransformation transformation){
        double sum = 0;
        for (int i :items){
            sum += transformation.Transform(i);
        }
        return sum;
    }

    public double dotProductWith(Vector other){
        if (size() != other.size()){
            throw new IllegalArgumentException("Sizes does not match");
        }

        double dotProduct = 0;
        for (int i = 0; i < size(); i++) {
            dotProduct += itemAt(i) * other.itemAt(i);
        }

        return dotProduct;
    }

    public double length(){
        return Math.sqrt(squaredSum());
    }

    public double covarianceWith(Vector other){
        if (size() != other.size()){
            throw new IllegalArgumentException("Sizes does not match");
        }

        double avg = average();
        double otherAvg = other.average();
        double temp = 0;
        for (int i = 0; i < size() ; i++) {
            temp +=  (itemAt(i)-avg)*(other.itemAt(i)-otherAvg);
        }

        return temp /size();
    }

    public double standardDeviation(){
        return standardDeviation(average());
    }

    private double standardDeviation(double avg){
        double sum = sum(new ITransformation() {
            @Override
            public double Transform(int i) {
                double temp = (i-avg);
                return temp * temp;
            }
        });

        return Math.sqrt(sum/size());
    }

    public double manhattanDistanceTo(Vector other){
        if (size() != other.size()){
            throw new IllegalArgumentException("Sizes does not match");
        }

        double distance = 0;

        for (int i = 0; i < size(); i++) {
            distance += Math.abs(itemAt(i)-other.itemAt(i));
        }

        return distance;
    }

    public double euclideanDistanceTo(Vector other){
        if (size() != other.size()){
            throw new IllegalArgumentException("Sizes does not match");
        }

        double distance = 0;

        for (int i = 0; i < size(); i++) {
            double temp = itemAt(i)-other.itemAt(i);
            distance += temp * temp;
        }

        return Math.sqrt(distance);
    }

    public double vectorSimilarityWith(Vector other)
    {
        return dotProductWith(other) /( length()* other.length());
    }

    public double pearsonCorrelationWith(Vector other){
        return covarianceWith(other) / (standardDeviation() * other.standardDeviation());
    }

    public interface ITransformation{
        double Transform(int i);
    }
}
