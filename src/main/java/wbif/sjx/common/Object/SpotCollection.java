package wbif.sjx.common.Object;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by sc13967 on 20/10/2016.
 */
public class SpotCollection extends HashSet<Spot> {
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

        while(iterator().hasNext()) {
            Spot next = iterator.next();
            if (next.getF() == frame) {
                to_return.add(next);
            }
        }

        return to_return;

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
