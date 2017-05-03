package wbif.sjx.common.HighContent.Object;

import ij.IJ;
import wbif.sjx.common.HighContent.Extractor.CellVoyagerFilenameExtractor;
import wbif.sjx.common.HighContent.GUI.ParameterWindow;
import wbif.sjx.common.HighContent.Module.IdentifyPrimaryObjects;
import wbif.sjx.common.HighContent.Module.ImageStackLoader;
import wbif.sjx.common.HighContent.Module.Module;
import wbif.sjx.common.HighContent.Module.ModuleCollection;
import wbif.sjx.common.HighContent.Process.Analysis;
import wbif.sjx.common.HighContent.Process.Exporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;


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

        // Initialising image stack loader for channel 1
        ImageStackLoader stackLoader1 = new ImageStackLoader();
        LinkedHashMap<String,Object> stackParameters1 = stackLoader1.initialiseParameters();
        stackParameters1.put(ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor());
        stackParameters1.put(ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Result.WELL,Result.FIELD)));
        stackParameters1.put(ImageStackLoader.OUTPUT_IMAGE,Stack1);
        stackParameters1.put(ImageStackLoader.SET_FIELDS,new HashMap<>().put(Result.CHANNEL,"1"));
        parameters.put(stackLoader1.hashCode(),stackParameters1);
        modules.add(stackLoader1);

        // Initialising image stack loader for channel 2
        ImageStackLoader stackLoader2 = new ImageStackLoader();
        LinkedHashMap<String,Object> stackParameters2 = stackLoader2.initialiseParameters();
        stackParameters2.put(ImageStackLoader.EXTRACTOR,new CellVoyagerFilenameExtractor());
        stackParameters2.put(ImageStackLoader.STATIC_FIELDS,new ArrayList<>(Arrays.asList(Result.WELL,Result.FIELD)));
        stackParameters2.put(ImageStackLoader.OUTPUT_IMAGE,Stack2);
        stackParameters2.put(ImageStackLoader.SET_FIELDS,new HashMap<>().put(Result.CHANNEL,"2"));
        parameters.put(stackLoader2.hashCode(),stackParameters2);
        modules.add(stackLoader2);

        // Initialising primary object identification
        IdentifyPrimaryObjects objIdent1 = new IdentifyPrimaryObjects();
        parameters.put(objIdent1.hashCode(),objIdent1.initialiseParameters());
        parameters.get(objIdent1.hashCode()).put(IdentifyPrimaryObjects.INPUT_IMAGE,Stack1);
        parameters.get(objIdent1.hashCode()).put(IdentifyPrimaryObjects.OUTPUT_OBJECT,NucleiObjs);
        modules.add(objIdent1);

        // Displaying settings for parameters
        new ParameterWindow().updateParameters(parameters);

    }

    @Override
    public ResultCollection execute(Workspace workspace) {
        // Running through modules
        for (Module module:workspace.getModules()) {
            module.execute(workspace);
        }

//        ArrayList<HCObject> nuclei = workspace.getObjects().get(NucleiObjs);
//        for (HCObject nucleus:nuclei) {
//            System.out.println(nucleus.toString());
//        }

        IJ.runMacro("waitForUser");

        return null;
    }

    @Override
    public Exporter getExporter() {
        return null;
    }

}
