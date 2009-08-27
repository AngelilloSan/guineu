/*
 * Copyright 2007-2008 VTT Biotechnology
 * This file is part of Guineu.
 *
 * Guineu is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Guineu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Guineu; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */

package guineu.desktop.impl;

import guineu.data.Dataset;
import guineu.data.ParameterSet;
import guineu.desktop.Desktop;
import guineu.desktop.GuineuMenu;
import guineu.main.GuineuCore;
import guineu.main.GuineuModule;
import guineu.data.impl.SimpleGCGCDataset;
import guineu.modules.mylly.gcgcaligner.datastruct.GCGCData;
import guineu.taskcontrol.impl.TaskControllerImpl;
import guineu.util.components.TaskProgressWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;



/**
 * This class is the main window of application
 * 
 */
public class MainWindow extends JFrame implements GuineuModule, Desktop,
        WindowListener{

    private DesktopParameters parameters;

    private JDesktopPane desktopPane;

    private JSplitPane split;

    private ItemSelector itemSelector;

    private TaskProgressWindow taskList;
    
    private HelpClass help;

    public TaskProgressWindow getTaskList() {
        return taskList;
    }

    private MainMenu menuBar;

    private Statusbar statusBar;

    public MainMenu getMainMenu() {
        return menuBar;
    }

    public void addInternalFrame(JInternalFrame frame){        
        desktopPane.add(frame); 
        frame.setVisible(true);        
        // TODO: adjust frame position
        
    }

    /**
     * This method returns the desktop
     */
    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    /**
     * WindowListener interface implementation
     */
    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        GuineuCore.exitGuineu();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void setStatusBarText(String text) {
        setStatusBarText(text, Color.black);
    }

    
    public void displayMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sorry",
                JOptionPane.ERROR_MESSAGE);
    }

    public void addMenuItem(GuineuMenu parentMenu, JMenuItem newItem) {
        menuBar.addMenuItem(parentMenu, newItem);
    }

    
    /**
     */
    public void initModule() {

        SwingParameters.initSwingParameters();       

        parameters = new DesktopParameters();

        try {
            BufferedImage GuineuIcon = ImageIO.read(new File(
                    "icons/GuineuIcon.png"));
            setIconImage(GuineuIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize item selector
        itemSelector = new ItemSelector(this);

        // Place objects on main window
        desktopPane = new JDesktopPane();

        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, itemSelector,
                desktopPane);

        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        desktopPane.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(split, BorderLayout.CENTER);

        statusBar = new Statusbar();
        c.add(statusBar, BorderLayout.SOUTH);

        // Construct menu
        menuBar = new MainMenu();
		help = new HelpClass();
		help.addMenuItem(menuBar);
        setJMenuBar(menuBar);

        // Initialize window listener for responding to user events
        addWindowListener(this);

        pack();

        // TODO: check screen size?
        setBounds(0, 0, 1000, 700);
        setLocationRelativeTo(null);

        // Application wants to control closing by itself
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setTitle("Guineu");

        taskList = new TaskProgressWindow(
                (TaskControllerImpl) GuineuCore.getTaskController());
        desktopPane.add(taskList, JLayeredPane.DEFAULT_LAYER);

    }

   
    public JFrame getMainFrame() {
        return this;
    }

    public HelpClass getHelp() {
        return help;
    }

   
    public JMenuItem addMenuItem(GuineuMenu parentMenu, String text,
            String toolTip, int mnemonic, ActionListener listener,
            String actionCommand) {
        return menuBar.addMenuItem(parentMenu, text, toolTip, mnemonic,
                listener, actionCommand);
    }

    
    public void addMenuSeparator(GuineuMenu parentMenu) {
        menuBar.addMenuSeparator(parentMenu);

    }

    
    public JInternalFrame getSelectedFrame() {
        return desktopPane.getSelectedFrame();        
    }

    
    public JInternalFrame[] getInternalFrames() {
        return desktopPane.getAllFrames();
    }

   
    public void setStatusBarText(String text, Color textColor) {
        statusBar.setStatusText(text, textColor);
    }

   
    
    public DesktopParameters getParameterSet() {
        return parameters;
    }

   
    public void setParameters(ParameterSet parameterValues) {
        this.parameters = (DesktopParameters) parameterValues;
    }

    public ItemSelector getItemSelector() {
        return itemSelector;
    }

    public Dataset[] getSelectedDataFiles() {
        return this.itemSelector.getSelectedDatasets();
    }	
    
    /*public Vector[] getSelectedExperiments() {
        return this.itemSelector.getSelectedExperiments();
    }*/
    
    public void AddNewFile(Dataset dataset){
        this.itemSelector.addNewFile(dataset);
    }	    
    
    public void removeData(Dataset file) { 
        this.itemSelector.removeData(file);
    }      

   
}
