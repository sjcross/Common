//TODO: Implement greedy energy minimisation algorithm

package io.github.sjcross.common.process.activecontour.energies;

import io.github.sjcross.common.process.activecontour.physicalmodel.Vertex;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Stephen on 07/09/2016.
 */
public class EnergyCollection {

    Set<Energy> energies = new HashSet<Energy>();

    public Iterator<Energy> iterator() {
        return energies.iterator();
    }

    public int size() {
        return energies.size();
    }

    public void add(Energy energy) {
        energies.add(energy);

    }

    public void remove(Energy energy) {
        energies.remove(energy);

    }

    public double getEnergy(Vertex node) {
        double energy = 0;

        Iterator<Energy> iterator = iterator();
        while (iterator.hasNext()) {
            energy += iterator.next().getEnergy(node);

        }

        return energy;
    }

}
