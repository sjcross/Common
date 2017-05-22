// TODO: Add controls for all parameter types (Measurement, arrays, hashsets, etc.)

package wbif.sjx.common.HighContent.GUI;

import org.apache.commons.io.FilenameUtils;
import org.reflections.Reflections;
import wbif.sjx.common.HighContent.Module.*;
import wbif.sjx.common.HighContent.Object.*;
import wbif.sjx.common.HighContent.Process.HCExporter;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * Created by Stephen on 20/05/2017.
 */
public class MainGUI implements ActionListener, FocusListener, MouseListener {
    private static final String addModuleText = "+";
    private static final String removeModuleText = "-";
    private static final String moveModuleUpText = "▲";
    private static final String moveModuleDownText = "▼";
    private static final String startAnalysisText = "✓";
    private static final String stopAnalysisText = "✕";
    private static final String saveAnalysis = "S";
    private static final String loadAnalysis = "L";

    private int frameWidth = 500;
    private int frameHeight = 750;
    private int elementHeight = 30;

    private HCModule activeModule = null;
    private Frame frame = new JFrame();
    private JPanel modulesPanel = new JPanel();
    private JPanel paramsPanel = new JPanel();
    JPopupMenu moduleListMenu = new JPopupMenu();
    Thread t = null;

    private GUIAnalysis analysis = new GUIAnalysis();
    private HCModuleCollection modules = analysis.modules;
    private String inputFilePath = "";
    private String outputFilePath = "";
    private boolean exportXML = false;
    private boolean exportXLSX = false;

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        MainGUI mainGUI = new MainGUI();

    }

    public MainGUI() throws InstantiationException, IllegalAccessException {
//        // DEBUG CALLS
//        modules = new HCModuleCollection();
//
//        // Getting current image open in ImageJ (must be 2 channel)
//        ImageJImageLoader IJLoader = new ImageJImageLoader();
//        modules.add(IJLoader);
//
//        // Splitting stack into two
//        ChannelExtractor chExtractor1 = new ChannelExtractor();
//        modules.add(chExtractor1);
//
//        // Splitting stack into two
//        ChannelExtractor chExtractor2 = new ChannelExtractor();
//        modules.add(chExtractor2);
//
//        // Running TrackMate and storing tracks as objects
//        RunTrackMate runTrackMate = new RunTrackMate();
//        modules.add(runTrackMate);
//
//        // Showing objects
//        ShowObjects showObjects = new ShowObjects();
//        modules.add(showObjects);
//
//        // Measuring track intensity
//        MeasureTrackIntensity trackIntensity = new MeasureTrackIntensity();
//        modules.add(trackIntensity);
//
//        // END DEBUG CALLS


        frame.setLayout(new FlowLayout());

        // Setting location of panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2);
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);

        // Populating the list containing all available modules
        listAvailableModules();

        // Creating buttons to add and remove modules
        JPanel addRemovePanel = createAddRemoveModulePanel();
        frame.add(addRemovePanel);

        // Populating the module list
        populateModuleList();
        frame.add(modulesPanel);

        // Initialising the parameters panel
        initialiseParametersPanel();
        frame.add(paramsPanel);

        frame.pack();

    }

    private JPanel createAddRemoveModulePanel() {
        int buttonSize = 50;

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(buttonSize + 15, frameHeight));
        panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.PAGE_START;

        // Add module button
        JButton addModuleButton = new JButton(addModuleText);
        addModuleButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        addModuleButton.addActionListener(this);
        addModuleButton.setName("ControlButton");
        panel.add(addModuleButton, c);

        // Remove module button
        JButton removeModuleButton = new JButton(removeModuleText);
        removeModuleButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        removeModuleButton.addActionListener(this);
        removeModuleButton.setName("ControlButton");
        c.gridy++;
        panel.add(removeModuleButton, c);

        // Move module up button
        JButton moveModuleUpButton = new JButton(moveModuleUpText);
        moveModuleUpButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        moveModuleUpButton.addActionListener(this);
        moveModuleUpButton.setName("ControlButton");
        c.gridy++;
        panel.add(moveModuleUpButton, c);

        // Move module up button
        JButton moveModuleDownButton = new JButton(moveModuleDownText);
        moveModuleDownButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        moveModuleDownButton.addActionListener(this);
        moveModuleDownButton.setName("ControlButton");
        c.gridy++;
        panel.add(moveModuleDownButton, c);

        // Load analysis protocol button
        JButton loadAnalysisButton = new JButton(loadAnalysis);
        loadAnalysisButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        loadAnalysisButton.addActionListener(this);
        loadAnalysisButton.setName("ControlButton");
        c.gridy++;
        c.weighty = 1;
        c.anchor = GridBagConstraints.PAGE_END;
        panel.add(loadAnalysisButton, c);

        // Save analysis protocol button
        JButton saveAnalysisButton = new JButton(saveAnalysis);
        saveAnalysisButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        saveAnalysisButton.addActionListener(this);
        saveAnalysisButton.setName("ControlButton");
        c.gridy++;
        c.weighty = 0;
        panel.add(saveAnalysisButton, c);

        // Start analysis button
        JButton startAnalysisButton = new JButton(startAnalysisText);
        startAnalysisButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        startAnalysisButton.addActionListener(this);
        startAnalysisButton.setName("ControlButton");
        c.gridy++;
        panel.add(startAnalysisButton, c);

        // Stop analysis button
        JButton stopAnalysisButton = new JButton(stopAnalysisText);
        stopAnalysisButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        stopAnalysisButton.addActionListener(this);
        stopAnalysisButton.setName("ControlButton");
        c.gridy++;
        panel.add(stopAnalysisButton, c);

        panel.validate();
        panel.repaint();

        return panel;

    }

    private void populateModuleList() {
        int buttonWidth = 200;

        modulesPanel.removeAll();

        // Initialising the panel for module buttons
        modulesPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        modulesPanel.setPreferredSize(new Dimension(buttonWidth + 15, frameHeight));
        modulesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;

        // Adding module buttons
        Iterator<HCModule> iterator = modules.iterator();
        while (iterator.hasNext()) {
            HCModule module = iterator.next();
            ModuleButton button = new ModuleButton(module);
            button.setSelected(false);
            button.addActionListener(this);
            button.setName("ModuleButton");
            c.gridy++;
            if (activeModule != null) {
                if (module == activeModule) button.setSelected(true);
            }
            modulesPanel.add(button, c);

        }

        // Adding analysis options button
        JButton analysisOptionsButton = new JButton();
        analysisOptionsButton.setSelected(false);
        analysisOptionsButton.addActionListener(this);
        analysisOptionsButton.setText("Analysis options");
        analysisOptionsButton.setName("AnalysisOptionsButton");
        c.gridy++;
        c.weighty = 1;
        c.anchor = GridBagConstraints.PAGE_END;
        modulesPanel.add(analysisOptionsButton, c);

        modulesPanel.validate();
        modulesPanel.repaint();

    }

    private void initialiseParametersPanel() {
        paramsPanel.removeAll();

        paramsPanel.setLayout(new GridBagLayout());
        paramsPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        paramsPanel.setPreferredSize(new Dimension(700, frameHeight));

        // Adding placeholder text
        JTextField textField = new JTextField("Select a module to edit its parameters");
        textField.setBorder(null);
        textField.setEditable(false);
        paramsPanel.add(textField);

        paramsPanel.validate();
        paramsPanel.repaint();

    }

    private void populateModuleParameters() {
        paramsPanel.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        // If the active module is set to null (i.e. we're looking at the analysis options panel) exit this method
        if (activeModule == null) {
            return;
        }

        Iterator<HCParameter> iterator = activeModule.getActiveParameters().getParameters().values().iterator();
        while (iterator.hasNext()) {
            HCParameter parameter = iterator.next();

            c.gridx = 0;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            if (!iterator.hasNext()) c.weighty = 1;

            JTextField parameterName = new JTextField(parameter.getName());
            parameterName.setPreferredSize(new Dimension(330, elementHeight));
            parameterName.setEditable(false);
            parameterName.setBorder(null);
            paramsPanel.add(parameterName, c);

            JComponent parameterControl = null;

            if (parameter.getType() == HCParameter.INPUT_IMAGE) {
                // Getting a list of available images
                ArrayList<HCParameter> images = modules.getParametersMatchingType(HCParameter.OUTPUT_IMAGE,
                        activeModule);

                parameterControl = new HCNameInputParameter(parameter);
                for (HCParameter image : images) {
                    ((HCNameInputParameter) parameterControl).addItem(image.getValue());

                }
                ((HCNameInputParameter) parameterControl).setSelectedItem(parameter.getValue());
                parameterControl.addFocusListener(this);
                parameterControl.setName("InputParameter");

            } else if (parameter.getType() == HCParameter.INPUT_OBJECTS) {
                // Getting a list of available images
                ArrayList<HCParameter> images = modules.getParametersMatchingType(HCParameter.OUTPUT_OBJECTS,
                        activeModule);

                parameterControl = new HCNameInputParameter(parameter);
                for (HCParameter image : images) {
                    ((HCNameInputParameter) parameterControl).addItem(image.getValue());

                }
                ((HCNameInputParameter) parameterControl).setSelectedItem(parameter.getValue());
                parameterControl.addFocusListener(this);
                parameterControl.setName("InputParameter");

            } else if (parameter.getType() == HCParameter.INTEGER | parameter.getType() == HCParameter.DOUBLE
                    | parameter.getType() == HCParameter.STRING | parameter.getType() == HCParameter.OUTPUT_IMAGE
                    | parameter.getType() == HCParameter.OUTPUT_OBJECTS) {

                parameterControl = new TextParameter(parameter);
                String name = parameter.getValue() == null ? "" : parameter.getValue().toString();
                ((TextParameter) parameterControl).setText(name);
                parameterControl.addFocusListener(this);
                parameterControl.setName("TextParameter");

            } else if (parameter.getType() == HCParameter.BOOLEAN) {
                parameterControl = new BooleanParameter(parameter);
                ((BooleanParameter) parameterControl).setSelected(parameter.getValue());
                ((BooleanParameter) parameterControl).addActionListener(this);
                parameterControl.setName("BooleanParameter");

            } else if (parameter.getType() == HCParameter.FILE_PATH) {
                parameterControl = new FileParameter(parameter);
                ((FileParameter) parameterControl).setText(FilenameUtils.getName(parameter.getValue()));
                ((FileParameter) parameterControl).addActionListener(this);
                parameterControl.setName("FileParameter");

            } else if (parameter.getType() == HCParameter.CHOICE_ARRAY) {
                String[] valueSource = (String[]) parameter.getValueSource();
                parameterControl = new ChoiceArrayParameter(parameter,valueSource);
                ((ChoiceArrayParameter) parameterControl).addActionListener(this);
                System.out.println((String) parameter.getValue());
                if (parameter.getValue() != null) {
                    for (int i=0;i<valueSource.length;i++) {
                        if (valueSource[i].equals(parameter.getValue())) {
                            ((ChoiceArrayParameter) parameterControl).setSelectedItem(i);
                        }
                    }
                }
                parameterControl.setName("ChoiceArrayParameter");

            } else if (parameter.getType() == HCParameter.MEASUREMENT) {
                HCMeasurementCollection measurements = modules.getMeasurements(activeModule);
                String[] measurementChoices = measurements.getMeasurementNames((HCName) parameter.getValueSource());
                parameterControl = new ChoiceArrayParameter(parameter,measurementChoices);
                if (parameter.getValue() != null) {
                    ((ChoiceArrayParameter) parameterControl).setSelectedItem(parameter.getValue());
                }
                ((ChoiceArrayParameter) parameterControl).addActionListener(this);
                parameterControl.setName("ChoiceArrayParameter");

            }

            c.gridx++;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            if (parameterControl != null) {
                paramsPanel.add(parameterControl, c);
                parameterControl.setPreferredSize(new Dimension(330, elementHeight));

            }

            c.gridy++;

        }

        paramsPanel.validate();
        paramsPanel.repaint();

    }

    private void populateAnalysisParameters() {
        paramsPanel.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.FIRST_LINE_START;

//        // Getting analysis mode
//        String[] analysisModes = new String[]{"Use image open in ImageJ","Load single image from file","Batch mode"};
//        JComboBox analysisMode = new JComboBox(analysisModes);
//        analysisMode.setName("AnalysisMode");
//        analysisMode.addActionListener(this);
//        paramsPanel.add(analysisMode,c);
//
//        // If loading from ImageJ do nothing
//        if (analysisMode.getSelectedItem().equals("Use image open in ImageJ"));


        // Select file export location
        JTextField exportFileName = new JTextField("Export location");
        exportFileName.setPreferredSize(new Dimension(200, elementHeight));
        exportFileName.setEditable(false);
        exportFileName.setBorder(null);
        c.gridy++;
        paramsPanel.add(exportFileName, c);

        JButton exportFileButton = new JButton(outputFilePath);
        exportFileButton.addActionListener(this);
        exportFileButton.setPreferredSize(new Dimension(200, elementHeight));
        exportFileButton.setName("OutputFilePath");
        c.gridx++;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        paramsPanel.add(exportFileButton, c);

        // Select export type
        JCheckBox xmlCheck = new JCheckBox("Export XML");
        xmlCheck.addActionListener(this);
        xmlCheck.setSelected(false);
        xmlCheck.setName("XMLCheck");
        c.gridx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridy++;
        paramsPanel.add(xmlCheck, c);

        JCheckBox xlsxCheck = new JCheckBox("Export XLSX");
        xlsxCheck.addActionListener(this);
        xlsxCheck.setSelected(true);
        xlsxCheck.setName("XLSXCheck");
        c.gridy++;
        c.weighty = 1;
        paramsPanel.add(xlsxCheck, c);

        paramsPanel.validate();
        paramsPanel.repaint();

    }

    private void listAvailableModules() throws IllegalAccessException, InstantiationException {
        // Using Reflections tool to get list of classes extending HCModule
        Reflections.log = null;
        Reflections reflections = new Reflections("wbif.sjx.common");
        Set<Class<? extends HCModule>> availableModules = reflections.getSubTypesOf(HCModule.class);

        // Creating new instances of these classes and adding to ArrayList
        ArrayList<HCModule> availableModulesList = new ArrayList<>();
        for (Class clazz : availableModules) {
            availableModulesList.add((HCModule) clazz.newInstance());
        }

        // Sorting the ArrayList based on module title
        Collections.sort(availableModulesList, Comparator.comparing(HCModule::getTitle));

        // Adding the modules to the list
        for (HCModule module : availableModulesList) {
            PopupMenuItem menuItem = new PopupMenuItem(module);
            menuItem.addActionListener(this);
            menuItem.setName("ModuleName");
            moduleListMenu.add(menuItem);

        }

        // Adding a close list option
        PopupMenuItem closeItem = new PopupMenuItem(null);
        closeItem.setText("[Close list]");
        closeItem.addActionListener(this);
        closeItem.setName("ModuleName");
        moduleListMenu.add(closeItem);

    }

    private void addModule() {
        moduleListMenu.addMouseListener(this);
        moduleListMenu.setLocation(frame.getX(), frame.getY());
        moduleListMenu.setVisible(true);

    }

    private void removeModule() {
        if (activeModule != null) {
            modules.remove(activeModule);
            activeModule = null;
            populateModuleList();
            initialiseParametersPanel();
        }
    }

    private void moveModuleUp() {
        if (activeModule != null) {
            int pos = modules.indexOf(activeModule);
            if (pos != 0) {
                modules.remove(activeModule);
                modules.add(pos - 1, activeModule);
                populateModuleList();
            }
        }
    }

    private void moveModuleDown() {
        if (activeModule != null) {
            int pos = modules.indexOf(activeModule);
            if (pos != modules.size()) {
                modules.remove(activeModule);
                modules.add(pos + 1, activeModule);
                populateModuleList();
            }
        }
    }

    private void saveAnalysis() throws IOException {
        FileDialog fileDialog = new FileDialog(new Frame(), "Select file to save", FileDialog.SAVE);
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);

        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileDialog.getFiles()[0]));

        outputStream.writeObject(analysis);
        outputStream.close();

        JOptionPane.showMessageDialog(null, "File saved", "File saved", JOptionPane.INFORMATION_MESSAGE);

    }

    private void loadAnalysis() throws IOException, ClassNotFoundException {
        FileDialog fileDialog = new FileDialog(new Frame(), "Select file to save", FileDialog.LOAD);
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileDialog.getFiles()[0]));

        analysis = (GUIAnalysis) inputStream.readObject();
        inputStream.close();

        modules = analysis.getModules();

        populateModuleList();

        JOptionPane.showMessageDialog(null, "File loaded", "File loaded", JOptionPane.INFORMATION_MESSAGE);


    }

    private void startAnalysis() {
        HCWorkspaceCollection workspaces = new HCWorkspaceCollection();
        HCWorkspace workspace;
        if (!inputFilePath.equals("")) {
            workspace = workspaces.getNewWorkspace(new File(inputFilePath));

        } else {
            workspace = workspaces.getNewWorkspace(null);

        }

        // Running the analysis
        analysis.execute(workspace, true);

        // Exporting XLSX
        if (exportXLSX & !outputFilePath.equals("")) {
            HCExporter exporter = new HCExporter(new File(outputFilePath), HCExporter.XLSX_EXPORT);
            exporter.exportResults(workspaces, analysis);

        }

        // Exporting XML
        if (exportXML & !outputFilePath.equals("")) {
            HCExporter exporter = new HCExporter(new File(outputFilePath), HCExporter.XML_EXPORT);
            exporter.exportResults(workspaces, analysis);

        }
    }

    private void selectModule(HCModule module) {
        // Clearing the previous module
        ModuleButton prevModuleButton = getModuleButton(activeModule);
        if (prevModuleButton != null) prevModuleButton.setSelected(false);

        // Getting new button and setting enabled
        ModuleButton activeModuleButton = getModuleButton(module);
        if (activeModuleButton != null) activeModuleButton.setSelected(true);
        activeModule = module;

        // Updating the displayed parameters
        populateModuleParameters();

    }

    private ModuleButton getModuleButton(HCModule module) {
        for (Component component : modulesPanel.getComponents()) {
            if (component.getName().equals("ModuleButton")) {
                if (((ModuleButton) component).getModule() == module) {
                    return (ModuleButton) component;

                }
            }
        }

        return null;

    }

    private void reactToAction(Object object)
            throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {

        System.out.println(((JComponent) object).getName());
        if (((JComponent) object).getName().equals("ControlButton")) {
            if (((JButton) object).getText().equals(addModuleText)) {
                addModule();

            } else if (((JButton) object).getText().equals(removeModuleText)) {
                removeModule();

            } else if (((JButton) object).getText().equals(moveModuleUpText)) {
                moveModuleUp();

            } else if (((JButton) object).getText().equals(moveModuleDownText)) {
                moveModuleDown();

            } else if (((JButton) object).getText().equals(saveAnalysis)) {
                saveAnalysis();

            } else if (((JButton) object).getText().equals(loadAnalysis)) {
                loadAnalysis();

            } else if (((JButton) object).getText().equals(startAnalysisText)) {
                t = new Thread(this::startAnalysis);
                t.start();

            } else if (((JButton) object).getText().equals(stopAnalysisText)) {
                analysis.shutdown();

            }

        } else if (((JComponent) object).getName().equals("ModuleName")) {
            moduleListMenu.setVisible(false);

            if (((PopupMenuItem) object).getModule() == null) return;

            // Adding it after the currently-selected module
            HCModule module = ((PopupMenuItem) object).getModule().getClass().newInstance();
            if (activeModule != null) {
                for (Component component : modulesPanel.getComponents()) {
                    if (((ModuleButton) component).getModule() == activeModule) {
                        int pos = modules.indexOf(((ModuleButton) component).getModule()) + 1;
                        modules.add(pos, module);

                        break;

                    }
                }

            } else {
                modules.add(module);

            }

            // Adding to the list of modules
            populateModuleList();

            // Selecting the added module
            selectModule(module);

        } else if (((JComponent) object).getName().equals("AnalysisOptionsButton")) {
            // Selecting the added module
            selectModule(null);

            populateAnalysisParameters();

        } else if (((JComponent) object).getName().equals("ModuleButton")) {
            selectModule(((ModuleButton) object).getModule());

        } else if (((JComponent) object).getName().equals("InputParameter")) {
            HCParameter parameter = ((HCNameInputParameter) object).getParameter();
            parameter.setValue(((HCNameInputParameter) object).getSelectedItem());
            populateModuleParameters();

        } else if (((JComponent) object).getName().equals("TextParameter")) {
            HCParameter parameter = ((TextParameter) object).getParameter();
            String text = ((TextParameter) object).getText();

            if (parameter.getType() == HCParameter.OUTPUT_IMAGE | parameter.getType() == HCParameter.OUTPUT_OBJECTS) {
                parameter.setValue(new HCName(text));

            } else if (parameter.getType() == HCParameter.INTEGER) {
                parameter.setValue(Integer.valueOf(text));

            } else if (parameter.getType() == HCParameter.DOUBLE) {
                parameter.setValue(Double.valueOf(text));

            } else if (parameter.getType() == HCParameter.STRING) {
                parameter.setValue(text);

            }

        } else if (((JComponent) object).getName().equals("BooleanParameter")) {
            HCParameter parameter = ((BooleanParameter) object).getParameter();

            parameter.setValue(((BooleanParameter) object).isSelected());
            populateModuleParameters();

        } else if (((JComponent) object).getName().equals("FileParameter")) {
            HCParameter parameter = ((FileParameter) object).getParameter();

            FileDialog fileDialog = new FileDialog(new Frame(), "Select image to load", FileDialog.LOAD);
            fileDialog.setMultipleMode(false);
            fileDialog.setVisible(true);

            parameter.setValue(fileDialog.getFiles()[0].getAbsolutePath());
            ((FileParameter) object).setText(FilenameUtils.getName(parameter.getValue()));

        } else if (((JComponent) object).getName().equals("ChoiceArrayParameter")) {
            HCParameter parameter = ((ChoiceArrayParameter) object).getParameter();
            parameter.setValue(((ChoiceArrayParameter) object).getSelectedItem());

        } else if (((JComponent) object).getName().equals("OutputFilePath")) {
            FileDialog fileDialog = new FileDialog(new Frame(), "Select output path", FileDialog.SAVE);
            fileDialog.setMultipleMode(false);
            fileDialog.setVisible(true);

            outputFilePath = fileDialog.getFiles()[0].getAbsolutePath();
            populateAnalysisParameters();

        } else if (((JComponent) object).getName().equals("XMLCheck")) {
            exportXML = ((JCheckBox) object).isSelected();
            populateAnalysisParameters();

        } else if (((JComponent) object).getName().equals("XLSXCheck")) {
            exportXLSX = ((JCheckBox) object).isSelected();
            populateAnalysisParameters();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            reactToAction(e.getSource());
        } catch (IllegalAccessException | InstantiationException | IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        try {
            reactToAction(e.getSource());
        } catch (IllegalAccessException | InstantiationException | IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
