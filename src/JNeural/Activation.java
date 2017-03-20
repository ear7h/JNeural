package JNeural;
/**
 * Created by julio on 3/16/17.
 */
public interface Activation {
    public float function(float x);
    public float prime(float x);
}