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
package guineu.modules.configuration.parameters;

import guineu.data.Dataset;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ParameterDataModel extends AbstractTableModel {

    /**
     * All data in the main windows. It can be LCMS or GCGC-Tof data.
     */
    private static final long serialVersionUID = 1L;
    private Vector<String> columns;
    private Vector<String[]> rows; //content all data
    private int numColumns;
    private int numRows;
    private JTable table;

    public ParameterDataModel(Dataset dataset, JTable table) {
        this.table = table;

        // Column names
        columns = dataset.getParametersName();
        if (!columns.contains("Samples")) {
            columns.insertElementAt("Samples", 0);
        }
        numColumns = columns.size();
        // First column with the name of the samples
        String[] col = dataset.getNameExperiments().toArray(new String[0]);
        rows = new Vector<String[]>();
        rows.addElement(col);
        numRows = dataset.getNameExperiments().size();

        // Parameter columns
        for (int i = 1; i < dataset.getParametersName().size(); i++) {
            String parameterName = dataset.getParametersName().elementAt(i);
            col = new String[dataset.getNameExperiments().size()];

            for (int e = 0; e < dataset.getNameExperiments().size(); e++) {
                String experimentName = dataset.getNameExperiments().elementAt(e);
                col[e] = dataset.getParametersValue(experimentName, parameterName);
            }
            rows.addElement(col);
        }
    }

    public void addColumn(String column) {
        this.columns.addElement(column);
        String[] newCol = new String[numRows];
        rows.addElement(newCol);
        numColumns++;
    }

    public int getColumnCount() {
        return numColumns;
    }

    public int getRowCount() {
        return numRows;
    }

    public String getValueAt(final int row, final int column) {
        return rows.elementAt(column)[row];
    }

    @Override
    public String getColumnName(int columnIndex) {
        try {
            return columns.elementAt(columnIndex);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int c) {
        if (getValueAt(0, c) != null) {
            return getValueAt(0, c).getClass();
        } else {
            return Object.class;
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        try {
            int[] trows = table.getSelectedRows();
            int[] tcolumns = table.getSelectedColumns();
            for (int trow : trows) {
                for (int tcolumn : tcolumns) {
                    rows.elementAt(tcolumn)[trow] = aValue.toString();
                }
            }
            rows.elementAt(column)[row] = aValue.toString();
            fireTableCellUpdated(row, column);
        } catch (Exception e) {
        }

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void addColumnObject(Object[][] o) {
        Object[][] oldRows = o.clone();
        o = new Object[oldRows.length][oldRows[0].length + 1];
        for (int i = 0; i < oldRows.length; i++) {
            for (int j = 0; j < oldRows[0].length; j++) {
                o[i][j] = oldRows[i][j];
            }
            o[i][oldRows[0].length] = " ";
        }
    }

    public void addColumnObject(int[][] o) {
        int[][] oldRows = o.clone();
        o = new int[oldRows.length][oldRows[0].length + 1];
        for (int i = 0; i < oldRows.length; i++) {
            for (int j = 0; j < oldRows[0].length; j++) {
                o[i][j] = oldRows[i][j];
            }
            o[i][oldRows[0].length] = 0;
        }
    }

    public void setColumnCount(int count) {
        this.numColumns = count;
    }

    public void addParameters(Dataset dataset) {
        for (int i = 1; i < this.getColumnCount(); i++) {
            String parameterName = this.getColumnName(i);
            for (int e = 0; e < this.rows.elementAt(i).length; e++) {
                String experimentName = this.rows.elementAt(0)[e];
                String parameterValue = this.rows.elementAt(i)[e];
                dataset.addParameter(experimentName, parameterName, parameterValue);
            }
        }
    }
}