/*
 * Copyright 2007-2011 VTT Biotechnology
 * 
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

package guineu.modules.statistics.PCA;

import guineu.parameters.UserParameter;



public class ColoringType {

	public static final ColoringType NOCOLORING = new ColoringType(
			"No coloring");

	public static final ColoringType COLORBYFILE = new ColoringType(
			"Color by file");

        public static final ColoringType COLORBYPARAMETER = new ColoringType(
			"Color by Parameter");

	private String name;
	private UserParameter parameter;

	public ColoringType(String name) {
		this.name = name;
	}

	public ColoringType(UserParameter parameter) {
		this("Color by parameter " + parameter.getName());
		this.parameter = parameter;
	}

	public boolean isByParameter() {
		return parameter != null;
	}

	public UserParameter getParameter() {
		return parameter;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ColoringType))
			return false;
		return name.equals(((ColoringType) obj).name);
	}
	
	public String toString() {
		return name;
	}

}
