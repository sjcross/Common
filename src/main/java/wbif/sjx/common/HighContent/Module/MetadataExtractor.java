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
    public void execute(Workspace workspace, ParameterCollection parameters, boolean verbose) {
        // Getting current result
        Metadata metadata = workspace.getMetadata();

        // Getting filename extractor
        Extractor filenameExtractor = parameters.getValue(this,FILENAME_EXTRACTOR);

        // Getting foldername extractor
        Extractor foldernameExtractor = parameters.getValue(this,FOLDERNAME_EXTRACTOR);

        // Preparing Result object
        metadata.setFile(workspace.getMetadata().getFile());
        foldernameExtractor.extract(metadata,metadata.getFile().getParent());
        filenameExtractor.extract(metadata,metadata.getFile().getName());

    }

    @Override
    public ParameterCollection initialiseParameters() {
        ParameterCollection parameters = new ParameterCollection();

        parameters.addParameter(new Parameter(this,FILENAME_EXTRACTOR,Parameter.OBJECT,null,false));
        parameters.addParameter(new Parameter(this,FOLDERNAME_EXTRACTOR,Parameter.OBJECT,null,false));

        return parameters;

    }
}


