/*
Copyright 2007-2008 VTT Biotechnology
This file is part of GUINEU.
 */
package guineu.modules.file.saveDatasetDB;

import guineu.data.Dataset;
import guineu.data.impl.DatasetType;
import guineu.data.impl.SimpleDataset;
import guineu.database.intro.InDataBase;
import guineu.database.intro.InOracle;
import guineu.taskcontrol.Task;
import java.sql.Connection;

/**
 *
 * @author scsandra
 */
public class SaveFileDBTask implements Task {

    private SimpleDataset dataset;
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private SaveFileParameters parameters;
    private String author;
    private InDataBase db;

    public SaveFileDBTask(Dataset dataset, SaveFileParameters parameters) {
        this.dataset = (SimpleDataset) dataset;
        this.parameters = parameters;
        this.author = (String) parameters.getParameterValue(SaveFileParameters.author);
        db = new InOracle();
    }

    public String getTaskDescription() {
        return "Saving Dataset into the database... ";
    }

    public double getFinishedPercentage() {
        return db.getProgress();
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
            saveFile();
        } catch (Exception e) {
            status = TaskStatus.ERROR;
            errorMessage = e.toString();
            return;
        }
    }

    //cambiar type...
    public void saveFile() {
        try {
            status = TaskStatus.PROCESSING;
            Connection connection = db.connect();
            String type;
            if (dataset.getType() == DatasetType.LCMS) {
                type = "LCMS";
                db.lcms(connection, dataset, type, author);
            } else if (dataset.getType() == DatasetType.GCGCTOF) {
                type = "GCGC-Tof";
                db.gcgctof(connection, dataset, type, author);
            }
            status = TaskStatus.FINISHED;
        } catch (Exception e) {
            status = TaskStatus.ERROR;
        }
    }
}
