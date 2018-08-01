package wbif.sjx.common.MathFunc.EdgeCorrection;

public abstract class EdgeCorrection {
    final double minX;
    final double maxX;
    final double minY;
    final double maxY;

    public EdgeCorrection(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public abstract double getCorrection(double x, double y, double r);
}
