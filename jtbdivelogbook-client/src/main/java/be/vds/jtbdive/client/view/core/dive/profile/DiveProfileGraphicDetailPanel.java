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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.swing.text.MinutesNumberFormat;
import be.vds.jtbdive.client.util.ExtensionFilter;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.comparator.DiveTankSwitchTimeComparator;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.logging.Syslog;
import be.vds.jtbdive.core.utils.FileUtilities;
import be.vds.jtbdive.core.utils.StringManipulator;

public class DiveProfileGraphicDetailPanel extends JPanel implements
		DiveProfileGraph {

	private static final Syslog LOGGER = Syslog
			.getLogger(DiveProfileGraphicDetailPanel.class);
	private static final long serialVersionUID = -3404057141190941746L;
	private JTextField instantDepthTf;
	private JTextField instantTimeTf;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private XYPlot xyp;
	private XYSeries depthSerie;
	private XYSeriesCollection decoWarningCollection;
	private XYSeries decoWarningSerie;
	private XYSeriesCollection ascentTooFastCollection;
	private XYSeries ascentTooFastSerie;
	private XYSeriesCollection remainBottomTimeCollection;
	private XYSeries remainBottomTimeSerie;
	private XYSeriesCollection decoEntriesCollection;
	private XYSeries decoEntriesSerie;
	private DiveProfile diveProfile;
	private Map<XYSeriesCollection, Integer> indexMap = new HashMap<XYSeriesCollection, Integer>();
	private JLabel instantDepthLabel;
	private DiveProfileWarningControlPanel warningControlPanel;
	private Dive currentDive;
	private LogBookManagerFacade logBookManagerFacade;
	private JButton exportButton;
	private DiveProfileOptionsControlPanel optionsPanel;
	private XYSeriesCollection gazMixCollection;
	private List<XYAnnotation> gazAnnotations = new ArrayList<XYAnnotation>();

	public DiveProfileGraphicDetailPanel(
			LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		this.setMinimumSize(new Dimension(200, 100));
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BorderLayout());

		panel.add(createControls(), BorderLayout.WEST);
		panel.add(createGraphicPanel(), BorderLayout.CENTER);
		JScrollPane scroll = new JScrollPane(panel);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		this.add(scroll);
	}

	private String getDomainLegend() {
		StringBuilder time = new StringBuilder();
		time.append(I18nResourceManager.sharedInstance().getString("dive.time"));
		time.append(" (min:sec)");
		return time.toString();
	}

	private String getRangeLegend() {
		StringBuilder depth = new StringBuilder();
		depth.append(I18nResourceManager.sharedInstance().getString("depth"));
		depth.append(" (")
				.append(UnitsAgent.getInstance().getLengthUnit().getSymbol())
				.append(")");
		return depth.toString();
	}

	private Component createControls() {
		JPanel controlPanel = new JPanel(new GridBagLayout());
		controlPanel.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		GridBagLayoutManager.addComponent(controlPanel,
				createInstantDataPanel(), c, 0, 0, 1, 1, 0, 0,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(controlPanel, createWarningsPanel(),
				c, 0, 1, 1, 1, 0, 0, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(controlPanel, createOptionsPanel(),
				c, 0, 2, 1, 1, 0, 0, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(controlPanel, createEditorButton(),
				c, 0, 3, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(controlPanel,
				createImportExportButtons(), c, 0, 4, 1, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(controlPanel,
				Box.createVerticalGlue(), c, 0, 5, 1, 1, 1, 1,
				GridBagConstraints.VERTICAL, GridBagConstraints.CENTER);

		return controlPanel;
	}

	private Component createImportExportButtons() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		GridBagLayoutManager.addComponent(p, createImportButton(), c, 0, 0, 1,
				1, 0.5, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(p, createExportButton(), c, 1, 0, 1,
				1, 0.5, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		return p;
	}

	private Component createImportButton() {
		JButton b = new JButton(new AbstractAction(null, UIAgent.getInstance()
				.getIcon(UIAgent.ICON_IMPORT_24)) {

			private static final long serialVersionUID = 985413615438877711L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser ch = new JFileChooser();
				ch.setFileFilter(new ExtensionFilter("xls", "Excel File"));
				int i = ch.showOpenDialog(null);
				if (i == JFileChooser.APPROVE_OPTION) {
					DiveProfileExcelParser p = new DiveProfileExcelParser();
					try {
						File f = ch.getSelectedFile();
						DiveProfile dp = p.read(f);
						I18nResourceManager i18n = I18nResourceManager
								.sharedInstance();
						int yes = JOptionPane.showConfirmDialog(
								DiveProfileGraphicDetailPanel.this,
								i18n.getString("overwritte.diveprofile.confirm"),
								i18n.getString("confirmation"),
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (yes == JOptionPane.YES_OPTION) {
							currentDive.setDiveProfile(dp);
							setDiveProfile(dp, currentDive);
							logBookManagerFacade.setDiveChanged(currentDive);
						}
					} catch (IOException e1) {
						ExceptionDialog.showDialog(e1,
								DiveProfileGraphicDetailPanel.this);
						LOGGER.error(e1.getMessage());
					}
				}

			}
		});
		b.setToolTipText("Import");
		return b;
	}

	private Component createExportButton() {
		exportButton = new JButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_EXPORT_24)) {

			private static final long serialVersionUID = 9021437098555924654L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser ch = new JFileChooser();
				ch.setFileFilter(new ExtensionFilter("xls", "Excel File"));
				int i = ch.showSaveDialog(null);
				if (i == JFileChooser.APPROVE_OPTION) {
					DiveProfileExcelParser p = new DiveProfileExcelParser();
					try {
						File f = ch.getSelectedFile();
						if (FileUtilities.getExtension(f) == null
								|| !("xls".equals(FileUtilities.getExtension(f)))) {
							f = new File(f.getAbsoluteFile() + ".xls");
						}
						p.export(currentDive.getDate(), diveProfile, f);
					} catch (IOException e1) {
						ExceptionDialog.showDialog(e1,
								DiveProfileGraphicDetailPanel.this);
						LOGGER.error(e1.getMessage());
					}
				}

			}
		});
		exportButton.setToolTipText("Export");
		return exportButton;
	}

	private Component createEditorButton() {
		JButton button = new I18nButton(new AbstractAction("profile.edit") {

			private static final long serialVersionUID = 489467930584458674L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DiveProfileEditorDialog editor = new DiveProfileEditorDialog();
				if (diveProfile != null) {
					editor.setDiveProfile(diveProfile);
				}

				int i = editor.showDialog(800, 600);
				if (i == DiveProfileEditorDialog.OPTION_OK) {
					DiveProfile dp = editor.getDiveProfile();
					currentDive.setDiveProfile(dp);
					setDiveProfile(dp, currentDive);
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		return button;
	}

	private JPanel createInstantDataPanel() {
		I18nLabel timeLabel = new I18nLabel("dive.time");
		instantDepthLabel = new JLabel();
		setDephtLabelText();

		instantTimeTf = new JTextField(6);
		instantTimeTf.setText(null);
		instantTimeTf.setEditable(false);
		instantTimeTf.setFocusable(false);

		instantDepthTf = new JTextField(6);
		instantDepthTf.setText(null);
		instantDepthTf.setEditable(false);
		instantDepthTf.setFocusable(false);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		JPanel instantDataPanel = new DetailPanel();
		instantDataPanel.setLayout(new GridBagLayout());
		instantDataPanel.setOpaque(false);

		GridBagLayoutManager.addComponent(instantDataPanel, timeLabel, c, 0, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(instantDataPanel, instantTimeTf, c,
				1, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(instantDataPanel, instantDepthLabel,
				c, 0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(instantDataPanel, instantDepthTf, c,
				1, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		return instantDataPanel;
	}

	private JPanel createOptionsPanel() {
		optionsPanel = new DiveProfileOptionsControlPanel(this);
		return optionsPanel;
	}

	private JPanel createWarningsPanel() {
		warningControlPanel = new DiveProfileWarningControlPanel(this);
		return warningControlPanel;
	}

	private Component createGraphicPanel() {
		depthSerie = new XYSeries(DiveProfileChartFactory.SERIE_DEPTH);
		XYSeriesCollection depthCollection = new XYSeriesCollection();
		depthCollection.addSeries(depthSerie);

		chart = ChartFactory.createXYAreaChart(null, getDomainLegend(),
				getRangeLegend(), depthCollection, PlotOrientation.VERTICAL,
				false, true, false);
		xyp = chart.getXYPlot();

		Paint p = new GradientPaint(0f, 0f, UIAgent.getInstance()
				.getColorWaterBottom(), 200f, 200f, UIAgent.getInstance()
				.getColorWaterSurface(), false);
		xyp.setBackgroundPaint(p);
		xyp.setDomainGridlinePaint(UIAgent.getInstance().getColorWaterGrid());
		xyp.setRangeGridlinePaint(UIAgent.getInstance().getColorWaterGrid());

		xyp.setDomainCrosshairVisible(true);
		xyp.setRangeCrosshairVisible(true);
		xyp.setRangeCrosshairPaint(UIAgent.getInstance()
				.getColorWaterCrossHair());
		xyp.setDomainCrosshairPaint(UIAgent.getInstance()
				.getColorWaterCrossHair());
		xyp.setRangeCrosshairLockedOnData(true);
		xyp.setDomainCrosshairLockedOnData(true);
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

		createDecoWarningCollection();
		createAscentTooFastCollection();
		createRemainBottomTimeCollection();
		createDecoEntriesCollection();

		createGazMixCollection();

		chartPanel = new ChartPanel(chart);
		chart.setBackgroundPaint(null);
		chartPanel.setOpaque(false);

		chartPanel.addChartMouseListener(new ChartMouseListener() {

			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
			}

			@Override
			public void chartMouseClicked(ChartMouseEvent evt) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						double x = xyp.getDomainCrosshairValue();
						double y = xyp.getRangeCrosshairValue();
						Calendar cal = new GregorianCalendar(0, 0, 0, 0, 0,
								(int) x);
						instantTimeTf.setText(new SimpleDateFormat("HH:mm:ss")
								.format(cal.getTime()));
						instantDepthTf.setText(StringManipulator
								.formatFixedDecimalNumber(y,
										UIAgent.PRECISION_DEPTH,
										UIAgent.NUMBER_DECIMAL_CHAR));
					}
				});
			}
		});
		return chartPanel;
	}

	private void createGazMixCollection() {
		gazMixCollection = new XYSeriesCollection();
		XYSeries gazMixSerie = new XYSeries("gazmix.entries");
		gazMixCollection.addSeries(gazMixSerie);
	}

	public void setDiveProfile(DiveProfile diveProfile, Dive currentDive) {
		setDiveProfile(diveProfile, currentDive, true);
	}

	private void setDiveProfile(DiveProfile diveProfile, Dive currentDive,
			boolean clearCrossHairs) {
		this.currentDive = currentDive;
		this.diveProfile = diveProfile;
		if (diveProfile != null) {
			fillProfile(clearCrossHairs);
			LOGGER.debug("Dive profile set");
		} else {
			clear();
			LOGGER.debug("Profile cleared due to a null dive profile");
		}
	}

	public void clear() {
		this.diveProfile = null;
		clearSeries();
		instantTimeTf.setText(null);
		instantDepthTf.setText(null);
		warningControlPanel.setActive(false);
		optionsPanel.setActive(false);
		clearCrosshairs();
		clearInstantData();
	}

	private void clearSeries() {
		// normal series
		depthSerie.clear();
		ascentTooFastSerie.clear();
		decoWarningSerie.clear();
		remainBottomTimeSerie.clear();
		decoEntriesSerie.clear();

		// options series
		clearOptions();

		clearCrosshairs();
	}

	private void clearCrosshairs() {
		xyp.setDomainCrosshairValue(0);
		xyp.setRangeCrosshairValue(0);
	}

	private void createDecoEntriesCollection() {
		decoEntriesCollection = new XYSeriesCollection();
		decoEntriesSerie = new XYSeries("deco.entries");
		decoEntriesCollection.addSeries(decoEntriesSerie);
	}

	@Override
	public void showDecoEntries(boolean b) {
		if (b) {
			showCollection(decoEntriesCollection,
					DiveProfileChartFactory.SHAPE_DECO_ENTRY, UIAgent
							.getInstance().getColorDecoEntries());
		} else {
			hideCollection(decoEntriesCollection);
		}
	}

	private void createDecoWarningCollection() {
		decoWarningCollection = new XYSeriesCollection();
		decoWarningSerie = new XYSeries(DiveProfileChartFactory.SERIE_DECO_STOP);
		decoWarningCollection.addSeries(decoWarningSerie);
	}

	@Override
	public void showDecoWarning(boolean b) {
		if (b) {
			showCollection(decoWarningCollection,
					DiveProfileChartFactory.SHAPE_DECO_WARNING, UIAgent
							.getInstance().getColorWarningDecoCeiling());
		} else {
			hideCollection(decoWarningCollection);
		}
	}

	private void createAscentTooFastCollection() {
		ascentTooFastCollection = new XYSeriesCollection();
		ascentTooFastSerie = new XYSeries(
				DiveProfileChartFactory.SERIE_WARNING_ASCENT_TOO_FAST);
		ascentTooFastCollection.addSeries(ascentTooFastSerie);
	}

	private void showCollection(XYSeriesCollection xyCollection, int shape,
			Color color) {
		if (null == indexMap.get(xyCollection)) {
			int index = registerCollectionOnPlot(xyCollection);

			XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
			renderer2.setSeriesLinesVisible(0, false);
			renderer2.setAutoPopulateSeriesShape(false);
			renderer2.setSeriesPaint(0, color);
			renderer2
					.setBaseShape(DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[shape]);
			xyp.setRenderer(index, renderer2);
		}
	}

	private int registerCollectionOnPlot(XYSeriesCollection xyCollection) {
		int index = xyp.getDatasetCount();
		indexMap.put(xyCollection, index);
		xyp.setDataset(index, xyCollection);
		return index;
	}

	private void hideCollection(XYSeriesCollection xyCollection) {
		if (xyCollection != null) {
			Integer index = indexMap.get(xyCollection);
			if (index != null) {
				xyp.setDataset(index, null);
				xyp.setRenderer(index, null);
				indexMap.remove(xyCollection);
			}
		}
	}

	@Override
	public void showAscentTooFastWarning(boolean b) {
		if (b) {
			showCollection(ascentTooFastCollection,
					DiveProfileChartFactory.SHAPE_ASCENT_TOO_FAST_WARNING,
					UIAgent.getInstance().getColorWarningAscentTooFast());
		} else {
			hideCollection(ascentTooFastCollection);
		}
	}

	private void createRemainBottomTimeCollection() {
		remainBottomTimeCollection = new XYSeriesCollection();
		remainBottomTimeSerie = new XYSeries(
				DiveProfileChartFactory.SERIE_REMAINING_BOTTOM_TIME);
		remainBottomTimeCollection.addSeries(remainBottomTimeSerie);
	}

	public void showRemainBottomTimeWarning(boolean b) {
		if (b) {
			showCollection(
					remainBottomTimeCollection,
					DiveProfileChartFactory.SHAPE_REMAINING_BOTTOM_TIME_WARNING,
					UIAgent.getInstance().getColorWarningRemainingBottomTime());
		} else {
			hideCollection(remainBottomTimeCollection);
		}
	}

	private void fillProfile(boolean clearCrosshairs) {
		double x = xyp.getDomainCrosshairValue();
		double y = xyp.getRangeCrosshairValue();

		// order is important because clearing the map has influence on the
		// collections
		clearOptions();
		indexMap.clear();

		fillAlarmsData();
		fillOptions();

		showOptions();

		if (clearCrosshairs) {
			clearCrosshairs();
			clearInstantData();
		} else {
			if (x > diveProfile.getLastTimeEntry()
					|| y < diveProfile.getMaxDepth()) {
				clearCrosshairs();
				clearInstantData();
			} else {
				xyp.setDomainCrosshairValue(x);
				xyp.setRangeCrosshairValue(y);
			}
		}
	}

	private void showOptions() {
		showGazMixes(optionsPanel.isShowGazMixes());
	}

	private void clearOptions() {
		// order is important here.
		hideCollection(gazMixCollection);
		gazMixCollection.removeAllSeries();
		showGazMixAnnotations(false);
		gazAnnotations.clear();
	}

	private void fillOptions() {
		List<DiveTankEquipment> dts = currentDive.getDiveEquipment()
				.getDiveTanks();
		if (dts != null) {
			Collections.sort(dts, new DiveTankSwitchTimeComparator());
			Collections.reverse(dts);
			double endTime = diveProfile.getLastTimeEntry();
			double maxDepth = diveProfile.getMaxDepth();
			for (DiveTankEquipment diveTankEquipment : dts) {
				GazMix gazMix = diveTankEquipment.getGazMix();

				XYSeries serie = new XYSeries(gazMix.toString());
				serie.add(diveTankEquipment.getSwitchTime(), maxDepth);
				serie.add(endTime, maxDepth);
				gazMixCollection.addSeries(serie);
				endTime = diveTankEquipment.getSwitchTime();

				XYTextAnnotation annotation = new XYTextAnnotation(
						gazMix.toString(), endTime, maxDepth);
				annotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
				annotation.setFont(new Font("Arial", Font.ITALIC + Font.BOLD,
						13));
				annotation.setPaint(Color.WHITE);
				gazAnnotations.add(annotation);
			}
		}
	}

	private void clearInstantData() {
		instantDepthTf.setText(null);
		instantTimeTf.setText(null);
	}

	private void fillAlarmsData() {
		fillGraphSeries(diveProfile.getDepthEntries(),
				diveProfile.getAscentWarnings(),
				diveProfile.getDecoCeilingWarnings(),
				diveProfile.getRemainingBottomTimeWarnings(),
				diveProfile.getDecoEntries());

		showAscentTooFastWarning(warningControlPanel.isAscentTooFastSelected()
				&& diveProfile != null);
		showDecoEntries(warningControlPanel.isDecoEntriesSelected()
				&& diveProfile != null);
		showDecoWarning(warningControlPanel.isDecoWarningSelected()
				&& diveProfile != null);
		showRemainBottomTimeWarning(warningControlPanel
				.isRemainBottomTimeSelected() && diveProfile != null);

		warningControlPanel.setActive(diveProfile != null);

		optionsPanel.setActive(diveProfile != null
				&& currentDive.getDiveEquipment().containsDiveTanks());
	}

	private void fillGraphSeries(Map<Double, Double> depthEntries,
			Set<Double> ascentWarning, Set<Double> decoWarnings,
			Set<Double> remainBottomTime, Set<Double> decoEntryTime) {

		depthSerie.clear();
		ascentTooFastSerie.clear();
		decoWarningSerie.clear();
		remainBottomTimeSerie.clear();
		decoEntriesSerie.clear();

		for (Integer index : indexMap.values()) {
			xyp.setDataset(index, null);
		}

		if (depthEntries.size() > 0 && depthEntries.get(0d) == null) {
			depthEntries.put(0d, 0d);
		}

		for (Double seconds : depthEntries.keySet()) {
			Double convertedDepth = UnitsAgent.getInstance()
					.convertLengthFromModel(depthEntries.get(seconds));
			depthSerie.add(seconds, convertedDepth);
		}

		if (null != ascentWarning) {
			for (Double seconds : ascentWarning) {
				Double convertedDepth = UnitsAgent.getInstance()
						.convertLengthFromModel(depthEntries.get(seconds));
				ascentTooFastSerie.add(seconds, convertedDepth);
			}
		}

		if (null != decoWarnings) {
			for (Double seconds : decoWarnings) {
				Double convertedDepth = UnitsAgent.getInstance()
						.convertLengthFromModel(depthEntries.get(seconds));
				decoWarningSerie.add(seconds, convertedDepth);
			}
		}

		if (null != remainBottomTime) {
			for (Double seconds : remainBottomTime) {
				Double convertedDepth = UnitsAgent.getInstance()
						.convertLengthFromModel(depthEntries.get(seconds));
				remainBottomTimeSerie.add(seconds, convertedDepth);
			}
		}

		if (null != decoEntryTime) {
			for (Double seconds : decoEntryTime) {
				Double convertedDepth = UnitsAgent.getInstance()
						.convertLengthFromModel(depthEntries.get(seconds));
				decoEntriesSerie.add(seconds, convertedDepth);
			}
		}
	}

	public DiveProfile getDisplayedProfile() {
		DiveProfile dp = new DiveProfile();

		Map<Double, Double> depthMap = new HashMap<Double, Double>();
		for (int i = 0; i < depthSerie.getItemCount(); i++) {
			XYDataItem data = depthSerie.getDataItem(i);
			depthMap.put(data.getXValue(), data.getYValue());
		}

		if (depthMap.size() > 0) {
			dp.setDepthEntries(depthMap);

			// Ascent warning
			Set<Double> ascentWarning = new HashSet<Double>();
			for (int i = 0; i < ascentTooFastSerie.getItemCount(); i++) {
				XYDataItem data = ascentTooFastSerie.getDataItem(i);
				ascentWarning.add(data.getXValue());
			}
			if (ascentWarning.size() > 0) {
				dp.setAscentWarnings(ascentWarning);
			}

			// Deco warning
			Set<Double> decoWarningSet = new HashSet<Double>();
			for (int i = 0; i < decoWarningSerie.getItemCount(); i++) {
				XYDataItem data = decoWarningSerie.getDataItem(i);
				decoWarningSet.add(data.getXValue());
			}
			if (decoWarningSet.size() > 0) {
				dp.setDecoCeilingWarnings(decoWarningSet);
			}

			// Remain bottom time
			Set<Double> remainBottomTimeSet = new HashSet<Double>();
			for (int i = 0; i < remainBottomTimeSerie.getItemCount(); i++) {
				XYDataItem data = remainBottomTimeSerie.getDataItem(i);
				remainBottomTimeSet.add(data.getXValue());
			}
			if (remainBottomTimeSet.size() > 0) {
				dp.setRemainingBottomTimeWarnings(remainBottomTimeSet);
			}

			// DecoEntries
			Set<Double> decoEntriesSet = new HashSet<Double>();
			for (int i = 0; i < decoEntriesSerie.getItemCount(); i++) {
				XYDataItem data = decoEntriesSerie.getDataItem(i);
				decoEntriesSet.add(data.getXValue());
			}
			if (decoEntriesSet.size() > 0) {
				dp.setDecoEntries(decoEntriesSet);
			}

			return dp;
		}

		return null;
	}

	public void fillDive(Dive displayedDive) {
		displayedDive.setDiveProfile(getDisplayedProfile());
	}

	private void setDephtLabelText() {
		instantDepthLabel.setText(I18nResourceManager.sharedInstance()
				.getString("depth")
				+ " ("
				+ UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
	}

	public void updateUnits() {
		setAxisLegendText();
		setDephtLabelText();
		setDiveProfile(this.diveProfile, currentDive);
		repaint();
	}

	private void setAxisLegendText() {
		chart.getXYPlot().getDomainAxis().setLabel(getDomainLegend());
		chart.getXYPlot().getRangeAxis().setLabel(getRangeLegend());
	}

	public void refreshGraph() {
		setDiveProfile(currentDive.getDiveProfile(), currentDive, false);
	}

	@Override
	public void showGazMixes(boolean selected) {
		LOGGER.debug("Show gaz mixes " + selected);
		showGazMixAnnotations(selected);
		if (selected) {
			registerCollectionOnPlot(gazMixCollection);
		} else {
			hideCollection(gazMixCollection);
		}
	}

	private void showGazMixAnnotations(boolean b) {
		for (XYAnnotation annotation : gazAnnotations) {
			if (b) {
				xyp.addAnnotation(annotation);
			} else {
				xyp.removeAnnotation(annotation);
			}
		}
	}
}
