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
package guineu.taskcontrol;


public interface Task extends Runnable {

    /**
     * WAITING - task is ready and waiting to start
     * PROCESSING - task is running
     * FINISHED - task finished succesfully, results can be obtained by getResult()
     * CANCELED - task was canceled by user
     * ERROR - task finished with error, error message can be obtained by getErrorMessage()
     *
     */
    public static enum TaskStatus {
        WAITING, PROCESSING, FINISHED, CANCELED, ERROR
    };

    public static enum TaskPriority {
        HIGH, NORMAL, LOW
    };

    public String getTaskDescription();

    public double getFinishedPercentage();

    public TaskStatus getStatus();

    public String getErrorMessage();

    /**
     * Cancel a running task by user request.
     * This method must clean up everything and return to 
     * a state prior to starting the task.
     *
     */
    public void cancel();

}
