package wbif.sjx.common.Filters;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.plugin.Filters3D;
import ij.plugin.PlugIn;
import ij.process.ImageConverter;
import wbif.sjx.common.Process.IntensityMinMax;

/**
 * Created by sc13967 on 14/11/2017.
 */
public class AutoLocalThreshold3D  implements PlugIn {
    private static final String PHANSALKAR = "Phansalkar";

    public void exec(ImagePlus ipl, String myMethod, int radiusXY, int radiusZ, double thrMult, double par1, double par2, boolean doIwhite) {
        switch(myMethod) {
            case PHANSALKAR:
                Phansalkar(ipl,radiusXY,radiusZ,thrMult,par1,par2,doIwhite);
                break;

        }
    }

    void Phansalkar(ImagePlus ipl, int radiusXY, int radiusZ, double thrMult, double par1, double par2, boolean doIwhite) {
        // Setting parameters (from Auto_Local_Threshold)
        ImagePlus meanIpl, varIpl, oriIpl;
        double k_value = 0.25;
        double r_value = 0.5;
        double p_value = 2.0;
        double q_value = 10.0;
        byte object;
        byte backg;

        if (par1!=0) k_value= par1;

        if (par2!=0)  r_value= par2;

        if (doIwhite){
            object =  (byte) 0xff;
            backg =   (byte) 0;
        } else {
            object =  (byte) 0;
            backg =  (byte) 0xff;
        }

        IntensityMinMax.run(ipl,true);
        IJ.run(ipl,"8-bit",null);

        // Normalising the image
        oriIpl = new Duplicator().run(ipl);

        // Converting to 32-bit and normalising
        IJ.run(oriIpl,"32-bit",null);
        ImageConverter ic = new ImageConverter(oriIpl);
        ic.convertToGray32();
        for (int z = 1; z <= oriIpl.getNSlices(); z++) {
            for (int c = 1; c <= oriIpl.getNChannels(); c++) {
                for (int t = 1; t <= oriIpl.getNFrames(); t++) {
                    oriIpl.setPosition(c, z, t);
                    oriIpl.getProcessor().multiply(1.0/255);

                }
            }
        }
        oriIpl.setPosition(1,1,1);

        // Duplicating oriIpl for calculation of mean and variance
        meanIpl = new Duplicator().run(oriIpl);
        varIpl = new Duplicator().run(oriIpl);

        // Applying 3D mean filter to meanIpl
        meanIpl.setStack(Filters3D.filter(meanIpl.getImageStack(),Filters3D.MEAN,radiusXY,radiusXY,radiusZ));

        // Applying 3D variance filter to varIpl
        varIpl.setStack(Filters3D.filter(varIpl.getImageStack(),Filters3D.VAR,radiusXY,radiusXY,radiusZ));

        for (int z = 1; z <= ipl.getNSlices(); z++) {
            for (int c = 1; c <= ipl.getNChannels(); c++) {
                for (int t = 1; t <= ipl.getNFrames(); t++) {
                    ipl.setPosition(c, z, t);
                    oriIpl.setPosition(c, z, t);
                    meanIpl.setPosition(c, z, t);
                    varIpl.setPosition(c, z, t);

                    byte[] pixels = (byte []) ipl.getProcessor().getPixels();
                    float[] ori = (float []) oriIpl.getProcessor().getPixels();
                    float[] mean = (float []) meanIpl.getProcessor().getPixels();
                    float[] var = (float []) varIpl.getProcessor().getPixels();

                    for (int i=0; i<pixels.length; i++)
                        pixels[i] = ( (ori[i]) > thrMult*( mean[i] * (1.0 + p_value * Math.exp(-q_value * mean[i]) + k_value * (( Math.sqrt(var[i]) / r_value)- 1.0)))) ? object : backg;

                }
            }
        }

        ipl.setPosition(1,1,1);

    }

    @Override
    public void run(String s) {

    }
}
