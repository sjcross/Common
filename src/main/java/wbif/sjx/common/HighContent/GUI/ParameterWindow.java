package wbif.sjx.common.HighContent.GUI;

import ij.gui.GenericDialog;
import wbif.sjx.common.HighContent.Object.Parameter;
import wbif.sjx.common.HighContent.Object.ParameterCollection;

import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class ParameterWindow {
    public void updateParameters(ParameterCollection parameters) {
        GenericDialog gd = new GenericDialog("Parameters");

        // Running through all the parameters in the ParameterCollection, adding them to a GenericDialog for ImageJ
        for (int key:parameters.getParameters().keySet()) {
            for (Map.Entry<String,Parameter> entry:parameters.getParameters().get(key).entrySet()) {
                if (entry.getValue().isVisible()) {
                    if (entry.getValue().getType() == Parameter.MODULE_TITLE) {
                        if (gd.getComponentCount() != 0) gd.addMessage(" ");
                        gd.addMessage((String) entry.getValue().getValue());

                    } else if (entry.getValue().getType() == Parameter.NUMBER) {
                        gd.addNumericField(entry.getKey(), (double) entry.getValue().getValue(), 1);

                    } else if (entry.getValue().getType() == Parameter.STRING) {
                        gd.addStringField(entry.getKey(), String.valueOf(entry.getValue()));

                    } else if (entry.getValue().getType() == Parameter.CHOICE_ARRAY) {
                        gd.addChoice(entry.getKey(),(String[]) entry.getValue().getValueRange(), (String) entry.getValue().getValue());

                    } else if (entry.getValue().getType() == Parameter.CHOICE_MAP) {
                        HashMap<String, String> map = (HashMap<String, String>) entry.getValue().getValue();

                        for (String k:map.keySet()) {
                            gd.addStringField(k,map.get(k),1);
                        }
                    }
                }
            }
        }

        // Only displays the dialog if parameters were written
        if (gd.getComponentCount() > 0) {
            gd.showDialog();

            // Retrieving the results
            for (int key : parameters.getParameters().keySet()) {
                for (Map.Entry<String, Parameter> entry : parameters.getParameters().get(key).entrySet()) {
                    if (entry.getValue().isVisible()) {
                        if (entry.getValue().getType() == Parameter.NUMBER) {
                            parameters.getParameters().get(key).get(entry.getKey()).setValue(gd.getNextNumber());

                        } else if (entry.getValue().getType() == Parameter.STRING) {
                            parameters.getParameters().get(key).get(entry.getKey()).setValue(gd.getNextString());

                        } else if (entry.getValue().getType() == Parameter.CHOICE_ARRAY) {
                            parameters.getParameters().get(key).get(entry.getKey()).setValue(gd.getNextChoice());

                        } else if (entry.getValue().getType() == Parameter.CHOICE_MAP) {
                            HashMap<String, String> map = (HashMap<String, String>) entry.getValue().getValue();

                            for (String k:map.keySet()) {
                                map.put(k,gd.getNextString());

                            }

                            parameters.getParameters().get(key).get(entry.getKey()).setValue(map);
                        }
                    }
                }
            }
        }
    }
}
