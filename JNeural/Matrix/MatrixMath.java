package JNeural.Matrix;

/*
multithreaded array mathematics
 */

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.*;


public class MatrixMath {
    static int POOL_SIZE = Runtime.getRuntime().availableProcessors() - 1;
    static ExecutorService pool;
    //constructor, sets pool size

    public static FloatMatrix dots (FloatMatrix a1, FloatMatrix a2) {
        FloatMatrix r = new FloatMatrix(a1.rows, a2.columns);
        for (int i = 0; i < a1.rows; i++) {
            for (int k = 0; k < a2.rows; k++) {
                for (int j = 0; j < a2.columns; j++) {
                    float val = a1.get(i, k) * a2.get(k, j);
                    r.set(r.get(i, j) + val, i, j);
                }
            }
        }

        return r;
    }

    //dot product method
    public static FloatMatrix dot (FloatMatrix a1, FloatMatrix a2){
        //check for matrix size compatibility
        if (a1.columns != a2.rows) {
            throw new IllegalArgumentException("Columns of matrix a1 must be equal to rows of matrix a2");
        }
        //this method will only be efficient if the first matrix has more columns than the computer has POOL_SIZE.
        if (a1.rows < POOL_SIZE){
            return a1.dot(a2);
        }
        //create the resulting matrix
        FloatMatrix res = new FloatMatrix(a1.rows, a2.columns);
        //set number of rows needed in for loop
        int m1 = a1.rows;
        //linked list for blocking loop
        Collection<Future<?>> futures = new LinkedList<Future<?>>();
        /*
        single threaded dot product:

        FloatMatrix r = new FloatMatrix(this.rows, a.columns);
        for (int i =0; i < this.rows; i++){
            for (int j = 0; j < a.columns; j++){
                for(int k = 0; k < a.rows; k++){
                    float val = this.get(i, k) * a.get(k, j);
                    r.set(r.get(i, j) + val, i, j);
                }
            }
        }

        the DotTask class executes part of the dot product loop
            for each row in the first matrix, a task will be added to the pool;
            for each completed task, an item will be added to the future linked list
         */

        //instantiate the pool
        pool = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 0; i < m1; i++){
            Runnable r = new DotTask(i, a1, a2, res);
            futures.add(pool.submit(r));
        }
        //blocking loop
        //loops over the future list until all threads have been completed
        for(Future<?> future: futures){
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }

        }
        //shuts down pool after all calculations have been completed;
        pool.shutdown();


        return res;
    }

    //Hadamard product
    public static FloatMatrix mult (FloatMatrix m1, FloatMatrix m2) {
        if (m1.rows != m2.rows || m1.columns != m2.columns){
            throw new IllegalArgumentException("Matrices require equal dimensions for Hadamard product");
        }

        FloatMatrix r = new FloatMatrix(m1.rows, m1.columns);
        for (int i = 0; i < r.length; i++) {
            r.arr[i] = m1.arr[i] * m2.arr[i];
        }
        return r;
    }

    public static FloatMatrix add (FloatMatrix m1, FloatMatrix m2) {
        if (m1.rows != m2.rows || m1.columns != m2.columns){
            throw new IllegalArgumentException("Matrices require equal dimensions for addition");
        }
        FloatMatrix r = new FloatMatrix(m1.rows, m1.columns);
        for (int i = 0; i < r.length; i++) {
            r.arr[i] = m1.arr[i] + m2.arr[i];
        }
        return r;
    }

}


//Runnable object for threads created in dot method
class DotTask implements Runnable {
    private int i = 0;
    private int n1;
    private int n2;
    private FloatMatrix a1;
    private FloatMatrix a2;
    private FloatMatrix res;
    DotTask (int i, FloatMatrix a1, FloatMatrix a2, FloatMatrix res){
        this.i = i;
        this.a1 = a1;
        this.a2 = a2;
        this.res = res;
        this.n1 = a1.columns;
        this.n2 = a2.columns;
    }
    @Override
    public void run() {
        for (int j = 0; j < n2; j++){
            for(int k = 0; k < n1; k++){
                float val = a1.get(i, k) * a2.get(k, j);
                res.set(res.get(i, j) + val, i, j);
            }
        }
    }
}