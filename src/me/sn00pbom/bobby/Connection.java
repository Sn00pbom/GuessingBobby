package me.sn00pbom.bobby;

import java.util.ArrayList;
import java.util.Random;

import static me.sn00pbom.bobby.NetFunc.DELTA;

public class Connection {
    public Connection(Neuron toNeuron, Neuron fromNeuron) {
        this.fromNeuron = fromNeuron;
        this.toNeuron = toNeuron;
        this.weight = NetFunc.random.nextDouble();

    }
    private Neuron fromNeuron;
    private Neuron toNeuron;

    private double stagedWeight;
    private double weight;
//    private double lastWeight;

    private boolean nudge;

    //add deltas to array, average deltas, change weight by delta
    private ArrayList<Double> deltas = new ArrayList<>();

    public double activate() {
        double w = getWeight();
        if(isNudge()) w += DELTA;
        setNudge(false);

        return w * fromNeuron.activate();
    }

    public double averageDeltas() {
        double out = 0;
        double size = deltas.size();
        for (int i = 0; i < size; i++) {
            out += deltas.get(i);
        }
        out /= size;
        return out;
    }

    public void flipWeights() {
//        setLastWeight(getWeight());
//        setWeight(getStagedWeight());
//        setStagedWeight(getLastWeight());
        double temp = getWeight();
        setWeight(getStagedWeight());
        setStagedWeight(temp);
    }

    public Neuron getFromNeuron() {
        return fromNeuron;
    }

    public void setFromNeuron(Neuron fromNeuron) {
        this.fromNeuron = fromNeuron;
    }

    public Neuron getToNeuron() {
        return toNeuron;
    }

    public void setToNeuron(Neuron toNeuron) {
        this.toNeuron = toNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ArrayList<Double> getDeltas() {
        return deltas;
    }

    public boolean isNudge() {
        return nudge;
    }

    public void setNudge(boolean nudge) {
        this.nudge = nudge;
    }

//    public double getLastWeight() {
//        return lastWeight;
//    }
//
//    public void setLastWeight(double lastWeight) {
//        this.lastWeight = lastWeight;
//    }

    public double getStagedWeight() {
        return stagedWeight;
    }

    public void setStagedWeight(double stagedWeight) {
        this.stagedWeight = stagedWeight;
    }
}
