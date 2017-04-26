package Neural;

import LocalMin.*;
import Matrix.*;

public class NeuralNetwork {
    //all fields public for custom Eliminator classes

    //each FloatMatrix in the array represents the weights for that layer
    public FloatMatrix[] layers;
    public FloatMatrix[] sums;
    public FloatMatrix[] adjustment;

    //changes every time run() is called
    public FloatMatrix result;




    //training constant
    public int totalIterations;
    public int currentIteration;
    public float learnRate;
    public Eliminator eliminator = new None();
    public Activation activationFunc = GetActivation.sigmoid();

    //network constants
    public final int[] LAYER_PROFILE;
    public final int NUM_OF_INPUTS;
    public final int NUM_OF_OUTPUTS;
    public final int NUM_OF_LAYERS;
    public final int INDEX_OF_OUTPUT;

    public NeuralNetwork (int[] layerProfile){

        this.layers = new FloatMatrix[layerProfile.length];

        this.LAYER_PROFILE = layerProfile;

        this.learnRate = 0.5f;
        this.NUM_OF_INPUTS = layerProfile[0];
        this.NUM_OF_OUTPUTS = layerProfile[layerProfile.length - 1];
        this.NUM_OF_LAYERS = this.layers.length;
        this.INDEX_OF_OUTPUT = this.layers.length - 1;

        //layers[0] serves as the input layer
        //only accepts single row inputs
        this.layers[0] = new FloatMatrix(1, layerProfile[0]);

        /*
        each layer is simply the weights for each neuron of that layer where:
           #rows = neurons in previous layer
           #columns = specified neurons in this layer

        the dot product of adjacent layers yields a matrix which can be multiplied against next layer
        */
        for(int i = 1; i < NUM_OF_LAYERS; i++){
            this.layers[i] = new FloatMatrix(layerProfile[i - 1], layerProfile[i]);
            this.layers[i].populate(-1.0f, 1.0f);
        }
    }

    //mutator methods
    public void setEliminator (Eliminator e){
        this.eliminator = e;
    }
    public void setLearnRate (float l){
        this.learnRate = l;
    }
    public void setActivationFunc (Activation a) {this.activationFunc = a;}

    //accessor methods
    public int getNUM_OF_INPUTS(){
        return NUM_OF_INPUTS;
    }
    public int getNUM_OF_OUTPUTS(){
        return NUM_OF_OUTPUTS;
    }


    public void train (TrainingSet t, int iterations) {
        this.totalIterations = iterations;
        //multithreaded train
        //start a thread for each item in the training set with
        //a max thread count depending on the total number of cores

        //singlethreaded train
        for (int i = 0; i < iterations; i++) {
            this.currentIteration = i;
            run(t.getInputs());
            backprop(t.getAnswers());
        }


    }

    //run method is overloaded

    //converts float array into FloatMatrix and calls native run method
    public FloatMatrix run (float[] in) {
        if(in.length != NUM_OF_INPUTS){
            throw new IllegalArgumentException("ERROR: This neuron network accepts " + NUM_OF_INPUTS + " inputs");
        }
        FloatMatrix inM = new FloatMatrix(1, in.length);

        for (int i = 0; i < in.length; i++) {
            inM.set(in[i], 0, i);
        }

        return run(inM);
    }

    //native run method, takes in float matrix
    public FloatMatrix run (FloatMatrix input){

        //ensure size compatibility
        if(input.columns != NUM_OF_INPUTS){
            throw new IllegalArgumentException("ERROR: This neuron network accepts " + NUM_OF_INPUTS + " inputs");
        }

        //clear sums
        this.sums = new FloatMatrix[NUM_OF_LAYERS];
        //feed inputMatrix to input layer(index 0)
        this.layers[0] = input;
        this.sums[0] = input;
        //placeholder for activated sum
        FloatMatrix activated = input;

        for (int i = 1; i < NUM_OF_LAYERS; i++) {
            this.sums[i] = MatrixMath.dot(activated, layers[i]);
            activated = actFunc(sums[i]);
        }

        this.result = activated;
        return activated;
    }

    private FloatMatrix actFunc (FloatMatrix m) {
        FloatMatrix r = new FloatMatrix(m.rows, m.columns);
        for (int i = 0; i < m.length; i++) {
            r.arr[i] = this.activationFunc.function(m.arr[i]);
        }
        return r;
    }
    private FloatMatrix actPrime (FloatMatrix m) {
        FloatMatrix r = new FloatMatrix(m.rows, m.columns);
        for (int i = 0; i < m.length; i++) {
            r.arr[i] = this.activationFunc.prime(m.arr[i]);
        }
        return  r;
    }

    public void backprop (FloatMatrix answer) {
        if (answer.rows != this.result.rows || answer.columns != this.result.columns){
            throw new IllegalArgumentException("ERROR: The passed error is not the same size as this.result " + this.result.rows + " " + this.result.columns);
        }


        FloatMatrix[] errors    = new FloatMatrix[NUM_OF_LAYERS];
        FloatMatrix[] deltas    = new FloatMatrix[NUM_OF_LAYERS - 1];
        //layer 0 is the input layer and does not need to be adjusted
        this.adjustment         = new FloatMatrix[NUM_OF_LAYERS - 1];
        FloatMatrix[] adjusted  = new FloatMatrix[NUM_OF_LAYERS];

        errors[INDEX_OF_OUTPUT] = answer.sub(this.result);

        //calculate error for each layer
        for (int i = INDEX_OF_OUTPUT - 1; i > 0 ; i--) {
            errors[i] = MatrixMath.dot(errors[i + 1], layers[i + 1].T());
        }

        //calculate delta and adjustment for each layer, except the inputs, given corresponding error
        for (int i = 0; i < NUM_OF_LAYERS - 1; i++) {
            deltas[i] = MatrixMath.mult(errors[i + 1], actPrime(this.sums[i + 1]));
            this.adjustment[i] = MatrixMath.dot(actFunc(sums[i]).T(), deltas[i]);
        }

        //run through local eliminator method
        eliminator.main(this, answer);

        //adjust weights
        //don't adjust layer 0 because it is the input layer
        for (int i = NUM_OF_LAYERS - 1; i > 0; i--) {
            //the adjustment array will be offset by one element
            adjusted[i] = MatrixMath.add(this.layers[i], this.adjustment[i - 1].mult(this.learnRate));
        }

        this.layers = adjusted;

    }



    public String toString () {
        String r = "";
        for(int i = 0; i < NUM_OF_LAYERS; i++){
            r += "Layer: " + i + "\tNodes: " + LAYER_PROFILE[i] + "\n";
            r += layers[i].toString() + "\n";
        }
        return r;
    }
}
