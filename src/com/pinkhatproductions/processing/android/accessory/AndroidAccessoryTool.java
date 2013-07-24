/*
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation, version 3.0.
  (http://www.gnu.org/licenses/lgpl-3.0.html)

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
  
  Contributors:
      PinkHatSpike - spike@pinkhatproductions.com
      
  Heavily borrows from [TellArt's Arduino_ADK Tool](http://stream.tellart.com/controlling-arduino-with-android/)
*/

package com.pinkhatproductions.processing.android.accessory;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import processing.app.Editor;
import processing.app.Sketch;
import processing.app.Base;
import processing.app.tools.Tool;
import processing.core.PApplet;
import processing.data.XML;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class AndroidAccessoryTool extends JFrame implements Tool, ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Editor editor;

    /* GUI */
    private JPanel contentPane;
    private JTextField textModel;
    private JTextField textManufacturer;
    private JButton btnSave;
    private JButton btnRemoveAdk;
    private JLabel lblVersion;
    private JTextField textVersion;
    private JComboBox presetComboBox;

    // when creating a tool, the name of the main class which implements Tool
    // must be the same as the value defined for project.name in your build.properties
 
    public String getMenuTitle() {
        return "Android Accessory Tool";
    }
 
    public void init(Editor theEditor) {
        this.editor = theEditor;
    }
 
    public void run() {
        // save it before running, or it will not compile
        Sketch sketch = editor.getSketch();
        try {
            sketch.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        String manifest_filename = editor.getSketch().getFolder() + "/AndroidManifest.xml";
        File manifest = new File(manifest_filename);
        if(!manifest.exists()) {
            System.out.println(getMenuTitle() + ": AndroidManifest.xml doesn't alerady exist. Run sketch at least once in Android mode to create.");
            editor.statusError(getMenuTitle() + ": AndroidManifest.xml doesn't already exist. Run sketch at least once in Android mode to create.");
        }
        else {
            this.loadGUI();
            this.setVisible(true);
            loadInformation();
            System.out.println(getMenuTitle() + " v" + serialVersionUID);
            this.editor.statusNotice(getMenuTitle() + " launched");
        }
    }

    /**
     * Create the frame.
     * @return 
     */
    public void loadGUI() {
        /* GUI */
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 250, 120);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
        gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gbl_contentPane.columnWeights = new double[] { 0.0, 1.0,
                Double.MIN_VALUE };
        gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        contentPane.setLayout(gbl_contentPane);

        JLabel lblPreset = new JLabel("Preset");
        GridBagConstraints gbc_lblPreset = new GridBagConstraints();
        gbc_lblPreset.anchor = GridBagConstraints.EAST;
        gbc_lblPreset.insets = new Insets(0, 0, 5, 5);
        gbc_lblPreset.gridx = 0;
        gbc_lblPreset.gridy = 0;
        contentPane.add(lblPreset, gbc_lblPreset);
        
        String[] presetStrings = {"<none>", "ADK 2011 Demo Kit", "ADK 2012 Demo Kit", "IOIO", "Handbag", "ADK 2011 Demo Kit (legacy)"};
        presetComboBox = new JComboBox(presetStrings);
        presetComboBox.addActionListener(this);
        GridBagConstraints gbc_presetComboBox = new GridBagConstraints();
        gbc_presetComboBox.insets = new Insets(0, 0, 5, 0);
        gbc_presetComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_presetComboBox.gridx = 1;
        gbc_presetComboBox.gridy = 0;
        contentPane.add(presetComboBox, gbc_presetComboBox);
        
        JLabel lblModel = new JLabel("Model");
        GridBagConstraints gbc_lblModel = new GridBagConstraints();
        gbc_lblModel.anchor = GridBagConstraints.EAST;
        gbc_lblModel.insets = new Insets(0, 0, 5, 5);
        gbc_lblModel.gridx = 0;
        gbc_lblModel.gridy = 1;
        contentPane.add(lblModel, gbc_lblModel);

        textModel = new JTextField();
        GridBagConstraints gbc_textModel = new GridBagConstraints();
        gbc_textModel.insets = new Insets(0, 0, 5, 0);
        gbc_textModel.fill = GridBagConstraints.HORIZONTAL;
        gbc_textModel.gridx = 1;
        gbc_textModel.gridy = 1;
        contentPane.add(textModel, gbc_textModel);
        textModel.setColumns(10);

        JLabel lblManufacturer = new JLabel("Manufacturer");
        GridBagConstraints gbc_lblManufacturer = new GridBagConstraints();
        gbc_lblManufacturer.insets = new Insets(0, 0, 5, 5);
        gbc_lblManufacturer.anchor = GridBagConstraints.EAST;
        gbc_lblManufacturer.gridx = 0;
        gbc_lblManufacturer.gridy = 2;
        contentPane.add(lblManufacturer, gbc_lblManufacturer);

        textManufacturer = new JTextField();
        GridBagConstraints gbc_textManufacturer = new GridBagConstraints();
        gbc_textManufacturer.insets = new Insets(0, 0, 5, 0);
        gbc_textManufacturer.fill = GridBagConstraints.HORIZONTAL;
        gbc_textManufacturer.gridx = 1;
        gbc_textManufacturer.gridy = 2;
        contentPane.add(textManufacturer, gbc_textManufacturer);
        textManufacturer.setColumns(10);

        lblVersion = new JLabel("Version");
        GridBagConstraints gbc_lblVersion = new GridBagConstraints();
        gbc_lblVersion.anchor = GridBagConstraints.EAST;
        gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
        gbc_lblVersion.gridx = 0;
        gbc_lblVersion.gridy = 3;
        contentPane.add(lblVersion, gbc_lblVersion);

        textVersion = new JTextField();
        GridBagConstraints gbc_textVersion = new GridBagConstraints();
        gbc_textVersion.insets = new Insets(0, 0, 5, 0);
        gbc_textVersion.fill = GridBagConstraints.HORIZONTAL;
        gbc_textVersion.gridx = 1;
        gbc_textVersion.gridy = 3;
        contentPane.add(textVersion, gbc_textVersion);
        textVersion.setColumns(10);
        
        btnSave = new JButton("Write XML files");
        btnSave.addActionListener(this);
        GridBagConstraints gbc_btnSave = new GridBagConstraints();
        gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnSave.gridx = 1;
        gbc_btnSave.gridy = 4;
        contentPane.add(btnSave, gbc_btnSave);

        btnRemoveAdk = new JButton("Remove Accessory Support");
        btnRemoveAdk.addActionListener(this);
        GridBagConstraints gbc_btnRemoveAdk = new GridBagConstraints();
        gbc_btnRemoveAdk.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRemoveAdk.gridx = 1;
        gbc_btnRemoveAdk.gridy = 5;
        contentPane.add(btnRemoveAdk, gbc_btnRemoveAdk);
        
        this.pack();
        
        this.addWindowListener(new WindowAdapter() {
            // Invoked when a window is in the process of being closed.
            public void windowClosing(WindowEvent e) {
                //System.out.println(getMenuTitle() + " closed without writing XML files");
                editor.statusError(getMenuTitle() + " closed without writing XML files");
                editor.stopIndeterminate();
                /* Close */
                dispose();
            }
        });
    }

    protected void writeAccessoryFilter(String manufacturer, String model, String version) {
        String accessory_filter_filename = editor.getSketch().getFolder() + "/res/xml/accessory_filter.xml";
        File accessory_filter = new File(accessory_filter_filename);
        
        XML rootXml = new XML("resources");
        XML usb_accessory = rootXml.addChild("usb-accessory");

        if(model != null && !model.equals("")) usb_accessory.setString("model", model);
        if(manufacturer != null && !manufacturer.equals("")) usb_accessory.setString("manufacturer", manufacturer);
        if(version != null && !version.equals("")) usb_accessory.setString("version", version);
    
        final PrintWriter writer = PApplet.createWriter(accessory_filter);
        writer.print(rootXml.format(2));
        writer.flush();
        writer.close();
    }
    
    protected void writeAndroidManifest(boolean accessoryEnabled) {
        String manifest_filename = editor.getSketch().getFolder() + "/AndroidManifest.xml";
        File manifest = new File(manifest_filename);
        try {
            XML rootXml = new XML(manifest);
            
            // uses-feature
            XML[] uses_feature_array = rootXml.getChildren("uses-feature");
            
            for(int i = 0; i < uses_feature_array.length; i++) {
                XML uses_feature = uses_feature_array[i];
                if(uses_feature.hasAttribute("android:name") && uses_feature.getString("android:name").equals("android.hardware.usb.accessory")) {
                    rootXml.removeChild(uses_feature);
                }
            }
            if(accessoryEnabled) {
                XML uses_feature = rootXml.addChild("uses-feature");
                uses_feature.setString("android:name", "android.hardware.usb.accessory");
                uses_feature.setString("android:required", "false");
            }
            
            // application
            XML application = rootXml.getChild("application");
            // uses-library
            XML[] uses_library_array = application.getChildren("uses-library");
            for(int i = 0; i < uses_library_array.length; i++) {
                XML uses_library = uses_library_array[i];
                if(uses_library.hasAttribute("android:name") && uses_library.getString("android:name").equals("com.android.future.usb.accessory")) {
                    application.removeChild(uses_library);
                }
            }
            if(accessoryEnabled) {
                XML uses_library = application.addChild("uses-library");
                uses_library.setString("android:name", "com.android.future.usb.accessory");
                uses_library.setString("android:required", "false");
            }
            // activity
            XML activity = application.getChild("activity");
            if(accessoryEnabled) {
                activity.setString("android:launchMode", "singleTask");
            }
            else {
                activity.setString("android:launchMode", "standard");
            }
            // intent-filter
            XML[] intent_filter_array = activity.getChildren("intent-filter");
            for(int i = 0; i < intent_filter_array.length; i++) {
                XML intent_filter = intent_filter_array[i];
                XML action = intent_filter.getChild("action");
                if(action != null && action.hasAttribute("android:name") && action.getString("android:name").equals("android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
                    activity.removeChild(intent_filter);
                }
            }
            if(accessoryEnabled) {
                XML intent_filter = activity.addChild("intent-filter");
                XML action = intent_filter.addChild("action");
                action.setString("android:name", "android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
            }
            // meta-data
            XML[] meta_data_array = activity.getChildren("meta-data");
            for(int i = 0; i < meta_data_array.length; i++) {
                XML meta_data = meta_data_array[i];
                if(meta_data.hasAttribute("android:name") && meta_data.getString("android:name").equals("android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
                    activity.removeChild(meta_data);
                }
            }
            if(accessoryEnabled) {
                XML meta_data = activity.addChild("meta-data");
                meta_data.setString("android:name", "android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
                meta_data.setString("android:resource", "@xml/accessory_filter");
            }
            // write out
            final PrintWriter writer = PApplet.createWriter(manifest);
            writer.print(rootXml.format(2));
            writer.flush();
            writer.close();
        }
        catch (IOException exception) {
            System.out.println(getMenuTitle() + ": error writing file accessory_filter.xml: " + exception.toString());
            editor.statusError(getMenuTitle() + ": error writing file accessory_filter.xml: " + exception.toString());
        }
        catch (ParserConfigurationException exception) {
            System.out.println(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
            editor.statusError(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
        }
        catch (SAXException exception) {
            System.out.println(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
            editor.statusError(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
        }
    }
    
    protected void populateTextFields(String manufacturer, String model, String version) {
        textManufacturer.setText(manufacturer);
        textModel.setText(model);
        textVersion.setText(version);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnSave) {
            editor.startIndeterminate();
            this.editor.statusNotice(getMenuTitle() + ": writing accessory filter and manifest files");
        
            // accessory_filter.xml
            String model = textModel.getText().trim();
            String manufacturer = textManufacturer.getText().trim();
            String version = textVersion.getText().trim();
            writeAccessoryFilter(manufacturer, model, version);
        
            // AndroidManifest.xml
            writeAndroidManifest(true);

            editor.stopIndeterminate();
        
            /* Close */
            this.dispose();
        }
        else if(e.getSource() == btnRemoveAdk) {
            editor.startIndeterminate();
            this.editor.statusNotice(getMenuTitle() + ": removing Accessory support");
        
            // AndroidManifest.xml
            writeAndroidManifest(false);

            editor.stopIndeterminate();
        
            /* Close */
            this.dispose();
        }
        else if(e.getSource() == presetComboBox) {
            String name = (String)presetComboBox.getSelectedItem();
            if(name.equals("<none>")) {
                populateTextFields("", "", "");
            }
            else if(name.equals("ADK 2011 Demo Kit")) {
                populateTextFields("Google, Inc.", "DemoKit", "1.0");
            }
            else if(name.equals("ADK 2012 Demo Kit")) {
                populateTextFields("Google, Inc.", "DemoKit", "2.0");
            }
            else if(name.equals("IOIO")) {
                populateTextFields("", "IOIO", "");
            }
            else if(name.equals("Handbag")) {
                populateTextFields("rancidbacon.com", "Handbag", "0.1");
            }
            else if(name.equals("ADK 2011 Demo Kit (legacy)")) {
                populateTextFields("Arduino SA", "Mega_ADK", "1.0");
            }
        }
    }

    /**
     * Loads the information from the present xml into the GUI, if there is a file.
     */
    private void loadInformation() {
        String accessory_filter_filename = editor.getSketch().getFolder() + "/res/xml/accessory_filter.xml";
        File accessory_filter = new File(accessory_filter_filename);

        if (accessory_filter.exists()) {
            /* Load information */
            try {
                XML rootXml = new XML(accessory_filter);
                XML usb_accessory = rootXml.getChild("usb-accessory");
                if(usb_accessory != null) {
                    if(usb_accessory.hasAttribute("manufacturer")) {
                        textManufacturer.setText(usb_accessory.getString("manufacturer"));
                    }
                    if(usb_accessory.hasAttribute("model")) {
                        textModel.setText(usb_accessory.getString("model"));
                    }
                    if(usb_accessory.hasAttribute("version")) {
                        textVersion.setText(usb_accessory.getString("version"));
                    }
                }
            }
            catch (IOException exception) {
                System.out.println(getMenuTitle() + ": error reading file accessory_filter.xml: " + exception.toString());
                editor.statusError(getMenuTitle() + ": error reading file accessory_filter.xml: " + exception.toString());
            }
            catch (ParserConfigurationException exception) {
                System.out.println(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
                editor.statusError(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
            }
            catch (SAXException exception) {
                System.out.println(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
                editor.statusError(getMenuTitle() + ": error parsing file accessory_filter.xml: " + exception.toString());
            }
        }
        else {
            String model = "IOIO";
            String manufacturer = "";
            String version = "";

            populateTextFields(manufacturer, model, version);
        }
    }
}
