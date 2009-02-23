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

package guineu.main;

import javax.swing.JOptionPane;

/**
 * This main class is executed when the user simply double-clicks on Guineu JAR
 * file. Only displays error message.
 */
public class DummyMain {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String msg = "Please run Guineu using one of the provided startGuineu scripts.";

        System.out.println(msg);

        try {
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            // do nothing
        }

    }

}
