package Neural;

/**
 * Created by j on 4/21/17.
 */

public interface Activation {
    public float function(float x);
    public float prime(float x);
}