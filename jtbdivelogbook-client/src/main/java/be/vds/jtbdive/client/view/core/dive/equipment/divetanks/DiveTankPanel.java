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
package be.vds.jtbdive.client.view.core.dive.equipment.divetanks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.component.DurationComponent;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.GazMixBar;
import be.vds.jtbdive.client.view.core.dive.equipment.EquipmentPanel;
import be.vds.jtbdive.client.view.events.ModificationListener;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.utils.UnitsUtilities;
import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.PointerType;
import eu.hansolo.steelseries.tools.Section;

public class DiveTankPanel extends EquipmentPanel implements
		ModificationListener {

	private static final long serialVersionUID = 604007873896002013L;
	private static final int PRESSURE_BAR_MAX = 400;
	private static final int PRESSURE_BAR_MIN = 0;
	private static final int PRESSURE_BAR_TRESHOLD_1 = 50;
	private static final int PRESSURE_BAR_TRESHOLD_2 = 100;
	private static final int MANO_WIDTH = 150;
	private static final Dimension DIMENSION_MANO = new Dimension(MANO_WIDTH,
			MANO_WIDTH);
	private JSpinner startSpinner;
	private JSpinner stopSpinner;
	private GazMixBar gazMixBar;
	private SpinnerNumberModel stopPressureSpinnerModel;
	private SpinnerNumberModel startPressureSpinnerModel;
	private JLabel beginPressionUnitLabel;
	private JLabel endPressionUnitLabel;
	private boolean enableChangePropagation = true;
	private DurationComponent switchTimeComponent;
	private Dive dive;

	private Radial airGauge;

	public DiveTankPanel(LogBookManagerFacade logBookManagerFacade,
			DiveEquipment diveEquipment, Dive dive) {
		super(logBookManagerFacade, diveEquipment);
		this.dive = dive;
		initParams();
	}

	@Override
	protected Component createCentralPanel() {

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		GridBagLayoutManager.addComponent(p, createPressureDataComponents(), c,
				0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHWEST);

		GridBagLayoutManager.addComponent(p, Box.createHorizontalGlue(), c, 1,
				0, 1, 1, 0.5, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHEAST);

		GridBagLayoutManager.addComponent(p, createStartGauge(), c, 2, 0, 1, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createHorizontalGlue(), c, 3,
				0, 1, 1, 0.5, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHEAST);

		GridBagLayoutManager.addComponent(p, createGazMixPanel(), c, 4, 0, 1,
				1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTHEAST);

		updatePressureComponents();

		return p;
	}

	private Component createEndComponent() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(createEndPressureSpinner(), BorderLayout.CENTER);
		p.add(createEndPressureUnitLabel(), BorderLayout.EAST);
		return p;
	}

	private Component createBeginComponent() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(createStartPressureSpinner(), BorderLayout.CENTER);
		p.add(createStartPressureUnitLabel(), BorderLayout.EAST);
		return p;
	}

	private Component createPressureDataComponents() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(2, 2, 2, 2);
		// begin pressure
		GridBagLayoutManager
				.addComponent(p, createBeginPressureLabel(), gc, 0, 0, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(p, createBeginComponent(), gc, 1, 0,
				1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		// end pressure
		GridBagLayoutManager
				.addComponent(p, createEndPressureLabel(), gc, 0, 1, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager
				.addComponent(p, createEndComponent(), gc, 1, 1, 1, 1, 0, 0,
						GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		// switch time
		GridBagLayoutManager.addComponent(p, createSwitchTimeLabel(), gc, 0, 2,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(p, createSwitchTimeComponent(), gc,
				1, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		return p;
	}

	private JComponent createStartGauge() {
		airGauge = new Radial();
		airGauge.setGaugeType(GaugeType.TYPE3);
		airGauge.setPointerType(PointerType.TYPE8);
		airGauge.setFrameDesign(FrameDesign.GLOSSY_METAL);
		airGauge.setBackgroundColor(BackgroundColor.CARBON);

		airGauge.setLcdVisible(true);
		airGauge.setLedVisible(false);
		airGauge.setSectionsVisible(true);
		airGauge.setThresholdVisible(true);
		airGauge.setTitle("");

		airGauge.setPreferredSize(DIMENSION_MANO);
		airGauge.setMaximumSize(DIMENSION_MANO);
		return airGauge;
	}

	private Component createGazMixPanel() {
		JPanel panel = new DetailPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int y = 0;
		GridBagLayoutManager.addComponent(panel, createGazMixLabel(), c, 0, y,
				1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTHEAST);
		GridBagLayoutManager.addComponent(panel, createGazMixComponent(), c, 0,
				++y, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.NORTHEAST);

		panel.setMinimumSize(new Dimension(150, 100));

		return panel;
	}

	private JComponent createGazMixComponent() {
		gazMixBar = new GazMixBar();
		gazMixBar.addModificationListener(new ModificationListener() {

			@Override
			public void isModified(JComponent component, boolean isModified) {
				((DiveTankEquipment) equipment).setGazMix(gazMixBar.getGazMix());
				notifyModificationListeners(isModified);
			}
		});
		return gazMixBar;
	}

	private Component createSwitchTimeLabel() {
		return new I18nLabel("gaz.mix.switch.time");
	}

	private JComponent createBeginPressureLabel() {
		return new I18nLabel("pressure.begin");
	}

	private JComponent createEndPressureLabel() {
		return new I18nLabel("pressure.end");
	}

	private JComponent createGazMixLabel() {
		I18nLabel label = new I18nLabel("gaz");
		return label;
	}

	private JComponent createSwitchTimeComponent() {

		switchTimeComponent = new DurationComponent();
		switchTimeComponent
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(
								DurationComponent.TIME_PROPERTY)) {
							if (enableChangePropagation) {
								long time = switchTimeComponent.getTime();
								((DiveTankEquipment) equipment).setSwitchTime(UnitsUtilities
										.convertMilisecondsToSeconds(time));
								notifyModificationListeners(true);

								adaptSwitchTimeMargins();
							}
						}
					}
				});
		return switchTimeComponent;
	}

	private void adaptSwitchTimeMargins() {
		enableChangePropagation = false;
		double max = dive.getDiveTime();
		switchTimeComponent.setMaximumValue((long) UnitsUtilities
				.convertSecondsToMiliseconds(max));
		enableChangePropagation = true;
	}

	private JComponent createStartPressureSpinner() {
		double initial = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MIN);
		double min = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MIN);
		double max = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MAX);

		startSpinner = new JSpinner();
		startPressureSpinnerModel = new SpinnerNumberModel(initial, min, max, 1);
		startSpinner.setModel(startPressureSpinnerModel);
		startSpinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					double a = (Double) startSpinner.getValue();
					a = UnitsAgent.getInstance().convertPressureToModel(a);
					((DiveTankEquipment) equipment).setBeginPressure(a);
					airGauge.setThreshold(a);
					notifyModificationListeners(true);

					double b = (Double) stopSpinner.getValue();
					if (a < b) {
						stopSpinner.setValue(a);
					}
				}
			}
		});
		return startSpinner;
	}

	private JComponent createEndPressureSpinner() {
		double initial = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MIN);
		double min = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MIN);
		double max = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MAX);

		stopSpinner = new JSpinner();
		stopPressureSpinnerModel = new SpinnerNumberModel(initial, min, max, 1);
		stopSpinner.setModel(stopPressureSpinnerModel);
		stopSpinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					double a = (Double) stopSpinner.getValue();
					a = UnitsAgent.getInstance().convertPressureToModel(a);

					((DiveTankEquipment) equipment).setEndPressure(a);
					airGauge.setValue(a);
					notifyModificationListeners(true);

					double b = (Double) startSpinner.getValue();
					if (a > b) {
						startSpinner.setValue(a);
					}
				}
			}
		});
		return stopSpinner;
	}

	protected void setEquipmentComponents() {
		if (equipment != null) {
			DiveTankEquipment diveTank = (DiveTankEquipment) equipment;

			startSpinner.setValue(UnitsAgent.getInstance()
					.convertPressureFromModel(diveTank.getBeginPressure()));
			stopSpinner.setValue(UnitsAgent.getInstance()
					.convertPressureFromModel(diveTank.getEndPressure()));

			gazMixBar.setGazMix(diveTank.getGazMix());
			switchTimeComponent.setTime((long) UnitsUtilities
					.convertSecondsToMiliseconds(diveTank.getSwitchTime()));
		} else {
			startSpinner.setValue(0);
			stopSpinner.setValue(0);
			gazMixBar.setGazMix(null);
			switchTimeComponent.setTime(0);
			airGauge.setValue(0);
		}
	}

	@Override
	public void isModified(JComponent component, boolean isModified) {
		notifyModificationListeners(isModified);
	}

	private Component createStartPressureUnitLabel() {
		beginPressionUnitLabel = new JLabel("("
				+ UnitsAgent.getInstance().getPressureUnit().getSymbol() + ")");
		return beginPressionUnitLabel;
	}

	private Component createEndPressureUnitLabel() {
		endPressionUnitLabel = new JLabel("("
				+ UnitsAgent.getInstance().getPressureUnit().getSymbol() + ")");
		return endPressionUnitLabel;
	}

	private void setEndPressureUnitLabel() {
		endPressionUnitLabel.setText("("
				+ UnitsAgent.getInstance().getPressureUnit().getSymbol() + ")");
	}

	private void setBeginPressureUnitLabel() {
		beginPressionUnitLabel.setText("("
				+ UnitsAgent.getInstance().getPressureUnit().getSymbol() + ")");
	}

	public void updateUnitLabels() {
		setEndPressureUnitLabel();
		setBeginPressureUnitLabel();
	}

	@Override
	public void updateUnits() {
		updateUnitLabels();

		enableChangePropagation = false;
		DiveTankEquipment diveTank = (DiveTankEquipment) equipment;
		double min = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MIN);
		double max = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MAX);

		startPressureSpinnerModel.setMinimum(min);
		startPressureSpinnerModel.setMaximum(max);
		startSpinner.setValue(UnitsAgent.getInstance()
				.convertPressureFromModel(diveTank.getBeginPressure()));

		stopPressureSpinnerModel.setMinimum(min);
		stopPressureSpinnerModel.setMaximum(max);
		stopSpinner.setValue(UnitsAgent.getInstance().convertPressureFromModel(
				diveTank.getEndPressure()));

		updatePressureComponents();

		enableChangePropagation = true;

		invalidate();
		repaint();
		validate();

	}

	private void updatePressureComponents() {
		double min = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MIN);
		double max = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_MAX);
		double threshold = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_TRESHOLD_1);
		double threshold2 = UnitsAgent.getInstance().convertPressureFromModel(
				PRESSURE_BAR_TRESHOLD_2);

		airGauge.setMinValue(min);
		airGauge.setMaxValue(max);
		airGauge.setUnitString(UnitsAgent.getInstance().getPressureUnit()
				.getSymbol());

		Section s = new Section();
		s.setStart(min);
		s.setStop(threshold);
		s.setColor(Color.RED);

		Section s2 = new Section();
		s2.setStart(threshold);
		s2.setStop(threshold2);
		s2.setColor(Color.YELLOW);

		Section[] sections = new Section[] { s, s2 };
		airGauge.setSections(sections);
	}

	private void initParams() {
		adaptSwitchTimeMargins();
	}

	@Override
	public void refreshForNewDiveTime() {
		adaptSwitchTimeMargins();
	}

}
