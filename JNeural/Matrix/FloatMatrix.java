package JNeural.Matrix;

/*
JNeural.Matrix class which uses a one dimensional array and contains some mathematical shortcuts
 */

import java.util.Arrays;
import java.util.Random;

public class FloatMatrix {
    public float[] arr;
    public int rows;
    public int columns;
    public int length;
    public FloatMatrix (int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.length = rows * columns;
        arr = new float[rows * columns];
    }

    public void populate (float  min, float max) {
        Random rd = new Random();
        float range = max - min;
        for(int i = 0; i < this.arr.length; i++){
            this.arr[i] = (rd.nextFloat() * range) + min;
        }
    }

    public void set (float val, int row, int column) {
        //
        arr[(this.columns*row) + column] = val;
        return;
    }

    public float get (int row, int column ){
        //
        return arr[(this.columns * row) + column];
    }

    public float[][] toArray () {
        float[][] a = new float[this.rows][this.columns];
        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++) {
                a[i][j] = this.get(i, j);
            }
        }
        return a;
    }

    public void print (String s) {
        System.out.print(s);
        float[] a = new float[this.columns];
        System.out.println("[");
        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++) {
                a[j] = this.get(i, j);
            }
            System.out.println(Arrays.toString(a));
        }
        System.out.println("]");
    }
    //overloaded, no prepended string
    public void print () {
        float[] a = new float[this.columns];
        System.out.println("[");
        for (int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++) {
                a[j] = this.get(i, j);
            }
            System.out.println(Arrays.toString(a));
        }
        System.out.println("]");
    }

    //dot product
    public FloatMatrix dot (FloatMatrix a) {
        if(this.columns != a.rows){
            throw new IllegalArgumentException("Incompatible matrix sizes for dot product");
        }
        FloatMatrix r = new FloatMatrix(this.rows, a.columns);
        for (int i =0; i < this.rows; i++){
            for (int j = 0; j < a.columns; j++){
                for(int k = 0; k < a.rows; k++){
                    float val = this.get(i, k) * a.get(k, j);
                    r.set(r.get(i, j) + val, i, j);
                }
            }
        }
        return r;
    }

    //add matrices
    public FloatMatrix add (FloatMatrix m) {
        if (m.rows != this.rows || m.columns != this.columns){
            throw new IllegalArgumentException("Matrices require equal dimensions for addition");
        }

        FloatMatrix r = new FloatMatrix(this.rows, this.columns);
        for(int i = 0; i < this.length; i++){
            r.arr[i] = this.arr[i] + m.arr[i];
        }

        return r;
    }

    //add scalar
    public FloatMatrix add (float s) {
        FloatMatrix r = new FloatMatrix(this.rows, this.columns);
        for(int i = 0; i < this.length; i++){
            r.arr[i] = this.arr[i] + s;
        }

        return r;
    }

    //subtract matrices
    public FloatMatrix sub (FloatMatrix m) {
        if (m.rows != this.rows || m.columns != this.columns){
            throw new IllegalArgumentException("Matrices require equal dimensions for subtraction");
        }

        FloatMatrix r = new FloatMatrix(this.rows, this.columns);
        for(int i = 0; i < this.length; i++){
            r.arr[i] = this.arr[i] - m.arr[i];
        }

        return r;
    }

    //subtract scalar
    public FloatMatrix sub (float s) {
        FloatMatrix r = new FloatMatrix(this.rows, this.columns);
        for(int i = 0; i < this.length; i++){
            r.arr[i] = this.arr[i] - s;
        }

        return r;
    }

    //multiply by scalar
    public FloatMatrix mult (float s) {
        FloatMatrix r = new FloatMatrix(this.rows, this.columns);
        for(int i = 0; i < this.length; i++){
            r.arr[i] = this.arr[i] * s;
        }

        return r;
    }

    //Hadamard product
    public FloatMatrix mult (FloatMatrix m) {
        if (m.rows != this.rows || m.columns != this.columns){
            throw new IllegalArgumentException("Matrices require equal dimensions for Hadamard product");
        }

        FloatMatrix r = new FloatMatrix(this.rows, this.columns);
        for (int i = 0; i < this.length; i++) {
            r.arr[i] = this.arr[i] * m.arr[i];
        }
        return r;
    }

    //flip dimensions of matrix
    public FloatMatrix T () {
        FloatMatrix res = new FloatMatrix(this.columns, this.rows);
        for(int c = 0; c < this.columns; c++){
            for(int r = 0; r < this.rows; r++){
                res.set(this.get(r, c), c, r);
            }
        }

        return res;
    }

    public String toString () {
        String r = "";
        for (int i = 0; i < this.rows ; i++) {
            r += "[";
            for (int j = 0; j < this.columns; j++) {
                float v = this.get(i, j);
                if (v >= 0) {
                    r += " ";
                }
                r += String.format("%.8f", v);

                if (j != this.columns - 1) {
                    r += ", ";
                }
            }
            r += "]\n";
        }
        return r;
    }


}
