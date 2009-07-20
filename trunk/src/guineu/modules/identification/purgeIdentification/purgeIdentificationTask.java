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
package guineu.modules.identification.purgeIdentification;

import guineu.data.Dataset;
import guineu.data.PeakListRow;
import guineu.data.impl.DatasetType;
import guineu.data.parser.impl.Lipidclass;
import guineu.data.impl.SimpleDataset;
import guineu.data.datamodels.DatasetDataModel;
import guineu.desktop.Desktop;
import guineu.taskcontrol.Task;
import guineu.util.Tables.DataTable;
import guineu.util.Tables.DataTableModel;
import guineu.util.Tables.impl.PushableTable;
import guineu.util.internalframe.DataInternalFrame;
import java.awt.Dimension;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author scsandra
 */
public class purgeIdentificationTask implements Task {

	private TaskStatus status = TaskStatus.WAITING;
	private String errorMessage;
	private Desktop desktop;
	private SimpleDataset dataset;
	private double count;
	private int NRows;
	Lipidclass LipidClassLib;

	public purgeIdentificationTask(Dataset dataset, Desktop desktop) {
		this.dataset = (SimpleDataset) dataset;
		this.desktop = desktop;
		this.LipidClassLib = new Lipidclass();
	}

	public String getTaskDescription() {
		return "Purge Identification ";
	}

	public double getFinishedPercentage() {
		return (count / this.NRows);
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
			status = TaskStatus.PROCESSING;
			SimpleDataset newDataset = dataset.clone();
			for (int i = 0; i < newDataset.getNumberRows(); i++) {
				PeakListRow lipid = newDataset.getRow(i);
				if (lipid == null) {
					continue;
				}
				this.getName(lipid);
			}
			newDataset.setType(DatasetType.LCMS);
			desktop.AddNewFile(newDataset);

			//creates internal frame with the table
			DataTableModel model = new DatasetDataModel(newDataset);
			DataTable table = new PushableTable(model);
			DataInternalFrame frame = new DataInternalFrame(newDataset.getDatasetName(), table.getTable(), new Dimension(800, 800));

			desktop.addInternalFrame(frame);
			frame.setVisible(true);
			status = TaskStatus.FINISHED;
		} catch (Exception e) {
			status = TaskStatus.ERROR;
			// errorMessage = e.toString();
			return;
		}
	}

	public void getName(PeakListRow lipid) {

		try {
			if (lipid.getName().matches(".*Cer.*")) {
				if (lipid.getRT() > 430 || lipid.getMZ() < 340 || !lipid.getName().matches(".*d18:1.*")) {
					this.getFirstName(lipid);
					this.getName(lipid);
				}
			} else if (lipid.getName().matches(".*Lyso.*")) {
				if (lipid.getRT() > 300 || lipid.getMZ() > 650) {
					this.getFirstName(lipid);
					this.getName(lipid);
				} else {
					Pattern carbons = Pattern.compile("\\d\\d?");
					Matcher matcher = carbons.matcher(lipid.getName());
					if (matcher.find()) {
						double num = Double.valueOf(lipid.getName().substring(matcher.start(), matcher.end()));
						if (num < 12 || num > 24) {
							this.getFirstName(lipid);
							this.getName(lipid);
						}
					}
				}
			} else if (lipid.getName().matches(".*PC.*") || lipid.getName().matches(".*PE.*")) {
				if (lipid.getRT() < 300 || lipid.getRT() > 420 || lipid.getMZ() < 550) {
					this.getFirstName(lipid);
					this.getName(lipid);
				} else {
					Pattern carbons = Pattern.compile("\\d\\d?");
					Matcher matcher = carbons.matcher(lipid.getName());
					if (matcher.find()) {
						double num = Double.valueOf(lipid.getName().substring(matcher.start(), matcher.end()));
						if (num < 30) {
							this.getFirstName(lipid);
							this.getName(lipid);
						}
					}
				}

			} else if (lipid.getName().matches(".*PS.*") || lipid.getName().matches(".*PI.*")) {
				lipid.setName("unknown");
				/*if (lipid.getRT() < 300 || lipid.getRT() > 420 || lipid.getMZ() < 550) {
					this.getFirstName(lipid);
					this.getName(lipid);
				} else {
					Pattern carbons = Pattern.compile("\\d\\d?");
					Matcher matcher = carbons.matcher(lipid.getName());
					if (matcher.find()) {
						double num = Double.valueOf(lipid.getName().substring(matcher.start(), matcher.end()));
						if (num > 41) {
							this.getFirstName(lipid);
							this.getName(lipid);
						}
					}
				}*/
			} else if (lipid.getName().matches(".*MG.*")) {
				/* if (lipid.getRT() > 300 || lipid.getMZ() > 500) {
				this.getFirstName(lipid);
				this.getName(lipid);
				}*/
				lipid.setName("unknown");
			} else if (lipid.getName().matches(".*SM.*")) {
				if (lipid.getRT() > 420 || lipid.getRT() < 330 || !lipid.getName().matches(".*d18:1.*")) {
					this.getFirstName(lipid);
					this.getName(lipid);
				}
			} else if (lipid.getName().matches(".*PA.*") || lipid.getName().matches(".*PG.*")) {
				lipid.setName("unknown");
			/* if (lipid.getRT() > 410 || lipid.getMZ() < 550) {
			this.getFirstName(lipid);
			this.getName(lipid);
			}else{
			Pattern carbons = Pattern.compile("\\d\\d?");
			Matcher matcher = carbons.matcher(lipid.getName());
			if (matcher.find()) {
			double num = Double.valueOf(lipid.getName().substring(matcher.start(), matcher.end()));
			if (num > 41) {
			this.getFirstName(lipid);
			this.getName(lipid);
			}
			}
			}*/
			} else if (lipid.getName().matches(".*DG.*")) {
				if (lipid.getRT() > 410 || lipid.getMZ() < 350|| lipid.getName().matches(".*e.*")) {
					this.getFirstName(lipid);
					this.getName(lipid);
				} else {
					Pattern carbons = Pattern.compile("\\d\\d?");
					Matcher matcher = carbons.matcher(lipid.getName());
					if (matcher.find()) {
						double num = Double.valueOf(lipid.getName().substring(matcher.start(), matcher.end()));
						if (num > 40) {
							this.getFirstName(lipid);
							this.getName(lipid);
						}
					}
				}

			} else if (lipid.getName().matches(".*TG.*")) {
				if (lipid.getRT() < 410) {
					this.getFirstName(lipid);
					this.getName(lipid);
				} else {
					Pattern carbons = Pattern.compile("\\d\\d?");
					Matcher matcher = carbons.matcher(lipid.getName());
					if (matcher.find()) {
						double num = Double.valueOf(lipid.getName().substring(matcher.start(), matcher.end()));
						if (num < 42 || num > 60) {
							this.getFirstName(lipid);
							this.getName(lipid);
						}
					}
				}
			} else if (lipid.getName().matches(".*ChoE.*")) {
				if (lipid.getRT() > 350 || lipid.getMZ() < 550) {
					this.getFirstName(lipid);
					this.getName(lipid);
				}
			} else if (lipid.getName().matches(".*CL.*")) {
				if (lipid.getRT() < 410 || lipid.getMZ() < 1000) {
					this.getFirstName(lipid);
					this.getName(lipid);
				}
			} else if (lipid.getName().matches(".*FA.*")) {
				if (lipid.getRT() > 300 || lipid.getMZ() > 550) {
					this.getFirstName(lipid);
					this.getName(lipid);
				}
			} else if (lipid.getName().matches(".*unknown.*")) {
				if (lipid.getAllNames().length() > 7) {
					this.getFirstName(lipid);
					this.getName(lipid);
				}
			}

			if (lipid.getName().matches(".*ee.*")) {
				if (lipid.getAllNames().length() > 7) {
					this.getFirstName(lipid);
					this.getName(lipid);
				}else{
					lipid.setName("unknown");
				}
			}
		} catch (Exception e) {
			lipid.setName("unknown");
			// System.out.println("getName ->  " + e.getMessage());
			return;
		}
	}

	public void getFirstName(PeakListRow lipid) {

		String[] lipidNames = null;
		try {
			lipidNames = lipid.getAllNames().split(" // ");
		} catch (Exception e) {
			lipid.setName("unknown");
		// System.out.println("e ->  " + e.getMessage());
		}
		if (lipidNames == null || lipidNames.length < 2) {
			try {
				if (lipid.getAllNames().length() > 7) {
					lipid.setName(lipid.getAllNames());
					lipid.setAllNames("");
				} else {
					lipid.setName("unknown");
				}
			} catch (Exception ee) {
				lipid.setName("unknown");
			// System.out.println("ee ->  " + ee.getMessage());
			}
		} else {
			if (lipidNames[0].length() > 7) {
				lipid.setName(lipidNames[0]);
			}
			if (lipidNames.length > 1) {
				String newName = "";
				for (int i = 1; i < lipidNames.length; i++) {
					newName += lipidNames[i] + " // ";
				}
				lipid.setAllNames(newName);
			} else {
				lipid.setAllNames("");
			}
		}
	}
}
