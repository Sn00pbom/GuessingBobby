package me.sn00pbom.bobby;

public class RestrictedNeuron extends Neuron {

    public RestrictedNeuron(int id) {// mode not necessary since will never have any ins
        super(id,0);
        Main.p("restrict created");
        restrict();
    }

    public RestrictedNeuron(int id, int value) {
        this(id);
        setValue(value);
    }

    @Override
    public double activate() {
        return getValue();
    }

    @Override
    public void addInboundConnection(Neuron fromNeuron) {
        return;
    }


}
