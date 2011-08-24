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

/* Code created was by or on behalf of Syngenta and is released under the open source license in use for the
 * pre-existing code or project. Syngenta does not assert ownership or copyright any over pre-existing work.
 */

package guineu.modules.R;

import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

import java.util.logging.Logger;

/**
 * Utilities for interfacing with R.
 *
 * @author $Author: cpudney $ 
 */
public class RUtilities {

    // Logger.
    private static final Logger LOG = Logger.getLogger(RUtilities.class.getName());

    /**
     * R semaphore - all usage of R engine must be synchronized using this semaphore.
     */
    public static final Object R_SEMAPHORE = new Object();

    // An R Engine singleton.
    private static Rengine rEngine = null;

    /**
     * Utility class - no public access.
     */
    private RUtilities() {
        // no public access.
    }

    /**
     * Gets the R Engine.
     *
     * @return the R Engine - creating it if necessary.
     */
    public static Rengine getREngine() {

        synchronized (R_SEMAPHORE) {

            if (rEngine == null) {

                LOG.finest("Checking R Engine.");
                if (!Rengine.versionCheck()) {
                    throw new IllegalStateException("JRI version mismatch");
                }

                LOG.finest("Creating R Engine.");
                rEngine = new Rengine(new String[]{"--vanilla"}, false, new LoggerConsole());

                LOG.finest("Rengine created, waiting for R.");
                if (!rEngine.waitForR()) {
                    throw new IllegalStateException("Could not start R");
                }
            }
            return rEngine;
        }
    }

    /**
     * Logs all output.
     */
    private static class LoggerConsole implements RMainLoopCallbacks {
        @Override
        public void rWriteConsole(final Rengine re, final String text, final int oType) {
            LOG.finest(text);
        }

        @Override
        public void rBusy(final Rengine re, final int which) {
            LOG.finest("rBusy(" + which + ')');
        }

        @Override
        public String rReadConsole(final Rengine re, final String prompt, final int addToHistory) {
            return null;
        }

        @Override
        public void rShowMessage(final Rengine re, final String message) {
            LOG.finest("rShowMessage \"" + message + '\"');
        }

        @Override
        public String rChooseFile(final Rengine re, final int newFile) {
            return null;
        }

        @Override
        public void rFlushConsole(final Rengine re) {
        }

        @Override
        public void rLoadHistory(final Rengine re, final String filename) {
        }

        @Override
        public void rSaveHistory(final Rengine re, final String filename) {
        }
    }
}
