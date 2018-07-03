package me.sn00pbom.bobby;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Random;

public class Neuron {
    public Neuron(int id) {
        this(id, 0);
    }
    public Neuron(int id, int mode) {
        setId(id);
        setMode(mode);
        setValue(0);
        Main.neuronDB.put(this.id, this);
    }
    private int id;
    // 0: no transform
    // 1: sigmoid
    // 2: ReLU
    // 3: Leaky ReLU
    private int mode = 0;
    private double value;
    private ArrayList<Connection> inboundConnections = new ArrayList<>();
    private boolean allowed = true;


    public double activate() {
        if(getMode() == -1) return getValue();// restricted mode

        double out = 0;
        for (Connection connection : inboundConnections) {
            out += connection.activate();
        }

        if (mode == 0) {
            //nothing
        } else if (mode == 1) {
            out = NetFunc.sigmoid(out);
        } else if (mode == 2) {
            out = NetFunc.ReLU(out);
        } else if (mode == 3) {
            out = NetFunc.leakyReLU(out);
        }else{
            //nothing
        }
        value = out;
        return out;

    }

    public void addInboundConnection(Neuron fromNeuron) {
        Main.p("init");
        Connection connection = new Connection(this, fromNeuron);
        inboundConnections.add(connection);
    }


    public void printInfo() {
        for (Connection connection : inboundConnections) {
            Main.p("->" + connection.getFromNeuron().id + ": " + connection.getWeight());
        }
    }

    public void printChildWeights() {
        for (Connection connection : getInboundConnections()) {
            System.out.println(getId() + ": " + connection.getWeight());
            connection.getFromNeuron().printChildWeights();
        }
    }

    public void updateAllChild() {
        for (Connection connection : getInboundConnections()) {

            connection.setWeight(connection.getWeight() + connection.averageDeltas());
            connection.getDeltas().clear();

            connection.getFromNeuron().updateAllChild();
        }
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public ArrayList<Connection> getInboundConnections() {
        return inboundConnections;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void restrict() {
        if (isAllowed() == false) {
            System.out.println("Neuron_" + id + ": Already restricted");
        } else {
            setAllowed(false);
        }
    }

    public void allow() {
        if (isAllowed() == true) {
            System.out.println("Neuron_" + id + ": Already allowed");
        } else {
            setAllowed(true);
        }
    }

    public boolean isAllowed() {
        return allowed;
    }

    private void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
