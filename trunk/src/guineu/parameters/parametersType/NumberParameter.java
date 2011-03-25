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
 * Guineu; if not, write to the Free Software Foundation, Inc., 51 Franklin
 * St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package guineu.parameters.parametersType;

import guineu.parameters.UserParameter;
import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;


import org.w3c.dom.Element;

/**
 * Simple Parameter implementation
 * 
 * 
 */
/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
public class NumberParameter implements UserParameter<Number, JFormattedTextField> {

	private String name, description;
	private Number value;
	private NumberFormat format;

	public NumberParameter(String name, String description) {
		this(name, description, NumberFormat.getNumberInstance(), null);
	}

	public NumberParameter(String name, String description, NumberFormat format) {
		this(name, description, format, null);
	}

	public NumberParameter(String name, String description,
			NumberFormat format, Number defaultValue) {
		this.name = name;
		this.description = description;
		this.value = defaultValue;
		this.format = format;
	}

	
	@Override
	public String getName() {
		return name;
	}

	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public JFormattedTextField createEditingComponent() {
		JFormattedTextField textField = new JFormattedTextField(format);
		textField.setPreferredSize(new Dimension(200, textField.getPreferredSize().height));
		return textField;
	}

	@Override
	public void setValueFromComponent(JFormattedTextField component) {
		value = (Number) component.getValue();
	}

	@Override
	public void setValue(Number value) {
		this.value = value;
	}

	@Override
	public NumberParameter clone() {
		NumberParameter copy = new NumberParameter(name, description, format);
		copy.setValue(this.getValue());
		return copy;
	}

	@Override
	public void setValueToComponent(JFormattedTextField component,
			Number newValue) {
		component.setValue(newValue);
	}

	@Override
	public Number getValue() {
		return value;
	}

	public Double getDouble() {
		if (value == null)
			return null;
		return value.doubleValue();
	}

	public Integer getInt() {
		if (value == null)
			return null;
		return value.intValue();
	}

	@Override
	public void loadValueFromXML(Element xmlElement) {
		String numString = xmlElement.getTextContent();
		if (numString.length() == 0)
			return;
		this.value = Double.parseDouble(numString);
	}

	@Override
	public void saveValueToXML(Element xmlElement) {
		if (value == null)
			return;
		xmlElement.setTextContent(value.toString());
	}

	@Override
	public String toString() {
		return name;
	}

}
