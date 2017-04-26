package JNeural;

import JNeural.matrix.FloatMatrix;

/**
 * Created by j on 4/22/17.
 */
public class TrainingSet {
    private FloatMatrix inputs;
    private FloatMatrix answers;
    private int length; //number of rows for both matrices
    private int NUM_OF_INPUTS; //number of columns for input matrix
    private int NUM_OF_OUTPUTS; //number of columns for output matrix

    public TrainingSet (NeuralNetwork n){
        NUM_OF_INPUTS = n.getNUM_OF_INPUTS();
        NUM_OF_OUTPUTS = n.getNUM_OF_OUTPUTS();
        length = 0;
    }

    public void addToSet(float[] in, float[] ans) {
        if (in.length != NUM_OF_INPUTS){
            throw new IllegalArgumentException("ERROR: Input size does not match number of network inputs");
        }
        if (ans.length != NUM_OF_OUTPUTS){
            throw new IllegalArgumentException("ERROR: Output size does not match number of network outputs");
        }


        FloatMatrix nInputs = new FloatMatrix(length + 1, NUM_OF_INPUTS);
        FloatMatrix nAnswers = new FloatMatrix(length + 1, NUM_OF_OUTPUTS);

        //copy existing rows
        for (int i = 0; i < length; i++) {

            for (int j = 0; j < NUM_OF_INPUTS; j++) {
                nInputs.set(inputs.get(i, j), i, j);
            }

            for (int j = 0; j < NUM_OF_OUTPUTS; j++) {
                nAnswers.set(answers.get(i, j), i, j);
            }

        }


        for (int i = 0; i < NUM_OF_INPUTS; i++) {
            nInputs.set(in[i], length, i);
        }

        for (int i = 0; i < NUM_OF_OUTPUTS; i++) {
            nAnswers.set(ans[i], length, i);
        }

        length++;
        this.inputs = nInputs;
        this.answers = nAnswers;

    }

    //accessor methods
    public int length() {
        return length;
    }
    public FloatMatrix getInputs() {
        return inputs;
    }

    public FloatMatrix getAnswers() {
        return answers;
    }
}
