package wbif.sjx.common.HighContent.Object;

import java.util.LinkedHashMap;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class ParameterCollection {
    private LinkedHashMap<Integer,LinkedHashMap<String,Parameter>> parameters = new LinkedHashMap<>();


    // PUBLIC METHODS

    public void addParameter(Object module, String name, Object value, boolean visible) {
        parameters.computeIfAbsent(module.hashCode(),k -> new LinkedHashMap<>());
        parameters.get(module.hashCode()).put(name,new Parameter(module,name,value,visible));

    }

    public void addParameter(Object module, String name, Object value) {
        addParameter(module, name, value, true);

    }

    public Object getParameter(Object module, String name) {
        return parameters.get(module.hashCode()).get(name).getValue();

    }


    // GETTERS AND SETTERS

    public LinkedHashMap<Integer, LinkedHashMap<String, Parameter>> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<Integer, LinkedHashMap<String, Parameter>> parameters) {
        this.parameters = parameters;
    }
}