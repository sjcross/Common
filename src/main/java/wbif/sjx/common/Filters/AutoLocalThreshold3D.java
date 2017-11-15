package wbif.sjx.common.Filters;

import ij.IJ;
import ij.ImageJ;
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
    public static final String BERNSEN = "Bernsen";
    public static final String MEAN = "Mean";
    public static final String PHANSALKAR = "Phansalkar";

    public static void main(String[] args) {
        new ImageJ();
        IJ.runMacro("waitForUser");

        ImagePlus ipl = IJ.getImage();

        new AutoLocalThreshold3D().Phansalkar(ipl,25,25,3,0,0,true);
        ipl.show();

    }

    public void exec(ImagePlus ipl, String myMethod, int radiusXY, int radiusZ, double thrMult, double par1, double par2, boolean doIwhite) {
        switch(myMethod) {
            case BERNSEN:
                Bernsen(ipl,radiusXY,radiusZ,thrMult,par1,par2,doIwhite);
                break;

            case MEAN:
                Mean(ipl,radiusXY,radiusZ,thrMult,par1,par2,doIwhite);
                break;

            case PHANSALKAR:
                Phansalkar(ipl,radiusXY,radiusZ,thrMult,par1,par2,doIwhite);
                break;

        }
    }

    void Bernsen(ImagePlus ipl, int radiusXY,  int radiusZ, double thrMult, double par1, double par2, boolean doIwhite ) {
        // Bernsen recommends WIN_SIZE = 31 and CONTRAST_THRESHOLD = 15.
        //  1) Bernsen J. (1986) "Dynamic Thresholding of Grey-Level Images"
        //    Proc. of the 8th Int. Conf. on Pattern Recognition, pp. 1251-1255
        //  2) Sezgin M. and Sankur B. (2004) "Survey over Image Thresholding
        //   Techniques and Quantitative Performance Evaluation" Journal of
        //   Electronic Imaging, 13(1): 146-165
        //  http://citeseer.ist.psu.edu/sezgin04survey.html
        // Ported to ImageJ plugin from E Celebi's fourier_0.8 routines
        // This version uses a circular local window, instead of a rectagular one
        ImagePlus maxIpl, minIpl;
        int contrast_threshold=15;
        int local_contrast;
        int mid_gray;
        byte object;
        byte backg;
        int temp;

        if (par1!=0) contrast_threshold= (int) par1;

        if (doIwhite){
            object =  (byte) 0xff;
            backg =   (byte) 0;
        }
        else {
            object =  (byte) 0;
            backg =  (byte) 0xff;
        }

        IntensityMinMax.run(ipl,true);
        IJ.run(ipl,"8-bit",null);

        // Creating maximum image
        maxIpl = new Duplicator().run(ipl);
        maxIpl.setStack(Filters3D.filter(maxIpl.getImageStack(),Filters3D.MAX,radiusXY,radiusXY,radiusZ));

        // Creating minimum image
        minIpl = new Duplicator().run(ipl);
        minIpl.setStack(Filters3D.filter(minIpl.getImageStack(),Filters3D.MIN,radiusXY,radiusXY,radiusZ));

        for (int z = 1; z <= ipl.getNSlices(); z++) {
            for (int c = 1; c <= ipl.getNChannels(); c++) {
                for (int t = 1; t <= ipl.getNFrames(); t++) {
                    ipl.setPosition(c, z, t);
                    maxIpl.setPosition(c, z, t);
                    minIpl.setPosition(c, z, t);

                    byte[] pixels = (byte[]) ipl.getProcessor().getPixels();
                    byte[] max = (byte[]) maxIpl.getProcessor().getPixels();
                    byte[] min = (byte[]) minIpl.getProcessor().getPixels();

                    for (int i = 0; i < pixels.length; i++) {
                        local_contrast = ((max[i] & 0xff) - (min[i] & 0xff));
                        mid_gray = ((min[i] & 0xff) + (max[i] & 0xff)) / 2;
                        temp = (pixels[i] & 0x0000ff);
                        if (local_contrast < contrast_threshold)
                            pixels[i] = (mid_gray >= 128) ? object : backg;  //Low contrast region
                        else
                            pixels[i] = (temp >= mid_gray) ? object : backg;
                    }
                }
            }
        }

        ipl.setPosition(1,1,1);

    }

    private void Mean(ImagePlus ipl, int radiusXY, int radiusZ,  double thrMult, double par1, double par2, boolean doIwhite ) {
        // See: Image Processing Learning Resourches HIPR2
        // http://homepages.inf.ed.ac.uk/rbf/HIPR2/adpthrsh.htm
        ImagePlus meanIpl;
        int c_value = 0;
        byte object;
        byte backg;

        if (par1!=0) c_value= (int)par1;

        if (doIwhite){
            object =  (byte) 0xff;
            backg =   (byte) 0;
        }
        else {
            object =  (byte) 0;
            backg =  (byte) 0xff;
        }

        IntensityMinMax.run(ipl,true);
        IJ.run(ipl,"8-bit",null);

        // Normalising the image
        meanIpl = new Duplicator().run(ipl);

        // Converting to 32-bit and normalising
        IJ.run(meanIpl,"32-bit",null);

        // Applying 3D mean filter to meanIpl
        meanIpl.setStack(Filters3D.filter(meanIpl.getImageStack(),Filters3D.MEAN,radiusXY,radiusXY,radiusZ));

        for (int z = 1; z <= ipl.getNSlices(); z++) {
            for (int c = 1; c <= ipl.getNChannels(); c++) {
                for (int t = 1; t <= ipl.getNFrames(); t++) {
                    ipl.setPosition(c, z, t);
                    meanIpl.setPosition(c, z, t);

                    byte[] pixels = (byte[]) ipl.getProcessor().getPixels();
                    float[] mean = (float[]) meanIpl.getProcessor().getPixels();

                    for (int i = 0; i < pixels.length; i++)
                        pixels[i] = ((pixels[i] & 0xff) > (int) (mean[i] - c_value)) ? object : backg;
                }
            }
        }

        ipl.setPosition(1,1,1);

    }

    private void Phansalkar(ImagePlus ipl, int radiusXY, int radiusZ, double thrMult, double par1, double par2, boolean doIwhite) {
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
