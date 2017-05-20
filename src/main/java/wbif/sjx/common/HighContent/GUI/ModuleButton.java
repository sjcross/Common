package wbif.sjx.common.HighContent.GUI;

import wbif.sjx.common.HighContent.Module.HCModule;

import javax.swing.*;

/**
 * Created by Stephen on 20/05/2017.
 */
public class ModuleButton extends JToggleButton {
    private HCModule module;


    // CONSTRUCTOR

    public ModuleButton(HCModule module) {
        this.module = module;
        if (module != null) setText(module.getTitle());

    }


    // GETTERS

    public HCModule getModule() {
        return module;
    }
}
