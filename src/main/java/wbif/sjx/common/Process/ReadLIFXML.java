package wbif.sjx.common.Process;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sc13967 on 03/11/2017.
 */
public class ReadLIFXML implements ActionListener{
    JFrame frame;
    JTextArea coordinatesArea;

    public static void main (String[] args) {
//        try {
//            FileInputStream  fis = new FileInputStream("C:\\Users\\sc13967\\Google Drive\\People\\C\\Stephen Cross\\2017-11-03 FRAP test\\2017-11-03 FRAP test.lif");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
//            StringBuilder buffer = new StringBuilder();
//
//            String line = reader.readLine();
//
//            while (!line.contains("\u0004")) {
//                line = reader.readLine();
//                buffer.append(new String(line.getBytes("UTF-8"), "UTF-16"));
//
//            }
//
//            System.out.println(buffer.toString());
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        new ReadLIFXML();

    }

    public ReadLIFXML() {
        frame = new JFrame();

        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.PAGE_AXIS));

        coordinatesArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(coordinatesArea);
        frame.add(scrollPane);

        JButton continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(this);
        frame.add(continueButton);

        frame.setSize(400,700);
        frame.setLocation(500,200);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Continue")) {
            String coordinatesText = coordinatesArea.getText();

            System.out.println(coordinatesText);

            frame.dispose();

        }
    }
}
