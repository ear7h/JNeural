package JNeural;

/**
 * Created by julio on 3/15/17.
 */
public class NeuronLayer {
    int NUM_OF_INPUTS; //number of inputs
    int NUM_OF_OUTPUTS; //number of neurons
    boolean IS_INPUT_LAYER;
    float[] result;
    Neuron[] nodes;

    public NeuronLayer (int numOfInputs, int layerSize, boolean isInputLayer) {
        this.NUM_OF_INPUTS = numOfInputs;
        this.NUM_OF_OUTPUTS = layerSize;
        this.IS_INPUT_LAYER = isInputLayer;
        this.nodes = new Neuron[layerSize];
        for (int i = 0; i < layerSize; i++) {
            //isInputLayer value tells neuron constructor to make a "clear" (weights = 0) or regular (random weights) neuron
            nodes[i] = new Neuron(numOfInputs, isInputLayer);
        }
    }

    public float[] fire (float[] inputs){
        float[] output = new float[NUM_OF_OUTPUTS];

        if(inputs.length != NUM_OF_INPUTS){
            throw new IllegalArgumentException("ERROR: This neuron layer accepts " + NUM_OF_INPUTS + " inputs");
        }


        if(this.IS_INPUT_LAYER){
            for (int i = 0; i < NUM_OF_OUTPUTS; i++) {
                output[i] = this.nodes[i].fire(inputs[i]);
            }
        } else {
            for (int i = 0; i < NUM_OF_OUTPUTS; i++) {
                output[i] = this.nodes[i].fire(inputs);
            }
        }

        this.result = output;
        return output;
    }

    public void print (){
        for (int i = 0; i < this.nodes.length; i++) {
            System.out.print("\tNode " + i);
            this.nodes[i].print();
        }
    }




}
