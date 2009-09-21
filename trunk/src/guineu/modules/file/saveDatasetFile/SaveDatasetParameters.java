/*
 * Copyright 2006-2009 The MZmine 2 Development Team
 * 
 * This file is part of MZmine 2.
 * 
 * MZmine 2 is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * MZmine 2; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */
package guineu.modules.file.saveDatasetFile;

import guineu.data.Parameter;
import guineu.data.ParameterType;
import guineu.data.datamodels.LCMSColumnName;
import guineu.data.impl.SimpleParameter;
import guineu.data.impl.SimpleParameterSet;

public class SaveDatasetParameters extends SimpleParameterSet {

	static Object[] objects = {"Excel", "csv"};
    public static final Parameter filename = new SimpleParameter(
            ParameterType.FILE_NAME,
            "Filename",
            "Name of exported peak list file name. If the file exists, it will be overwritten.");
    public static final Parameter fieldSeparator = new SimpleParameter(
            ParameterType.STRING, "Field separator",
            "Character(s) used to separate fields in the exported file",
            (Object) ",");
    public static final Parameter exportItemMultipleSelection = new SimpleParameter(
            ParameterType.MULTIPLE_SELECTION, "Export elements",
            "Multiple selection of row's elements to export", null, LCMSColumnName.values());
    public static final Parameter type = new SimpleParameter(
            ParameterType.STRING, "type",
            "Type of file", null, null, objects, null);

    public SaveDatasetParameters() {
        super(new Parameter[]{filename, type, exportItemMultipleSelection});
    }
}
