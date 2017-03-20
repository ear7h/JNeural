package JNeural;
/**
 * Created by julio on 3/16/17.
 */
public class GetActivation {
    public static Activation sigmoid() {
        Activation sig = new Activation() {
            @Override
            public float function(float x) {
                return (float) (1 / (1 + Math.exp(-1 * x)));
            }

            @Override
            public float prime(float x) {
                return (float) (Math.exp(x) /  Math.pow(
                        (1 + Math.exp(x)),
                        2));
            }
        };
        return sig;
    }

    //TODO: fix this
    public static Activation ReLU () {
        Activation ReLU = new Activation() {
            @Override
            public float function(float x) {
                if (x < 0){
                    return 0;
                } else {
                    return x;
                }
            }

            @Override
            public float prime(float x) {
                if (x < 0){
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        return ReLU;
    }

}


