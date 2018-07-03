package me.sn00pbom.bobby;


import java.util.*;

public abstract class NeuralNet {
    public NeuralNet(int numIn, int numOut, int... hiddens) {

        layerInput = new ArrayList<>();
        layerHiddens = new ArrayList[hiddens.length];
        layerOutput = new ArrayList<>();



        if (numIn == 0 || numOut == 0) oops();

        // init input layer
        for (int i = 0; i < numIn; i++) {
            layerInput.add(new RestrictedNeuron(nextID++));
        }


        // loop over hidden layers
        for (int i = 0; i < hiddens.length; i++) {

            if (hiddens[i] == 0) oops();

            layerHiddens[i] = new ArrayList<>();
            // init hidden layer i
            for (int n = 0; n < hiddens[i]; n++) {
                layerHiddens[i].add(new Neuron(nextID++));
            }

        }

        // init output layer
        for (int i = 0; i < numOut; i++) {
            layerOutput.add(new Neuron(nextID++));
        }

        //connect hiddens backwards
        for (int i = 0; i < layerHiddens.length; i++) {
            ArrayList<Neuron> layerHidden = layerHiddens[i];
            if(layerHidden.size()!=0){
                if (i == 0) {
                    for (Neuron kNeuron : layerHidden) {
                        for (Neuron nNeuron : layerInput) {
                            kNeuron.addInboundConnection(nNeuron);
                        }
                        // bias connection
                        kNeuron.addInboundConnection(new RestrictedNeuron(nextID++,1));
                    }
                }else{
                    ArrayList<Neuron> lastLayerHidden = layerHiddens[i - 1];
                    for (Neuron kNeuron : layerHidden) {
                        for (Neuron nNeuron : lastLayerHidden) {
                            kNeuron.addInboundConnection(nNeuron);
                        }
                        // bias connection
                        kNeuron.addInboundConnection(new RestrictedNeuron(nextID++,1));
                    }
                }
            }
        }

        // connect outs to last hidden layer
        for (Neuron kNeuron : layerOutput) {
            if (hiddens.length != 0) {
                for(Neuron nNeuron : layerHiddens[layerHiddens.length-1]){
                    kNeuron.addInboundConnection(nNeuron);
                }
            }else{
                for (Neuron nNeuron : layerInput) {
                    kNeuron.addInboundConnection(nNeuron);
                }
            }

            // bias connection
            kNeuron.addInboundConnection(new RestrictedNeuron(nextID++,1));
        }

        putLayers();
    }


    public ArrayList<RestrictedNeuron> layerInput;
    public ArrayList<Neuron>[] layerHiddens;
    public ArrayList<Neuron> layerOutput;
    public ArrayList<ArrayList<Neuron>> layers;

    private int nextID = 0;





    public double[] activate() {
        double[] out = new double[layerOutput.size()];
        for (int i = 0; i < layerOutput.size(); i++) {
            out[i] = layerOutput.get(i).activate();
        }
        return out;
    }

    public void printInfo() {
        System.out.println("NeuralNet info:\n" +
                "# inputs: " + layerInput.size() + "\n" +
                "# outputs: " + layerOutput.size() + "\n");
        Arrays.stream(layerHiddens).forEach(layer -> System.out.println("# hidden nodes in l: " + layer.size()));
    }
    public void printWeights() {
        for (Neuron neuron : layerOutput) {

            neuron.printChildWeights();
        }
    }
    public ArrayList<Connection> getAllConnections() {
        ArrayList<Connection> connections = new ArrayList<>();
        //TODO FINISH THIS
        return null;
    }


    public double[] getLastOutput() {
        double[] out = new double[layerOutput.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = layerOutput.get(i).getValue();
        }
        return out;
    }

    public void setInputs(double[] inputs) {
        if(inputs.length != layerInput.size()) oops();

        for (int i = 0; i < inputs.length; i++) {
            layerInput.get(i).setValue(inputs[i]);
        }
    }

    private void putLayers() {
        layers = new ArrayList<>();
//        layers.add((Neuron)layerInput);
        Arrays.stream(layerHiddens).forEach(layer -> layers.add(layer));
        layers.add(layerOutput);
    }

    private void oops() {
        System.out.println("Err: illegal occurrence");
        System.exit(1);

    }

    public int getNumLayers() {
        return layers.size();
    }


}
