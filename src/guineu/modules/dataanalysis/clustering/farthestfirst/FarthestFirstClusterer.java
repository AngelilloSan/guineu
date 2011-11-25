/*
 * Copyright 2006-2011 The MZmine 2 Development Team
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
package guineu.modules.dataanalysis.clustering.farthestfirst;

import guineu.modules.dataanalysis.clustering.ClusteringAlgorithm;
import guineu.modules.dataanalysis.clustering.VisualizationType;
import guineu.parameters.ParameterSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.Clusterer;
import weka.clusterers.FarthestFirst;
import weka.core.Instance;
import weka.core.Instances;

public class FarthestFirstClusterer implements ClusteringAlgorithm {

        private ParameterSet parameters;
        private int numberOfGroups = 0;

        public FarthestFirstClusterer() {
                parameters = new FarthestFirstClustererParameters();
        }

        public String toString() {
                return "Farthest First";
        }

       
        public ParameterSet getParameterSet() {
                return parameters;
        }

        public List<Integer> getClusterGroups(Instances dataset) {
                List<Integer> clusters = new ArrayList<Integer>();
                String[] options = new String[2];
                Clusterer clusterer = new FarthestFirst();

                int numberOfGroups = parameters.getParameter(FarthestFirstClustererParameters.numberOfGroups).getValue();
                options[0] = "-N";
                options[1] = String.valueOf(numberOfGroups);

                try {
                        ((FarthestFirst) clusterer).setOptions(options);
                        clusterer.buildClusterer(dataset);
                        Enumeration e = dataset.enumerateInstances();
                        while (e.hasMoreElements()) {
                                clusters.add(clusterer.clusterInstance((Instance) e.nextElement()));
                        }
                        this.numberOfGroups = clusterer.numberOfClusters();
                } catch (Exception ex) {
                        Logger.getLogger(FarthestFirstClusterer.class.getName()).log(Level.SEVERE, null, ex);
                }
                return clusters;
        }

        public String getHierarchicalCluster(Instances dataset) {
                return null;
        }

        public VisualizationType getVisualizationType() {
                return parameters.getParameter(FarthestFirstClustererParameters.visualization).getValue();
        }

        public int getNumberOfGroups() {
                return this.numberOfGroups;
        }
}
