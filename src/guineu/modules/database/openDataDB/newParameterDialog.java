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
package guineu.modules.database.openDataDB;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author scsandra
 */
public class newParameterDialog extends javax.swing.JPanel {

    DefaultComboBoxModel tissueModel = new DefaultComboBoxModel(new String[]{"All", "Serum", "Liver", "Adipocites", "Muscle", "Brain", "Custom"});
    DefaultComboBoxModel organismModel = new DefaultComboBoxModel(new String[]{"All", "Mouse", "Human", "Custom"});
    DefaultComboBoxModel methodModel = new DefaultComboBoxModel(new String[]{"All", "LC-MS", "GCxGC-MS"});
    DefaultComboBoxModel subtypeModel = new DefaultComboBoxModel(new String[]{"All", "WT", "Disease", "Custom"});

    /** Creates new form newParameterDialog */
    public newParameterDialog() {
        initComponents();
        this.valueParameterCB.setModel(tissueModel);
    }

    public void setTypeModel(DefaultComboBoxModel model) {
        this.typeParameterCB.setModel(model);
    }

    public void setValueModel(DefaultComboBoxModel model) {
        this.valueParameterCB.setModel(model);
    }

    public void setLogicLabel(String label) {
        this.logicLabel.setText(label);
    }

    public String getValue() {
        return this.valueParameterCB.getSelectedItem().toString();
    }

    public String getType() {
        return this.typeParameterCB.getSelectedItem().toString();
    }

    public String getLogic() {
        return this.logicLabel.getText();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        removeButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        logicLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        typeParameterCB = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        valueParameterCB = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(254, 254, 254));
        setMinimumSize(new java.awt.Dimension(600, 24));
        setPreferredSize(new java.awt.Dimension(600, 24));
        setLayout(new java.awt.GridLayout(1, 0, 2, 0));

        removeButton.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        removeButton.setText("Remove");
        removeButton.setPreferredSize(new java.awt.Dimension(52, 26));
        add(removeButton);

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));
        jPanel1.setLayout(new java.awt.BorderLayout());

        logicLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel1.add(logicLabel, java.awt.BorderLayout.EAST);

        add(jPanel1);

        jLabel2.setBackground(new java.awt.Color(254, 254, 254));
        jLabel2.setText("         Type:  ");
        add(jLabel2);

        typeParameterCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Method", "Tissue", "Organism", "Subtype", "Contain:" }));
        typeParameterCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeParameterCBActionPerformed(evt);
            }
        });
        add(typeParameterCB);

        jLabel3.setBackground(new java.awt.Color(254, 254, 254));
        jLabel3.setText("       value:");
        add(jLabel3);

        valueParameterCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(valueParameterCB);
        add(jTextField1);
    }// </editor-fold>//GEN-END:initComponents

    private void typeParameterCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeParameterCBActionPerformed
        if (this.typeParameterCB.getSelectedItem().toString().equals("Tissue")) {
            this.valueParameterCB.setModel(tissueModel);
        } else if (this.typeParameterCB.getSelectedItem().toString().equals("Organism")) {
            this.valueParameterCB.setModel(organismModel);
        } else if (this.typeParameterCB.getSelectedItem().toString().equals("Subtype")) {
            this.valueParameterCB.setModel(subtypeModel);
        } else if (this.typeParameterCB.getSelectedItem().toString().equals("Method")) {
            this.valueParameterCB.setModel(methodModel);
        }
}//GEN-LAST:event_typeParameterCBActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel logicLabel;
    public javax.swing.JButton removeButton;
    private javax.swing.JComboBox typeParameterCB;
    private javax.swing.JComboBox valueParameterCB;
    // End of variables declaration//GEN-END:variables
}
