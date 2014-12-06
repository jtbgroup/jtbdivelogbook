/*
 * Jt'B Dive Logbook - Electronic dive logbook.
 * 
 * Copyright (C) 2010  Gautier Vanderslyen
 * 
 * Jt'B Dive Logbook is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package be.vds.jtbdive.client.view.core.stats;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.stats.DiveDepthsStatQueryObject;
import be.vds.jtbdive.client.core.stats.DiveSiteStatQueryObject;
import be.vds.jtbdive.client.core.stats.DiveStatQueryObject;
import be.vds.jtbdive.client.core.stats.DiverStatQueryObject;
import be.vds.jtbdive.client.core.stats.StatPoint;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.client.core.stats.StatSerie;
import be.vds.jtbdive.client.core.stats.YearsStatQueryObject;
import be.vds.jtbdive.client.core.stats.axisy.StatYAxis;
import be.vds.jtbdive.client.swing.text.HoursMinutesNumberFormat;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;

public class StatChartGenerator {

	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();

	public static JFreeChart buildChart(StatQueryObject sqo) {
		if (sqo instanceof DiveStatQueryObject) {
			return buildChartForDive(sqo);
		} else if (sqo instanceof DiverStatQueryObject) {
			return buildChartForDiver(sqo);
		} else if (sqo instanceof DiveSiteStatQueryObject) {
			return buildChartForDiveSite(sqo);
		} else if (sqo instanceof YearsStatQueryObject) {
			return buildChartForYear(sqo);
		} else if (sqo instanceof DiveDepthsStatQueryObject) {
			return buildChartForDiveDepths(sqo);
		}
		return null;
	}

	private static JFreeChart buildChartForDiveDepths(StatQueryObject sqo) {
		Collection<StatSerie> s = sqo.getValues();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (StatSerie statSerie : s) {
			List<StatPoint> points = statSerie.getPoints();
			Collections.sort(points, new Comparator<StatPoint>() {
				@Override
				public int compare(StatPoint o1, StatPoint o2) {
					return -((Double) (o1).getX()).compareTo((Double) (o2)
							.getX());
				}
			});

			// int i = 0;
			// int indexMax = points.size()-1;
			// for (StatPoint point : points) {
			// String label =null;
			// if(i == indexMax){
			// label = "< "+((Double) point.getX());
			// }else{
			// label = (Double) point.getX() + " - "+(Double)
			// points.get(i+1).getX();
			// }
			// dataset.addValue(point.getY(),
			// label, "");
			// i++;
			// }

			for (StatPoint point : points) {
				dataset.addValue(point.getY(), String.valueOf(UnitsAgent
						.getInstance().convertLengthFromModel(
								(Double) point.getX())), "");
			}
		}

		String xLabel = i18n.getString("depth") + " ("
				+ UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")";
		String yLabel = i18n.getString("dives.numberof");
		JFreeChart chart = createBarChart(dataset, xLabel, yLabel);
		return chart;
	}

	private static JFreeChart buildChartForYear(StatQueryObject sqo) {
		Collection<StatSerie> s = sqo.getValues();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (StatSerie statSerie : s) {
			List<StatPoint> points = statSerie.getPoints();
			Collections.sort(points, new Comparator<StatPoint>() {
				@Override
				public int compare(StatPoint o1, StatPoint o2) {
					return ((Integer) (o1).getX()).compareTo((Integer) (o2)
							.getX());
				}
			});

			for (StatPoint point : points) {
				dataset.addValue(point.getY(),
						((Integer) point.getX()).toString(), "");
			}
		}

		JFreeChart chart = createBarChart(dataset, getXLabel(sqo),
				getYLabel(sqo));

		if (sqo.getStatYAxisParams().getStatYAxis().equals(StatYAxis.DIVE_TIME)) {
			CategoryPlot xyp = (CategoryPlot) chart.getPlot();
			((NumberAxis) xyp.getRangeAxis())
					.setNumberFormatOverride(new HoursMinutesNumberFormat());
		}

		return chart;
	}

	private static String getYLabel(StatQueryObject sqo) {
		switch (sqo.getStatYAxisParams().getStatYAxis()) {
		case DEPTHS:
			return i18n.getString("depths") + " ("
					+ UnitsAgent.getInstance().getLengthUnit().getSymbol()
					+ ")";
		case DIVE_TIME:
			return i18n.getString("dive.time");
		case NUMBER_OF_DIVE:
			return i18n.getString("dives.numberof");
		case TEMPERATURES:
			return i18n.getString("temperatures.water") + " ("
					+ UnitsAgent.getInstance().getTemperatureUnit().getSymbol()
					+ ")";
		}
		return null;
	}

	private static String getXLabel(StatQueryObject sqo) {
		if (sqo instanceof DiveStatQueryObject) {
			return i18n.getString("dives");
		} else if (sqo instanceof DiverStatQueryObject) {
			return i18n.getString("divers");
		} else if (sqo instanceof DiveSiteStatQueryObject) {
			return i18n.getString("divesites");
		} else if (sqo instanceof YearsStatQueryObject) {
			return i18n.getString("years");
		} else if (sqo instanceof DiveDepthsStatQueryObject) {
			return i18n.getString("dive.depths") + " ("
					+ UnitsAgent.getInstance().getLengthUnit().getSymbol()
					+ ")";
		}
		return null;
	}

	private static JFreeChart buildChartForDiveSite(StatQueryObject sqo) {
		Collection<StatSerie> s = sqo.getValues();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (StatSerie statSerie : s) {
			for (StatPoint point : statSerie.getPoints()) {
				dataset.addValue(point.getY(),
						((DiveSite) point.getX()).getName(), "");
			}
		}

		JFreeChart chart = createBarChart(dataset, getXLabel(sqo),
				getYLabel(sqo));

		return chart;
	}

	private static JFreeChart buildChartForDiver(StatQueryObject sqo) {
		Collection<StatSerie> s = sqo.getValues();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (StatSerie statSerie : s) {
			for (StatPoint point : statSerie.getPoints()) {
				dataset.addValue(point.getY(),
						((Diver) point.getX()).getFullName(), "");
			}
		}

		JFreeChart chart = createBarChart(dataset, getXLabel(sqo),
				getYLabel(sqo));

		return chart;
	}

	private static JFreeChart buildChartForDive(StatQueryObject sqo) {
		Collection<StatSerie> s = sqo.getValues();
		String legend = I18nResourceManager.sharedInstance().getString(
				"dive.times");
		TimeSeriesCollection collection = new TimeSeriesCollection();
		for (StatSerie statSerie : s) {
			TimeSeries ts = new TimeSeries(legend);
			for (StatPoint point : statSerie.getPoints()) {
				Date dd = (Date) point.getX();
				FixedMillisecond day = new FixedMillisecond(dd);
				Object index = ts.getDataItem(day);
				if (null != index) {
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(dd);
					gc.add(Calendar.MILLISECOND, 1);
					day = new FixedMillisecond(gc.getTime());
				}

				if (sqo.getStatYAxisParams().getStatYAxis()
						.equals(StatYAxis.DEPTHS)) {
					ts.add(day, UnitsAgent.getInstance()
							.convertLengthFromModel((Double) point.getY()));
				} else if (sqo.getStatYAxisParams().getStatYAxis()
						.equals(StatYAxis.TEMPERATURES)) {
					ts.add(day, UnitsAgent.getInstance()
							.convertTemperatureFromModel((Double) point.getY()));
				} else {
					ts.add(day, point.getY());
				}
			}
			collection.addSeries(ts);
		}

		JFreeChart chart = createLineChart(collection, getXLabel(sqo),
				getYLabel(sqo));

		if (sqo.getStatYAxisParams().getStatYAxis().equals(StatYAxis.DIVE_TIME)) {
			XYPlot xyp = (XYPlot) chart.getPlot();
			((NumberAxis) xyp.getRangeAxis())
					.setNumberFormatOverride(new HoursMinutesNumberFormat());
		}

		return chart;
	}

	private static JFreeChart createLineChart(TimeSeriesCollection collection,
			String xLabel, String yLabel) {
		JFreeChart chart = ChartFactory.createTimeSeriesChart("", // chart title
				xLabel, // domain axis label
				yLabel, // range axis label
				collection, // data
				true, // include legend
				true, // tooltips
				false // urls
				);
		return chart;
	}

	private static JFreeChart createBarChart(DefaultCategoryDataset dataset,
			String xLabel, String yLabel) {
		JFreeChart chart = ChartFactory.createBarChart("", // chart title
				xLabel, // domain axis label
				yLabel, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL,// Plot orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		return chart;
	}
}
