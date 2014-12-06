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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
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
import be.vds.jtbdive.client.swing.text.MinutesNumberFormat;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class DiveProfileChartPanelEditor extends JPanel implements Observer,
		DiveProfileGraph {

	private static final long serialVersionUID = 741488154766575036L;
	private XYPlot plot;
	private ChartPanel panel;
	private XYSeries depthSerie;
	private double lowestDepth = -10;
	private double maxTime = 600;
	private NumberAxis dAxis;
	private NumberAxis rAxis;
	private DiveProfileEditor diveProfileEditor;
	private Map<XYSeriesCollection, Integer> indexMap = new HashMap<XYSeriesCollection, Integer>();

	private XYSeriesCollection decoWarningCollection;
	private XYSeries decoWarningSerie;
	private XYSeriesCollection ascentTooFastCollection;
	private XYSeries ascentTooFastSerie;
	private XYSeriesCollection remainBottomTimeCollection;
	private XYSeries remainBottomTimeSerie;
	private XYSeriesCollection decoEntriesCollection;
	private XYSeries decoEntriesSerie;

	public DiveProfileChartPanelEditor(DiveProfileEditor diveProfileEditor) {
		registerDiveProfileEditor(diveProfileEditor);
		init();
	}

	public void registerDiveProfileEditor(DiveProfileEditor diveProfileEditor) {
		if(this.diveProfileEditor != null)
			this.diveProfileEditor.deleteObserver(this);
		
		this.diveProfileEditor = diveProfileEditor;
		
		if (this.diveProfileEditor != null)
			this.diveProfileEditor.addObserver(this);
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.add(createChartPanel(), BorderLayout.CENTER);
	}

	private Component createChartPanel() {
		depthSerie = new XYSeries("depth");
		XYSeriesCollection depthCollection = new XYSeriesCollection();
		depthCollection.addSeries(depthSerie);

		JFreeChart chart = ChartFactory.createXYLineChart("",
				I18nResourceManager.sharedInstance().getString("time"),
				I18nResourceManager.sharedInstance().getString("depth"),
				depthCollection, PlotOrientation.VERTICAL, true, true, false);
		plot = chart.getXYPlot();
		Paint p = new GradientPaint(0f, 0f, UIAgent.getInstance()
				.getColorWaterBottom(), 200f, 200f, UIAgent.getInstance()
				.getColorWaterSurface(), false);
		plot.setBackgroundPaint(p);

		XYAreaRenderer renderer0 = new XYAreaRenderer();
		renderer0.setOutline(true);
		renderer0.setBaseOutlinePaint(UIAgent.getInstance()
				.getColorProfileEdition());

		Color baseColor = UIAgent.getInstance().getColorBaseBackground();
		renderer0.setSeriesPaint(
				0,
				new Color(baseColor.getRed(), baseColor.getGreen(), baseColor
						.getBlue(), 50));
		plot.setRenderer(0, renderer0);

		dAxis = new NumberAxis();
		rAxis = new NumberAxis();
		// dAxis.setAutoRange(false);
		// rAxis.setAutoRange(false);
		// dAxis.setRange(0, maxTime);
		// rAxis.setRange(lowestDepth, 0);
		plot.setDomainAxis(dAxis);
		plot.setRangeAxis(rAxis);

		// plot.setRangeCrosshairLockedOnData(false);
		// plot.setDomainCrosshairLockedOnData(false);
		((NumberAxis) plot.getDomainAxis())
				.setNumberFormatOverride(new MinutesNumberFormat());

		panel = new ChartPanel(chart);
		// panel.addChartMouseListener(new ChartMouseListener() {
		//
		// @Override
		// public void chartMouseMoved(ChartMouseEvent arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void chartMouseClicked(ChartMouseEvent evt) {
		//
		// double x = plot.getDomainAxis().java2DToValue(
		// panel.translateScreenToJava2D(
		// evt.getTrigger().getPoint()).getX(),
		// panel.getChartRenderingInfo().getPlotInfo()
		// .getDataArea(), plot.getDomainAxisEdge());
		//
		// double y = plot.getRangeAxis().java2DToValue(
		// panel.translateScreenToJava2D(
		// evt.getTrigger().getPoint()).getY(),
		// panel.getChartRenderingInfo().getPlotInfo()
		// .getDataArea(), plot.getRangeAxisEdge());
		//
		// diveProfileEditor.addEntry(x, y, this);
		// }
		// });

		createDecoWarningCollection();
		createAscentTooFastCollection();
		createRemainBottomTimeCollection();
		createDecoEntriesCollection();

		return panel;
	}

	private void createDecoWarningCollection() {
		decoWarningCollection = new XYSeriesCollection();
		decoWarningSerie = new XYSeries(DiveProfileChartFactory.SERIE_DECO_STOP);
		decoWarningCollection.addSeries(decoWarningSerie);
	}

	private void createAscentTooFastCollection() {
		ascentTooFastCollection = new XYSeriesCollection();
		ascentTooFastSerie = new XYSeries(
				DiveProfileChartFactory.SERIE_WARNING_ASCENT_TOO_FAST);
		ascentTooFastCollection.addSeries(ascentTooFastSerie);
	}

	private void createRemainBottomTimeCollection() {
		remainBottomTimeCollection = new XYSeriesCollection();
		remainBottomTimeSerie = new XYSeries(
				DiveProfileChartFactory.SERIE_REMAINING_BOTTOM_TIME);
		remainBottomTimeCollection.addSeries(remainBottomTimeSerie);
	}

	private void createDecoEntriesCollection() {
		decoEntriesCollection = new XYSeriesCollection();
		decoEntriesSerie = new XYSeries("deco.entries");
		decoEntriesCollection.addSeries(decoEntriesSerie);
	}

	// private void adaptAxis(double x, double y) {
	// if (maxTime - 120 < x) {
	// maxTime = x + 300;
	// dAxis.setRange(dAxis.getRange().getLowerBound(), maxTime);
	// // System.out.println("adapt domain: " + maxTime);
	// }
	//
	// if (lowestDepth + 5 > y) {
	// lowestDepth = y - 5;
	// rAxis.setRange(lowestDepth, rAxis.getRange().getUpperBound());
	// // System.out.println("adapt range : " + lowestDepth);
	// }
	//
	// }

	public Map<Double, Double> getDepthEntries() {
		Map<Double, Double> map = new HashMap<Double, Double>();
		for (int i = 0; i < depthSerie.getItemCount(); i++) {
			map.put(depthSerie.getDataItem(i).getXValue(), depthSerie
					.getDataItem(i).getYValue());
		}
		return map;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof DiveProfileEditionEvent) {

			DiveProfileEditionEvent event = (DiveProfileEditionEvent) arg;

			if (event.getType() == DiveProfileEditionEvent.ENTRY_SELECTED) {
				if ((ProfileEntry) event.getNewValue() != null) {
					ProfileEntry entry = (ProfileEntry) event.getNewValue();
					setCross(entry.getTime(), entry.getDepth());
				}
			} else if (event.getType() == DiveProfileEditionEvent.DIVE_PROFILE_SET) {
				reloadDepthSerie();
			} else {
				reloadDepthSerie();
			}
		}
	}

	private void setCross(double time, Double depth) {
		if (time == -1) {
			plot.setDomainCrosshairVisible(false);
			plot.setRangeCrosshairVisible(false);
			return;
		}

		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		plot.setRangeCrosshairPaint(UIAgent.getInstance()
				.getColorWaterCrossHair());
		plot.setDomainCrosshairPaint(UIAgent.getInstance()
				.getColorWaterCrossHair());
		plot.setRangeCrosshairLockedOnData(true);
		plot.setDomainCrosshairLockedOnData(true);

		plot.setDomainCrosshairValue(time);
		plot.setRangeCrosshairValue(depth);
	}

	private void reloadDepthSerie() {
		plot.setDomainCrosshairVisible(false);
		plot.setRangeCrosshairVisible(false);

		clearSeries();
		for (ProfileEntry entry : diveProfileEditor.getProfileEntries()) {
			double time = entry.getTime();
			double depth = entry.getDepth();
			depthSerie.add(time, depth);

			if (entry.isAscentWarning())
				ascentTooFastSerie.add(time, depth);

			if (entry.isDecoCeilingWarning())
				decoWarningSerie.add(time, depth);

			if (entry.isRemainingBottomTimeWarning())
				remainBottomTimeSerie.add(time, depth);

			if (entry.isDecoEntry())
				decoEntriesSerie.add(time, depth);
		}
	}

	private void clearSeries() {
		depthSerie.clear();
		ascentTooFastSerie.clear();
		decoWarningSerie.clear();
		remainBottomTimeSerie.clear();
		decoEntriesSerie.clear();
	}

	public Component getWarningControlPanel() {
		DiveProfileWarningControlPanel p = new DiveProfileWarningControlPanel(this);
		return p;
	}

	public void showDecoEntries(boolean b) {
		if (b) {
			showWarningCollection(decoEntriesCollection, 1, UIAgent
					.getInstance().getColorDecoEntries());
		} else {
			hideWarningCollection(decoEntriesCollection);
		}
	}

	public void showRemainBottomTimeWarning(boolean b) {
		if (b) {
			showWarningCollection(remainBottomTimeCollection, 3, UIAgent
					.getInstance().getColorWarningRemainingBottomTime());
		} else {
			hideWarningCollection(remainBottomTimeCollection);
		}
	}

	public void showAscentTooFastWarning(boolean b) {
		if (b) {
			showWarningCollection(ascentTooFastCollection, 2, UIAgent
					.getInstance().getColorWarningAscentTooFast());
		} else {
			hideWarningCollection(ascentTooFastCollection);
		}
	}

	public void showDecoWarning(boolean b) {
		if (b) {
			showWarningCollection(decoWarningCollection, 1, UIAgent
					.getInstance().getColorWarningDecoCeiling());
		} else {
			hideWarningCollection(decoWarningCollection);
		}
	}

	private void showWarningCollection(XYSeriesCollection xyCollection,
			int shape, Color color) {
		if (null == indexMap.get(xyCollection)) {
			int index = plot.getDatasetCount();
			indexMap.put(xyCollection, index);
			XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
			renderer2.setSeriesLinesVisible(0, false);
			renderer2.setAutoPopulateSeriesShape(false);
			renderer2.setSeriesPaint(0, color);
			renderer2
					.setBaseShape(DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[shape]);
			plot.setDataset(index, xyCollection);
			plot.setRenderer(index, renderer2);
		}
	}

	private void hideWarningCollection(XYSeriesCollection xyCollection) {
		if (xyCollection != null) {
			Integer index = indexMap.get(xyCollection);
			if (index != null) {
				plot.setDataset(index, null);
				plot.setRenderer(index, null);
				indexMap.remove(xyCollection);
			}
		}
	}

    @Override
    public void showGazMixes(boolean selected) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
        
}
