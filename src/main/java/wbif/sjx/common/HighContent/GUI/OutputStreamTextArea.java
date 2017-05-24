package wbif.sjx.common.HighContent.GUI;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by sc13967 on 24/05/2017.
 */
public class OutputStreamTextArea extends OutputStream {
    private final JTextArea textArea;
    private String currText = "";

    public OutputStreamTextArea(JTextArea textArea) {
        this.textArea = textArea;

    }

    @Override
    public void write(int b) throws IOException {
        // If the current character is a return line don't add anything to the
        if (b == '\n') {
            currText = "";

        } else {
            textArea.setText(currText+(char) b);
            currText = textArea.getText();

        }
    }
}
