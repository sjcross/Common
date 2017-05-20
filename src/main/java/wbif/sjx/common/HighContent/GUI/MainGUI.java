// TODO: Add controls for all parameter types (Measurement, arrays, hashsets, etc.)

package wbif.sjx.common.HighContent.GUI;

import org.reflections.Reflections;
import wbif.sjx.common.HighContent.Module.*;
import wbif.sjx.common.HighContent.Object.HCModuleCollection;
import wbif.sjx.common.HighContent.Object.HCName;
import wbif.sjx.common.HighContent.Object.HCParameter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private int frameWidth = 600;
    private int frameHeight = 750;
    private int elementHeight = 30;

    private ModuleButton activeModuleButton = null;
    private Frame frame = new JFrame();
    private JPanel modulesPanel = new JPanel();
    private JPanel paramsPanel = new JPanel();
    JPopupMenu moduleListMenu = new JPopupMenu();

    private HCModuleCollection modules = new HCModuleCollection();


    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        MainGUI mainGUI = new MainGUI();

    }

    public MainGUI() throws InstantiationException, IllegalAccessException {
        // DEBUG CALLS
        modules = new HCModuleCollection();

        // Getting current image open in ImageJ (must be 2 channel)
        ImageJImageLoader IJLoader = new ImageJImageLoader();
        modules.add(IJLoader);

        // Splitting stack into two
        ChannelExtractor chExtractor1 = new ChannelExtractor();
        modules.add(chExtractor1);

        // Splitting stack into two
        ChannelExtractor chExtractor2 = new ChannelExtractor();
        modules.add(chExtractor2);

        // Running TrackMate and storing tracks as objects
        RunTrackMate runTrackMate = new RunTrackMate();
        modules.add(runTrackMate);

        // Showing objects
        ShowObjects showObjects = new ShowObjects();
        modules.add(showObjects);

        // Measuring track intensity
        MeasureTrackIntensity trackIntensity = new MeasureTrackIntensity();
        modules.add(trackIntensity);

        // END DEBUG CALLS



        frame.setLayout(new FlowLayout());

        // Setting location of panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width-frameWidth)/2,(screenSize.height-frameHeight)/2);
        frame.setSize(frameWidth,frameHeight);
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
        panel.setPreferredSize(new Dimension(buttonSize,frameHeight));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;
        c.insets = new Insets(0,0,5,0);
        c.anchor = GridBagConstraints.PAGE_START;

        // Add module button
        JButton addModuleButton = new JButton(addModuleText);
        addModuleButton.setPreferredSize(new Dimension(buttonSize,buttonSize));
        addModuleButton.addActionListener(this);
        panel.add(addModuleButton,c);

        // Remove module button
        JButton removeModuleButton = new JButton(removeModuleText);
        removeModuleButton.setPreferredSize(new Dimension(buttonSize,buttonSize));
        removeModuleButton.addActionListener(this);
        c.gridy++;
        panel.add(removeModuleButton,c);

        // Move module up button
        JButton moveModuleUpButton = new JButton(moveModuleUpText);
        moveModuleUpButton.setPreferredSize(new Dimension(buttonSize,buttonSize));
        moveModuleUpButton.addActionListener(this);
        c.gridy++;
        panel.add(moveModuleUpButton,c);

        // Move module up button
        JButton moveModuleDownButton = new JButton(moveModuleDownText);
        moveModuleDownButton.setPreferredSize(new Dimension(buttonSize,buttonSize));
        moveModuleDownButton.addActionListener(this);
        c.gridy++;
        panel.add(moveModuleDownButton,c);

        // Start analysis button
        JButton startAnalysisButton = new JButton(startAnalysisText);
        startAnalysisButton.setPreferredSize(new Dimension(buttonSize,buttonSize));
        startAnalysisButton.addActionListener(this);
        c.gridy++;
        c.weighty = 1;
        c.anchor = GridBagConstraints.PAGE_END;
        panel.add(startAnalysisButton,c);

        panel.validate();
        panel.repaint();

        return panel;

    }

    private void populateModuleList() {
        int buttonWidth = 200;

        modulesPanel.removeAll();

        modulesPanel.setPreferredSize(new Dimension(buttonWidth,frameHeight));
        modulesPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(0,0,5,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;

        Iterator<HCModule> iterator = modules.iterator();
        while (iterator.hasNext()) {
            HCModule module = iterator.next();
            ModuleButton button = new ModuleButton(module);
            button.addActionListener(this);
            c.gridy++;
            if (!iterator.hasNext()) c.weighty = 1;
            if (activeModuleButton != null) {
                if (module == activeModuleButton.getModule()) button.setSelected(true);
            }
            modulesPanel.add(button,c);

        }

        modulesPanel.validate();
        modulesPanel.repaint();

    }

    private void initialiseParametersPanel() {
        paramsPanel.removeAll();

        paramsPanel.setLayout(new GridBagLayout());
        paramsPanel.setPreferredSize(new Dimension(500,700));

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
        c.insets = new Insets(0,0,5,0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        HCModule activeModule = activeModuleButton.getModule();

        Iterator<HCParameter> iterator = activeModule.getActiveParameters().getParameters().values().iterator();
        while (iterator.hasNext()) {
            HCParameter parameter = iterator.next();

            c.gridx = 0;
            if (!iterator.hasNext()) c.weighty = 1;

            JTextField parameterName = new JTextField(parameter.getName());
            parameterName.setPreferredSize(new Dimension(200,elementHeight));
            parameterName.setEditable(false);
            parameterName.setBorder(null);
            paramsPanel.add(parameterName,c);

            JComponent parameterControl = null;

            if (parameter.getType()==HCParameter.INPUT_IMAGE) {
                // Getting a list of available images
                ArrayList<HCParameter> images = modules.getParametersMatchingType(HCParameter.OUTPUT_IMAGE,activeModule);

                parameterControl = new HCNameInputParameter(parameter);
                for (HCParameter image:images) {
                    ((HCNameInputParameter) parameterControl).addItem(image.getValue());

                }
                ((HCNameInputParameter) parameterControl).setSelectedItem(parameter.getValue());

            } else if (parameter.getType()==HCParameter.INPUT_OBJECTS) {
                // Getting a list of available images
                ArrayList<HCParameter> images = modules.getParametersMatchingType(HCParameter.OUTPUT_OBJECTS,activeModule);

                parameterControl = new HCNameInputParameter(parameter);
                for (HCParameter image:images) {
                    ((HCNameInputParameter) parameterControl).addItem(image.getValue());

                }
                ((HCNameInputParameter) parameterControl).setSelectedItem(parameter.getValue());

            } else if (parameter.getType()==HCParameter.OUTPUT_IMAGE | parameter.getType()==HCParameter.OUTPUT_OBJECTS | parameter.getType()==HCParameter.INTEGER | parameter.getType()==HCParameter.DOUBLE | parameter.getType()==HCParameter.STRING) {
                parameterControl = new TextParameter(parameter);
                String name = parameter.getValue() == null ? "" : parameter.getValue().toString();
                ((TextParameter) parameterControl).setText(name);

            } else if (parameter.getType()==HCParameter.BOOLEAN) {
                parameterControl = new BooleanParameter(parameter);
                ((BooleanParameter) parameterControl).setSelected(parameter.getValue());
                ((BooleanParameter) parameterControl).addActionListener(this);

            }

            c.gridx++;
            c.weightx = 1;
            if (parameterControl != null) {
                parameterControl.addFocusListener(this);
                paramsPanel.add(parameterControl,c);
                parameterControl.setPreferredSize(new Dimension(200,elementHeight));

            }

            c.gridy++;

        }

        paramsPanel.validate();
        paramsPanel.repaint();

    }

    private void listAvailableModules() throws IllegalAccessException, InstantiationException {
        // Using Reflections tool to get list of classes extending HCModule
        Reflections reflections = new Reflections("wbif.sjx.common");
        Set<Class<? extends HCModule>> availableModules = reflections.getSubTypesOf(HCModule.class);

        // Creating new instances of these classes and adding to ArrayList
        ArrayList<HCModule> availableModulesList = new ArrayList<>();
        for (Class clazz:availableModules) {
                availableModulesList.add((HCModule) clazz.newInstance());
        }

        // Sorting the ArrayList based on module title
        Collections.sort(availableModulesList, Comparator.comparing(HCModule::getTitle));

        // Adding the modules to the list
        for (HCModule module:availableModulesList) {
            PopupMenuItem menuItem = new PopupMenuItem(module);
            menuItem.addActionListener(this);
            moduleListMenu.add(menuItem);

        }

        // Adding a close list option
        PopupMenuItem closeItem = new PopupMenuItem(null);
        closeItem.setText("[Close list]");
        closeItem.addActionListener(this);
        moduleListMenu.add(closeItem);

    }

    private void addModule() {
        moduleListMenu.addMouseListener(this);
        moduleListMenu.setLocation(frame.getX(),frame.getY());
        moduleListMenu.setVisible(true);

    }

    private void removeModule() {
        if (activeModuleButton != null) {
            modules.remove(activeModuleButton.getModule());
            activeModuleButton = null;
            populateModuleList();
            initialiseParametersPanel();
        }
    }

    private void moveModuleUp() {
        if (activeModuleButton != null) {
            int pos = modules.indexOf(activeModuleButton.getModule());
            if (pos != 0) {
                modules.remove(activeModuleButton.getModule());
                modules.add(pos - 1, activeModuleButton.getModule());
                populateModuleList();
            }
        }
    }

    private void moveModuleDown() {
        if (activeModuleButton != null) {
            int pos = modules.indexOf(activeModuleButton.getModule());
            if (pos != modules.size()) {
                modules.remove(activeModuleButton.getModule());
                modules.add(pos + 1, activeModuleButton.getModule());
                populateModuleList();
            }
        }
    }

    private void reactToAction(Object object) throws IllegalAccessException, InstantiationException {
        if (object instanceof JButton) {
            if (((JButton) object).getText().equals(addModuleText)) {
                addModule();

            } else if (((JButton) object).getText().equals(removeModuleText)) {
                removeModule();

            } else if (((JButton) object).getText().equals(moveModuleUpText)) {
                moveModuleUp();

            } else if (((JButton) object).getText().equals(moveModuleDownText)) {
                moveModuleDown();

            }

        } else if (object instanceof PopupMenuItem) {
            moduleListMenu.setVisible(false);

            if (((PopupMenuItem) object).getModule() == null) {
                return;

            }

            // Adding it after the currently-selected module
            if (activeModuleButton != null) {
                int pos = modules.indexOf(activeModuleButton.getModule());
                modules.add(pos + 1, ((PopupMenuItem) object).getModule().getClass().newInstance());

            } else {
                modules.add(((PopupMenuItem) object).getModule().getClass().newInstance());

            }

            populateModuleList();


        } else if (object instanceof ModuleButton) {
            // Deselecting last pressed button (if there is one)
            if (activeModuleButton != null) {
                if (activeModuleButton != object) activeModuleButton.setSelected(false);
            }

            // Getting new button and displaying parameters
            activeModuleButton = (ModuleButton) object;
            populateModuleParameters();

        } else if (object instanceof HCNameInputParameter) {
            HCParameter parameter = ((HCNameInputParameter) object).getParameter();
            parameter.setValue(((HCNameInputParameter) object).getSelectedItem());

        } else if (object instanceof TextParameter) {
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

        } else if (object instanceof BooleanParameter) {
            HCParameter parameter = ((BooleanParameter) object).getParameter();

            parameter.setValue(((BooleanParameter) object).isSelected());
            populateModuleParameters();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            reactToAction(e.getSource());
        } catch (IllegalAccessException | InstantiationException e1) {
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
        } catch (IllegalAccessException | InstantiationException e1) {
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
