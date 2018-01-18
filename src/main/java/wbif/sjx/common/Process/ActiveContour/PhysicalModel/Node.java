package wbif.sjx.common.Process.ActiveContour.PhysicalModel;

public class Node
{
    public static int TOPLEFT = 1;
    public static int BOTTOMLEFT = 2;
    public static int TOPRIGHT = 3;
    public static int BOTTOMRIGHT = 4;

    private Node top_neighbour = null;
    private Node bottom_neighbour = null;
    private Node left_neighbour = null;
    private Node right_neighbour = null;
    private boolean fixed_x = false;
    private boolean fixed_y = false;
    private double x;
    private double y;
    private double energy = 0;
    private int ID; // Specific to that node
    private int special = 0; //No special state
    private boolean moved = false; // Condition that can be used to check if the Node moved last time

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        this.ID = 0;
    }

    public Node(double x, double y, int ID) {
        this.x = x;
        this.y = y;
        this.ID = ID;

    }

    public void setX(double x) {
        if (!fixed_x) this.x = x;

    }

    public void setY(double y) {
        if (!fixed_y) this.y = y;

    }

    public void setXY(double x, double y) {
        if (!fixed_x) this.x = x;
        if (!fixed_y) this.y = y;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setFixedX(boolean fixed_x) {
        this.fixed_x = fixed_x;

    }

    public void setFixedY(boolean fixed_y) {
        this.fixed_y = fixed_y;

    }

    public void setTopNeighbour(Node top_neighbour) {
        this.top_neighbour = top_neighbour;

    }

    public void setBottomNeighbour(Node bottom_neighbour) {
        this.bottom_neighbour = bottom_neighbour;

    }

    public void setLeftNeighbour(Node left_neighbour) {
        this.left_neighbour = left_neighbour;

    }

    public void setRightNeighbour(Node right_neighbour) {
        this.right_neighbour = right_neighbour;

    }

    public void setSpecial(int special) {
        this.special = special;

    }

    public double getX() {
        return x;

    }

    public double getY() {
        return y;

    }

    public Node getTopNeighbour() {
        return top_neighbour;

    }

    public Node getBottomNeighbour() {
        return bottom_neighbour;

    }

    public Node getLeftNeighbour() {
        return left_neighbour;

    }

    public Node getRightNeighbour() {
        return right_neighbour;

    }

    public boolean getFixedX() {
        return fixed_x;

    }

    public boolean getFixedY() {
        return fixed_y;

    }

    public int getSpecial() {
        return special;

    }

    public boolean isSpecial() {
        boolean is_special = false;

        if (special != 0) {
            is_special = true;

        }

        return is_special;

    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }
}
