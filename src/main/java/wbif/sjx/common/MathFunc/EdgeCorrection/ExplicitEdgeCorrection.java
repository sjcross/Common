package wbif.sjx.common.MathFunc.EdgeCorrection;

public class ExplicitEdgeCorrection extends EdgeCorrection {
    public ExplicitEdgeCorrection(double minX, double maxX, double minY, double maxY) {
        super(minX, maxX, minY, maxY);
    }

    public double getCorrection(double x, double y, double r) {
        double d1 = y-minY; // Distance to bottom
        double d2 = x-minX; // Distance to left
        double d3 = maxY-y; // Distance to top
        double d4 = maxX-x; // Distance to right

        // If the point is entirely within the sample region, return 0; otherwise, calculate the interior fraction
        if (r<=d1 && r<=d2 && r<=d3 && r<=d4) return 1;

        int xMin = (int) Math.floor(x-r)-1;
        int xMax = (int) Math.ceil(x+r)+1;
        int yMin = (int) Math.floor(y-r)-1;
        int yMax = (int) Math.ceil(y+r)+1;

        double count = 0;
        double total = 0;
        for (int xx=xMin;xx<=xMax;xx++) {
            for (int yy=yMin;yy<=yMax;yy++) {
                double dist = Math.sqrt((xx-x)*(xx-x)+(yy-y)*(yy-y));
                if (dist < r) {
                    total++;
                    if (xx > minX && xx < maxX && yy > minY && yy < maxY) {
                        count++;
                    }
                }
            }
        }

        return total/count;

    }
}
