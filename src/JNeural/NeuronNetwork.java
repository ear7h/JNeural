package JNeural;

import java.util.Arrays;

/**
 * Created by julio on 3/15/17.
 */



public class NeuronNetwork {
    NeuronLayer[] layers;
    public float[] result;
    public float latestError;
    public float localMinProbability[];
    final float LOCAL_FACTOR = 0.05f;
    final float ERROR_THRESHOLD = 0.1f;
    final float LEARN_RATE = 0.5f;
    final int NUM_OF_INPUTS;
    final int NUM_OF_OUTPUTS;
    final int NUM_OF_LAYERS;
    final int OUTPUT_INDEX;
    //final Activation activation;

    public NeuronNetwork(int[] layerProfile){
        NUM_OF_INPUTS = layerProfile[0];
        NUM_OF_OUTPUTS = layerProfile[layerProfile.length -1];
        NUM_OF_LAYERS = layerProfile.length;
        OUTPUT_INDEX = layerProfile.length - 1;
        layers = new NeuronLayer[layerProfile.length];

        //create clear input layer (weights = 1.0 and are not changed in back propagation)
        layers[0] = new NeuronLayer(layerProfile[0], layerProfile[0], true);

        for (int i = 1; i < layerProfile.length; i++) {
            layers[i] = new NeuronLayer(layerProfile[i - 1], layerProfile[i], false);
        }
        return;
    }

    public float[] run (float[] inputs){
        if(inputs.length != NUM_OF_INPUTS){
            throw new IllegalArgumentException("ERROR: This neuron network accepts " + NUM_OF_INPUTS + " inputs");
        }

        layers[0].fire(inputs);
        for (int i = 1; i < NUM_OF_LAYERS; i++) {
            layers[i].fire(layers[i-1].result);
        }
        this.result = layers[NUM_OF_LAYERS - 1].result;
        return this.result;
    }

    //train network with multiple sets
    public void train(float[][] inputs, float[][] outputs, int iterations){
        this.localMinProbability = new float [inputs.length];
        for (int i = 0; i < iterations ; i++) {
            for (int j = 0; j < inputs.length ; j++) {

                trainOnce(inputs[j] , outputs[j], this.localMinProbability[j] * this.LOCAL_FACTOR);

                this.localMinProbability[j] = ((this.latestError/this.ERROR_THRESHOLD) * i)/(i + iterations);

                System.out.println("In: " + Arrays.toString(inputs[j]) + "\t out: " + Arrays.toString(this.result));
                
            }
            System.out.print("\n");
        }

    }

    public void trainOnce(float[] inputs, float[] outputs, float localElimination){
        //back prop

        //check if the outputs is compatible with the network output layer
        if (outputs.length != this.NUM_OF_OUTPUTS) {
            throw new IllegalArgumentException("The training set output is not compatible with this network: Network outputs = "
            + NUM_OF_OUTPUTS + ", training set outputs = " + outputs.length);
        }


        //each node of the output layer has a corresponding result and therefore also a margin of error
        //results.length == layers[OUTPUT_INDEX].nodes.length == networkError.length;

        //results of network
        float[] results;
        results = run(inputs);

        //error at output nodes = network error
        float[] networkError = new float[results.length];
        this.latestError = 0;

        for (int i = 0; i < results.length; i++) {
            layers[OUTPUT_INDEX].nodes[i].error = networkError[i] = outputs[i] - results[i];
            this.latestError += networkError[i];
        }


        //error of inputwardly nodes

        //for each layer, working backwards
        for (int h = OUTPUT_INDEX - 1; h > 0; h--) {

            //calculate the error between each node
            for (int i = 0; i < layers[h].nodes.length; i++) {
                layers[h].nodes[i].error = 0;

                //and all of its dependents(dependents -> nodes one layer outputwardly of calculated layer)
                for (int j = 0; j < layers[h + 1].nodes.length; j++) {
                    layers[h].nodes[i].error += (
                            layers[h + 1].nodes[j].inputWeights[i] *
                            layers[h + 1].nodes[j].error
                    );
                }
            }
        }

        //calculate node delta
        for (int i = 0; i < NUM_OF_LAYERS; i++) {
            for (int j = 0; j < layers[i].nodes.length; j++) {
                layers[i].nodes[j].setDelta();
            }
        }

        //adjust weights

        for (int i = NUM_OF_LAYERS - 1; i > 0  ; i--) {
            for (int j = 0; j < layers[i].nodes.length; j++) {
                for (int k = 0; k < layers[i].nodes[j].inputWeights.length; k++) {
                    layers[i].nodes[j].inputWeights[k] += this.LEARN_RATE *
                            (layers[i].nodes[j].delta *
                            layers[i - 1].nodes[k].result) + (localElimination);
                }
            }
        }
        //for...

        //backprop done!
    }

    public void print() {
        for (int i = 0; i < this.layers.length; i++) {
            System.out.println("Layer: " + i);
            this.layers[i].print();
        }
    }

}
