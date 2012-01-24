/*
 * Copyright 2007-2012 VTT Biotechnology
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
package guineu.modules.filter.Alignment.RANSAC;

import guineu.util.Range;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.optimization.fitting.PolynomialFitter;
import org.apache.commons.math.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class RANSAC {

        /**
         * input:
         * data - a set of observed data points
         * n - the minimum number of data values required to fit the model
         * k - the maximum number of iterations allowed in the algorithm
         * t - a threshold value for determining when a data point fits a model
         * d - the number of close data values required to assert that a model fits well to data
         *
         * output:
         * model which best fit the data
         */
        private int n;
        private double d = 1;
        private int k = 0;
        private int AlsoNumber;
        private double numRatePoints, t;
        private boolean Linear;
        private int iterationsDone = 0;

        public RANSAC(RansacAlignerParameters parameters) {

                this.numRatePoints = parameters.getParameter(RansacAlignerParameters.NMinPoints).getValue();

                this.t = parameters.getParameter(RansacAlignerParameters.Margin).getValue();

                this.k = parameters.getParameter(RansacAlignerParameters.Iterations).getValue();

                this.Linear = parameters.getParameter(RansacAlignerParameters.Linear).getValue();

        }

        public double getProgress() {
                return (double) iterationsDone / k;
        }

        /**
         * Set all parameters and start ransac.
         * @param data vector with the points which represent all possible alignments.
         */
        public void alignment(Vector<AlignStructMol> data) {
                try {
                        // If the model is non linear 4 points are taken to build the model,
                        // if it is linear only 2 points are taken.
                        if (!Linear) {
                                n = 4;
                        } else {
                                n = 2;
                        }

                        // Minimun number of points required to assert that a model fits well to data
                        if (data.size() < 10) {
                                d = 3;
                        } else {
                                d = data.size() * numRatePoints;
                        }

                        // Calculate the number of trials if the user has not define them
                        if (k == 0) {
                                k = (int) getK();
                        }

                        ransac(data);
                } catch (Exception exception) {
                        exception.printStackTrace();
                }
        }

        /**
         * Calculate k (number of trials)
         * @return number of trials "k" required to select a subset of n good data points.
         */
        private double getK() {
                double w = numRatePoints;
                double b = Math.pow(w, n);
                return Math.log10(1 - 0.99) / Math.log10(1 - b) + (Math.sqrt(1 - b) / b);
        }

        /**
         * RANSAC algorithm
         * @param data vector with the points which represent all possible alignments.
         */
        public void ransac(Vector<AlignStructMol> data) {
                double besterr = 9.9E99;

                for (int iterations = 0; iterations < k; iterations++, iterationsDone++) {
                        AlsoNumber = n;
                        // Get the initial points
                        boolean initN = getInitN(data);
                        if (!initN) {
                                continue;
                        }

                        // Calculate the model
                        if (!Linear) {
                                fittPolinomialFunction(data);
                        } else {
                                getAllModelPoints(data);
                        }

                        // If the model has the minimun number of points
                        if (AlsoNumber >= d) {
                                // Get the error of the model based on the number of points
                                double error = 9.9E99;
                                try {
                                        error = newError(data);
                                } catch (Exception ex) {
                                        Logger.getLogger(RANSAC.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                // If the error is less than the error of the last model
                                if (error < besterr) {
                                        besterr = error;
                                        for (int i = 0; i < data.size(); i++) {
                                                AlignStructMol alignStruct = data.elementAt(i);
                                                if (alignStruct.ransacAlsoInLiers || alignStruct.ransacMaybeInLiers) {
                                                        alignStruct.Aligned = true;
                                                } else {
                                                        alignStruct.Aligned = false;
                                                }

                                                alignStruct.ransacAlsoInLiers = false;
                                                alignStruct.ransacMaybeInLiers = false;

                                        }
                                }
                        }
                        // remove the model
                        for (int i = 0; i < data.size(); i++) {
                                AlignStructMol alignStruct = data.elementAt(i);
                                alignStruct.ransacAlsoInLiers = false;
                                alignStruct.ransacMaybeInLiers = false;
                        }


                }
        }

        /**
         * Take the initial points ramdoly. The points are divided by the initial number
         * of points. If the fractions contain enough number of points took one point
         * from each part.
         * @param data vector with the points which represent all possible alignments.
         * @return false if there is any problem.
         */
        private boolean getInitN(Vector<AlignStructMol> data) {
                if (data.size() > n) {
                        Collections.sort(data, new AlignStructMol());
                        double min = data.firstElement().RT;
                        double max = data.lastElement().RT;

                        Range rtRange = new Range(min, ((max - min) / 2) + min);


                        int cont = 0, bucle = 0;
                        while (cont < n / 2 && bucle < 1000) {
                                int index = (int) (data.size() * Math.random());
                                if (!data.elementAt(index).ransacMaybeInLiers && rtRange.contains(data.elementAt(index).RT)) {
                                        data.elementAt(index).ransacMaybeInLiers = true;
                                        cont++;

                                }

                                bucle++;
                        }
                        if (bucle >= 1000) {
                                getN(data, (n / 2) - cont);
                        }

                        bucle = 0;
                        rtRange = new Range(((max - min) / 2) + min, max);

                        while (cont < n && bucle < 1000) {

                                int index = (int) (data.size() * Math.random());
                                if (!data.elementAt(index).ransacMaybeInLiers && rtRange.contains(data.elementAt(index).RT)) {
                                        data.elementAt(index).ransacMaybeInLiers = true;
                                        cont++;
                                }
                                bucle++;
                        }
                        if (bucle >= 1000) {
                                getN(data, n - cont);
                        }
                        return true;
                } else {
                        return false;
                }
        }

        private void getN(Vector<AlignStructMol> data, int newN) {
                if (newN < 1) {
                        return;
                }
                int cont = 0;
                while (cont < newN) {
                        int index = (int) (data.size() * Math.random());
                        if (!data.elementAt(index).ransacMaybeInLiers) {
                                data.elementAt(index).ransacMaybeInLiers = true;
                                cont++;
                        }
                }
        }

        /**
         * Build the model creating a line with the 2 points
         * @param data vector with the points which represent all possible alignments.
         */
        private void getAllModelPoints(Vector<AlignStructMol> data) {

                // Create the regression line using the two points
                SimpleRegression regression = new SimpleRegression();

                for (int i = 0; i < data.size(); i++) {
                        AlignStructMol point = data.elementAt(i);
                        if (point.ransacMaybeInLiers) {
                                regression.addData(point.RT, point.RT2);
                        }
                }

                // Add all the points which fit the model (the difference between the point
                // and the regression line is less than "t"
                for (AlignStructMol point : data) {
                        double y = point.RT2;
                        double bestY = regression.predict(point.RT);
                        if (Math.abs(y - bestY) < t) {
                                point.ransacAlsoInLiers = true;
                                AlsoNumber++;
                        } else {
                                point.ransacAlsoInLiers = false;
                        }
                }

        }

        private void fittPolinomialFunction(Vector<AlignStructMol> data) {
                Vector<AlignStructMol> points = new Vector<AlignStructMol>();

                PolynomialFitter fitter = new PolynomialFitter(3, new GaussNewtonOptimizer(true));
                for (int i = 0; i < data.size(); i++) {
                        AlignStructMol point = data.elementAt(i);
                        if (point.ransacMaybeInLiers) {
                                points.add(point);
                                fitter.addObservedPoint(1, point.RT, point.RT2);
                        }
                }
                try {
                        PolynomialFunction function = fitter.fit();
                        for (AlignStructMol point : data) {
                                double y = point.RT2;
                                double bestY = function.value(point.RT);
                                if (Math.abs(y - bestY) < t) {
                                        point.ransacAlsoInLiers = true;
                                        AlsoNumber++;
                                } else {
                                        point.ransacAlsoInLiers = false;
                                }
                        }
                } catch (Exception ex) {
                }
        }

        /**
         * calculate the error in the model
         * @param data vector with the points which represent all possible alignments.
         * @param regression regression of the alignment points
         * @return the error in the model
         */
        private double newError(Vector<AlignStructMol> data) throws Exception {

                double numT = 1;
                for (int i = 0; i < data.size(); i++) {
                        if (data.elementAt(i).ransacAlsoInLiers || data.elementAt(i).ransacMaybeInLiers) {
                                numT++;
                        }
                }
                return 1 / numT;

        }
}
