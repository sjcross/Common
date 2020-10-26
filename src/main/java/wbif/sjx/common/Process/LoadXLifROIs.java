package wbif.sjx.common.Process;

import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ij.gui.PolygonRoi;
import ij.plugin.frame.PlugInFrame;
import ij.plugin.frame.RoiManager;

/**
 * Created by sc13967 on 03/11/2017.
 */
public class LoadXLifROIs extends PlugInFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1023989637738977899L;

    public LoadXLifROIs() {
        super("LoadXLifROIs");
    }

//    public static void main (String[] args) {
//        Class<?> clazz = LoadXLifROIs.class;
//        new ImageJ();
//
//        IJ.runPlugIn(clazz.getName(), "");
//
//    }

    public void run(String arg) {
        try {
            RoiManager roiManager = new RoiManager();

            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.showOpenDialog(this);

            FileInputStream  fis = new FileInputStream(jfc.getSelectedFile());

            // Initialising the XML reading
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fis);
            doc.getDocumentElement().normalize();

            // Getting parameters necessary for converting ROI coordinates to pixel values
            Element dimensionsEl = (Element) doc.getElementsByTagName("Dimensions").item(0);
            Element dimensionDescriptionEl = (Element) dimensionsEl.getElementsByTagName("DimensionDescription").item(0);
            float frameWidth = Float.parseFloat(dimensionDescriptionEl.getAttribute("Length"));

            Element settingsEl = (Element) doc.getElementsByTagName("ATLConfocalSettingDefinition").item(0);
            float imageWidthPx = Float.parseFloat(settingsEl.getAttribute("InDimension"));
            float distXY = frameWidth/(imageWidthPx-1);
            distXY = distXY*1E6f;

            float panX = Float.parseFloat(settingsEl.getAttribute("PanFirstDim"));
            panX = (panX/63)*1E6f/distXY;
            float panY = Float.parseFloat(settingsEl.getAttribute("PanSecondDim"));
            panY = (panY/63)*1E6f/distXY;

            // Loading each ROI
            Element roiEl = (Element) settingsEl.getElementsByTagName("ROI").item(0);
            Element childEl = (Element) roiEl.getElementsByTagName("Children").item(0);

            NodeList singleRoisList = childEl.getElementsByTagName("Element");

            for (int i = 0;i<singleRoisList.getLength();i++) {
                Element singleRoi = (Element) singleRoisList.item(i);

                // Getting translation and scaling values
                Element scalingEl = (Element) singleRoi.getElementsByTagName("Scaling").item(0);
                float scaleX = Float.parseFloat(scalingEl.getAttribute("XScale"));
                float scaleY = Float.parseFloat(scalingEl.getAttribute("YScale"));

                Element translationEl = (Element) singleRoi.getElementsByTagName("Translation").item(0);
                float translationX = Float.parseFloat(translationEl.getAttribute("X"));
                translationX = translationX*1E6f/distXY;

                float translationY = Float.parseFloat(translationEl.getAttribute("Y"));
                translationY = translationY*1E6f/distXY;

                // Getting vertex coordinates
                Element vertexEl = (Element) singleRoi.getElementsByTagName("Vertices").item(0);
                NodeList vertices = vertexEl.getElementsByTagName("P");

                float[] xx = new float[vertices.getLength()];
                float[] yy = new float[vertices.getLength()];

                for (int j=0;j<vertices.getLength();j++) {
                    Element pointEl = (Element) vertices.item(j);

                    float x = Float.parseFloat(pointEl.getAttribute("X"));
                    xx[j] = x*1E6f*scaleX/distXY+translationX+(imageWidthPx/2)-1+panX;

                    float y = Float.parseFloat(pointEl.getAttribute("Y"));
                    yy[j] = y*1E6f*scaleY/distXY+translationY+(imageWidthPx/2)-1-panY;

                }

                PolygonRoi roi = new PolygonRoi(xx,yy,PolygonRoi.POLYGON);
                roiManager.addRoi(roi);

            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }
}
