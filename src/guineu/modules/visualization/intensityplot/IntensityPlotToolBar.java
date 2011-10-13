/*
 * Copyright 2007-2011 VTT Biotechnology
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
/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */

package guineu.modules.visualization.intensityplot;

import guineu.util.GUIUtils;
import guineu.util.dialogs.AxesSetupDialog;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;



import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * Intensity plot toolbar class
 */
class IntensityPlotToolBar extends JToolBar implements ActionListener {

    static final Icon pointsIcon = new ImageIcon("icons/pointsicon.png");
    static final Icon linesIcon = new ImageIcon("icons/linesicon.png");
    static final Icon axesIcon = new ImageIcon("icons/axesicon.png");

    private IntensityPlotFrame frame;
    private JButton linesVisibleButton, setupAxesButton;

    IntensityPlotToolBar(IntensityPlotFrame frame) {

        super(JToolBar.VERTICAL);

        setFloatable(false);
        setMargin(new Insets(5, 5, 5, 5));
        setBackground(Color.white);

        this.frame = frame;

        linesVisibleButton = GUIUtils.addButton(this, null, linesIcon, this,
                null, "Switch lines on/off");

        if (frame.getChart().getPlot() instanceof XYPlot) {
            addSeparator();
            setupAxesButton = GUIUtils.addButton(this, null, axesIcon, this,
                    "SETUP_AXES", "Setup ranges for axes");
        }

    }

    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

        if (src == linesVisibleButton) {

            Plot plot = frame.getChart().getPlot();

            Boolean linesVisible;

            if (plot instanceof CategoryPlot) {
                LineAndShapeRenderer renderer = (LineAndShapeRenderer) ((CategoryPlot) plot).getRenderer();
                linesVisible = renderer.getBaseLinesVisible();
            } else {
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) ((XYPlot) plot).getRenderer();
                linesVisible = renderer.getBaseLinesVisible();
            }

            // check for null value
            if (linesVisible == null)
                linesVisible = false;

            // update the icon
            if (linesVisible) {
                linesVisibleButton.setIcon(linesIcon);
            } else {
                linesVisibleButton.setIcon(pointsIcon);
            }

            // switch the button
            linesVisible = !linesVisible;

            if (plot instanceof CategoryPlot) {
                LineAndShapeRenderer renderer = (LineAndShapeRenderer) ((CategoryPlot) plot).getRenderer();
                renderer.setBaseLinesVisible(linesVisible);
            } else {
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) ((XYPlot) plot).getRenderer();
                renderer.setBaseLinesVisible(linesVisible);
            }

        }

        if (src == setupAxesButton) {
            AxesSetupDialog dialog = new AxesSetupDialog(
            frame.getChart().getXYPlot());
            dialog.setVisible(true);
        }

    }

}
