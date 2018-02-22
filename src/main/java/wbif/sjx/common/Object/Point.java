package wbif.sjx.common.Object;

import java.io.Serializable;

public class Point<T extends Number> implements Comparable<Point<T>>, Serializable {
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


    @Override
    public int compareTo(Point o) {
        Point<T> p2 = (Point<T>) o;

        double x1 = x.doubleValue();
        double x2 = p2.getX().doubleValue();
        double y1 = y.doubleValue();
        double y2 = p2.getY().doubleValue();
        double z1 = z.doubleValue();
        double z2 = p2.getZ().doubleValue();

        if (x1 > x2) {
            return 1;
        } else if (x1 < x2) {
            return -1;
        } else {
            if (y1 > y2) {
                return 1;
            } else if (y1 < y2) {
                return -1;
            } else {
                if (z1 > z2) {
                    return 1;
                } else if (z1 < z2){
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}