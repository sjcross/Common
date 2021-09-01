package io.github.sjcross.common.Process.ActiveContour;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import io.github.sjcross.common.Process.ActiveContour.Energies.*;
import io.github.sjcross.common.Process.ActiveContour.Minimisers.GreedyMinimiser;
import io.github.sjcross.common.Process.ActiveContour.PhysicalModel.NodeCollection;
import io.github.sjcross.common.Process.ActiveContour.Visualisation.GridOverlay;
import io.github.sjcross.common.Process.IntensityMinMax;

import java.awt.*;

/**
 * Created by Stephen on 08/09/2016.
 */
public class ActiveContour {
    public static void main(String[] args) throws InterruptedException {
        new ImageJ();
        IJ.runMacro("waitForUser");

        ImagePlus ipl = IJ.getImage();
        Roi roi = ipl.getRoi();

        Polygon roiPoly = roi.getPolygon();
        int[] xCoords = roiPoly.xpoints;
        int[] yCoords = roiPoly.ypoints;

        // Reducing the number of points per contour
        int div = 5;
        int[] xCoordsSub = new int[(int) Math.floor(xCoords.length/div)];
        int[] yCoordsSub = new int[(int) Math.floor(yCoords.length/div)];

        for (int i=0;i<xCoordsSub.length;i++) {
            xCoordsSub[i] = xCoords[i*div];
            yCoordsSub[i] = yCoords[i*div];
        }

        // Initialising the contour
        NodeCollection nodes = ContourInitialiser.buildContour(xCoordsSub,yCoordsSub);

        //Assigning energies
        EnergyCollection energies = new EnergyCollection();
        energies.add(new ElasticEnergy(100));
        energies.add(new BendingEnergy(1));
        energies.add(new PathEnergy(1,ipl));

        GreedyMinimiser greedy = new GreedyMinimiser(energies);
        greedy.setWidth(10);
        greedy.setSequence(GreedyMinimiser.RANDOM);

        GridOverlay gridOverlay = new GridOverlay();
        gridOverlay.setNodeRadius(2);

        ipl.deleteRoi();
        ImagePlus dispIpl = new Duplicator().run(ipl);
        IntensityMinMax.run(dispIpl,true);
        dispIpl.show();

        for (int i=0;i<1000;i++) {
            greedy.evaluateGreedy(nodes);
            if (nodes.getAverageDistanceMoved() < 0.01) break;
            gridOverlay.drawOverlay(nodes, dispIpl);
        }

        Roi newRoi = nodes.getROI();
        Overlay overlay = new Overlay(newRoi);
        ipl.setOverlay(overlay);

    }
}
