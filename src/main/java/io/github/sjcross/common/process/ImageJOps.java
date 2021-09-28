package io.github.sjcross.common.process;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;
import ij.measure.ResultsTable;
import ij.process.ImageProcessor;

public class ImageJOps 
{
	public static double[] getColumn(ResultsTable rt, int col_num) {

		double[] col = new double[rt.size()];

		for (int i=0;i<rt.size();i++) {
			col[i] = Double.valueOf(rt.getStringValue(col_num, i));
		}

		return col;
	}

	public static double[] getColumn(ResultsTable rt, String col_name) {
		int col_num = rt.getColumnIndex(col_name);

		double[] col = new double[rt.size()];

		for (int i=0;i<rt.size();i++) {
			col[i] = Double.valueOf(rt.getStringValue(col_num, i));
		}

		return col;
	}

	public static double[] getColumn(ResultsTable rt, String col_name, double conv) {
		int col_num = rt.getColumnIndex(col_name);

		double[] col = new double[rt.size()];

		for (int i=0;i<rt.size();i++) {
			col[i] = Double.valueOf(rt.getStringValue(col_num, i));
		}

		col = ImageJOps.calToPixelDistance(col, conv);

		return col;
	}
	
	public static String[] getColumnString(ResultsTable rt, String col_name) {
		int col_num = rt.getColumnIndex(col_name);

		String[] col = new String[rt.size()];

		for (int i=0;i<rt.size();i++) {
			col[i] = rt.getStringValue(col_num, i);
		}

		return col;
	}
		
	public static int getNumberMatching(ResultsTable rt, int col_num, String tar) {
		int n = 0;
		
		for (int i=0;i<rt.size();i++){
			if (rt.getStringValue(col_num, i)==tar) {
				n++;
			}			
		}

		return n;
	}

	public static ImagePlus arrayToImagePlus(double[] arr) {
		ImagePlus im_pl = NewImage.createFloatImage("Histogram",arr.length, 1, 1, NewImage.FILL_BLACK);
		ImageProcessor im_pro = im_pl.getProcessor();
		for (int i=0;i<arr.length;i++) {
			im_pro.putPixelValue(i, 0, arr[i]);
		}
		return im_pl;
	}

	public static ImagePlus arrayListToImagePlus(ArrayList<Double> arr) {
		ImagePlus im_pl = NewImage.createFloatImage("Histogram",arr.size(), 1, 1, NewImage.FILL_BLACK);
		ImageProcessor im_pro = im_pl.getProcessor();
		for (int i=0;i<arr.size();i++) {
			im_pro.putPixelValue(i, 0, arr.get(i));
		}
		return im_pl;
	}

	public static double[] calToPixelDistance(double[] arr, double dist_per_px) {
		double[] arr_out = new double[arr.length];

		for (int i=0;i<arr.length;i++) {
			arr_out[i] = arr[i]/dist_per_px;
		}

		return arr_out;
	}

	public static double[] measureIntensityTrace(ImagePlus ip, double[] posx, double[] posy, double[] posf, double[] posr) {
		ip.setT((int) posf[0]);
		int im_w = ip.getWidth();
		int im_h = ip.getHeight();

		double[] arr_i = new double[posx.length];

		for (int i=0;i<posf.length;i++) {
			ip.setT((int) posf[i]);
			ArrayList<Integer> temp_al = new ArrayList<Integer>();

			for (double xx=-posr[i];xx<=posr[i];xx++) {
				for (double yy=-posr[i];yy<=posr[i];yy++) {
					if (posx[i]+xx>=0 & posx[i]+xx<im_w & posy[i]+yy>=0 & posy[i]+yy<im_h & Math.sqrt(xx*xx+yy*yy)<=posr[i]) {
						int[] temp1 = ip.getPixel((int) (posx[i]+xx), (int) (posy[i]+yy));
						temp_al.add((Integer) temp1[0]);
					}    				
				}    			
			}

			double[] temp_ar = new double[temp_al.size()];
			for (int j=0;j<temp_ar.length;j++) {
				temp_ar[j] = temp_al.get(j);
			}
			arr_i[i] = new Mean().evaluate(temp_ar);
		}

		return arr_i;
	}

	public static ImagePlus addStackToHyperStack(ImagePlus hs_in, ImageStack ist, int t) {
		
		ImageProcessor hs_ipr = hs_in.getProcessor();

		for (int s=1;s<=ist.getSize();s++) {
			hs_in.setPositionWithoutUpdate(1,s,t);
			ImageProcessor ipl_ipr = ist.getProcessor(s);
			for (int y=0;y<ist.getHeight();y++) {
				for (int x=0;x<ist.getWidth();x++) {
					hs_ipr.putPixel(x, y, ipl_ipr.getPixel(x, y));
				}
			}
		}
				
		return hs_in;
	}

}