/*
Copyright 2007-2008 VTT Biotechnology
This file is part of GUINEU.
 */
package guineu.modules.statistics.Media;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.data.impl.SimpleDataset;
import guineu.data.maintable.DatasetDataModel;
import guineu.data.parser.impl.Lipidclass;
import guineu.desktop.Desktop;
import guineu.taskcontrol.Task;
import guineu.util.Tables.DataTable;
import guineu.util.Tables.impl.PushableTable;
import guineu.util.internalframe.DataInternalFrame;
import java.awt.Dimension;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author scsandra
 */
public class mediaFilterTask implements Task {

    private Dataset[] datasets;
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private Desktop desktop;
    private double progress;
    private Lipidclass lipidClass;

    public mediaFilterTask(Dataset[] datasets, Desktop desktop) {
        this.datasets = datasets;
        this.desktop = desktop;
        this.lipidClass = new Lipidclass();
    }

    public String getTaskDescription() {
        return "std Dev scores... ";
    }

    public double getFinishedPercentage() {
        return progress;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void cancel() {
        status = TaskStatus.CANCELED;
    }

    public void run() {
        try {
            this.median();
        } catch (Exception e) {
            status = TaskStatus.ERROR;
            errorMessage = e.toString();
            return;
        }
    }

    public void median() {
        status = TaskStatus.PROCESSING;
        try {
            progress = 0.0f;
            for(Dataset dataset : datasets){
                double[] median = this.getSTDDev((SimpleDataset)dataset);
                SimpleDataset newDataset = new SimpleDataset("Median - " + dataset.getDatasetName());
                newDataset.setType(dataset.getType());                
                newDataset.AddNameExperiment("Median");
                int cont = 0;

                for (PeakListRow row : ((SimpleDataset)dataset).getRows()) {
                    PeakListRow newRow = row.clone();
                    newRow.removePeaks();
                    newRow.setPeak("Median", median[cont++]);
                    newDataset.AddRow(newRow);
                }

                DatasetDataModel model = new DatasetDataModel(newDataset);

                DataTable table = new PushableTable(model);
                table.formatNumbers(11);
                DataInternalFrame frame = new DataInternalFrame("Median" + dataset.getDatasetName(), table.getTable(), new Dimension(450, 450));
                desktop.addInternalFrame(frame);
                desktop.AddNewFile(newDataset);
                frame.setVisible(true);
            }
            progress = 1f;

        } catch (Exception ex) {
        }
        status = TaskStatus.FINISHED;
    }

    public double[] getSTDDev(SimpleDataset dataset) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        double[] median = new double[dataset.getNumberRows()];
        int numRows = 0;
        for (PeakListRow peak : dataset.getRows()) {
            stats.clear();
            for(String nameExperiment : dataset.getNameExperiments()){
                stats.addValue((Double)peak.getPeak(nameExperiment));
            }
            double[] values = stats.getSortedValues();
            median[numRows++] = values[values.length/2];
        }
        return median;
    }
    
}
