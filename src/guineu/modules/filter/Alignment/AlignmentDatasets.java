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
package guineu.modules.filter.Alignment;

import guineu.data.ParameterSet;
import guineu.desktop.Desktop;
import guineu.desktop.GuineuMenu;
import guineu.main.GuineuCore;
import guineu.main.GuineuModule;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskStatus;

import guineu.taskcontrol.TaskListener;
import guineu.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class AlignmentDatasets implements GuineuModule, TaskListener, ActionListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Desktop desktop;
    private AlignmentParameters parameters;

    public void initModule() {
        this.desktop = GuineuCore.getDesktop();
        desktop.addMenuItem(GuineuMenu.FILTER, "Combine datasets..",
                "TODO write description", KeyEvent.VK_A, this, null, null);
        parameters = new AlignmentParameters();
    }

    public ParameterSet getParameterSet() {
        return null;
    }

    public void taskStarted(Task task) {
        logger.info("Running alignment");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished alignment on " + ((AlignmentDatasetsTask) task).getTaskDescription());
        }

        if (task.getStatus() == TaskStatus.ERROR) {

            String msg = "Error while alignment on .. " + ((AlignmentDatasetsTask) task).getErrorMessage();
            logger.severe(msg);
            desktop.displayErrorMessage(msg);

        }
    }

    public void actionPerformed(ActionEvent e) {
        ExitCode exitCode = setupParameters();

        if (exitCode != ExitCode.OK) {
            return;
        }

        runModule();
    }

    public ExitCode setupParameters() {
        GetAlignmentParameters parametersDialog = new GetAlignmentParameters(parameters);
        parametersDialog.setVisible(true);
        return parametersDialog.getExitCode();
    }

    public void setParameters(ParameterSet parameterValues) {
    }

    public String toString() {
        return "Alingment";
    }

    public Task[] runModule() {

        // prepare a new group of tasks       
        Task tasks[] = new AlignmentDatasetsTask[1];
        tasks[0] = new AlignmentDatasetsTask(desktop.getSelectedDataFiles(), desktop, parameters);

        GuineuCore.getTaskController().addTasks(tasks);

        return tasks;


    }
}
