package wbif.sjx.common.HighContent.Object;

import ij.IJ;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.GUI.ParameterWindow;
import wbif.sjx.common.HighContent.Module.*;
import wbif.sjx.common.HighContent.Process.Analysis;
import wbif.sjx.common.HighContent.Process.Exporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created by sc13967 on 03/05/2017.
 */
public class TestAnalysis implements Analysis {
    @Override
    public void initialise(Workspace workspace) {
        // Getting parameter and module stores
        ParameterCollection parameters = workspace.getParameters();
        ModuleCollection modules = workspace.getModules();

        // Initialising variable names for images and objects
        ImageName Stack1 = new ImageName("C1");
        ImageName Stack2 = new ImageName("C2");
        HCObjectName NucleiObjs = new HCObjectName("Nuclei");
        HCObjectName NucleiObjsProj = new HCObjectName("NucleiProj");
        HCObjectName CellObjsProj = new HCObjectName("CellsProj");

        // Initialising image stack loader for channel 1
        ImageStackLoader stackLoader1 = new ImageStackLoader();
        stackLoader1.initialiseParameters(parameters);
        parameters.addParameter(stackLoader1,ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor(),false);
        parameters.addParameter(stackLoader1,ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Result.WELL,Result.FIELD)),false);
        parameters.addParameter(stackLoader1,ImageStackLoader.OUTPUT_IMAGE,Stack1,false);
        HashMap<String,String> setFields1 = new HashMap<>();
        setFields1.put(Result.CHANNEL,"1");
        parameters.addParameter(stackLoader1,ImageStackLoader.SET_FIELDS,setFields1,false);
        modules.add(stackLoader1);

        // Initialising image stack loader for channel 2
        ImageStackLoader stackLoader2 = new ImageStackLoader();
        stackLoader2.initialiseParameters(parameters);
        parameters.addParameter(stackLoader2,ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor(),false);
        parameters.addParameter(stackLoader2,ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Result.WELL,Result.FIELD)),false);
        parameters.addParameter(stackLoader2,ImageStackLoader.OUTPUT_IMAGE,Stack2,false);
        HashMap<String,String> setFields2 = new HashMap<>();
        setFields2.put(Result.CHANNEL,"2");
        parameters.addParameter(stackLoader2,ImageStackLoader.SET_FIELDS,setFields2,false);
        modules.add(stackLoader2);

        // Initialising primary object identification
        IdentifyPrimaryObjects objIdent1 = new IdentifyPrimaryObjects();
        objIdent1.initialiseParameters(parameters);
        parameters.addParameter(objIdent1,IdentifyPrimaryObjects.INPUT_IMAGE,Stack1,false);
        parameters.addParameter(objIdent1,IdentifyPrimaryObjects.OUTPUT_OBJECT,NucleiObjs,false);
        modules.add(objIdent1);

        // Creating Z-projected nuclei
        ProjectObjects projectObjects = new ProjectObjects();
        parameters.addParameter(projectObjects,ProjectObjects.INPUT_OBJECTS,NucleiObjs,false);
        parameters.addParameter(projectObjects,ProjectObjects.OUTPUT_OBJECTS,NucleiObjsProj,false);
        modules.add(projectObjects);

        // Displaying the first channel
        ShowImage showImage1 = new ShowImage();
        parameters.addParameter(showImage1,ShowImage.DISPLAY_IMAGE,Stack1,false);
        modules.add(showImage1);

        // Displaying the first channel
        ShowImage showImage2 = new ShowImage();
        parameters.addParameter(showImage2,ShowImage.DISPLAY_IMAGE,Stack2,false);
        modules.add(showImage2);

        // Displaying 3D nuclei
        ShowObjects showObjects3D = new ShowObjects();
        parameters.addParameter(showObjects3D,ShowObjects.INPUT_OBJECTS,NucleiObjs,false);
        modules.add(showObjects3D);

        // Displaying 2D nuclei
        ShowObjects showObjects2D = new ShowObjects();
        parameters.addParameter(showObjects2D,ShowObjects.INPUT_OBJECTS,NucleiObjsProj,false);
        modules.add(showObjects2D);

        /////// NEED TO CREATE CLASS TO Z-PROJECT IMAGES /////////

        // Secondary object identification
        IdentifySecondaryObjects objIdent2 = new IdentifySecondaryObjects();
        objIdent2.initialiseParameters(parameters);
        parameters.addParameter(objIdent2,IdentifySecondaryObjects.INPUT_IMAGE,Stack2,false); // Image needs to be Z-projected
        parameters.addParameter(objIdent2,IdentifySecondaryObjects.INPUT_OBJECTS,NucleiObjsProj,false);
        parameters.addParameter(objIdent2,IdentifySecondaryObjects.OUTPUT_OBJECTS,CellObjsProj,false);


        // Displaying settings for parameters
        new ParameterWindow().updateParameters(parameters);

    }

    @Override
    public ResultCollection execute(Workspace workspace) {
        ResultCollection results = new ResultCollection();

        // Running through modules
        for (Module module:workspace.getModules()) {
            module.execute(workspace);
        }

        IJ.runMacro("waitForUser");

        ParameterCollection parameters = workspace.getParameters();
        ModuleCollection modules = workspace.getModules();
        HCObjectName nucName = (HCObjectName) parameters.getParameter(modules.get(3),ProjectObjects.INPUT_OBJECTS);
        HCObjectName nucProjName = (HCObjectName) parameters.getParameter(modules.get(3),ProjectObjects.OUTPUT_OBJECTS);

        ArrayList<HCObject> nucs = workspace.getObjects().get(nucName);
        ArrayList<HCObject> nucProjs = workspace.getObjects().get(nucProjName);

        System.out.println("Nuclei 3D");
        for (HCObject nuc:nucs) {
            System.out.println(nuc.toString());
        }

        System.out.println("Nuclei 2D");
        for (HCObject nucProj:nucProjs) {
            System.out.println(nucProj.toString());
        }

        return results;
    }

    @Override
    public Exporter getExporter() {
        return null;
    }

}
