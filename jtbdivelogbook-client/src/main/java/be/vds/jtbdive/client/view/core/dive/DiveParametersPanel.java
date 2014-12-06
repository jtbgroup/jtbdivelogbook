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
package be.vds.jtbdive.client.view.core.dive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtb.swing.component.DurationComponent;
import be.vds.jtb.swing.component.MultiSelectionComboBox;
import be.vds.jtb.swing.component.StarRater;
import be.vds.jtb.swing.component.StarRater.StarListener;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.core.comparator.KeyedCatalogComparator;
import be.vds.jtbdive.client.core.event.DiveModification;
import be.vds.jtbdive.client.swing.component.DateTimePicker;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.I18nTitlePanel;
import be.vds.jtbdive.client.swing.component.ValuePanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.core.divesite.DiveSiteChooser;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogMultiSelectionComboBoxFormatter;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PhysiologicalStatus;
import be.vds.jtbdive.core.core.Rating;
import be.vds.jtbdive.core.core.catalogs.DivePlatform;
import be.vds.jtbdive.core.core.catalogs.DivePurpose;
import be.vds.jtbdive.core.core.catalogs.DiveType;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;
import be.vds.jtbdive.core.utils.UnitsUtilities;

public class DiveParametersPanel extends JPanel {

	private static final long serialVersionUID = -4453276350971596381L;
	private static final Dimension DIM_COMMENT = new Dimension(200, 100);
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private JSpinner maxDepthComponent;
	private DurationComponent diveTimeComponent;
	private DurationComponent surfaceTimeComp;
	private JSpinner waterTemperatureComp;
	private JSpinner numberComponent;
	private JTextArea commentTextArea;
	private Window parentWindow;
	private DiverManagerFacade diverManagerFacade;
	private DateTimePicker dateTimeComponent;
	private PalanqueeTablePanel palanqueeTablePanel;
	private DiveSiteManagerFacade diveLocationManagerFacade;
	private DiveSiteChooser diveSiteChooser;
	private PhysiologicalParametersPanel physioParametersPanel;
	private Dive currentDive;
	private boolean enableChangePropagation;
	private JLabel temperatureUnitLabel;
	private JLabel maxDepthUnitLabel;
	private LogBookManagerFacade logBookManagerFacade;
	private StarRater starRaterPanel;
	private JComboBox divePlaformCb;
	private MultiSelectionComboBox diveTypeCb;
	private MultiSelectionComboBox divePurposeCb;

	public DiveParametersPanel(Window parentWindow,
			LogBookManagerFacade logBookManagerFacade,
			DiverManagerFacade diverManagerFacade,
			DiveSiteManagerFacade diveLocationManagerFacade) {
		this.parentWindow = parentWindow;
		this.logBookManagerFacade = logBookManagerFacade;
		this.diverManagerFacade = diverManagerFacade;
		this.diveLocationManagerFacade = diveLocationManagerFacade;
		init();
		createUIcontrollerObserver();
	}

	private void createUIcontrollerObserver() {
		UIAgent.getInstance().addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				updateUIObjects();
			}
		});
	}

	private void init() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBackground(UIAgent.getInstance().getColorBaseBackground());
		panel.setOpaque(true);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		GridBagLayoutManager.addComponent(panel, createBorderPanel("detail"),
				c, 0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(panel, createDetailPanel(), c, 0, 1,
				1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel, createBorderPanel("comment"),
				c, 2, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(panel, createCommentPanel(), c, 2, 1,
				1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel,
				createBorderPanel("physiological.status"), c, 0, 2, 1, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(panel,
				createPhysiologicalStatusPanel(), c, 0, 3, 1, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel, createBorderPanel("buddies"),
				c, 2, 2, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(panel, createPalanqueePanel(), c, 2,
				3, 1, 1, 1, 0, GridBagConstraints.BOTH,
				GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel,
				createBorderPanel("criterias"), c, 0, 4, 1, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(panel, createCaracteristicsPanel(),
				c, 0, 5, 1, 1, 1, 0, GridBagConstraints.BOTH,
				GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel, createBorderPanel("rating"),
				c, 2, 4, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(panel, createRatingPanel(), c, 2, 5,
				1, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel, Box.createGlue(), c, 0, 6, 1,
				1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		this.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(panel);
		SwingComponentHelper.displayJScrollPane(scroll);
		this.add(scroll, BorderLayout.CENTER);

		updateUnits();
	}

	private Component createCaracteristicsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();

		c.insets.set(0, 0, 0, 2);
		GridBagLayoutManager.addComponent(panel, createDivePurposeValuePanel(),
				c, 0, 0, 1, 1, 1, 0, GridBagConstraints.BOTH,
				GridBagConstraints.NORTH);
		GridBagLayoutManager.addComponent(panel, createDiveTypeValuePanel(), c,
				1, 0, 1, 1, 1, 0, GridBagConstraints.BOTH,
				GridBagConstraints.NORTH);
		GridBagLayoutManager.addComponent(panel,
				createDivePlatformValuePanel(), c, 2, 0, 1, 1, 1, 0,
				GridBagConstraints.BOTH, GridBagConstraints.NORTH);

		return panel;
	}

	private Component createDivePlatformValuePanel() {
		ValuePanel p = new ValuePanel("platform");
		p.setComponent(createDivePlatformComponent());
		return p;
	}

	private JComponent createDivePlatformComponent() {
		KeyedCatalog[] values = DivePlatform.values();
		Arrays.sort(values, new KeyedCatalogComparator());
		divePlaformCb = new JComboBox(values);
		divePlaformCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (enableChangePropagation) {
					currentDive.setDivePlatform((DivePlatform) divePlaformCb
							.getSelectedItem());
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		divePlaformCb.setRenderer(new KeyedCatalogRenderer());
		divePlaformCb.setSelectedIndex(-1);
		return divePlaformCb;
	}

	private Component createDivePurposeValuePanel() {
		ValuePanel p = new ValuePanel("purpose", UIAgent.getInstance().getIcon(
				UIAgent.ICON_DIVE_PURPOSE_16));
		p.setComponent(createDivePurposeComponent());
		return p;
	}

	private JComponent createDivePurposeComponent() {
		List<Object> obj = new ArrayList<Object>();
		DivePurpose[] values = DivePurpose.values();
		Arrays.sort(values, new KeyedCatalogComparator());
		for (DivePurpose t : values) {
			obj.add(t);
		}
		divePurposeCb = new MultiSelectionComboBox();
		divePurposeCb.setVisibleRowCount(4);
		divePurposeCb
				.setFormatter(new KeyedCatalogMultiSelectionComboBoxFormatter());
		divePurposeCb.setData(obj);
		divePurposeCb.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						MultiSelectionComboBox.SELECTION_CHANGED)) {
					adaptDive(evt);
				}

			}

			private void adaptDive(PropertyChangeEvent evt) {
				@SuppressWarnings("unchecked")
				List<Object> selection = (List<Object>) evt.getNewValue();
				List<DivePurpose> dtps = new ArrayList<DivePurpose>();
				if (selection != null) {
					for (Object o : selection) {
						dtps.add((DivePurpose) o);
					}
				}
				currentDive.setDivePurposes(dtps);
				logBookManagerFacade.setDiveChanged(currentDive);
			}
		});

		return divePurposeCb;
	}

	private Component createDiveTypeValuePanel() {
		ValuePanel p = new ValuePanel("dive.type");
		p.setComponent(createDiveTypeComponent());
		return p;
	}

	private JComponent createDiveTypeComponent() {
		List<Object> obj = new ArrayList<Object>();
		DiveType[] values = DiveType.values();
		Arrays.sort(values, new KeyedCatalogComparator());
		for (DiveType t : values) {
			obj.add(t);
		}
		diveTypeCb = new MultiSelectionComboBox();
		diveTypeCb.setVisibleRowCount(4);
		diveTypeCb
				.setFormatter(new KeyedCatalogMultiSelectionComboBoxFormatter());
		diveTypeCb.setData(obj);
		diveTypeCb.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						MultiSelectionComboBox.SELECTION_CHANGED)) {
					adaptDive(evt);
				}

			}

			private void adaptDive(PropertyChangeEvent evt) {
				@SuppressWarnings("unchecked")
				List<Object> selection = (List<Object>) evt.getNewValue();
				List<DiveType> dtps = new ArrayList<DiveType>();
				if (selection != null) {
					for (Object o : selection) {
						dtps.add((DiveType) o);
					}
				}
				currentDive.setDiveType(dtps);
				logBookManagerFacade.setDiveChanged(currentDive);
			}
		});

		return diveTypeCb;
	}

	private Component createDetailPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();

		c.insets.set(0, 0, 0, 2);
		GridBagLayoutManager.addComponent(panel, createNumberValuePanel(), c,
				0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);
		GridBagLayoutManager.addComponent(panel, createDateValuePanel(), c, 1,
				0, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel, createDiveTimeLabel(), c, 0,
				1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);
		GridBagLayoutManager.addComponent(panel, createDepthLabel(), c, 1, 1,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);
		GridBagLayoutManager.addComponent(panel, createSurfaceTimeLabel(), c,
				2, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);

		c.insets.set(0, 0, 0, 0);
		GridBagLayoutManager.addComponent(panel, createDivelocationLabel(), c,
				3, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);
		GridBagLayoutManager.addComponent(panel, createTemperatureLabel(), c,
				3, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(panel, Box.createVerticalGlue(), c,
				0, 2, 1, 1, 0, 1, GridBagConstraints.VERTICAL,
				GridBagConstraints.CENTER);

		return panel;
	}

	private JComponent createBorderPanel(String title) {
		return new I18nTitlePanel(title);
	}

	private Component createPhysiologicalStatusPanel() {
		physioParametersPanel = new PhysiologicalParametersPanel(
				logBookManagerFacade);
		return physioParametersPanel;
	}

	private Component createPalanqueePanel() {
		JPanel panel = new DetailPanel();

		panel.setLayout(new BorderLayout());
		panel.add(createPalanqueeComponent(), BorderLayout.CENTER);
		return panel;
	}

	private Component createPalanqueeComponent() {
		palanqueeTablePanel = new PalanqueeTablePanel(parentWindow,
				logBookManagerFacade, diverManagerFacade);

		palanqueeTablePanel.setMinimumSize(new Dimension(200, 150));
		palanqueeTablePanel.setPreferredSize(DIM_COMMENT);
		return palanqueeTablePanel;
	}

	private JComponent createNumberComponent() {
		SpinnerModel sm = new SpinnerNumberModel(0, 0, 50000, 1);
		numberComponent = new JSpinner(sm);
		numberComponent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					currentDive.setNumber((Integer) numberComponent.getValue());
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		return numberComponent;
	}

	private Component createCommentPanel() {
		DetailPanel p = new DetailPanel();
		p.setLayout(new BorderLayout());
		p.add(createCommentComponent(), BorderLayout.CENTER);
		return p;
	}

	private Component createRatingPanel() {
		starRaterPanel = new StarRater(10);
		starRaterPanel.addStarListener(new StarListener() {

			@Override
			public void handleSelection(int selection) {
				if (selection == 0) {
					currentDive.setRating(null);
				} else {
					if (currentDive.getRating() == null) {
						currentDive.setRating(new Rating());
					}
					currentDive.getRating().setValue((short) selection);
				}
				starRaterPanel.setSelection(0);
				starRaterPanel.setRating(selection);
				logBookManagerFacade.setDiveChanged(currentDive);
			}
		});

		ValuePanel vp = new ValuePanel("score");
		vp.setComponent(starRaterPanel);
		return vp;
	}

	private JComponent createNumberValuePanel() {
		ValuePanel p = new ValuePanel("number", UIAgent.getInstance().getIcon(
				UIAgent.ICON_DIVE_NUMBER_16));
		p.setComponent(createNumberComponent());

		return p;
	}

	private JComponent createDateValuePanel() {
		ValuePanel p = new ValuePanel("date", UIAgent.getInstance().getIcon(
				UIAgent.ICON_DIVE_DATE_16));
		p.setComponent(createDateTimeComponent());

		return p;
	}

	private JComponent createDateTimeComponent() {
		dateTimeComponent = new DateTimePicker();
		dateTimeComponent.setFormats(UIAgent.getInstance()
				.getFormatDateHoursFull().toPattern());
		dateTimeComponent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (enableChangePropagation) {
					currentDive.setDate(dateTimeComponent.getDate());
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});

		return dateTimeComponent;

	}

	private JComponent createMaxDepthComponent() {
		double initial = UnitsAgent.getInstance().convertLengthFromModel(0d);
		double min = UnitsAgent.getInstance().convertLengthFromModel(-1000d);
		double max = UnitsAgent.getInstance().convertLengthFromModel(0d);

		SpinnerModel sm = new SpinnerNumberModel(initial, min, max, 0.5d);
		maxDepthComponent = new JSpinner(sm);
		maxDepthComponent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					performChangeDepth();
				}
			}
		});

		JPanel p = new JPanel(new BorderLayout(2, 0));
		p.setOpaque(false);
		p.add(maxDepthComponent, BorderLayout.CENTER);
		maxDepthUnitLabel = new JLabel();
		p.add(maxDepthUnitLabel, BorderLayout.EAST);

		return p;
	}

	private JComponent createTemperatureLabel() {
		ValuePanel p = new ValuePanel("temperature", UIAgent.getInstance()
				.getIcon(UIAgent.ICON_WATER_TEMPERATURE_16));
		p.setComponent(createWaterTemperatureComponent());
		return p;
	}

	private JComponent createSurfaceTimeLabel() {
		ValuePanel p = new ValuePanel("surface.time", UIAgent.getInstance()
				.getIcon(UIAgent.ICON_DIVE_SURFACE_TIME_16));
		p.setComponent(createSurfaceTimeComponent());
		return p;
	}

	private JComponent createDepthLabel() {
		ValuePanel p = new ValuePanel("depth", UIAgent.getInstance().getIcon(
				UIAgent.ICON_DIVE_DEPTH_16));
		p.setComponent(createMaxDepthComponent());
		return p;
	}

	private JComponent createDiveTimeLabel() {
		ValuePanel p = new ValuePanel("dive.time", UIAgent.getInstance()
				.getIcon(UIAgent.ICON_DIVE_TIME_16));
		p.setComponent(createDiveTimeComponent());
		return p;
	}

	private JComponent createDiveTimeComponent() {
		diveTimeComponent = new DurationComponent();
		diveTimeComponent
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(
								DurationComponent.TIME_PROPERTY)) {
							if (enableChangePropagation) {
								performChangeTime();
							}
						}
					}
				});

		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setOpaque(false);
		p.add(diveTimeComponent, BorderLayout.CENTER);
		return p;
	}

	private void performChangeDepth() {
		double depth = currentDive.getMaxDepth();
		double newDepth = UnitsAgent.getInstance().convertLengthToModel(
				(Double) maxDepthComponent.getValue());
		boolean doRollBack = false;

		if (LogBookUtilities.doesNewDepthInfluenceDiveData(currentDive,
				newDepth)) {
			if (UserPreferences.getInstance()
					.skipDiveDepthModificationInfluenceMessage()) {
				doRollBack = false;
			} else {

				DiveInfluenceDialog dlg = new DiveInfluenceDialog(
						WindowUtils.getParentFrame(this),
						i18n.getString("dive.depth.modification.influence.message"));
				int i = dlg.showDialog(400, 300);
				doRollBack = !(PromptDialog.OPTION_OK == i);
				if (PromptDialog.OPTION_OK == i) {
					UserPreferences.getInstance()
							.setSkipDiveDepthmodificationInfluenceMessage(
									dlg.skipSelected());
					UserPreferences.getInstance().savePreferences(false);
				}
			}
		}

		if (!doRollBack) {
			currentDive.setMaxDepth(newDepth);
			logBookManagerFacade.setDiveChanged(currentDive,
					DiveModification.DIVE_DEPTH);
		} else {
			enableChangePropagation(false);
			maxDepthComponent.setValue(depth);
			enableChangePropagation(true);
		}
	}

	private void performChangeTime() {
		int diveTime = currentDive.getDiveTime();
		int newDiveTime = (int) UnitsUtilities
				.convertMilisecondsToSeconds(diveTimeComponent.getTime());
		boolean doRollBack = false;

		if (LogBookUtilities.doesNewTimeInfluenceDiveData(currentDive,
				newDiveTime)) {
			if (UserPreferences.getInstance().skipDiveTimeInfluenceMessage()) {
				doRollBack = false;
			} else {
				DiveInfluenceDialog dlg = new DiveInfluenceDialog(
						WindowUtils.getParentFrame(this),
						i18n.getString("dive.time.modification.influence.message"));

				int i = dlg.showDialog(400, 300);
				doRollBack = !(PromptDialog.OPTION_OK == i);
				if (PromptDialog.OPTION_OK == i) {
					UserPreferences
							.getInstance()
							.setSkipDiveTimeInfluenceMessage(dlg.skipSelected());
					UserPreferences.getInstance().savePreferences(false);
				}
			}
		}

		if (!doRollBack) {
			currentDive.setDiveTime(newDiveTime);
			logBookManagerFacade.setDiveChanged(currentDive,
					DiveModification.DIVE_TIME);
		} else {
			enableChangePropagation(false);
			diveTimeComponent.setTime((long) UnitsUtilities
					.convertSecondsToMiliseconds(diveTime));
			enableChangePropagation(true);
		}
	}

	private JComponent createSurfaceTimeComponent() {
		surfaceTimeComp = new DurationComponent();
		surfaceTimeComp.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						DurationComponent.TIME_PROPERTY)) {
					if (enableChangePropagation) {
						currentDive.setSurfaceTime((int) UnitsUtilities
								.convertMilisecondsToSeconds(surfaceTimeComp
										.getTime()));
						logBookManagerFacade.setDiveChanged(currentDive);
					}
				}
			}
		});

		JPanel p = new JPanel(new BorderLayout(0, 0));
		p.setOpaque(false);
		p.add(surfaceTimeComp, BorderLayout.CENTER);

		return p;
	}

	private JComponent createWaterTemperatureComponent() {
		double initial = UnitsAgent.getInstance().convertTemperatureFromModel(
				0d);
		double min = UnitsAgent.getInstance().convertTemperatureFromModel(-50d);
		double max = UnitsAgent.getInstance().convertTemperatureFromModel(50d);

		SpinnerModel sm = new SpinnerNumberModel(initial, min, max, 0.5d);
		waterTemperatureComp = new JSpinner(sm);
		waterTemperatureComp.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					double temp = (Double) waterTemperatureComp.getValue();
					currentDive.setWaterTemperature(UnitsAgent.getInstance()
							.convertTemperatureToModel(temp));
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});

		JPanel p = new JPanel(new BorderLayout(2, 0));
		p.setOpaque(false);
		p.add(waterTemperatureComp, BorderLayout.CENTER);
		temperatureUnitLabel = new JLabel();
		p.add(temperatureUnitLabel, BorderLayout.EAST);
		return p;
	}

	private JComponent createCommentComponent() {
		commentTextArea = new JTextArea();
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);

		commentTextArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (enableChangePropagation) {
					currentDive.setComment(commentTextArea.getText());
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		JScrollPane scroll = new JScrollPane(commentTextArea);
		scroll.setPreferredSize(DIM_COMMENT);
		return scroll;
	}

	private JComponent createDivelocationLabel() {
		ValuePanel p = new ValuePanel("divesite", UIAgent.getInstance()
				.getIcon(UIAgent.ICON_DIVE_SITE_16));
		p.setComponent(createDivelocationComponent());
		return p;
	}

	private JComponent createDivelocationComponent() {
		diveSiteChooser = new DiveSiteChooser(diveLocationManagerFacade);
		diveSiteChooser.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						DiveSiteChooser.DIVESITE_SITE_PROPERTY)) {
					if (enableChangePropagation) {
						currentDive.setDiveSite((DiveSite) evt.getNewValue());
						logBookManagerFacade.setDiveChanged(currentDive);
					}
				}
			}
		});
		return diveSiteChooser;
	}

	/**
	 * Sets all the values in the components and display them
	 * 
	 * @param dive
	 */
	public void setDive(Dive dive) {
		this.currentDive = dive;
		if (currentDive != null) {
			enableChangePropagation(false);
			adaptValues();
			enableChangePropagation(true);
		} else {
			clear();
		}
	}

	private void adaptValues() {
		numberComponent.setValue(currentDive.getNumber());
		maxDepthComponent.setValue(UnitsAgent.getInstance()
				.convertLengthFromModel(currentDive.getMaxDepth()));
		diveTimeComponent.setTime((long) UnitsUtilities
				.convertSecondsToMiliseconds(currentDive.getDiveTime()));
		commentTextArea.setText(currentDive.getComment());
		commentTextArea.setCaretPosition(0);
		surfaceTimeComp.setTime((long) UnitsUtilities
				.convertSecondsToMiliseconds(currentDive.getSurfaceTime()));
		if (null != currentDive.getDate()) {
			dateTimeComponent.setDate(currentDive.getDate());
		} else {
			dateTimeComponent.setDate(null);
		}

		waterTemperatureComp
				.setValue(UnitsAgent.getInstance().convertTemperatureFromModel(
						currentDive.getWaterTemperature()));

		Palanquee palanquee = currentDive.getPalanquee();
		if (palanquee != null) {
			palanqueeTablePanel.setPalanquee(currentDive.getPalanquee(),
					currentDive);
		} else {
			currentDive.setPalanquee(new Palanquee());
			palanqueeTablePanel.setPalanquee(currentDive.getPalanquee(),
					currentDive);
		}

		diveSiteChooser.setDiveSite(currentDive.getDiveSite());

		PhysiologicalStatus physiologicalStatus = currentDive
				.getPhysiologicalStatus();
		if (null != physiologicalStatus) {
			physioParametersPanel.setPhysiologicalStatus(physiologicalStatus,
					currentDive);
		} else {
			currentDive.setPhysiologicalStatus(new PhysiologicalStatus());
			physioParametersPanel.setPhysiologicalStatus(
					currentDive.getPhysiologicalStatus(), currentDive);
		}

		Rating rating = currentDive.getRating();
		if (rating != null) {
			starRaterPanel.setRating(rating.getValue());
		} else {
			starRaterPanel.setRating(0f);
		}

		List<DivePurpose> pps = currentDive.getDivePurposes();
		divePurposeCb.setSelectedItems(pps);

		List<DiveType> types = currentDive.getDiveTypes();
		diveTypeCb.setSelectedItems(types);

		divePlaformCb.setSelectedItem(currentDive.getDivePlatform());
	}

	public void clear() {
		// block propagation changes
		enableChangePropagation(false);
		currentDive = null;
		numberComponent.setValue(((SpinnerNumberModel) numberComponent
				.getModel()).getMinimum());
		maxDepthComponent.setValue(((SpinnerNumberModel) maxDepthComponent
				.getModel()).getMinimum());
		diveTimeComponent.setTime(diveTimeComponent.getMinimumValue());
		commentTextArea.setText(null);
		surfaceTimeComp.setTime(surfaceTimeComp.getMinimumValue());
		dateTimeComponent.setDate(null);
		waterTemperatureComp.setValue(0d);
		palanqueeTablePanel.setPalanquee(null, null);
		diveSiteChooser.setDiveSite(null);
		physioParametersPanel.setPhysiologicalStatus(null, null);
		starRaterPanel.setRating(0);
		diveTypeCb.setSelectedItems(null);
		divePurposeCb.setSelectedItems(null);
		divePlaformCb.setSelectedIndex(-1);
		// enable propagation changes
		enableChangePropagation(true);
	}

	private void enableChangePropagation(boolean b) {
		this.enableChangePropagation = b;
	}

	public void updateUnits() {
		setLengthUnitLabel();
		setTemperatureUnitLabel();

		if (null != currentDive) {
			enableChangePropagation = false;
			maxDepthComponent.setValue(UnitsAgent.getInstance()
					.convertLengthFromModel(currentDive.getMaxDepth()));
			waterTemperatureComp.setValue(UnitsAgent.getInstance()
					.convertTemperatureFromModel(
							currentDive.getWaterTemperature()));
			enableChangePropagation = true;
		}
	}

	private void updateKeyedCatalogs() {
		enableChangePropagation(false);

		// DIVE PLATFORM
		Object platformSel = divePlaformCb.getSelectedItem();
		KeyedCatalog[] platformVal = DivePlatform.values();
		Arrays.sort(platformVal, new KeyedCatalogComparator());
		divePlaformCb.removeAllItems();
		for (KeyedCatalog keyedCatalog : platformVal) {
			divePlaformCb.addItem(keyedCatalog);
		}
		if (null != platformSel) {
			divePlaformCb.setSelectedItem(platformSel);
		}

		// PURPOSES
		List<Object> purposesSel = divePurposeCb.getSelectedItems();
		List<Object> data = new ArrayList<Object>();
		DivePurpose[] purposesVal = DivePurpose.values();
		Arrays.sort(purposesVal, new KeyedCatalogComparator());
		for (DivePurpose t : purposesVal) {
			data.add(t);
		}
		divePurposeCb.setData(data);
		if (null != purposesSel) {
			divePurposeCb.setSelectedItems(purposesSel);
		}

		// PURPOSES
		List<Object> typeSel = diveTypeCb.getSelectedItems();
		List<Object> dataType = new ArrayList<Object>();
		DiveType[] typeVal = DiveType.values();
		Arrays.sort(typeVal, new KeyedCatalogComparator());
		for (DiveType t : typeVal) {
			dataType.add(t);
		}
		diveTypeCb.setData(dataType);
		if (null != typeSel) {
			diveTypeCb.setSelectedItems(typeSel);
		}

		enableChangePropagation(true);
	}

	private void setTemperatureUnitLabel() {
		temperatureUnitLabel.setText("("
				+ UnitsAgent.getInstance().getTemperatureUnit().getSymbol()
				+ ")");
	}

	private void setLengthUnitLabel() {
		maxDepthUnitLabel.setText("("
				+ UnitsAgent.getInstance().getLengthUnit().getSymbol() + ")");
	}

	@Override
	public void updateUI() {
		super.updateUI();
		if (numberComponent != null) {
			updateKeyedCatalogs();
		}
	}

	public void updateUIObjects() {
		dateTimeComponent.setFormats(UIAgent.getInstance()
				.getFormatDateHoursFull().toPattern());
	}
}
