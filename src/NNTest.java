
import JNeural.NeuronNetwork;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by julio on 3/15/17.
 */
public class NNTest {
    public static void main (String[] args){
        //first and last ints in profile are reserved for input and output respectively
        //any ints in between are hidden layers with specified number of nodes
        Scanner in = new Scanner(System.in);
        System.out.println("Training for XOR gate\n");

        int[] profile = new int[]{2, 4, 2, 1};
        NeuronNetwork n = new NeuronNetwork(profile);

        System.out.println("Initial weights: ");
        n.print();
        System.out.print("\nPress any key to train the neural network");
        in.nextLine();


        //xor gate
        float[][] trainingSetProblems = new float[][] {
                {0.0f, 0.0f},
                {1.0f, 0.0f},
                {0.0f, 1.0f},
                {1.0f, 1.0f}
        };
        float[][] trainingSetAnswers = new float[][] {
                {0.0f},
                {1.0f},
                {1.0f},
                {0.0f}
        };


        n.train(trainingSetProblems, trainingSetAnswers, 10000);

        System.out.println("\nFinal weights: ");
        n.print();

        float[] test = new float[]{0.0f, 0.0f};
        n.run(test);
        System.out.println("Testing " + Arrays.toString(test) + " -> " + Arrays.toString(n.result));

        boolean run = true;
        int option;
        while (run){
            System.out.println("\nChoose an option to test or 0 to quit");
            System.out.println(
                    "1. " + Arrays.toString(trainingSetProblems[0]) + "\n" +
                    "2. " + Arrays.toString(trainingSetProblems[1]) + "\n" +
                    "3. " + Arrays.toString(trainingSetProblems[2]) + "\n" +
                    "4. " + Arrays.toString(trainingSetProblems[3])
            );
            option = in.nextInt();
            in.nextLine();

            if (option == 0){
                run = false;
            } else if (option >= 1 && option <= 4) {
                n.run(trainingSetProblems[option - 1]);
                System.out.println("Testing " + Arrays.toString(trainingSetProblems[option -1]) + " -> " + Arrays.toString(n.result));
            } else {
                System.out.println(option + " is not a valid option");
            }
        }
    }
}
