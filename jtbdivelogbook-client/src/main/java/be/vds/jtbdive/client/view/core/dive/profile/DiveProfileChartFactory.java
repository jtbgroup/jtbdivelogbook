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
package be.vds.jtbdive.client.view.core.dive.profile;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.swing.text.MinutesNumberFormat;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.units.LengthUnit;

public class DiveProfileChartFactory {

	public static final String SERIE_DECO_ENTRY = "deco.entries";
	public static final String SERIE_REMAINING_BOTTOM_TIME = "remaining.bottom.time";
	public static final String SERIE_WARNING_ASCENT_TOO_FAST = "warning.ascenttoofast";
	public static final String SERIE_DECO_STOP = "warning.decostop";
	public static final String SERIE_DEPTH = "depth";

	public static final int SHAPE_DECO_ENTRY = 1;
	public static final int SHAPE_DECO_WARNING = 2;
	public static final int SHAPE_ASCENT_TOO_FAST_WARNING = 3;
	public static final int SHAPE_REMAINING_BOTTOM_TIME_WARNING = 4;

	private static String getDomainLegend(Locale locale) {
		StringBuilder time = new StringBuilder();
		time.append(I18nResourceManager.sharedInstance().getString("dive.time",
				locale));
		time.append(" (min:sec)");
		return time.toString();
	}

	private static String getRangeLegend(Locale locale, LengthUnit lengthUnit) {
		StringBuilder depth = new StringBuilder();
		depth.append(I18nResourceManager.sharedInstance().getString("depth",
				locale));
		depth.append(" (").append(lengthUnit.getSymbol()).append(")");
		return depth.toString();
	}

	public static JFreeChart createDiveProfileChartPanel(
			DiveProfile diveProfile, Locale locale, LengthUnit lengthUnit) {
		XYSeries depthSerie = new XYSeries(SERIE_DEPTH);
		XYSeriesCollection depthCollection = new XYSeriesCollection();
		depthCollection.addSeries(depthSerie);

		JFreeChart chart = ChartFactory.createXYAreaChart(null,
				getDomainLegend(locale), getRangeLegend(locale, lengthUnit),
				depthCollection, PlotOrientation.VERTICAL, false, true, false);
		XYPlot xyp = chart.getXYPlot();

		Paint p = new GradientPaint(0f, 0f, UIAgent.getInstance()
				.getColorWaterBottom(), 200f, 200f, UIAgent.getInstance()
				.getColorWaterSurface(), false);
		xyp.setBackgroundPaint(p);
		xyp.setDomainGridlinePaint(UIAgent.getInstance()
				.getColorWaterGrid());
		xyp.setRangeGridlinePaint(UIAgent.getInstance()
				.getColorWaterGrid());
		((NumberAxis) xyp.getDomainAxis())
				.setNumberFormatOverride(new MinutesNumberFormat());

		XYAreaRenderer renderer0 = new XYAreaRenderer();
		renderer0.setOutline(true);
		renderer0.setBaseOutlinePaint(UIAgent.getInstance()
				.getColorWaterBottom());

		Color baseColor = UIAgent.getInstance().getColorBaseBackground();
		renderer0.setSeriesPaint(
				0,
				new Color(baseColor.getRed(), baseColor.getGreen(), baseColor
						.getBlue(), 50));
		xyp.setRenderer(0, renderer0);

		int i = 1;

		XYSeriesCollection decoEntriesCollection = new XYSeriesCollection();
		XYSeries decoEntriesSerie = new XYSeries(SERIE_DECO_ENTRY);
		decoEntriesCollection.addSeries(decoEntriesSerie);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
		renderer2.setSeriesLinesVisible(0, false);
		renderer2.setAutoPopulateSeriesShape(false);
		renderer2.setSeriesPaint(0, UIAgent.getInstance()
				.getColorDecoEntries());
		renderer2
				.setBaseShape(DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[SHAPE_DECO_ENTRY]);
		xyp.setDataset(i, decoEntriesCollection);
		xyp.setRenderer(i, renderer2);

		
		i++;
		XYSeriesCollection ascentTooFastCollection = new XYSeriesCollection();
		XYSeries ascentTooFastSerie = new XYSeries(
				SERIE_WARNING_ASCENT_TOO_FAST);
		ascentTooFastCollection.addSeries(ascentTooFastSerie);
		renderer2 = new XYLineAndShapeRenderer();
		renderer2.setSeriesLinesVisible(0, false);
		renderer2.setAutoPopulateSeriesShape(false);
		renderer2.setSeriesPaint(0, UIAgent.getInstance()
				.getColorWarningAscentTooFast());
		renderer2
				.setBaseShape(DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[SHAPE_ASCENT_TOO_FAST_WARNING]);
		xyp.setDataset(i, ascentTooFastCollection);
		xyp.setRenderer(i, renderer2);

		i++;
		XYSeriesCollection decoWarningCollection = new XYSeriesCollection();
		XYSeries decoWarningSerie = new XYSeries(SERIE_DECO_STOP);
		decoWarningCollection.addSeries(decoWarningSerie);
		renderer2 = new XYLineAndShapeRenderer();
		renderer2.setSeriesLinesVisible(0, false);
		renderer2.setAutoPopulateSeriesShape(false);
		renderer2.setSeriesPaint(0, UIAgent.getInstance()
				.getColorWarningDecoCeiling());
		renderer2
				.setBaseShape(DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[SHAPE_DECO_WARNING]);
		xyp.setDataset(i, decoWarningCollection);
		xyp.setRenderer(i, renderer2);

		i++;
		XYSeriesCollection remainBottomTimeCollection = new XYSeriesCollection();
		XYSeries remainBottomTimeSerie = new XYSeries(
				SERIE_REMAINING_BOTTOM_TIME);
		remainBottomTimeCollection.addSeries(remainBottomTimeSerie);
		renderer2 = new XYLineAndShapeRenderer();
		renderer2.setSeriesLinesVisible(0, false);
		renderer2.setAutoPopulateSeriesShape(false);
		renderer2.setSeriesPaint(0, UIAgent.getInstance()
				.getColorWarningRemainingBottomTime());
		renderer2
				.setBaseShape(DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[SHAPE_REMAINING_BOTTOM_TIME_WARNING]);
		xyp.setDataset(i, remainBottomTimeCollection);
		xyp.setRenderer(i, renderer2);

		
		
		
		
		Map<Double, Double> depthEntries = diveProfile.getDepthEntries();
		Set<Double> ascentWarning = diveProfile.getAscentWarnings();
		Set<Double> decoWarnings = diveProfile.getDecoCeilingWarnings();
		Set<Double> remainBottomTime = diveProfile
				.getRemainingBottomTimeWarnings();
		Set<Double> decoEntryTime = diveProfile.getDecoEntries();

		if (depthEntries.size() > 0 && depthEntries.get(0d) == null) {
			depthEntries.put(0d, 0d);
		}

		for (Double seconds : depthEntries.keySet()) {
			double d = UnitsAgent.getInstance().convertLengthFromModel(
					depthEntries.get(seconds), lengthUnit);
			depthSerie.add(seconds, Double.valueOf(d));
		}

		if (null != ascentWarning) {
			for (Double seconds : ascentWarning) {
				ascentTooFastSerie.add(seconds, depthEntries.get(seconds));
			}
		}

		if (null != decoWarnings) {
			for (Double seconds : decoWarnings) {
				decoWarningSerie.add(seconds, depthEntries.get(seconds));
			}
		}

		if (null != remainBottomTime) {
			for (Double seconds : remainBottomTime) {
				remainBottomTimeSerie.add(seconds, depthEntries.get(seconds));
			}
		}

		if (null != decoEntryTime) {
			for (Double seconds : decoEntryTime) {
				decoEntriesSerie.add(seconds, depthEntries.get(seconds));
			}
		}
		return chart;
	}

	public static BufferedImage createDiveProfilePicture(
			DiveProfile diveProfile, int width, int height,
			LengthUnit lengthUnit) {
		return createDiveProfileChartPanel(diveProfile,
				I18nResourceManager.sharedInstance().getDefaultLocale(),
				lengthUnit).createBufferedImage(width, height);
	}

	public static BufferedImage createDiveProfilePicture(
			DiveProfile diveProfile, int width, int height, Locale locale,
			LengthUnit lengthUnit) {
		return createDiveProfileChartPanel(diveProfile, locale, lengthUnit)
				.createBufferedImage(width, height);
	}
}
