package wbif.sjx.common.Object;

import java.util.HashSet;

/**
 * Created by sc13967 on 25/01/2018.
 */
public class Vertex extends Point<Integer> {
    private HashSet<Vertex> neighbours = new HashSet<>();

    public Vertex(Integer x, Integer y, Integer z) {
        super(x, y, z);
    }

    public int getNumberOfNeighbours() {
        return neighbours.size();
    }

    public void addNeighbour(Vertex neighbour) {
        neighbours.add(neighbour);
    }

    public void removeNeighbour(Vertex neighbour) {
        neighbours.remove(neighbour);
    }

    public HashSet<Vertex> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(HashSet<Vertex> neighbours) {
        this.neighbours = neighbours;
    }

    public double getEdgeLength(Vertex vertex) {
        int x1 = getX();
        int x2 = vertex.getX();
        int y1 = getY();
        int y2 = vertex.getY();
        int z1 = getZ();
        int z2 = vertex.getZ();

        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) + (z2-z1)*(z2-z1));

    }

}
