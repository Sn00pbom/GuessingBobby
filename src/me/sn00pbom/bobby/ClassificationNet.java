package me.sn00pbom.bobby;

import java.util.ArrayList;

public class ClassificationNet extends NeuralNet {
    public ClassificationNet(int numIn, int numOut, int... hiddens) {
        super(numIn, numOut, hiddens);

        // set hidden layer modes to sigmoid
        for (ArrayList<Neuron> layer : layerHiddens) {
            for (Neuron neuron : layer) {
                neuron.setMode(1);
            }
        }

        // set output layer modes to sigmoid
        for (Neuron neuron : layerOutput) {
            neuron.setMode(1);
        }

    }
}
