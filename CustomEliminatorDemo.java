import JNeural.LocalMin.Eliminator;
import JNeural.NeuralNetwork;
import JNeural.Matrix.FloatMatrix;

/**
 * Created by j on 4/21/17.
 */
public class CustomEliminatorDemo implements Eliminator {

    //variables to escape local min
    private float FACTOR;
    private float ERROR_THRESHOLD;

    public CustomEliminatorDemo(float threshold, float factor){
        this.ERROR_THRESHOLD = threshold;
        this.FACTOR = factor;
    }

    @Override
    public void main(NeuralNetwork n, FloatMatrix a){
        FloatMatrix errors = n.result.sub(a);

        float errorSum = 0;
        for (int i = 0; i < errors.length; i++) {
            errorSum += Math.abs(errors.arr[i]);
        }

        float errorNet = errorSum/errors.length;

        //System.out.println(Arrays.toString(n.adjustment));
        float ti = n.totalIterations;

        for (int i = 0; i < n.NUM_OF_LAYERS - 1; i++) {
            n.adjustment[i] = n.adjustment[i].add(FACTOR *
                    (errorNet / ERROR_THRESHOLD) * (float) Math.tanh( 3 * n.currentIteration / ti)
            );
        }

    }
}
