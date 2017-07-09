package wbif.sjx.common.Object;

public class Point {
    double x;
    double y;
    double z;
    int f;

    public Point(double x, double y, double z, int f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getF() {
        return f;
    }

    public void setX(double x) {
        this.x = x;

    }

    public void setY(double y) {
        this.y = y;

    }

    public void setZ(double z) {
        this.z = z;

    }

    public void setF(int f) {
        this.f = f;
    }
}