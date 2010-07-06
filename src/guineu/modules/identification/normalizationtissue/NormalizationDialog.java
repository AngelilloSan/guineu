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
package guineu.modules.identification.normalizationtissue;

import guineu.data.Dataset;
import guineu.desktop.impl.DesktopParameters;
import guineu.main.GuineuCore;
import guineu.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  scsandra
 */
public class NormalizationDialog extends javax.swing.JDialog implements ActionListener {

    private Vector<StandardUmol> standards;
    ExitCode exit = ExitCode.UNKNOWN;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    Dataset dataset;
    Hashtable<String, Double> weights;
    String filePath;

    /** Creates new form NormalizationDialog */
    public NormalizationDialog(Vector<StandardUmol> standards, Dataset dataset, Hashtable<String, Double> weights) {
        super(GuineuCore.getDesktop().getMainFrame(),
                "Please fill the standards...", true);

        this.standards = standards;
        this.dataset = dataset;
        this.weights = weights;
        initComponents();

        DefaultTableModel d = (DefaultTableModel) this.jTable.getModel();
        int cont = 0;
        for (String name : dataset.getNameExperiments()) {

            try {
                Object[] row = new Object[2];
                row[0] = name;
                if (weights.containsKey(name)) {
                    row[1] = (Double) weights.get(name);
                }
                d.addRow(row);

                this.jTable.setEditingRow(cont++);

            } catch (Exception e) {
            }
        }



        StandardsDataModel model = new StandardsDataModel(this.standards);
        UnknownsDataModel unknownModel = new UnknownsDataModel(this.standards);

        this.jTable1.setModel(model);
        this.jTable2.setModel(unknownModel);
        this.jButtonClose.addActionListener(this);
        this.jButtonOk.addActionListener(this);
        this.jButtonReset.addActionListener(this);
        this.readFileButton.addActionListener(this);
        logger.finest("Displaying Normalization Serum dialog");
    }

    public void fillStandards() {
        try {
            ((StandardsDataModel) this.jTable1.getModel()).fillStandards();
            ((UnknownsDataModel) this.jTable2.getModel()).fillStandards();
            for (int i = 0; i < this.jTable.getRowCount(); i++) {
                String name = (String) this.jTable.getValueAt(i, 0);
                Double value = (Double) this.jTable.getValueAt(i, 1);                
                this.weights.put(name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error, You have not introduced a correct value.", "Error", JOptionPane.ERROR_MESSAGE);
            exit = ExitCode.CANCEL;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonReset = new javax.swing.JButton();
        readFileButton = new javax.swing.JButton();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(587, 551));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setPreferredSize(new java.awt.Dimension(300, 400));
        jPanel5.setRequestFocusEnabled(false);
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("umol / l.blood sample  --- umol / g sample"));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 200));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 200));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);

        jPanel5.add(jPanel1);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Uknown compounds"));
        jPanel6.setMaximumSize(new java.awt.Dimension(32779, 32796));
        jPanel6.setMinimumSize(new java.awt.Dimension(37, 54));
        jPanel6.setPreferredSize(new java.awt.Dimension(300, 200));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 200));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable2.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(jTable2);

        jPanel6.add(jScrollPane2);

        jPanel5.add(jPanel6);

        jPanel3.add(jPanel5);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Sample weights"));
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 400));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 390));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Experiment Name", "Weight (mg)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable.setCellSelectionEnabled(true);
        jTable.setPreferredSize(new java.awt.Dimension(480, 380));
        jScrollPane3.setViewportView(jTable);

        jPanel2.add(jScrollPane3);

        jPanel3.add(jPanel2);

        getContentPane().add(jPanel3);

        jPanel4.setMaximumSize(new java.awt.Dimension(400, 30));
        jPanel4.setMinimumSize(new java.awt.Dimension(60, 37));
        jPanel4.setPreferredSize(new java.awt.Dimension(60, 37));

        jButtonOk.setText("  Ok ");
        jPanel4.add(jButtonOk);

        jButtonClose.setText("Close");
        jPanel4.add(jButtonClose);

        jButtonReset.setText("Reset");
        jButtonReset.setMaximumSize(new java.awt.Dimension(100, 27));
        jButtonReset.setMinimumSize(new java.awt.Dimension(60, 27));
        jButtonReset.setOpaque(true);
        jButtonReset.setPreferredSize(new java.awt.Dimension(80, 27));
        jPanel4.add(jButtonReset);

        readFileButton.setText("Read File");
        jPanel4.add(readFileButton);

        getContentPane().add(jPanel4);
    }// </editor-fold>//GEN-END:initComponents

    public ExitCode getExitCode() {
        return exit;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    public javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonReset;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton readFileButton;
    // End of variables declaration//GEN-END:variables

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.jButtonOk) {
            exit = ExitCode.OK;
            fillStandards();
            dispose();
        } else if (e.getSource() == this.jButtonClose) {
            exit = ExitCode.CANCEL;
            dispose();
        } else if (e.getSource() == this.jButtonReset) {
            this.reset();
        } else if (e.getSource() == this.readFileButton) {
            ExitCode exitCode = setupParameters();
            if (exitCode != ExitCode.OK) {
                return;
            }
            readWeithgs();
        }
    }

    public ExitCode setupParameters() {
        DesktopParameters deskParameters = (DesktopParameters) GuineuCore.getDesktop().getParameterSet();
        String lastPath = deskParameters.getLastNormalizationPath();
        if (lastPath == null) {
            lastPath = "";
        }
        File lastFilePath = new File(lastPath);
        readFileDialog dialog = new readFileDialog(lastFilePath);
        dialog.setVisible(true);
        try {
            this.filePath = dialog.getCurrentDirectory();
        } catch (Exception e) {
        }
        return dialog.getExitCode();
    }

    public void reset() {
        try {
            ((StandardsDataModel) this.jTable1.getModel()).resetStandards();
            ((UnknownsDataModel) this.jTable2.getModel()).resetStandards();

            for (int i = 0; i < this.jTable.getRowCount(); i++) {
                this.jTable.setValueAt(0.0, i, 1);
            }
        } catch (Exception e) {
        }
    }

    public void readWeithgs() {
        FileReader fr = null;

        try {
            fr = new FileReader(new File(this.filePath));
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = (br.readLine())) != null) {
                if (!line.isEmpty()) {
                    String[] w = line.split(",");
                    for (int i = 0; i < this.jTable.getRowCount(); i++) {
                        w[0] = w[0].replaceAll(" \"", "").toLowerCase();
                        if (w[0].contains(this.jTable.getValueAt(i, 0).toString().toLowerCase())) {
                            this.jTable.setValueAt(Double.valueOf(w[1]), i, 1);
                            break;
                        }
                    }
                }
            }
            fr.close();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(NormalizationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
