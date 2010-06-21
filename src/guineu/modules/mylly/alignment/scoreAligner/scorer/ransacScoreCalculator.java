/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guineu.modules.mylly.alignment.scoreAligner.scorer;

import guineu.modules.mylly.alignment.scoreAligner.ScoreAlignmentParameters;
import guineu.modules.mylly.datastruct.GCGCDatum;
import guineu.modules.mylly.datastruct.Peak;
import guineu.modules.mylly.datastruct.Spectrum;

/**
 *
 * @author scsandra
 */
public class ransacScoreCalculator implements ScoreCalculator {

    private final static double WORST_SCORE = Double.MAX_VALUE;
	
    public double calculateScore(Peak path, Peak peak, ScoreAlignmentParameters params) {
        double score;

		double rtiDiff = Math.abs(path.getRTI() - peak.getRTI());
		if (rtiDiff > (Double) params.getParameterValue(ScoreAlignmentParameters.rtiLax)) {
			return WORST_SCORE;
		}
		double rt2Diff = Math.abs(path.getRT2() - peak.getRT2());
		if (rt2Diff > (Double) params.getParameterValue(ScoreAlignmentParameters.rt2Lax)) {
			return WORST_SCORE;
		}
		double rt1Diff = Math.abs(path.getRT1() - peak.getRT1());
		if (rt1Diff > (Double) params.getParameterValue(ScoreAlignmentParameters.rt1Lax)) {
			return WORST_SCORE;
		}
		double comparison = compareSpectraVal(path.getSpectrum(), peak.getSpectrum());
		if (comparison > (Double) params.getParameterValue(ScoreAlignmentParameters.minSpectrumMatch)) {
			score = rtiDiff * (Double) params.getParameterValue(ScoreAlignmentParameters.rtiPenalty) +
					rt1Diff * (Double) params.getParameterValue(ScoreAlignmentParameters.rt1Penalty) +
					rt2Diff * (Double) params.getParameterValue(ScoreAlignmentParameters.rt2Penalty);
            //System.out.println(path.matchesWithName(peak)  +" - " +peak.names() );
			if (path.matchesWithName(peak)) {
				score -= (Double) params.getParameterValue(ScoreAlignmentParameters.nameMatchBonus);
			}
		} else {
			score = WORST_SCORE;
		}
		return score;
    }

    /**
     * Assumes that params#getMinSpectrumMatch() returns a value in [0,1]
	 * @param s1
	 * @param s2
	 * @return
	 **/
	public double compareSpectraVal(Spectrum s1, Spectrum s2) {
		/*if (s1.getSortingMode() != Spectrum.SortingMode.REVERSEMASS)
		{
			s1.sort(Spectrum.SortingMode.REVERSEMASS);
		}
		if (s2.getSortingMode() != Spectrum.SortingMode.REVERSEMASS)
		{
			s2.sort(Spectrum.SortingMode.REVERSEMASS);
		}
		int masses1[] = s1.getMasses();
		int masses2[] = s2.getMasses();
		int int1[] = s1.getIntensities();
		int int2[] = s2.getIntensities();

		double pathMaxIntensity = int1[0];
		double peakMaxIntensity = int2[0];

		double spec1Sum = 0.0;
		double spec2Sum = 0.0;
		double bothSpecSum = 0.0;

		int i = 0;
		int j = 0;
		int len1 = masses1.length;
		int len2 = masses2.length;
		double mass1 = masses1[0];
		double mass2 = masses2[0];

		while(i < len1 || j < len2)
		{
			while ((mass1 > mass2 || j == len2) && i < len1)
			{
				double relInt1 = int1[i++] / pathMaxIntensity;
				spec1Sum += dotTerm(mass1, relInt1);
				if (i < len1){mass1 = masses1[i];}
			}
			while((mass2 > mass1 || i == len1) && j < len2)
			{
				double relInt2 = int2[j++] / peakMaxIntensity;
				spec2Sum += dotTerm(mass2, relInt2);
				if (j < len2){mass2 = masses2[j];}
			}
			while (mass1 == mass2 && i < len1 && j < len2)
			{
				double relInt1 = int1[i++] / pathMaxIntensity;
				double relInt2 = int2[j++] / peakMaxIntensity;
				spec1Sum += dotTerm(mass1, relInt1);
				spec2Sum += dotTerm(mass2, relInt2);
				bothSpecSum += dotTerm(mass1, Math.sqrt(relInt1 * relInt2));
				if (i < len1){mass1 = masses1[i];}
				if (j < len2){mass2 = masses2[j];}
			}
//			if (i == len1 && j == len2){break;}
		}
		double dotSum = (bothSpecSum * bothSpecSum / (spec1Sum * spec2Sum));




		return dotSum;*/
        return 0;
	}

    private double dotTerm(final double mass, final double intensity) {
		return mass * mass * intensity;
	}

    public double getWorstScore() {
        return WORST_SCORE;
    }

    public boolean matches(Peak path, Peak peak, ScoreAlignmentParameters params) {
        return calculateScore(path, peak, params) < getWorstScore();
    }

    public boolean isValid(GCGCDatum peak) {
        return peak.getSpectrum() != null;
    }

    public String name() {
        return "Uses spectrum and retention times";
    }
}