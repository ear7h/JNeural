package JNeural;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by julio on 3/15/17.
 */
public class Neuron {
    float []    inputWeights;
    int         NUM_OF_INPUTS;
    boolean     IS_INPUT_NEURON;
    float       sum; //sum of inputs
    float       result; //activation.function(sum);
    float       delta;
    float       error;
    Activation activation = GetActivation.sigmoid();



    //a "clear" neuron has weights of 1.0 for all inputs;
    public Neuron (int inputs, boolean isInputLayer) {
        this.IS_INPUT_NEURON = isInputLayer;
        if (isInputLayer){
            //one input - the data;
            NUM_OF_INPUTS = 1;

            //one weight for the single input
            inputWeights = new float[1];
            inputWeights[0] = 1.0f;
        } else {
            inputWeights = new float[inputs];
            NUM_OF_INPUTS = inputs;

            Random rd = new Random();
            for(int i = 0; i < inputs; i ++){
                inputWeights[i] = (rd.nextFloat() * 2) -1;
            }
        }

    }

    public float fire (float[] inputs) {
        if(inputs.length != NUM_OF_INPUTS){
            throw new IllegalArgumentException("ERROR: This neuron accepts " + inputWeights.length + " inputs");
        }

        this.sum = 0;
        for(int i = 0; i < NUM_OF_INPUTS; i++){
            sum += inputs[i] * inputWeights[i];
        }

        this.result = activation.function(sum);

        return result;
    }

    public float fire(float input){
        if (!this.IS_INPUT_NEURON){
            throw new IllegalArgumentException("ERROR: This is not an input neuron, it takes float[] with  " + inputWeights.length + " length");
        }
        this.result = input;
        return input;
    }

    public void setDelta () {
        this.delta = this.error * this.activation.prime(this.sum);
    }

    public void print() {
        System.out.println(Arrays.toString(this.inputWeights));

    }

}
