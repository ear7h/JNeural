import LocalMin.LocalMinProbability;
import LocalMin.None;
import Neural.CustomEliminatorDemo;
import Neural.GetActivation;
import Neural.NeuralNetwork;
import Neural.TrainingSet;

/**
 * Created by rio on 3/13/17.
 */


//this class tests the effectiveness of training
public class JNeuralTest {
    public static void main (String[] args){
        int trains = 100;
        int correct = 0;

        float sum = 0;
        for (int i = 0; i < trains; i++) {
            float tac = trainAndCheck(0.1f);
            sum += tac;
            if ( tac < 0.1f) {
                correct ++;
            }
        }

        System.out.println("average error: " + (sum/trains));
        System.out.println(correct + " correct trains, out of " + trains);
    }

    public static float trainAndCheck (float threshold){
        NeuralNetwork n = new NeuralNetwork(new int[]{2, 4, 2, 1});
        n.setLearnRate(0.01f);

        /*
        the Neural.Activation interface allows for the creation of custom activation functions.
        the interface contains function and prime methods for calculations and back propagation
        the Neural.GetActivation class provides a few prewriten activation functions
        */

        n.setActivationFunc(GetActivation.sigmoid());
        n.setActivationFunc(GetActivation.ReLU());
        n.setActivationFunc(GetActivation.LReLU(0.2f));

        /*
        the Eliminator interface's main method gets called near the end of
        the train method, after the new weights have been calculated
        but before they replace the previous weights
         */


        //LocalMinProbability calculates the probability of a local min and
        // adds (factor * probability) to each weight
        n.setEliminator(new LocalMinProbability(0.5f, 0.15f));

        //a custom Eliminator can be added as well, by implementing the Eliminator class
        n.setEliminator(new CustomEliminatorDemo(0.5f, 0.15f));
        //a None Eliminator is the default
        n.setEliminator(new None());

        //the training set class allows users to create a training set using native java arrays
        TrainingSet t = new TrainingSet(n);


        //arrays will be added to a FloatMatrix for training
        t.addToSet(new float[] {0, 0}, new float[] {0});
        t.addToSet(new float[] {0, 1}, new float[] {1});
        t.addToSet(new float[] {1, 0}, new float[] {1});
        t.addToSet(new float[] {1, 1}, new float[] {0});


        n.train(t, 50000);
        float sum = 0;
        for (int i = 0; i < n.result.length; i++) {
            float diff = n.result.arr[i] - t.getAnswers().arr[i];
            sum += diff;
            if (Float.isNaN(n.result.arr[0])){
                return 1;
            }
        }

        return (sum/n.result.length);
    }

}