/*
    Copyright 2006-2007 VTT Biotechnology

    This file is part of MYLLY.

    MYLLY is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    MYLLY is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MYLLY; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
package guineu.modules.mylly.filter.linearNormalizer;



import guineu.data.impl.SimplePeakListRowGCGC;
import guineu.data.impl.SimpleGCGCDataset;
import guineu.modules.mylly.alignment.scoreAligner.functions.AlignmentSorterFactory;
import guineu.modules.mylly.gcgcaligner.datastruct.GCGCDatum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class LinearNormalizer 
{
	
	private final static double BASE_LEVEL = 100.0;
	
	private final int REPORTING_FREQUENCY = 500;
	
	private final double baseLevel;

	private final SimplePeakListRowGCGC onlyStandard;
	private final List<SimplePeakListRowGCGC> _standards;
	private final double[] ends;
	private volatile double _done;
	private double _total;
	private SimpleGCGCDataset _input;

	
	public LinearNormalizer(Collection<SimplePeakListRowGCGC> standards, SimpleGCGCDataset input)
	{
		if (standards.size() == 0)
		{
			throw new IllegalArgumentException("No standards given!");
		}
		ends = new double[standards.size()];
		baseLevel = BASE_LEVEL;
		
		if (standards.size() > 1)
		{
			onlyStandard = null;

			ArrayList<SimplePeakListRowGCGC> tempRows = new ArrayList<SimplePeakListRowGCGC>(standards);
			sort(tempRows);

			_standards = Collections.unmodifiableList(tempRows);

			for (int i = 0; i < tempRows.size(); i++)
			{
				double curPoint = tempRows.get(i).getRT1();
				double nextPoint = (i == tempRows.size() - 1 ? Double.POSITIVE_INFINITY : tempRows.get(i+1).getRT1());
				double end = (curPoint + nextPoint) / 2;
				ends[i] = end;
			}
		}
		else if (standards.size() == 1)
		{
			_standards = null;
			Iterator<SimplePeakListRowGCGC> i = standards.iterator();
			onlyStandard = i.next();
		}
		else{throw new IllegalArgumentException("Empty standard list");}
		
		_input = input;
		_total = input == null ? 0 : input.rowCount(); 
	}
	
	public LinearNormalizer(Collection<SimplePeakListRowGCGC> standards)
	{
		this(standards, null);
	}

	private void sort(List<SimplePeakListRowGCGC> rows)
	{
		Collections.sort(rows, AlignmentSorterFactory.getComparator(AlignmentSorterFactory.SORT_MODE.rt2));
		Collections.sort(rows, AlignmentSorterFactory.getComparator(AlignmentSorterFactory.SORT_MODE.rt1));
	}

	
	protected SimpleGCGCDataset actualMap(SimpleGCGCDataset input)
	{
		_input = input;
		_total = input.rowCount();
		
		try
		{
			return call();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private int findProperIndex(SimplePeakListRowGCGC r)
	{
		int index = java.util.Arrays.binarySearch(ends, r.getRT1());
		if (index < 0)
		{
			index = -(index + 1);
		}
		return index;
	}


	public String getName()
	{
		return "Linear normalizer";
	}

	public SimpleGCGCDataset call() throws Exception
	{
		SimpleGCGCDataset normalized = new SimpleGCGCDataset(_input.getColumnNames(),
		                                     _input.getParameters(), 
		                                     _input.getAligner());
		if (onlyStandard == null) //Multiple standards
		{
			GCGCDatum[][] stds = new GCGCDatum[_standards.size()][];
			for (int i = 0; i < _standards.size(); i++)
			{
				stds[i] = _standards.get(i).getRow().toArray(new GCGCDatum[0]);
			}
			double[][] coeffs = new double[stds.length][];
			int count = 0;
			for (int i = 0; i < stds.length; i++)
			{
				GCGCDatum[] curStd = stds[i];
				double[] curCoeffs = new double[curStd.length];
				for (int j = 0; j < curCoeffs.length; j++)
				{
					curCoeffs[j] = baseLevel / curStd[i].getArea();
				}
				coeffs[i] = curCoeffs;
			}
			ArrayList<SimplePeakListRowGCGC> rows = new ArrayList<SimplePeakListRowGCGC>(_input.getAlignment());
			
                        for (int i = 0; i < rows.size(); i++)
			{
				SimplePeakListRowGCGC cur = rows.get(i);
                                if(cur.getMass() < 0){
                                    int ix = findProperIndex(cur);
                                    rows.set(i, cur.scaleArea(coeffs[ix]));
                                    if (count++ % REPORTING_FREQUENCY == 0)
                                    {
                                            _done = count;
                                    }
                                }
			}
			normalized.addAll(rows);
		}
		else //Only one standard
		{
			int count = 0;
			GCGCDatum[] stds = onlyStandard.getRow().toArray(new GCGCDatum[0]);
			double[] coeffs = new double[stds.length];
			for (int i = 0; i < stds.length; i++)
			{
				coeffs[i] = baseLevel / stds[i].getArea();
			}
			ArrayList<SimplePeakListRowGCGC> rows = new ArrayList<SimplePeakListRowGCGC>(_input.getAlignment());
			for (int i = 0; i < rows.size(); i++)
			{				
				SimplePeakListRowGCGC scaled = rows.get(i).scaleArea(coeffs);
				rows.set(i, scaled);
				if (count++ % REPORTING_FREQUENCY == 0)
				{
					_done = count;
				}
			}
			normalized.addAll(rows);
		}
		_done = _total;
		return normalized;
	}

	public double getDone()
	{
		return _done;
	}

	public double getTotal()
	{
		return _total;
	}
}
