package me.sn00pbom.bobby;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import static me.sn00pbom.bobby.NetFunc.DELTA;


public class NetTrainer {

    public NetTrainer(NeuralNet neuralNet, DataChunk[] dataSet) {
        this.neuralNet = neuralNet;
        this.DATASET = dataSet;
    }

    private NeuralNet neuralNet;
    private final DataChunk[] DATASET;
    private ArrayList<Double[]> results = new ArrayList<>();
    private HashMap<String, Double> weightChanges = new HashMap<>();
    private double learnRate = 1;

    private double momentum = 1;

    private boolean announce = true;

    public void train(int maxTimes, double minError) {

        int i;
        double error = 1000000000;

        double totalLoops = 0;
        for (i = 0; (i < maxTimes || maxTimes == -1) && error > minError; i++) {
            error = 0;//TODO MOVE SHIT HERE
            DataChunk[] set = NetFunc.pullSubset(DATASET, 10);

            for (DataChunk example : set) {
                neuralNet.setInputs(example.features);
                backPropagate(neuralNet.layerOutput, example.labels);
                error += Arrays.stream(NetFunc.squareError(example.labels, neuralNet.getLastOutput())).sum();
            }
//            momentum = Math.sqrt(totalLoops)/totalLoops;
            totalLoops += set.length;
            double terr = error/totalLoops;
//            updateWeights();
            for (Neuron neuron : neuralNet.layerOutput) {
                neuron.updateAllChild();
            }
            if(announce) System.out.println("Epoch " + i + " cost: " + terr);
            if (terr < minError) {
                break;
            }
//            for (int p = 0; p < neuralNet.layerInput.size(); p++) {
//                for (DataChunk chunk : set) {
//                    neuralNet.setInputs(chunk.features);
//                    double[] prediction = neuralNet.activate();
//                    double[] actual = chunk.labels;
//                    double[] err = NetFunc.squareError(prediction, actual);
//                    error += Arrays.stream(err).sum();
//                    Main.p("training: " + error);
//                    backPropagate(actual);
//
//                }
//            }
        }
        System.out.println("-Finished Training-");
        // gen subset to use


    }

    private void backPropagate(ArrayList<Neuron> layer, double[] actual) {
        ArrayList<Connection> allConnections = new ArrayList<>();
        for (Neuron neuron : layer) {
            for (Connection connection : neuron.getInboundConnections()) {
                allConnections.add(connection);
            }
        }
        backPropagate(layer, actual, allConnections);
    }

    private void backPropagate(ArrayList<Neuron> layer, double[] actual, ArrayList<Connection> allConnections) {

        ArrayList<Neuron> nextLayer = new ArrayList<>();

        for (Neuron neuron : layer) {
            for (Connection connection : neuron.getInboundConnections()) {
                double dydw = partialDerivative(connection, actual);
                dydw *= -1;
                double step = dydw * learnRate * momentum;
                connection.getDeltas().add(step);

                connection.setStagedWeight(connection.getWeight() + step);

                Neuron fromNeuron = connection.getFromNeuron();
                if (!nextLayer.contains(fromNeuron)) {
                    nextLayer.add(fromNeuron);
                }
            }
        }
        for (Neuron neuron : layer) {
            for (Connection connection : neuron.getInboundConnections()) {
//                connection.flipWeights();
            }
        }

        // add to all
        for (Neuron neuron : nextLayer) {
            for (Connection connection : neuron.getInboundConnections()) {
                allConnections.add(connection);
            }
        }

        if (nextLayer.size() != 0) {
            backPropagate(nextLayer, actual, allConnections);
        }else{
            for (Connection connection : allConnections) {
//                connection.flipWeights();
            }
        }






//        int flayerIndex = neuralNet.getNumLayers() -1;
//
//        for (int l = flayerIndex; l > 0; l--) {// >0 because input layer has no inbound
//
//            for (Neuron neuron : neuralNet.layers.get(l)) {
//
//                for (Connection connection : neuron.getInboundConnections()) {
//                    double dydw = partialDerivative(connection, actual);
//                    double steepestDescent = dydw * -1;
//                    double step = steepestDescent * learnRate;
//                    connection.getDeltas().add(step);
//
//                    connection.setStagedWeight(connection.getWeight() + step);
//                }
////                neuron.getInboundConnections().forEach(connection -> connection.pushWeights());
//
//            }
//        }
////        flipWeights();

    }

    private void flipWeights() {
        for (ArrayList<Neuron> layer : neuralNet.layers) {

            for (Neuron neuron : layer) {
                for (Connection connection : neuron.getInboundConnections()) {
                    connection.flipWeights();
                }

            }
        }

    }
    private void updateWeights() {
//        for (ArrayList<Neuron> layer : neuralNet.layers) {
//            for (Neuron neuron : layer) {
//                for (Connection connection : neuron.getInboundConnections()) {
//                    connection.setWeight(connection.getWeight() + connection.averageDeltas());
//                    connection.getDeltas().clear();
//                }
//            }
//        }
    }

    private double partialDerivative(Connection connection, double[] actual) {
        connection.getFromNeuron().restrict();

        double[] outi = neuralNet.activate();
        connection.setNudge(true);
        double[] outf = neuralNet.activate();

        // ys will be total error from each
        double y1 = NetFunc.errorTotal(outi, actual);
        double y2 = NetFunc.errorTotal(outf, actual);

        double dydw = (y2-y1)/DELTA;

        connection.getFromNeuron().allow();

        return dydw;
    }


    public static DataChunk[] genOrangeAppleSet(int size) {
        DataChunk[] set = new DataChunk[size];

        int apples = size/2;
        int oranges = size - apples;

        int cur = 0;

        //generates set of apples and oranges with features: weight, rough and label: apple/orange



        for (int i = 0; i < apples; i++) {
            set[cur] = new DataChunk(new double[]{10 + NetFunc.random.nextDouble()*15, 0}, new double[]{0,1});
            cur ++;
        }

        for (int i = 0; i < oranges; i++) {
            set[cur] = new DataChunk(new double[]{100 + NetFunc.random.nextDouble()*15, 1}, new double[]{1,0});
            cur ++;
        }
        return set;
    }

    public static DataChunk[] genLinearSet(int size) {
        ArrayList<DataChunk> set = new ArrayList<>();

        double toterr = 0;
        for (double x = 1; x < size; x++) {
            double error = NetFunc.random.nextDouble() * 5;
            toterr += error;
            if(NetFunc.random.nextInt(2)==1) error *= -1;
            double y = 3 * x + 2000 + 1*error;
            set.add(new DataChunk(new double[]{x}, new double[]{y}));
        }
        Main.p(toterr / size);
        return set.toArray(new DataChunk[set.size()]);
    }

    public static DataChunk[] genQuadSet(int size) {
        ArrayList<DataChunk> set = new ArrayList<>();

        double toterr = 0;
        for (double x = 1*3; x < size*3; x++) {
            double error = NetFunc.random.nextDouble() * 5;
            toterr += error;
            if(NetFunc.random.nextInt(2)==1) error *= -1;
            double y = Math.pow(x,2) + 2000 + 0*error;
            set.add(new DataChunk(new double[]{x}, new double[]{y}));
        }
        Main.p(toterr / size);
        return set.toArray(new DataChunk[set.size()]);
    }

    public double getLearnRate() {
        return learnRate;
    }

    public void setLearnRate(double learnRate) {
        this.learnRate = learnRate;
    }

    public boolean isAnnounce() {
        return announce;
    }

    public void setAnnounce(boolean announce) {
        this.announce = announce;
    }
}
