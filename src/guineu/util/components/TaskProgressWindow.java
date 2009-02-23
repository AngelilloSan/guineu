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


package guineu.util.components;

import guineu.taskcontrol.Task;
import guineu.taskcontrol.Task.TaskPriority;
import guineu.taskcontrol.Task.TaskStatus;
import guineu.taskcontrol.impl.TaskControllerImpl;
import guineu.util.GUIUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;



/**
 * 
 */
public class TaskProgressWindow extends JInternalFrame implements
        ActionListener {

    private JTable taskTable;
    private TaskControllerImpl taskController;

    // popup menu
    private JPopupMenu popupMenu;
    private JMenu priorityMenu;
    private JMenuItem cancelTaskMenuItem;
    private JMenuItem highPriorityMenuItem;
    private JMenuItem normalPriorityMenuItem;
    private JMenuItem lowPriorityMenuItem;

    /**
     * 
     */
    public TaskProgressWindow(TaskControllerImpl taskController) {
        super("Tasks in progress...", true, true, true, true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.taskController = taskController;
        taskTable = new JTable(taskController.getTaskQueue());
        taskTable.setCellSelectionEnabled(false);
        taskTable.setColumnSelectionAllowed(false);
        taskTable.setRowSelectionAllowed(true);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setDefaultRenderer(JComponent.class, new ComponentCellRenderer());
        JScrollPane jJobScroll = new JScrollPane(taskTable);
        add(jJobScroll, java.awt.BorderLayout.CENTER);

        // create popup menu items

        popupMenu = new JPopupMenu();

        priorityMenu = new JMenu("Set priority...");
        highPriorityMenuItem = GUIUtils.addMenuItem(priorityMenu, "High", this);
        normalPriorityMenuItem = GUIUtils.addMenuItem(priorityMenu, "Normal",
                this);
        lowPriorityMenuItem = GUIUtils.addMenuItem(priorityMenu, "Low", this);
        popupMenu.add(priorityMenu);

        cancelTaskMenuItem = GUIUtils.addMenuItem(popupMenu, "Cancel task",
                this);

        taskTable.setComponentPopupMenu(popupMenu);

        // set the width for first column (task description)
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(350);

        pack();

        // set position and size
        setBounds(120, 30, 600, 150);

    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
        Object src = event.getSource();
        Task selectedTask;

        int selectedRow = taskTable.getSelectedRow();
        selectedTask = taskController.getTask(selectedRow);

        if (selectedTask == null)
            return;

        if (src == cancelTaskMenuItem) {
            TaskStatus status = selectedTask.getStatus();
            if ((status == TaskStatus.WAITING)
                    || (status == TaskStatus.PROCESSING)) {
                selectedTask.cancel();
            }
        }

        if (src == highPriorityMenuItem) {
            taskController.setTaskPriority(selectedTask, TaskPriority.HIGH);
        }

        if (src == normalPriorityMenuItem) {
            taskController.setTaskPriority(selectedTask, TaskPriority.NORMAL);
        }

        if (src == lowPriorityMenuItem) {
            taskController.setTaskPriority(selectedTask, TaskPriority.LOW);
        }

    }

}
