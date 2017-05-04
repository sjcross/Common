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
        ImageName NucleiImage3D = new ImageName("Nuclei 3D");
        ImageName CellImage3D = new ImageName("Cells 3D");
        ImageName NucleiImage2D = new ImageName("Nuclei 2D");
        ImageName CellImage2D = new ImageName("Cells 2D");

        HCObjectName NucleiObjs3D = new HCObjectName("Nuclei objects 3D");
        HCObjectName NucleiObjs2D = new HCObjectName("Nuclei objects 2D");
        HCObjectName CellObjs2D = new HCObjectName("Cell objects 2D");

        // Initialising image stack loader for channel 1
        ImageStackLoader stackLoader1 = new ImageStackLoader();
        stackLoader1.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(stackLoader1,Parameter.OBJECT,ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor(),false));
        parameters.addParameter(new Parameter(stackLoader1,Parameter.OBJECT,ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Result.WELL,Result.FIELD)),false));
        parameters.addParameter(new Parameter(stackLoader1,Parameter.OBJECT_NAME,ImageStackLoader.OUTPUT_IMAGE,NucleiImage3D,false));
        HashMap<String,String> setFields1 = new HashMap<>();
        setFields1.put(Result.CHANNEL,"1");
        parameters.addParameter(new Parameter(stackLoader1,Parameter.OBJECT,ImageStackLoader.SET_FIELDS,setFields1,false));
        modules.add(stackLoader1);

        // Initialising image stack loader for channel 2
        ImageStackLoader stackLoader2 = new ImageStackLoader();
        stackLoader2.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(stackLoader2,Parameter.OBJECT,ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor(),false));
        parameters.addParameter(new Parameter(stackLoader2,Parameter.OBJECT,ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Result.WELL,Result.FIELD)),false));
        parameters.addParameter(new Parameter(stackLoader2,Parameter.OBJECT_NAME,ImageStackLoader.OUTPUT_IMAGE,CellImage3D,false));
        HashMap<String,String> setFields2 = new HashMap<>();
        setFields2.put(Result.CHANNEL,"2");
        parameters.addParameter(new Parameter(stackLoader2,Parameter.OBJECT,ImageStackLoader.SET_FIELDS,setFields2,false));
        modules.add(stackLoader2);

        // Initialising primary object identification
        IdentifyPrimaryObjects objIdent1 = new IdentifyPrimaryObjects();
        objIdent1.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(objIdent1,Parameter.IMAGE_NAME,IdentifyPrimaryObjects.INPUT_IMAGE,NucleiImage3D,false));
        parameters.addParameter(new Parameter(objIdent1,Parameter.OBJECT_NAME,IdentifyPrimaryObjects.OUTPUT_OBJECT,NucleiObjs3D,false));
        modules.add(objIdent1);

        // Creating Z-projected nuclei
        ProjectObjects projectObjects = new ProjectObjects();
        parameters.addParameter(new Parameter(projectObjects,Parameter.OBJECT_NAME,ProjectObjects.INPUT_OBJECTS,NucleiObjs3D,false));
        parameters.addParameter(new Parameter(projectObjects,Parameter.OBJECT_NAME,ProjectObjects.OUTPUT_OBJECTS,NucleiObjs2D,false));
        modules.add(projectObjects);

        // Displaying the first channel
        ShowImage showImage1 = new ShowImage();
        parameters.addParameter(new Parameter(showImage1,Parameter.IMAGE_NAME,ShowImage.DISPLAY_IMAGE,NucleiImage3D,false));
        modules.add(showImage1);

        // Displaying the first channel
        ShowImage showImage2 = new ShowImage();
        parameters.addParameter(new Parameter(showImage2,Parameter.IMAGE_NAME,ShowImage.DISPLAY_IMAGE,CellImage3D,false));
        modules.add(showImage2);

        // Displaying 3D nuclei
        ShowObjects showNucleiObjects3D = new ShowObjects();
        parameters.addParameter(new Parameter(showNucleiObjects3D,Parameter.OBJECT_NAME,ShowObjects.INPUT_OBJECTS,NucleiObjs3D,false));
        modules.add(showNucleiObjects3D);

        // Displaying 2D nuclei
        ShowObjects showNucleiObjects2D = new ShowObjects();
        parameters.addParameter(new Parameter(showNucleiObjects2D,Parameter.OBJECT_NAME,ShowObjects.INPUT_OBJECTS,NucleiObjs2D,false));
        modules.add(showNucleiObjects2D);

        // Z-projecting nuclei image
        ProjectImage projectNucleiImage = new ProjectImage();
        parameters.addParameter(new Parameter(projectNucleiImage,Parameter.IMAGE_NAME,ProjectImage.INPUT_IMAGE,NucleiImage3D,false));
        parameters.addParameter(new Parameter(projectNucleiImage,Parameter.IMAGE_NAME,ProjectImage.OUTPUT_IMAGE,NucleiImage2D,false));
        modules.add(projectNucleiImage);

        // Z-projecting cell image
        ProjectImage projectImage2 = new ProjectImage();
        parameters.addParameter(new Parameter(projectImage2,Parameter.IMAGE_NAME,ProjectImage.INPUT_IMAGE,CellImage3D,false));
        parameters.addParameter(new Parameter(projectImage2,Parameter.IMAGE_NAME,ProjectImage.OUTPUT_IMAGE,CellImage2D,false));
        modules.add(projectImage2);

        // Displaying projected nuclei image
        ShowImage showImage3 = new ShowImage();
        parameters.addParameter(new Parameter(showImage3,Parameter.IMAGE_NAME,ShowImage.DISPLAY_IMAGE,NucleiImage2D,false));
        modules.add(showImage3);

        // Secondary object identification
        IdentifySecondaryObjects objIdent2 = new IdentifySecondaryObjects();
        objIdent2.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(objIdent2,Parameter.IMAGE_NAME,IdentifySecondaryObjects.INPUT_IMAGE,CellImage2D,false));
        parameters.addParameter(new Parameter(objIdent2,Parameter.OBJECT_NAME,IdentifySecondaryObjects.INPUT_OBJECTS,NucleiObjs2D,false));
        parameters.addParameter(new Parameter(objIdent2,Parameter.OBJECT_NAME,IdentifySecondaryObjects.OUTPUT_OBJECTS,CellObjs2D,false));
        modules.add(objIdent2);

        // Displaying secondary objects
        ShowObjects showObjectsCells2D = new ShowObjects();
        parameters.addParameter(new Parameter(showObjectsCells2D,Parameter.OBJECT_NAME,ShowObjects.INPUT_OBJECTS,CellObjs2D,false));
        modules.add(showObjectsCells2D);

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

        return results;
    }

    @Override
    public Exporter getExporter() {
        return null;
    }

}
