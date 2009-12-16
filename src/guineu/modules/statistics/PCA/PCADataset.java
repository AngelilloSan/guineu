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

package guineu.modules.statistics.PCA;

import guineu.data.Parameter;
import guineu.data.PeakListRow;
import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.taskcontrol.TaskStatus;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import jmprojection.PCA;
import jmprojection.Preprocess;
import jmprojection.ProjectionStatus;

import org.jfree.data.xy.AbstractXYDataset;

public class PCADataset extends AbstractXYDataset implements
        ProjectionPlotDataset {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private double[] component1Coords;
    private double[] component2Coords;
    private ProjectionPlotParameters parameters;
    private Parameter selectedParameter;
    private Vector<String> selectedRawDataFiles;
    private List<PeakListRow> selectedRows;
    private int[] groupsForSelectedRawDataFiles;
    private Object[] parameterValuesForGroups;
    int numberOfGroups;
    private String datasetTitle;
    private int xAxisPC;
    private int yAxisPC;
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private ProjectionStatus projectionStatus;

    public PCADataset(ProjectionPlotParameters parameters) {

        this.parameters = parameters;

        this.xAxisPC = (Integer) parameters.getParameterValue(ProjectionPlotParameters.xAxisComponent);
        this.yAxisPC = (Integer) parameters.getParameterValue(ProjectionPlotParameters.yAxisComponent);

        selectedParameter = parameters.getSelectedParameter();
        selectedRawDataFiles = parameters.getSelectedDataFiles();
        selectedRows = parameters.getSelectedRows();

        datasetTitle = "Principal component analysis";

        // Determine groups for selected raw data files
        groupsForSelectedRawDataFiles = new int[selectedRawDataFiles.size()];

        if (parameters.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeSingleColor) {
            // All files to a single group
            for (int ind = 0; ind < selectedRawDataFiles.size(); ind++) {
                groupsForSelectedRawDataFiles[ind] = 0;
            }

            numberOfGroups = 1;
        }

        if (parameters.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeByFile) {
            // Each file to own group
            for (int ind = 0; ind < selectedRawDataFiles.size(); ind++) {
                groupsForSelectedRawDataFiles[ind] = ind;
            }

            numberOfGroups = selectedRawDataFiles.size();
        }

        if (parameters.getParameterValue(ProjectionPlotParameters.coloringType) == ProjectionPlotParameters.ColoringTypeByParameterValue) {
            // Group files with same parameter value to same group
          /*  MZmineProject project = MZmineCore.getCurrentProject();
            
            for (String rawDataFile : selectedRawDataFiles) {
                Object paramValue = project.getParameterValue(
                        selectedParameter, rawDataFile);
                if (!availableParameterValues.contains(paramValue)) {
                    availableParameterValues.add(paramValue);
                }
            }*/

            /*for (int ind = 0; ind < selectedRawDataFiles.size(); ind++) {
                Object paramValue = project.getParameterValue(
                        selectedParameter, selectedRawDataFiles[ind]);
                groupsForSelectedRawDataFiles[ind] = availableParameterValues.indexOf(paramValue);
            }*/
            Vector<Object> availableParameterValues = new Vector<Object>();
            parameterValuesForGroups = availableParameterValues.toArray();

            numberOfGroups = parameterValuesForGroups.length;
        }

    }

    public String toString() {
        return datasetTitle;
    }

    public String getXLabel() {
        if (xAxisPC == 1) {
            return "1st PC";
        }
        if (xAxisPC == 2) {
            return "2nd PC";
        }
        if (xAxisPC == 3) {
            return "3rd PC";
        }
        return "" + xAxisPC + "th PC";
    }

    public String getYLabel() {
        if (yAxisPC == 1) {
            return "1st PC";
        }
        if (yAxisPC == 2) {
            return "2nd PC";
        }
        if (yAxisPC == 3) {
            return "3rd PC";
        }
        return "" + yAxisPC + "th PC";
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return 1;
    }

    public int getItemCount(int series) {
        return component1Coords.length;
    }

    public Number getX(int series, int item) {
        return component1Coords[item];
    }

    public Number getY(int series, int item) {
        return component2Coords[item];
    }

    public String getRawDataFile(int item) {
        return selectedRawDataFiles.elementAt(item);
    }

    public int getGroupNumber(int item) {
        return groupsForSelectedRawDataFiles[item];
    }

    public Object getGroupParameterValue(int groupNumber) {
        if (parameterValuesForGroups == null) {
            return null;
        }
        if ((parameterValuesForGroups.length - 1) < groupNumber) {
            return null;
        }
        return parameterValuesForGroups[groupNumber];
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public void run() {

        status = TaskStatus.PROCESSING;

        logger.info("Computing projection plot");

        double[][] rawData = new double[selectedRawDataFiles.size()][selectedRows.size()];
        for (int rowIndex = 0; rowIndex < selectedRows.size(); rowIndex++) {
            PeakListRow peakListRow = selectedRows.get(rowIndex);
            for (int fileIndex = 0; fileIndex < selectedRawDataFiles.size(); fileIndex++) {
                String rawDataFile = selectedRawDataFiles.elementAt(fileIndex);
                Object p = peakListRow.getPeak(rawDataFile);
                try {
                    rawData[fileIndex][rowIndex] = (Double) p;
                } catch (Exception e) {
                }

            }
        }

        int numComponents = xAxisPC;
        if (yAxisPC > numComponents) {
            numComponents = yAxisPC;
        }

        // Scale data and do PCA
        Preprocess.scaleToUnityVariance(rawData);
        PCA pcaProj = new PCA(rawData, numComponents);
        projectionStatus = pcaProj.getProjectionStatus();

        double[][] result = pcaProj.getState();

        if (status == TaskStatus.CANCELED) {
            return;
        }

        component1Coords = result[xAxisPC - 1];
        component2Coords = result[yAxisPC - 1];


        Desktop desktop = GuineuCore.getDesktop();
        ProjectionPlotWindow newFrame = new ProjectionPlotWindow(desktop, this,
                parameters);
        desktop.addInternalFrame(newFrame);

        status = TaskStatus.FINISHED;
        logger.info("Finished computing projection plot.");

    }

    public void cancel() {
        if (projectionStatus != null) {
            projectionStatus.cancel();
        }
        status = TaskStatus.CANCELED;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getTaskDescription() {
        if ((parameters == null) || (parameters.getSourcePeakList() == null)) {
            return "PCA projection";
        }
        return "PCA projection " + parameters.getSourcePeakList();
    }

    public double getFinishedPercentage() {
        if (projectionStatus == null) {
            return 0;
        }
        return projectionStatus.getFinishedPercentage();
    }

    public Object[] getCreatedObjects() {
        return null;
    }
}
