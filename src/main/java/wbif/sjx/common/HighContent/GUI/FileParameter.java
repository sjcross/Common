package wbif.sjx.common.HighContent.GUI;

import wbif.sjx.common.HighContent.Object.HCParameter;

import javax.swing.*;

/**
 * Created by Stephen on 20/05/2017.
 */
public class FileParameter extends JButton {
    private HCParameter parameter;

    public FileParameter(HCParameter parameter) {
        this.parameter = parameter;
        setFocusPainted(false);

    }

    public HCParameter getParameter() {
        return parameter;
    }
}
