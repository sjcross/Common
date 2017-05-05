package wbif.sjx.common.HighContent.Object;

import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFoldernameExtractor;
import wbif.sjx.common.HighContent.GUI.ParameterWindow;
import wbif.sjx.common.HighContent.Module.*;
import wbif.sjx.common.HighContent.Process.Analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created by sc13967 on 03/05/2017.
 */
public class TestAnalysis implements Analysis {
    @Override
    public void initialise() {
        // Initialising variable names for images and objects
        ImageName NucleiImage3D = new ImageName("Nuclei 3D");
        ImageName CellImage3D = new ImageName("Cells 3D");
        ImageName NucleiImage2D = new ImageName("Nuclei 2D");
        ImageName CellImage2D = new ImageName("Cells 2D");

        HCObjectName NucleiObjs3D = new HCObjectName("Nuclei objects 3D");
        HCObjectName NucleiObjs2D = new HCObjectName("Nuclei objects 2D");
        HCObjectName CellObjs2D = new HCObjectName("Cell objects 2D");

        // Running metadata extraction on current file in workspace
        MetadataExtractor metadataExtractor = new MetadataExtractor();
        parameters.addParameter(new Parameter(metadataExtractor,Parameter.OBJECT,MetadataExtractor.FILENAME_EXTRACTOR,new CellVoyagerFilenameExtractor(),false));
        parameters.addParameter(new Parameter(metadataExtractor,Parameter.OBJECT,MetadataExtractor.FOLDERNAME_EXTRACTOR,new CellVoyagerFoldernameExtractor(),false));
        modules.add(metadataExtractor);

        // Initialising image stack loader for channel 1
        ImageStackLoader stackLoader1 = new ImageStackLoader();
        stackLoader1.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(stackLoader1,Parameter.MODULE_TITLE,ImageStackLoader.MODULE_TITLE,"Nuclear channel",true));
        parameters.addParameter(new Parameter(stackLoader1,Parameter.OBJECT,ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor(),false));
        parameters.addParameter(new Parameter(stackLoader1,Parameter.OBJECT,ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Metadata.WELL, Metadata.FIELD)),false));
        parameters.addParameter(new Parameter(stackLoader1,Parameter.OBJECT_NAME,ImageStackLoader.OUTPUT_IMAGE,NucleiImage3D,false));
        HashMap<String,String> setFields1 = new HashMap<>();
        setFields1.put(Metadata.CHANNEL,"1");
        parameters.addParameter(new Parameter(stackLoader1,Parameter.CHOICE_MAP,ImageStackLoader.SET_FIELDS,setFields1,true));
        modules.add(stackLoader1);

        // Initialising image stack loader for channel 2
        ImageStackLoader stackLoader2 = new ImageStackLoader();
        stackLoader2.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(stackLoader2,Parameter.MODULE_TITLE,ImageStackLoader.MODULE_TITLE,"Cell channel",true));
        parameters.addParameter(new Parameter(stackLoader2,Parameter.OBJECT,ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor(),false));
        parameters.addParameter(new Parameter(stackLoader2,Parameter.OBJECT,ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Metadata.WELL, Metadata.FIELD)),false));
        parameters.addParameter(new Parameter(stackLoader2,Parameter.OBJECT_NAME,ImageStackLoader.OUTPUT_IMAGE,CellImage3D,false));
        HashMap<String,String> setFields2 = new HashMap<>();
        setFields2.put(Metadata.CHANNEL,"2");
        parameters.addParameter(new Parameter(stackLoader2,Parameter.CHOICE_MAP,ImageStackLoader.SET_FIELDS,setFields2,true));
        modules.add(stackLoader2);

        // Initialising primary object identification
        IdentifyPrimaryObjects objIdent1 = new IdentifyPrimaryObjects();
        objIdent1.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(objIdent1,Parameter.IMAGE_NAME,IdentifyPrimaryObjects.INPUT_IMAGE,NucleiImage3D,false));
        parameters.addParameter(new Parameter(objIdent1,Parameter.OBJECT_NAME,IdentifyPrimaryObjects.OUTPUT_OBJECT,NucleiObjs3D,false));
        modules.add(objIdent1);

        // Creating Z-projected nuclei objects
        ProjectObjects projectObjects = new ProjectObjects();
        parameters.addParameter(new Parameter(projectObjects,Parameter.OBJECT_NAME,ProjectObjects.INPUT_OBJECTS,NucleiObjs3D,false));
        parameters.addParameter(new Parameter(projectObjects,Parameter.OBJECT_NAME,ProjectObjects.OUTPUT_OBJECTS,NucleiObjs2D,false));
        modules.add(projectObjects);

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

        // Secondary object identification
        IdentifySecondaryObjects objIdent2 = new IdentifySecondaryObjects();
        objIdent2.initialiseParameters(parameters);
        parameters.addParameter(new Parameter(objIdent2,Parameter.IMAGE_NAME,IdentifySecondaryObjects.INPUT_IMAGE,CellImage2D,false));
        parameters.addParameter(new Parameter(objIdent2,Parameter.OBJECT_NAME,IdentifySecondaryObjects.INPUT_OBJECTS,NucleiObjs2D,false));
        parameters.addParameter(new Parameter(objIdent2,Parameter.OBJECT_NAME,IdentifySecondaryObjects.OUTPUT_OBJECTS,CellObjs2D,false));
        modules.add(objIdent2);

        // Measuring nuclear intensity in nuclear channel
        MeasureObjectIntensity measureObjectIntensity1 = new MeasureObjectIntensity();
        parameters.addParameter(new Parameter(measureObjectIntensity1,Parameter.OBJECT_NAME,MeasureObjectIntensity.INPUT_OBJECTS,NucleiObjs3D,false));
        parameters.addParameter(new Parameter(measureObjectIntensity1,Parameter.IMAGE_NAME,MeasureObjectIntensity.INPUT_IMAGE,NucleiImage3D,false));
        modules.add(measureObjectIntensity1);

        // Measuring nuclear intensity in cell channel
        MeasureObjectIntensity measureObjectIntensity2 = new MeasureObjectIntensity();
        parameters.addParameter(new Parameter(measureObjectIntensity2,Parameter.OBJECT_NAME,MeasureObjectIntensity.INPUT_OBJECTS,NucleiObjs3D,false));
        parameters.addParameter(new Parameter(measureObjectIntensity2,Parameter.IMAGE_NAME,MeasureObjectIntensity.INPUT_IMAGE,CellImage3D,false));
        modules.add(measureObjectIntensity2);

        // Measuring cell intensity in nuclear channel
        MeasureObjectIntensity measureObjectIntensity3 = new MeasureObjectIntensity();
        parameters.addParameter(new Parameter(measureObjectIntensity3,Parameter.OBJECT_NAME,MeasureObjectIntensity.INPUT_OBJECTS,CellObjs2D,false));
        parameters.addParameter(new Parameter(measureObjectIntensity3,Parameter.IMAGE_NAME,MeasureObjectIntensity.INPUT_IMAGE,NucleiImage2D,false));
        modules.add(measureObjectIntensity3);

        // Measuring cell intensity in cell channel
        MeasureObjectIntensity measureObjectIntensity4 = new MeasureObjectIntensity();
        parameters.addParameter(new Parameter(measureObjectIntensity4,Parameter.OBJECT_NAME,MeasureObjectIntensity.INPUT_OBJECTS,CellObjs2D,false));
        parameters.addParameter(new Parameter(measureObjectIntensity4,Parameter.IMAGE_NAME,MeasureObjectIntensity.INPUT_IMAGE,CellImage2D,false));
        modules.add(measureObjectIntensity4);

        // Displaying settings for parameters
        new ParameterWindow().updateParameters(parameters);

    }

    @Override
    public void execute(Workspace workspace) {
        // Running through modules
        for (Module module:modules) {
            module.execute(workspace,parameters);
        }
    }
}
