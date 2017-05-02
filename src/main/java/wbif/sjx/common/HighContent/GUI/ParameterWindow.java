package wbif.sjx.common.HighContent.GUI;

import ij.gui.GenericDialog;
import wbif.sjx.common.HighContent.Object.ModuleTitle;
import wbif.sjx.common.HighContent.Object.ParameterCollection;

import java.util.Map;

/**
 * Created by sc13967 on 02/05/2017.
 */
public class ParameterWindow {
    public void updateParameters(ParameterCollection parameters) {
        GenericDialog gd = new GenericDialog("Parameters");

        // Running through all the parameters in the ParameterCollection, adding them to a GenericDialog for ImageJ
        for (int key:parameters.keySet()) {
            for (Map.Entry<String,Object> entry:parameters.get(key).entrySet()) {
                if (entry.getValue() instanceof ModuleTitle) {
                    gd.addMessage(entry.getKey());

                } else if (entry.getValue() instanceof Integer || entry.getValue() instanceof Double) {
                    gd.addNumericField(entry.getKey(),(double) entry.getValue(),1);

                } else if (entry.getValue() instanceof Double) {
                    gd.addStringField(entry.getKey(),String.valueOf(entry.getValue()));


                }
            }

            // Adding a blank space
            gd.addMessage(" ");

        }

        gd.showDialog();

        // Retrieving the results
        for (int key:parameters.keySet()) {
            for (Map.Entry<String,Object> entry:parameters.get(key).entrySet()) {
                if (entry.getValue() instanceof Integer || entry.getValue() instanceof Double) {
                    entry.setValue(gd.getNextNumber());

                } else if (entry.getValue() instanceof Double) {
                    entry.setValue(gd.getNextString());

                }
            }
        }
    }
}
