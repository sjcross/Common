package wbif.sjx.common.HighContent.Object;

import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class ParameterCollection {
    private LinkedHashMap<Integer,LinkedHashMap<String,Parameter>> parameters = new LinkedHashMap<>();


    // PUBLIC METHODS

    public void addParameter(Parameter parameter) {
        parameters.computeIfAbsent(parameter.getModule().hashCode(),k -> new LinkedHashMap<>());
        parameters.get(parameter.getModule().hashCode()).put(parameter.getName(),parameter);

    }

    public Parameter getParameter(Object module, String name) {
        return parameters.get(module.hashCode()).get(name);

    }


    // GETTERS AND SETTERS

    public LinkedHashMap<Integer, LinkedHashMap<String, Parameter>> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<Integer, LinkedHashMap<String, Parameter>> parameters) {
        this.parameters = parameters;
    }
}