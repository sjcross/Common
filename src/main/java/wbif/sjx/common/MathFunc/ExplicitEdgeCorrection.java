package wbif.sjx.common.MathFunc;

public class ExplicitEdgeCorrection {
    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;


    public ExplicitEdgeCorrection(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public double getFractionInsideRectangle(double x, double y, double r) {
        double d1 = y-minY; // Distance to bottom
        double d2 = x-minX; // Distance to left
        double d3 = maxY-y; // Distance to top
        double d4 = maxX-x; // Distance to right

        // If the point is entirely within the sample region, return 0; otherwise, calculate the interior fraction
        if (r<=d1 && r<=d2 && r<=d3 && r<=d4) return 1;
        return 1/calculateInteriorFraction(x,y,r);

    }

    private double calculateInteriorFraction(double x, double y, double r) {
        int xMin = (int) Math.floor(x-r)-1;
        int xMax = (int) Math.ceil(x+r)+1;
        int yMin = (int) Math.floor(y-r)-1;
        int yMax = (int) Math.ceil(y+r)+1;

        double count = 0;
        double total = 0;
        for (int xx=xMin;xx<=xMax;xx++) {
            for (int yy=yMin;yy<=yMax;yy++) {
                double dist = Math.sqrt((xx-x)*(xx-x)+(yy-y)*(yy-y));
                if (dist < r-0.5) {
                    total++;
                    if (xx > minX-0.5 && xx < maxX+0.5 && yy > minY-0.5 && yy < maxY+0.5) {
                        count++;
                    }
                }
            }
        }

        return count/total;

    }
}
