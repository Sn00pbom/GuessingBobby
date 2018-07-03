package me.sn00pbom.bobby;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static HashMap<Integer, Neuron> neuronDB = new HashMap<>();

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        DataChunk[] set = NetTrainer.genQuadSet(2000000);
//        stdin.next();

        RegressionNet nnet = new RegressionNet(1,1,30);
        nnet.setInputs(set[0].features);

        nnet.printWeights();

        NetTrainer trainer = new NetTrainer(nnet, set);
        trainer.setLearnRate(.00001);
        trainer.setAnnounce(true);
        trainer.train(-1,.01);
//        nnet.printWeights();



        while (true) {
            p("what do:\n" +
                    "0: make a guess\n" +
                    "1: quit");
            int input = stdin.nextInt();
            if (input == 0) {
                p("enter value:");
                double x = stdin.nextDouble();
//                p("enter texture 0,1:");
//                double x2 = stdin.nextDouble();
                nnet.setInputs(new double[]{x});
                pLayer(nnet.activate());
            } else if (input == 1) {
                break;
            }
        }



    }

    public static void p(Object o) {
        System.out.println("db: " + o.toString());
    }
    public static void pLayer(double[] layer) {
        for (int i = 0; i < layer.length; i++) {
            p(i + ": " + layer[i]);
        }
    }
    public static void pDB() {
        neuronDB.forEach((k,v) -> {
            p(k + ": " + v);
            v.printInfo();
        });

    }
}
