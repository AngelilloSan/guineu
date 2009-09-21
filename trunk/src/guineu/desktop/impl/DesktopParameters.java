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
package guineu.desktop.impl;

import guineu.data.StorableParameterSet;
import guineu.data.impl.SimpleParameterSet;
import guineu.main.GuineuCore;
import guineu.modules.configuration.tables.GCGC.GCGCColumnsViewParameters;
import guineu.modules.configuration.tables.LCMS.LCMSColumnsViewParameters;
import guineu.util.NumberFormatter;
import guineu.util.NumberFormatter.FormatterType;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Iterator;



import org.dom4j.Element;

/**
 * 
 */
public class DesktopParameters implements StorableParameterSet,
        ComponentListener {

    public static final String FORMAT_ELEMENT_NAME = "format";
    public static final String FORMAT_TYPE_ATTRIBUTE_NAME = "type";
    public static final String FORMAT_TYPE_ATTRIBUTE_MZ = "m/z";
    public static final String FORMAT_TYPE_ATTRIBUTE_RT = "Retention time";
    public static final String FORMAT_TYPE_ATTRIBUTE_INT = "Intensity";
    public static final String MAINWINDOW_ELEMENT_NAME = "mainwindow";
    public static final String X_ELEMENT_NAME = "x";
    public static final String Y_ELEMENT_NAME = "y";
    public static final String WIDTH_ELEMENT_NAME = "width";
    public static final String HEIGHT_ELEMENT_NAME = "height";
    public static final String LASTPATH_ELEMENT_NAME = "lastdirectory";
    public static final String LAST_PROJECT_PATH_ELEMENT_NAME = "lastProjectDirectory";
    public static final String LAST_MYLLY_PATH_ELEMENT_NAME = "lastMyllyDirectory";
    public static final String LAST_SAVE_PATH_ELEMENT_NAME = "lastSaveDirectory";
    public static final int MAXIMIZED = -1;
    private NumberFormatter mzFormat,  rtFormat,  intensityFormat;
    private int mainWindowX,  mainWindowY,  mainWindowWidth,  mainWindowHeight;
    private String lastOpenPath = "";
    private String lastOpenProjectPath = "";
    private String lastSavePath = "";
    private String lastMyllyPath = " ";
    private SimpleParameterSet LCMSViewParameters, GCGCViewParameters;

    DesktopParameters() {
        this(new NumberFormatter(FormatterType.NUMBER, "0.000"),
                new NumberFormatter(FormatterType.TIME, "m:ss"),
                new NumberFormatter(FormatterType.NUMBER, "0.00E0"));
    }

    DesktopParameters(NumberFormatter mzFormat, NumberFormatter rtFormat,
            NumberFormatter intensityFormat) {
        this.mzFormat = mzFormat;
        this.rtFormat = rtFormat;
        this.intensityFormat = intensityFormat;

        LCMSViewParameters = new LCMSColumnsViewParameters();
		GCGCViewParameters = new GCGCColumnsViewParameters();

        MainWindow mainWindow = (MainWindow) GuineuCore.getDesktop();
        mainWindow.addComponentListener(this);

    }

    public SimpleParameterSet getViewLCMSParameters() {
        return LCMSViewParameters;
    }

    public void setViewLCMSParameters(LCMSColumnsViewParameters LCMSViewParameters) {
        this.LCMSViewParameters = LCMSViewParameters;
    }

	 public SimpleParameterSet getViewGCGCParameters() {
        return GCGCViewParameters;
    }

    public void setViewGCGCParameters(GCGCColumnsViewParameters GCGCViewParameters) {
        this.GCGCViewParameters = GCGCViewParameters;
    }

    /**
     * @return Returns the intensityFormat.
     */
    NumberFormatter getIntensityFormat() {
        return intensityFormat;
    }

    /**
     * @return Returns the mzFormat.
     */
    NumberFormatter getMZFormat() {
        return mzFormat;
    }

    /**
     * @return Returns the rtFormat.
     */
    NumberFormatter getRTFormat() {
        return rtFormat;
    }

    /**
     * @return Returns the mainWindowHeight.
     */
    int getMainWindowHeight() {
        return mainWindowHeight;
    }

    /**
     * @param mainWindowHeight
     *            The mainWindowHeight to set.
     */
    void setMainWindowHeight(int mainWindowHeight) {
        this.mainWindowHeight = mainWindowHeight;
    }

    /**
     * @return Returns the mainWindowWidth.
     */
    int getMainWindowWidth() {
        return mainWindowWidth;
    }

    /**
     * @param mainWindowWidth
     *            The mainWindowWidth to set.
     */
    void setMainWindowWidth(int mainWindowWidth) {
        this.mainWindowWidth = mainWindowWidth;
    }

    /**
     * @return Returns the mainWindowX.
     */
    int getMainWindowX() {
        return mainWindowX;
    }

    /**
     * @param mainWindowX
     *            The mainWindowX to set.
     */
    void setMainWindowX(int mainWindowX) {
        this.mainWindowX = mainWindowX;
    }

    /**
     * @return Returns the mainWindowY.
     */
    int getMainWindowY() {
        return mainWindowY;
    }

    /**
     * @param mainWindowY
     *            The mainWindowY to set.
     */
    void setMainWindowY(int mainWindowY) {
        this.mainWindowY = mainWindowY;
    }

    /**
     * @return Returns the lastOpenPath.
     */
    public String getLastOpenPath() {
        return lastOpenPath;
    }

    /**
     * @param lastOpenProjectPath
     *            The lastOpenProjectPath to set.
     */
    public void setLastOpenProjectPath(String lastOpenPath) {
        this.lastOpenProjectPath = lastOpenPath;
    }

    /**
     * @return Returns the lastOpenPath.
     */
    public String getLastOpenProjectPath() {
        return lastOpenProjectPath;
    }

    /**
     * @param lastOpenPath
     *            The lastOpenPath to set.
     */
    public void setLastOpenPath(String lastOpenPath) {
        this.lastOpenPath = lastOpenPath;
    }

    /**
     * @return Returns the lastSavePath.
     */
    public String getLastSavePath() {
        return lastSavePath;
    }

    /**
     * @return Returns the lastMyllyPath (GCGC-tof part).
     */
    public String getLastMyllyPath() {
        return lastMyllyPath;
    }

    /**
     * @return Returns the lastMyllyPath (GCGC-tof part).
     */
    public void setLastMyllyPath(String lastMyllyPath) {
        this.lastMyllyPath = lastMyllyPath;
    }

    /**
     * @param lastSavePath
     *            The lastSavePath to set.
     */
    public void setLastSavePath(String lastSavePath) {
        this.lastSavePath = lastSavePath;
    }

    public void exportValuesToXML(Element element) {
        Element mzFormatElement = element.addElement(FORMAT_ELEMENT_NAME);
        mzFormatElement.addAttribute(FORMAT_TYPE_ATTRIBUTE_NAME,
                FORMAT_TYPE_ATTRIBUTE_MZ);
        mzFormat.exportToXML(mzFormatElement);

        Element rtFormatElement = element.addElement(FORMAT_ELEMENT_NAME);
        rtFormatElement.addAttribute(FORMAT_TYPE_ATTRIBUTE_NAME,
                FORMAT_TYPE_ATTRIBUTE_RT);
        rtFormat.exportToXML(rtFormatElement);

        Element intensityFormatElement = element.addElement(FORMAT_ELEMENT_NAME);
        intensityFormatElement.addAttribute(FORMAT_TYPE_ATTRIBUTE_NAME,
                FORMAT_TYPE_ATTRIBUTE_INT);
        intensityFormat.exportToXML(intensityFormatElement);

        Element mainWindowElement = element.addElement(MAINWINDOW_ELEMENT_NAME);
        mainWindowElement.addElement(X_ELEMENT_NAME).setText(
                String.valueOf(mainWindowX));
        mainWindowElement.addElement(Y_ELEMENT_NAME).setText(
                String.valueOf(mainWindowY));
        mainWindowElement.addElement(WIDTH_ELEMENT_NAME).setText(
                String.valueOf(mainWindowWidth));
        mainWindowElement.addElement(HEIGHT_ELEMENT_NAME).setText(
                String.valueOf(mainWindowHeight));

        element.addElement(LASTPATH_ELEMENT_NAME).setText(lastOpenPath);
        element.addElement(LAST_PROJECT_PATH_ELEMENT_NAME).setText(
                lastOpenProjectPath);
        element.addElement(LAST_SAVE_PATH_ELEMENT_NAME).setText(
                lastSavePath);
        element.addElement(LAST_MYLLY_PATH_ELEMENT_NAME).setText(
                lastMyllyPath);

        LCMSViewParameters.exportValuesToXML(element);
		GCGCViewParameters.exportValuesToXML(element);

    }

    public void importValuesFromXML(Element element) {
        Iterator i = element.elements(FORMAT_ELEMENT_NAME).iterator();
        while (i.hasNext()) {
            Element formatElement = (Element) i.next();
            if (formatElement.attributeValue(FORMAT_TYPE_ATTRIBUTE_NAME).equals(FORMAT_TYPE_ATTRIBUTE_MZ)) {
                mzFormat.importFromXML(formatElement);
            }
            if (formatElement.attributeValue(FORMAT_TYPE_ATTRIBUTE_NAME).equals(FORMAT_TYPE_ATTRIBUTE_RT)) {
                rtFormat.importFromXML(formatElement);
            }
            if (formatElement.attributeValue(FORMAT_TYPE_ATTRIBUTE_NAME).equals(FORMAT_TYPE_ATTRIBUTE_INT)) {
                intensityFormat.importFromXML(formatElement);
            }
        }

        Element mainWindowElement = element.element(MAINWINDOW_ELEMENT_NAME);
        if (mainWindowElement != null) {
            mainWindowX = Integer.parseInt(mainWindowElement.elementText(X_ELEMENT_NAME));
            mainWindowY = Integer.parseInt(mainWindowElement.elementText(Y_ELEMENT_NAME));
            mainWindowWidth = Integer.parseInt(mainWindowElement.elementText(WIDTH_ELEMENT_NAME));
            mainWindowHeight = Integer.parseInt(mainWindowElement.elementText(HEIGHT_ELEMENT_NAME));
        }

        MainWindow mainWindow = (MainWindow) GuineuCore.getDesktop();
        if (mainWindowX > 0) {
            mainWindow.setLocation(mainWindowX, mainWindowY);
        }

        if ((mainWindowWidth > 0) || (mainWindowHeight > 0)) {
            mainWindow.setSize(mainWindowWidth, mainWindowHeight);
        }

        int newState = Frame.NORMAL;
        if (mainWindowWidth == MAXIMIZED) {
            newState |= Frame.MAXIMIZED_HORIZ;
        }

        if (mainWindowHeight == MAXIMIZED) {
            newState |= Frame.MAXIMIZED_VERT;
        }

        mainWindow.setExtendedState(newState);

        lastOpenPath = element.elementText(LASTPATH_ELEMENT_NAME);
        lastOpenProjectPath = element.elementText(LAST_PROJECT_PATH_ELEMENT_NAME);
        lastSavePath = element.elementText(LAST_SAVE_PATH_ELEMENT_NAME);
        lastMyllyPath = element.elementText(LAST_MYLLY_PATH_ELEMENT_NAME);

        LCMSViewParameters.importValuesFromXML(element);
		GCGCViewParameters.importValuesFromXML(element);
    }

    public DesktopParameters clone() {
        return new DesktopParameters(mzFormat.clone(), rtFormat.clone(),
                intensityFormat.clone());
    }

    /**
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent arg0) {
    }

    /**
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(ComponentEvent arg0) {
        MainWindow mainWindow = (MainWindow) GuineuCore.getDesktop();
        Point location = mainWindow.getLocation();
        mainWindowX = location.x;
        mainWindowY = location.y;
    }

    /**
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(ComponentEvent arg0) {
        MainWindow mainWindow = (MainWindow) GuineuCore.getDesktop();
        int state = mainWindow.getExtendedState();
        Dimension size = mainWindow.getSize();
        if ((state & Frame.MAXIMIZED_HORIZ) != 0) {
            mainWindowWidth = MAXIMIZED;
        } else {
            mainWindowWidth = size.width;
        }
        if ((state & Frame.MAXIMIZED_VERT) != 0) {
            mainWindowHeight = MAXIMIZED;
        } else {
            mainWindowHeight = size.height;
        }
    }

    /**
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent arg0) {
    }
}
