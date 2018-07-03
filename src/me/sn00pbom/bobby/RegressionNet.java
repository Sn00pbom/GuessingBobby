package me.sn00pbom.bobby;

import java.util.ArrayList;

public class RegressionNet extends NeuralNet {
    public RegressionNet(int numIn, int numOut, int... hiddens) {
        super(numIn, numOut, hiddens);

        // set hidden layer modes to Leaky ReLU
        for (ArrayList<Neuron> layer : layerHiddens) {
            for (Neuron neuron : layer) {
                neuron.setMode(0);
            }
        }
        for (Neuron neuron : layerOutput) {
            neuron.setMode(0);
        }


    }
}
