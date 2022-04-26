package io.github.sjcross.common.mathfunc.edgecorrection;

public class ExplicitEdgeCorrection extends EdgeCorrection {
    public ExplicitEdgeCorrection(double minX, double maxX, double minY, double maxY) {
        super(minX, maxX, minY, maxY);
    }

    public ExplicitEdgeCorrection(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, boolean is2D) {
        super(minX, maxX, minY, maxY, minZ, maxZ, is2D);
    }

    public double getCorrection(double x, double y, double r) {
        return getCorrection(x,y,0,r);

    }

    @Override
    public double getCorrection(double x, double y, double z, double r) {
        double d1 = y - minY; // Distance to bottom
        double d2 = x - minX; // Distance to left
        double d3 = z - minZ; // Distance to lower surface
        double d4 = maxY - y; // Distance to top
        double d5 = maxX - x; // Distance to right
        double d6 = maxZ - z; // Distance to upper surface

        // If the point is entirely within the sample region, return 0; otherwise, calculate the interior fraction
        if (is2D && r <= d1 && r <= d2 && r <= d4 && r <= d5) return 1;
        else if (r <= d1 && r <= d2 && r <= d3 && r <= d4 && r <= d5 && r <= d6) return 1;

        int xMin = (int) Math.floor(x-r);
        int xMax = (int) Math.ceil(x+r);
        int yMin = (int) Math.floor(y-r);
        int yMax = (int) Math.ceil(y+r);
        int zMin = is2D ? 0 : (int) Math.floor(z-r);
        int zMax = is2D ? 0 : (int) Math.ceil(z+r);

        double count = 0;
        double total = 0;
        for (int xx=xMin;xx<=xMax;xx++) {
            for (int yy = yMin; yy <= yMax; yy++) {
                for (int zz = zMin; zz <= zMax; zz++) {
                    double dist = Math.sqrt((xx - x) * (xx - x) + (yy - y) * (yy - y) + (zz - z) * (zz - z));
                    if (dist < r) {
                        total++;
                        if (xx >= minX && xx <= maxX && yy >= minY && yy <= maxY && zz >= minZ && zz <= maxZ) {
                            count++;
                        }
                    }
                }
            }
        }

        return total/count;
    }
}
