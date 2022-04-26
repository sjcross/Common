clear

im = imread('C:\Users\sc13967\Documents\Java Projects\Common\src\test\resources\images\NoisyGradient\NoisyGradient2D_8bit.tif');
glcm = graycomatrix(im,'NumLevels',8);
stats = graycoprops(glcm,'all')