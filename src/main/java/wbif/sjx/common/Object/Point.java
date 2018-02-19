package wbif.sjx.common.Object;

import java.io.Serializable;

public class Point<T extends Number> implements Serializable {
    protected T x;
    protected T y;
    protected T z;

    public Point(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public T getZ() {
        return z;
    }

    public void setX(T x) {
        this.x = x;

    }

    public void setY(T y) {
        this.y = y;

    }

    public void setZ(T z) {
        this.z = z;

    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = 31*hash + x.hashCode();
        hash = 31*hash + y.hashCode();
        hash = 31*hash + z.hashCode();

        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Point)) return false;

        Point point = (Point) obj;
        return x.equals(point.x )&& y.equals(point.y) && z.equals(point.z);

    }
}