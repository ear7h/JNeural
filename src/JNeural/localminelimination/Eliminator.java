package JNeural.localminelimination;

import JNeural.NeuralNetwork;
import Matrix.FloatMatrix;

/**
 * Created by j on 4/21/17.
 */
public interface Eliminator {


    //the local elimination function will be set by the user
    //the JNeural.NeuralNetwork.backprop will call this.localElimination.main() and pass this and the given current answer for the state
    void main (NeuralNetwork n, FloatMatrix a);
}
