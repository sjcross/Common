package wolfson.common.Object;

import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sc13967 on 20/10/2016.
 */
public class SpotCollection {
    Set<Spot> spots = new HashSet<Spot>();

    public Iterator<Spot> iterator() {
        return spots.iterator();

    }

    public int size() {
        return spots.size();

    }

    public SpotCollection getByID(double ID) {
        Iterator<Spot> iterator = iterator();

        SpotCollection to_return = new SpotCollection();

        while(iterator.hasNext()) {
            Spot next = iterator.next();
            if (next.getID() == ID) {
                to_return.add(next);
            }
        }

        return to_return;

    }

    public SpotCollection getByFrame(double frame) {
        Iterator<Spot> iterator = iterator();

        SpotCollection to_return = new SpotCollection();

        while(iterator.hasNext()) {
            Spot next = iterator.next();
            if (next.getFrame() == frame) {
                to_return.add(next);
            }
        }

        return to_return;

    }

    public void add(Spot ref) {
        spots.add(ref);

    }

    public void remove(Spot ref) {
        spots.remove(ref);

    }

    public void deleteByID(double ID) {
        SpotCollection to_delete = getByID(ID);

        Iterator<Spot> iterator = to_delete.iterator();

        while (iterator.hasNext()) {
            remove(iterator.next());

        }
    }

    public void deleteByFrame(double frame) {
        SpotCollection to_delete = getByFrame(frame);

        Iterator<Spot> iterator = to_delete.iterator();

        while (iterator.hasNext()) {
            remove(iterator.next());

        }
    }

    public void deleteAll() {
        Iterator<Spot> iterator = iterator();

        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();

        }

    }

    public SpotCollection getByType(int type, Object val) {
        Iterator<Spot> iterator = iterator();

        SpotCollection to_return = new SpotCollection();

        while(iterator.hasNext()) {
            Spot next = iterator.next();

            if (next.getVal(type).equals(val)) {
                to_return.add(next);

            }
        }

        return to_return;

    }

    public void deleteByType(int type, Object val) {
        SpotCollection to_delete = getByType(type, val);
        Iterator<Spot> iterator = to_delete.iterator();

        while(iterator.hasNext()) {
            remove(iterator.next());

        }

    }
}
