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

package guineu.modules.dataanalysis.clustering;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ClusteringReportWindow extends JInternalFrame {

	private JTable table;

	public ClusteringReportWindow(String[] samplesOrVariables,
			Integer[] clusteringData, String title) {
		super(title, true, true, true, true);
		String[] columnNames = { "Variables", "Cluster number" };
		Object[][] data = new Object[samplesOrVariables.length][2];
		for (int i = 0; i < samplesOrVariables.length; i++) {
			data[i][0] = samplesOrVariables[i];
			data[i][1] = clusteringData[i];
		}

		table = new JTable(data, columnNames);

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		this.add(scrollPane);

		pack();
	}
}
