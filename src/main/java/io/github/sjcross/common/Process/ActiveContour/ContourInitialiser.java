package io.github.sjcross.common.Process.ActiveContour;

import io.github.sjcross.common.Process.ActiveContour.PhysicalModel.Vertex;
import io.github.sjcross.common.Process.ActiveContour.PhysicalModel.NodeCollection;

/**
 * Created by sc13967 on 31/01/2017.
 */
public class ContourInitialiser {
    public static NodeCollection buildContour(int[] xCoords, int[] yCoords){
        Vertex[] nodeArr = new Vertex[xCoords.length];

        //Adding Nodes to Vertex array
        for (int i = 0;i<xCoords.length;i++) {
            nodeArr[i] = new Vertex(xCoords[i],yCoords[i],i);

        }

        //Adding node links
        for (int i=0;i<nodeArr.length;i++) {
            nodeArr[i].setTopNeighbour(null);
            nodeArr[i].setBottomNeighbour(null);

            if (i == 0) {
                nodeArr[i].setLeftNeighbour(nodeArr[nodeArr.length-1]);
                nodeArr[i].setRightNeighbour(nodeArr[i+1]);

            } else if (i == nodeArr.length-1) {
                nodeArr[i].setLeftNeighbour(nodeArr[i-1]);
                nodeArr[i].setRightNeighbour(nodeArr[0]);

            } else {
                nodeArr[i].setLeftNeighbour(nodeArr[i-1]);
                nodeArr[i].setRightNeighbour(nodeArr[i+1]);

            }
        }

        //Adding Nodes to NodeCollection
        NodeCollection nodes = new NodeCollection();
        for (int i=0;i<nodeArr.length;i++) {
            nodes.add(nodeArr[i]);
        }

        return nodes;

    }
}
