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
}