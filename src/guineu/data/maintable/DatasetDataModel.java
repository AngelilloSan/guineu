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
package guineu.data.maintable;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.data.impl.DatasetType;
import guineu.data.impl.SimpleDataset;
import guineu.data.impl.SimplePeakListRowLCMS;
import guineu.data.impl.SimplePeakListRowGCGC;
import guineu.util.Tables.DataTableModel;
import guineu.util.Tables.impl.TableComparator.SortingDirection;
import javax.swing.table.AbstractTableModel;



import java.util.*;

public class DatasetDataModel extends AbstractTableModel implements DataTableModel {

    /**
     * All data in the main windows. It can be LCMS or GCGC-Tof data.
     */
    private static final long serialVersionUID = 1L;
    private String columns[];
    private Object[][] rows; //content all data   
    private int numColumns;
    private int numRows;
    private Vector<String> columns_mol = new Vector<String>();
    private boolean type;
    private SimpleDataset dataset;
    protected SortingDirection isSortAsc = SortingDirection.Ascending;
    protected int sortCol = 0;

    public DatasetDataModel(Dataset dataset) {
        this.dataset = (SimpleDataset) dataset;
        if (this.dataset.getType() == DatasetType.GCGCTOF) {
            //GCGC-Tof files
            columns_mol.add("Selection");
            columns_mol.add("ID");
            columns_mol.add("RT1");
            columns_mol.add("RT2");
            columns_mol.add("RTI");
            columns_mol.add("N Found");
            columns_mol.add("Max similarity");
            columns_mol.add("Mean similarity");
            columns_mol.add("Similarity std dev");
            columns_mol.add("Metabolite name");
            columns_mol.add("Metabolite all names");
            columns_mol.add("Pubchem ID");
            columns_mol.add("Mass");
            columns_mol.add("Difference");
            columns_mol.add("Spectrum");
        } else if(this.dataset.getType() == DatasetType.LCMS){
            //LCMS files
            columns_mol.add("Selection");
            columns_mol.add("ID");
            columns_mol.add("Average M/Z");
            columns_mol.add("Average RT");
            columns_mol.add("Lipid Name");
            columns_mol.add("Class");
            columns_mol.add("N Found");
            columns_mol.add("Standard");
            columns_mol.add("FA Composition");
            columns_mol.add("All Names");
            columns_mol.add("Alignment");
        }
        this.set_samples();
    }

    /**
     * Makes a new rows[][] with the new dates. First add the columns names with "writeSamplesNames(x)", and then
     * rewrite all data (rows[][]).
     * @param sampleNames vector with the names of the experiment whitch have to be in the table.
     * @param type is true for GCGC-Tof data and false to LCMS data
     */
    public void set_samples() {
        this.writeSamplesName();
        this.writeData();
        numColumns = columns.length;
        numRows = rows.length;
    }

    /*public Object[][] getRows(){
    return rows;
    }	
    public void setRows(Object[][] rows){
    this.rows = rows;
    numRows = rows.length;
    } */
    /**
     * Adds the name of the experiments in the "columns" variable. There are the title of the columns.
     * @param sampleNames list of all experiments names.
     */
    public void writeSamplesName() {
        columns = new String[dataset.getNumberCols() + this.columns_mol.size()];
        for (int i = 0; i < columns_mol.size(); i++) {
            columns[i] = (String) columns_mol.elementAt(i);
        }
        int cont = columns_mol.size();
        for (String nameExperiment : this.dataset.getNameExperiments()) {
            try {
                columns[cont++] = nameExperiment;
            } catch (Exception e) {
            }
        }
    }

    /**
     * Takes all necessary information from the database and writes it in rows[][]. 
     * @param data 
     */
    public void writeData() {
        rows = new Object[dataset.getNumberRows()][dataset.getNumberCols() + this.columns_mol.size()];

        if (dataset.getType() == DatasetType.LCMS) {
            for (int i = 0; i < dataset.getNumberRows(); i++) {
                SimplePeakListRowLCMS lipid = (SimplePeakListRowLCMS) dataset.getRow(i);
                rows[i][0] = new Boolean(false);
                if (lipid.getID() != -1) {
                    rows[i][1] = lipid.getID();
                } else {
                    rows[i][1] = i;
                    lipid.setID(i);
                }
                rows[i][2] = lipid.getMZ();
                rows[i][3] = lipid.getRT();
                rows[i][4] = lipid.getName();
                rows[i][5] = lipid.getLipidClass();
                rows[i][6] = lipid.getNumFound();
                if (lipid.getStandard() == 1) {
                    rows[i][7] = new Boolean(true);
                } else {
                    rows[i][7] = new Boolean(false);
                }
                rows[i][8] = lipid.getFAComposition();
                rows[i][9] = lipid.getAllNames();
                if (lipid.getNumberAlignment() != -1) {
                    rows[i][10] = lipid.getNumberAlignment();
                } else {
                    rows[i][10] = 0;
                }
                int cont = this.columns_mol.size();
                for (String nameExperiment : this.dataset.getNameExperiments()) {
                    try {
                        rows[i][cont++] = lipid.getPeak(nameExperiment);
                    } catch (Exception e) {
                    }
                }

            }

        } else if(this.dataset.getType() == DatasetType.GCGCTOF){
            for (int i = 0; i < dataset.getNumberRows(); i++) {
                SimplePeakListRowGCGC metabolite = (SimplePeakListRowGCGC) dataset.getRow(i);
                rows[i][0] = new Boolean(false);
                if (metabolite.getID() == -1) {
                    rows[i][1] = i;
                    metabolite.setID(i);
                } else {
                    rows[i][1] = metabolite.getID();
                }
                rows[i][2] = metabolite.getRT1();
                rows[i][3] = metabolite.getRT2();
                rows[i][4] = metabolite.getRTI();
                rows[i][5] = metabolite.getNumFound();
                rows[i][6] = metabolite.getMaxSimilarity();
                rows[i][7] = metabolite.getMeanSimilarity();
                rows[i][8] = metabolite.getSimilaritySTDDev();
                rows[i][9] = metabolite.getName();
                rows[i][10] = metabolite.getAllNames();
                rows[i][11] = metabolite.getPubChemID();
                rows[i][12] = metabolite.getMass();
                rows[i][13] = metabolite.getDifference();
                rows[i][14] = metabolite.getSpectrum();
                int cont = this.columns_mol.size();
                for (String nameExperiment : this.dataset.getNameExperiments()) {
                    try {
                        rows[i][cont++] = metabolite.getPeak(nameExperiment);
                    } catch (Exception e) {
                    }
                }

            }
        }
    }

    public SimpleDataset removeRows() {
        SimpleDataset newDataset = new SimpleDataset(this.dataset.getDatasetName());
        for (int i = 0; i < rows.length; i++) {
            if (!(Boolean) rows[i][0]) {
                PeakListRow peakListRow = dataset.getRow(i).clone();
                newDataset.AddRow(peakListRow);
            }
        }
        newDataset.setNameExperiments(dataset.getNameExperiments());
        newDataset.setType(dataset.getType());
        return newDataset;
    }

    public int getColumnCount() {
        return numColumns;
    }

    public int getRowCount() {
        return numRows;
    }

    public Object getValueAt(final int row, final int column) {
        return rows[row][column];
    }

    @Override
    public String getColumnName(int columnIndex) {
        String str = columns[columnIndex];
        /* if (columnIndex == sortCol && columnIndex != 0)
        str += isSortAsc ? " >>" : " <<";*/
        return str;
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
        rows[row][column] = aValue;
        fireTableCellUpdated(row, column);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    void addColumn() {
        String[] oldColumns = this.columns.clone();
        this.columns = new String[oldColumns.length + 1];
        for (int i = 0; i < oldColumns.length; i++) {
            System.out.println(oldColumns[i]);
            this.columns[i] = oldColumns[i];
        }
        this.columns[oldColumns.length] = "New Column";
        this.numColumns = this.columns.length;

        this.addColumnObject(this.rows);
        this.numRows = this.rows.length;
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

    public SortingDirection getSortDirection() {
        return isSortAsc;
    }

    public int getSortCol() {
        return sortCol;
    }

    public void setSortDirection(SortingDirection direction) {
        this.isSortAsc = direction;
    }

    public void setSortCol(int column) {
        this.sortCol = column;
    }

    public Object[][] getData() {
        return rows;
    }

    public void changeData(int column, int row) {
        if (dataset.getType() == DatasetType.LCMS) {
            SimplePeakListRowLCMS peakListRow = (SimplePeakListRowLCMS) this.dataset.getRow(row);
            try {
                switch (column) {
                    case 1:
                        peakListRow.setID((Integer) rows[row][column]);
                        break;
                    case 2:
                        peakListRow.setMZ((Double) rows[row][column]);
                        break;
                    case 3:
                        peakListRow.setRT((Double) rows[row][column]);
                        break;
                    case 4:
                        peakListRow.setName((String) rows[row][column]);
                        break;
                    case 5:
                        peakListRow.setLipidClass((Integer) rows[row][column]);
                        break;
                    case 6:
                        peakListRow.setNumFound((Double) rows[row][column]);
                        break;
                    case 7:
                        if ((Boolean) rows[row][column]) {
                            peakListRow.setStandard(1);
                        } else {
                            peakListRow.setStandard(0);
                        }
                        break;
                    case 8:
                        peakListRow.setFAComposition((String) rows[row][column]);
                        break;
                    case 9:
                        peakListRow.setAllNames((String) rows[row][column]);
                        break;
                    case 10:
                        peakListRow.setNumberAlignment((Integer) rows[row][column]);
                        break;
                    default:
                        String experimentName = this.columns[column];
                        peakListRow.setPeak(experimentName, (Double) rows[row][column]);                        
                        break;
                }
            } catch (Exception e) {
            }
        }else if(this.dataset.getType() == DatasetType.GCGCTOF){
            //GCGC changes....
        }
    }

    public DatasetType getType() {
        return this.dataset.getType();
    }
   
}
