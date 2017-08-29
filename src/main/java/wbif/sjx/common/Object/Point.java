package wbif.sjx.common.Object;

public class Point<T extends Number> {
    T x;
    T y;
    T z;
    int f;

    public Point(T x, T y, T z, int f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;

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

    public int getF() {
        return f;
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

    public void setF(int f) {
        this.f = f;
    }
}