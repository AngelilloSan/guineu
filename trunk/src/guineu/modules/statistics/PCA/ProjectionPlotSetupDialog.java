/*
 * Copyright 2007-2010 VTT Biotechnology
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

package guineu.modules.statistics.PCA;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.util.GUIUtils;
import guineu.util.PeakListRowSorter;
import guineu.util.SortingDirection;
import guineu.util.SortingProperty;
import guineu.util.components.ExtendedCheckBox;
import guineu.util.dialogs.ExitCode;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 * 
 */
public class ProjectionPlotSetupDialog extends JDialog implements
        ActionListener {

    static final int PADDING_SIZE = 5;
    private ExitCode exitCode = ExitCode.CANCEL;
    private Desktop desktop;
    private Dataset alignedPeakList;
    private ProjectionPlotParameters parameterSet;

    // dialog components
    private JComboBox comboColoringMethod;
    private JComboBox comboXAxisComponent;
    private JComboBox comboYAxisComponent;
    private String[] parametersInCombo;
    private Vector<ExtendedCheckBox<String>> rawDataFileCheckBoxes;
    private Vector<ExtendedCheckBox<PeakListRow>> peakCheckBoxes;
    private JButton btnSelectAllFiles,  btnDeselectAllFiles,  btnSelectAllPeaks,  btnDeselectAllPeaks,  btnOK,  btnCancel;

    public ProjectionPlotSetupDialog(Dataset peakList,
            ProjectionPlotParameters parameterSet, boolean forceXYComponents) {

        // make dialog modal
        super(GuineuCore.getDesktop().getMainFrame(), "Projection plot setup",
                true);

        this.desktop = GuineuCore.getDesktop();
        this.alignedPeakList = peakList;
        this.parameterSet = parameterSet;

        Vector<String> selectedDataFiles = parameterSet.getSelectedDataFiles();
        List<PeakListRow> selectedRows = parameterSet.getSelectedRows();

        GridBagConstraints constraints = new GridBagConstraints();

        // set default layout constraints
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(PADDING_SIZE, PADDING_SIZE,
                PADDING_SIZE, PADDING_SIZE);

        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        JComponent comp;
        GridBagLayout layout = new GridBagLayout();

        JPanel components = new JPanel(layout);

        comp = GUIUtils.addLabel(components, "Data files");
        constraints.gridx = 0;
        constraints.gridy = 0;
        layout.setConstraints(comp, constraints);

        JPanel dataFileCheckBoxesPanel = new JPanel();
        dataFileCheckBoxesPanel.setBackground(Color.white);
        dataFileCheckBoxesPanel.setLayout(new BoxLayout(
                dataFileCheckBoxesPanel, BoxLayout.Y_AXIS));
        rawDataFileCheckBoxes = new Vector<ExtendedCheckBox<String>>();
        int minimumHorizSize = 0;
        Vector<String> files = alignedPeakList.getAllColumnNames();
        for (int i = 0; i < files.size(); i++) {
            ExtendedCheckBox<String> ecb = new ExtendedCheckBox<String>(
                    files.elementAt(i), selectedDataFiles.contains(files.elementAt(i)));
            rawDataFileCheckBoxes.add(ecb);
            minimumHorizSize = Math.max(minimumHorizSize,
                    ecb.getPreferredWidth());
            dataFileCheckBoxesPanel.add(ecb);
        }
        int minimumVertSize = (int) rawDataFileCheckBoxes.get(0).getPreferredSize().getHeight() * 3;
        JScrollPane dataFilePanelScroll = new JScrollPane(
                dataFileCheckBoxesPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dataFilePanelScroll.setPreferredSize(new Dimension(minimumHorizSize,
                minimumVertSize));
        constraints.gridx = 1;
        components.add(dataFilePanelScroll, constraints);

        JPanel dataFileButtonsPanel = new JPanel();
        dataFileButtonsPanel.setLayout(new BoxLayout(dataFileButtonsPanel,
                BoxLayout.Y_AXIS));
        btnSelectAllFiles = GUIUtils.addButton(dataFileButtonsPanel,
                "Select all", null, this);
        btnDeselectAllFiles = GUIUtils.addButton(dataFileButtonsPanel,
                "Deselect all", null, this);
        dataFileButtonsPanel.add(Box.createGlue());
        constraints.gridx = 2;
        components.add(dataFileButtonsPanel);
        layout.setConstraints(dataFileButtonsPanel, constraints);

        comp = GUIUtils.addLabel(components, "Coloring style");
        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(comp, constraints);

        String projectParameters[] = peakList.getParametersName().toArray(new String[0]);
        Object availableColoringStyles[] = new Object[projectParameters.length + 2];
        parametersInCombo = new String[projectParameters.length + 2];
        availableColoringStyles[0] = ProjectionPlotParameters.ColoringTypeSingleColor;
        availableColoringStyles[1] = ProjectionPlotParameters.ColoringTypeByFile;
        parametersInCombo[0] = null;
        parametersInCombo[1] = null;
        for (int i = 0; i < projectParameters.length; i++) {
            availableColoringStyles[i + 2] = "Color by parameter "
                    + projectParameters[i];
            parametersInCombo[i + 2] = projectParameters[i];
        }
        comboColoringMethod = new JComboBox(availableColoringStyles);
        if (parameterSet.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeSingleColor) {
            comboColoringMethod.setSelectedItem(ProjectionPlotParameters.ColoringTypeSingleColor);
        }
        if (parameterSet.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeByFile) {
            comboColoringMethod.setSelectedItem(ProjectionPlotParameters.ColoringTypeByFile);
        }
        if ((parameterSet.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeByParameterValue) && (parameterSet.getSelectedParameter() != null)) {
            comboColoringMethod.setSelectedItem(parameterSet.getSelectedParameter());
        }
        constraints.gridx = 1;
        components.add(comboColoringMethod, constraints);

        comp = GUIUtils.addLabel(components, "Peak measuring approach");
        constraints.gridx = 0;
        constraints.gridy = 2;
        layout.setConstraints(comp, constraints);

        comp = GUIUtils.addLabel(components, "Peaks");
        constraints.gridx = 0;
        constraints.gridy = 3;
        layout.setConstraints(comp, constraints);

        JPanel peakCheckBoxesPanel = new JPanel();
        peakCheckBoxesPanel.setBackground(Color.white);
        peakCheckBoxesPanel.setLayout(new BoxLayout(peakCheckBoxesPanel,
                BoxLayout.Y_AXIS));
        peakCheckBoxes = new Vector<ExtendedCheckBox<PeakListRow>>();
        minimumHorizSize = 0;
        List<PeakListRow> rows = alignedPeakList.getRows();
        Collections.sort(rows, new PeakListRowSorter(SortingProperty.MZ,
                SortingDirection.Ascending));
        for (int i = 0; i < rows.size(); i++) {
            ExtendedCheckBox<PeakListRow> ecb = new ExtendedCheckBox<PeakListRow>(
                    rows.get(i), selectedRows.contains(rows.get(i)));
            peakCheckBoxes.add(ecb);
            minimumHorizSize = Math.max(minimumHorizSize,
                    ecb.getPreferredWidth());
            peakCheckBoxesPanel.add(ecb);
        }
        minimumVertSize = (int) peakCheckBoxes.get(0).getPreferredSize().getHeight() * 6;
        JScrollPane peakPanelScroll = new JScrollPane(peakCheckBoxesPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        peakPanelScroll.setPreferredSize(new Dimension(minimumHorizSize,
                minimumVertSize));
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        components.add(peakPanelScroll, constraints);

        JPanel peakButtonsPanel = new JPanel();
        peakButtonsPanel.setLayout(new BoxLayout(peakButtonsPanel,
                BoxLayout.Y_AXIS));
        btnSelectAllPeaks = GUIUtils.addButton(peakButtonsPanel, "Select all",
                null, this);
        btnDeselectAllPeaks = GUIUtils.addButton(peakButtonsPanel,
                "Deselect all", null, this);
        peakButtonsPanel.add(Box.createGlue());
        constraints.gridx = 2;
        components.add(peakButtonsPanel);
        layout.setConstraints(peakButtonsPanel, constraints);

        // Controls for selecting components to be shown on the x- and y-axes
        comp = GUIUtils.addLabel(components, "Component on X-axis");
        constraints.gridx = 0;
        constraints.gridy = 4;
        layout.setConstraints(comp, constraints);

        Object[] availableXComponents = ProjectionPlotParameters.xAxisComponent.getPossibleValues();
        comboXAxisComponent = new JComboBox(availableXComponents);
        comboXAxisComponent.setSelectedItem(parameterSet.getParameterValue(ProjectionPlotParameters.xAxisComponent));
        constraints.gridx = 1;
        if (forceXYComponents) {
            comboXAxisComponent.setSelectedItem(availableXComponents[0]);
            components.add(new JLabel(availableXComponents[0].toString()),
                    constraints);
        } else {
            components.add(comboXAxisComponent, constraints);
        }

        comp = GUIUtils.addLabel(components, "Component on Y-axis");
        constraints.gridx = 0;
        constraints.gridy = 5;
        layout.setConstraints(comp, constraints);

        Object[] availableYComponents = ProjectionPlotParameters.yAxisComponent.getPossibleValues();
        comboYAxisComponent = new JComboBox(availableYComponents);
        comboYAxisComponent.setSelectedItem(parameterSet.getParameterValue(ProjectionPlotParameters.yAxisComponent));
        constraints.gridx = 1;
        if (forceXYComponents) {
            comboYAxisComponent.setSelectedItem(availableYComponents[1]);
            components.add(new JLabel(availableYComponents[1].toString()),
                    constraints);
        } else {
            components.add(comboYAxisComponent, constraints);
        }

        comp = GUIUtils.addSeparator(components, PADDING_SIZE);
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        layout.setConstraints(comp, constraints);

        JPanel buttonsPanel = new JPanel();
        btnOK = GUIUtils.addButton(buttonsPanel, "OK", null, this);
        btnCancel = GUIUtils.addButton(buttonsPanel, "Cancel", null, this);
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 3;
        components.add(buttonsPanel, constraints);

        GUIUtils.addMargin(components, PADDING_SIZE);
        add(components);

        // finalize the dialog
        pack();
        setLocationRelativeTo(desktop.getMainFrame());
        setResizable(true);

    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {

        Object src = event.getSource();

        if (src == btnOK) {

            Vector<String> selectedFiles = new Vector<String>();
            Vector<PeakListRow> selectedPeaks = new Vector<PeakListRow>();

            for (ExtendedCheckBox<String> box : rawDataFileCheckBoxes) {
                if (box.isSelected()) {
                    selectedFiles.add(box.getObject());
                }
            }

            // Validate that there are at least equally many files as the
            // maximum selected PC
            int numPC = (Integer) comboXAxisComponent.getSelectedItem();
            if (numPC > selectedFiles.size()) {
                GuineuCore.getDesktop().displayMessage(
                        "Selected PC (" + numPC
                                + ") for X-axis too high with only "
                                + selectedFiles.size()
                                + " files selected currently.");
                return;
            }
            numPC = (Integer) comboYAxisComponent.getSelectedItem();
            if (numPC > selectedFiles.size()) {
                GuineuCore.getDesktop().displayMessage(
                        "Selected PC (" + numPC
                                + ") for Y-axis too high with only "
                                + selectedFiles.size()
                                + " files selected currently.");
                return;
            }

            for (ExtendedCheckBox<PeakListRow> box : peakCheckBoxes) {
                if (box.isSelected())
                    selectedPeaks.add(box.getObject());
            }

            if (selectedFiles.size() == 0) {
                desktop.displayErrorMessage("Please select at least one data file");
                return;
            }

            if (selectedPeaks.size() == 0) {
                desktop.displayErrorMessage("Please select at least one peak");
                return;
            }
            parameterSet.setSourcePeakList(alignedPeakList);
            parameterSet.setSelectedDataFiles(selectedFiles);
            parameterSet.setSelectedRows(selectedPeaks);

            if (comboColoringMethod.getSelectedItem() == ProjectionPlotParameters.ColoringTypeSingleColor) {
                parameterSet.setParameterValue(
                        ProjectionPlotParameters.coloringType,
                        ProjectionPlotParameters.ColoringTypeSingleColor);
            }
            if (comboColoringMethod.getSelectedItem() == ProjectionPlotParameters.ColoringTypeByFile) {
                parameterSet.setParameterValue(
                        ProjectionPlotParameters.coloringType,
                        ProjectionPlotParameters.ColoringTypeByFile);
            }
            if (comboColoringMethod.getSelectedIndex() > 1) {
                parameterSet.setParameterValue(
                        ProjectionPlotParameters.coloringType,
                        ProjectionPlotParameters.ColoringTypeByParameterValue);
                String selectedParameter = parametersInCombo[comboColoringMethod.getSelectedIndex()];
                parameterSet.setSelectedParameter(selectedParameter);
            }

            
            parameterSet.setParameterValue(
                    ProjectionPlotParameters.xAxisComponent,
                    comboXAxisComponent.getSelectedItem());
            parameterSet.setParameterValue(
                    ProjectionPlotParameters.yAxisComponent,
                    comboYAxisComponent.getSelectedItem());

            exitCode = ExitCode.OK;
            dispose();
            return;
        }

        if (src == btnCancel) {
            exitCode = ExitCode.CANCEL;
            dispose();
            return;
        }

        if (src == btnSelectAllFiles) {
            for (JCheckBox box : rawDataFileCheckBoxes) {
                box.setSelected(true);
            }
            return;
        }

        if (src == btnDeselectAllFiles) {
            for (JCheckBox box : rawDataFileCheckBoxes) {
                box.setSelected(false);
            }
            return;
        }

        if (src == btnSelectAllPeaks) {
            for (JCheckBox box : peakCheckBoxes) {
                box.setSelected(true);
            }
            return;
        }

        if (src == btnDeselectAllPeaks) {
            for (JCheckBox box : peakCheckBoxes) {
                box.setSelected(false);
            }
            return;
        }

    }

    public ExitCode getExitCode() {
        return exitCode;
    }
}
