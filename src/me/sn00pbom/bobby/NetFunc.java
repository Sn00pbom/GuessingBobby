package me.sn00pbom.bobby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NetFunc {

    public static final double DELTA = 0.00000000001;

    public static Random random = new Random();


    public static double sigmoid(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

    public static double leakyReLU(double x) {
        if (x > 0) {
            return x;
        }else{
            return 0.01*x;
        }
    }

    public static double ReLU(double x) {
        if (x > 0) {
            return x;
        }else{
            return 0;
        }
    }

    @Deprecated
    public static double meanSquareError(double[] predict, double[] actual) {
        return 0;
    }

    public static double errorTotal(double[] predict, double[] actual) {
        double[] errorArr = squareError(predict, actual);
        double total = Arrays.stream(errorArr).sum();
        return total;
    }

    public static double[] error(double[] predict, double[] actual) {
        double[] out = new double[predict.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = actual[i] - predict[i];
        }
        return out;
    }
    public static double[] absError(double[] predict, double[] actual) {
        double[] out = error(predict, actual);
        for (int i = 0; i < out.length; i++) {
            out[i] = Math.abs(out[i]);

        }
        return out;
    }

    public static double[] squareError(double[] predict, double[] actual) {
        double[] out = error(predict, actual);
        for (int i = 0; i < out.length; i++) {
            out[i] = Math.pow(out[i],2);

        }
        return out;
    }

    public static void normalize(double[] actual) {
        for (int i = 0; i < actual.length; i++) {
            double n = actual[i];
            if (n < 0 || n > 1) {
                if (n < 0) {
                    actual[i] = 0 + DELTA;
                }else{
                    actual[i] = 1 - DELTA;
                }
            }
        }
    }

    public static DataChunk[] pullSubset(DataChunk[] dataSet, int amount) {

        ArrayList<DataChunk> out = new ArrayList<>();

        do {
            int r = NetFunc.random.nextInt(amount);
            DataChunk dataChunk = dataSet[r];
            if (!out.contains(dataChunk)) {
                out.add(dataChunk);
            }
        } while (out.size()!= amount);

        DataChunk[] nout = out.toArray(new DataChunk[out.size()]);
        return nout;
    }



}
