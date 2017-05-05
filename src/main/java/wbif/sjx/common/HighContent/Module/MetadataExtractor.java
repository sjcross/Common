package wbif.sjx.common.HighContent.Module;

import wbif.sjx.common.HighContent.Extractor.Extractor;
import wbif.sjx.common.HighContent.Object.Parameter;
import wbif.sjx.common.HighContent.Object.ParameterCollection;
import wbif.sjx.common.HighContent.Object.Metadata;
import wbif.sjx.common.HighContent.Object.Workspace;


/**
 * Created by sc13967 on 05/05/2017.
 */
public class MetadataExtractor implements Module {
    public static final String FILENAME_EXTRACTOR = "Filename extractor";
    public static final String FOLDERNAME_EXTRACTOR = "Foldername extractor";

    @Override
    public void execute(Workspace workspace, ParameterCollection parameters) {
        // Getting current result
        Metadata metadata = workspace.getMetadata();

        // Getting filename extractor
        Extractor filenameExtractor = (Extractor) parameters.getParameter(this,FILENAME_EXTRACTOR).getValue();

        // Getting foldername extractor
        Extractor foldernameExtractor = (Extractor) parameters.getParameter(this,FOLDERNAME_EXTRACTOR).getValue();

        // Preparing Result object
        metadata.setFile(workspace.getCurrentFile());
        foldernameExtractor.extract(metadata,metadata.getFile().getParent());
        filenameExtractor.extract(metadata,metadata.getFile().getName());

    }

    @Override
    public void initialiseParameters(ParameterCollection parameters) {
        parameters.addParameter(new Parameter(this,Parameter.OBJECT,FILENAME_EXTRACTOR,null,false));
        parameters.addParameter(new Parameter(this,Parameter.OBJECT,FOLDERNAME_EXTRACTOR,null,false));

    }
}


