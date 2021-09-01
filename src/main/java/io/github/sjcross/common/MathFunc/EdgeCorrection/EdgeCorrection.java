package io.github.sjcross.common.MathFunc.EdgeCorrection;

public abstract class EdgeCorrection {
    final double minX;
    final double maxX;
    final double minY;
    final double maxY;
    final double minZ;
    final double maxZ;
    final boolean is2D;


    public EdgeCorrection(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, boolean is2D) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.is2D = is2D;
    }

    public EdgeCorrection(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = 0;
        this.maxZ = 0;
        this.is2D = true;
    }

    public abstract double getCorrection(double x, double y, double r);

    public abstract double getCorrection(double x, double y, double z, double r);
}
