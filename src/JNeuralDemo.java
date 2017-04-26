import JNeural.CustomEliminatorDemo;
import JNeural.GetActivation;
import JNeural.NeuralNetwork;
import JNeural.TrainingSet;
import JNeural.localminelimination.LocalMinProbability;
import JNeural.localminelimination.None;
import JNeural.matrix.FloatMatrix;

/**
 * Created by rio on 3/13/17.
 */

public class JNeuralDemo {
    public static void main (String[] args){

        //initializing the neural network requires and array where the first and last
        //elements indicate inputs and outputs respectively
        NeuralNetwork n = new NeuralNetwork(new int[]{2, 4, 2, 1});

        //n.setLearnRate() sets the learn rate, the default value is 0.5f
        n.setLearnRate(0.01f);

        /*
        the Activation interface allows for the creation of custom activation functions.
        the interface contains function and prime methods for calculations and back propagation
        the GetActivation class provides a few prewriten activation functions
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

        //n.run() returns a FloatMatrix with the results
        FloatMatrix r = n.run(t.getInputs());

        //the latest result can also be acquired with n.result
        System.out.println(n.result);
    }

}