package io.github.sjcross.common.Process.ActiveContour.Energies;

import io.github.sjcross.common.Process.ActiveContour.PhysicalModel.Vertex;

/**
 * Created by Stephen on 16/09/2016.
 */
public class Energy {
    public double weight = 1;

    public Energy(double weight) {
        this.weight = weight;

    }

    public double getWeight() {
        return weight;

    }

    public void setWeight(double weight) {
        this.weight = weight;

    }

    public double getEnergy(Vertex node) {
        return 0;

    }

}
