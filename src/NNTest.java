
import JNeural.NeuronNetwork;
import java.util.Arrays;

/**
 * Created by julio on 3/15/17.
 */
public class NNTest {
    public static void main (String[] args){
        //first and last ints in profile are reserved for input and output respectively
        //any ints in between are hidden layers with specified number of nodes
        int[] profile = new int[]{2, 4, 2, 1};
        NeuronNetwork n = new NeuronNetwork(profile);


        //xor gate
        float[][] trainingSetProblems = new float[][] {
                {1.0f, 0.0f},
                {0.0f, 0.0f},
                {0.0f, 1.0f},
                {1.0f, 1.0f}
        };
        float[][] trainingSetAnswers = new float[][] {
                {1.0f},
                {0.0f},
                {1.0f},
                {0.0f}
        };


        n.train(trainingSetProblems, trainingSetAnswers, 100000);

        float[] test = new float[]{0.0f, 0.0f};
        n.run(test);
        System.out.println("Testing " + Arrays.toString(test) + " -> " + Arrays.toString(n.result));
    }
}
