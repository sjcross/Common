package wbif.sjx.common.Filters;

import ij.ImagePlus;

/**
 * Created by sc13967 on 17/01/2018.
 */
public class RidgeEnhancement {
    // Need to generate 1D zero-order and second-order Gaussians
    // Combine Gaussians into a 2D array
    // Do convolution using 2D array

    public static void run(ImagePlus ipl, double sigma, boolean stack) {
        // Generating 1D zero-order Gaussian distribution

    }

    public static int[] getGaussian(double sigma, int order) {
        int nElements = (int) Math.round(sigma)*5;
        int[] gauss = new int[nElements];

        for (int i=0;i<nElements;i++) {

        }

        return null;

    }
}


// function [mask] = computeGaussMask(mode,sigma)
//
// N = rdGaussN(mode,sigma);
// mask = zeros(2*N+1,1);
//
// for i=1:2*N+1
//     mask(i) = rdGauss(mode,-(i-N)+0.5,sigma) - rdGauss(mode,-(i-N)-0.5,sigma);
// end

// function phi = rdGauss(mode, x, sigma)
//
// if mode == 0
//    phi = normcdf(x/sigma);
// elseif mode == 1
//    phi = 1/(sqrt(2*pi)*sigma)*exp(-x^2/(2*sigma^2));
// elseif mode == 2
//    phi = -x/(sqrt(2*pi)*sigma^3)*exp(-x^2/(2*sigma^2));
// end

//    function N = rdGaussN(mode,sigma)
//
//    err_lim = 1E-4;
//    N=0;
//    sum_gauss = 0;
//    val = 1;
//
//    while 2*val/(sum_gauss+val) > err_lim
//        N = N + 1;
//        if mode == 0
//            val = abs(1 - rdGauss(mode,N,sigma));
//        else
//            val = abs(rdGauss(mode,N,sigma));
//        end
//        sum_gauss = sum_gauss + val;
//    end