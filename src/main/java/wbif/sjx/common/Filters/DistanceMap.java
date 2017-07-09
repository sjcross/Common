package wbif.sjx.common.Filters;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;

/**
 * Created by sc13967 on 07/06/2017.
 */
public class DistanceMap implements ExtendedPlugInFilter{
    private int flags = DOES_8G | DOES_STACKS | PARALLELIZE_STACKS;

    public static void main(String[] args) {
        // Loading a new ImageJ, so an image can be opened
        new ImageJ();
        IJ.runMacro("waitForUser");

        // Getting the ImagePlus and running the plugin
        ImagePlus ipl = IJ.getImage();
        new DistanceMap().run(ipl.getProcessor());

        ipl.updateAndDraw();

    }

    @Override
    public int showDialog(ImagePlus imagePlus, String s, PlugInFilterRunner plugInFilterRunner) {
        return flags;

    }

    @Override
    public void setNPasses(int i) {

    }

    @Override
    public int setup(String s, ImagePlus imagePlus) {
        IJ.register(DistanceMap.class);
        return flags;

    }

    @Override
    public void run(ImageProcessor iprOrig) {
        ImageProcessor distMap = iprOrig.duplicate();

        // Running through each pixel in the image
        for (int x=0;x<iprOrig.getWidth();x++) {
            for (int y=0;y<iprOrig.getHeight();y++) {
                // Checking the current pixel isn't background
                if (iprOrig.get(x,y) != 0) {
                    // Checking if this pixel is on the edge.  If not, looking at the two neighbours from the distance map
                    if (x-1>=0) {
                        if (iprOrig.get(x - 1, y) == 0) {
                            distMap.set(x, y, 1);
                            checkNeighbours(distMap, x, y);
                            continue;

                        }
                    }

                    if (x+1<iprOrig.getWidth()-1) {
                        if (iprOrig.get(x+1,y)==0) {
                            distMap.set(x, y, 1);
                            checkNeighbours(distMap, x, y);
                            continue;

                        }
                    }

                    if (y-1>=0) {
                        if (iprOrig.get(x,y-1)==0) {
                            distMap.set(x, y, 1);
                            checkNeighbours(distMap, x, y);
                            continue;

                        }
                    }

                    if (y+1<iprOrig.getHeight()-1) {
                        if (iprOrig.get(x,y+1)==0) {
                            distMap.set(x, y, 1);
                            checkNeighbours(distMap, x, y);
                            continue;

                        }
                    }

                    //  The current value is one more than its neighbours
                    if (x-1 < 0 & y-1 < 0) {
                        distMap.set(x,y,1);

                    } else if (x-1 < 0 & y-1 >= 0) {
                        distMap.set(x,y,distMap.get(x, y-1));

                    } else if (x-1 >= 0 & y-1 < 0) {
                        distMap.set(x,y,distMap.get(x-1, y));

                    } else {
                        distMap.set(x, y, Math.min(distMap.get(x - 1, y), distMap.get(x, y - 1)) + 1);

                    }

                } else {
                    distMap.set(x,y,0);

                }
            }
        }

        // Assigning the distance map pixels to the input ImageProcessor
//        iprOrig.setPixels(distMap.getPixels());

    }

    private void checkNeighbours(ImageProcessor distMap, int x, int y) {
        // Checking if the pixel's neighbours are more than 1 larger than the current pixel.  If they are, correcting 
        // their value and testing their neighbours.
        int val = distMap.get(x, y);

        if (x-1 >= 0 & x+1<distMap.getWidth()-1 ) {
            if (distMap.get(x-1,y)>val+1) {
                distMap.set(x-1,y,val+1);
                checkNeighbours(distMap,x-1,y);

            }
        }

        if (x+1<distMap.getWidth()-1) {
            if (distMap.get(x + 1, y) > val + 1) {
                distMap.set(x + 1, y, val + 1);
                checkNeighbours(distMap, x + 1, y);

            }
        }

        if (y-1>=0) {
            if (distMap.get(x, y - 1) > val + 1) {
                distMap.set(x, y - 1, val + 1);
                checkNeighbours(distMap, x, y - 1);

            }
        }

        if (y+1<distMap.getHeight()-1) {
            if (distMap.get(x, y + 1) > val + 1) {
                distMap.set(x, y + 1, val + 1);
                checkNeighbours(distMap, x, y + 1);

            }
        }
    }
}
