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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.ValuePanel;
import be.vds.jtbdive.client.swing.text.RegexPlainDocument;
import be.vds.jtbdive.client.view.events.EmptyComboBoxKeyListener;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.PhysiologicalStatus;

public class PhysiologicalParametersPanel extends JPanel {

	private static final long serialVersionUID = 6770441471886649402L;
	private JSpinner arterialMBSpinner;
	private JSpinner skinCoolComponent;
	private JSpinner maxPPO2Spinner;
	private JComboBox saturationIndexAfterDiveCb;
	private JTextField cnsBeforeDiveTf;
	private JTextField interPulmonaryTf;
	private PhysiologicalStatus currentPhysiologicalStatus;
	private boolean enableChangePropagation;
	private Dive currentDive;
	private LogBookManagerFacade logBookManagerFacade;

	public PhysiologicalParametersPanel(
			LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		
		c.insets.set(0, 0, 0, 2);
		GridBagLayoutManager.addComponent(this, createSaturationIndexLabel(),
				c, 0, 0, 1, 1, 0, 0, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, createMaxPPO2Label(), c, 1, 0,
				1, 1, 1, 0, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this,
				createSkinCoolTemperaturePanel(), c, 0, 1, 1, 1, 1, 0,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this,
				createArterialMicroBubblePanel(), c, 1, 1, 1, 1, 1, 0,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		
		c.insets.set(0, 0, 0, 0);
		GridBagLayoutManager.addComponent(this, createCnsBeforeDiveLabel(), c,
				2, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		
		GridBagLayoutManager.addComponent(this,
				createInterPulmonaryShuntLabel(), c, 2, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
	}

	private JComponent createInterPulmonaryShuntComponent() {
		interPulmonaryTf = new JTextField(5);
		interPulmonaryTf.setDocument(new RegexPlainDocument(
				new String[] { RegexPlainDocument.DOUBLE }));
		interPulmonaryTf.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (enableChangePropagation) {
					double i = 0;
					String s = interPulmonaryTf.getText().trim();
					if (s != null && s.length() > 0) {
						i = Double.valueOf(s);
					}

					currentPhysiologicalStatus.setInterPulmonaryShunt(i);
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		return interPulmonaryTf;
	}

	private JComponent createCnsBeforeDiveComponent() {
		cnsBeforeDiveTf = new JTextField(5);
		cnsBeforeDiveTf.setDocument(new RegexPlainDocument(
				new String[] { RegexPlainDocument.INTEGER }));
		cnsBeforeDiveTf.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (enableChangePropagation) {
					int i = 0;
					String s = cnsBeforeDiveTf.getText().trim();
					if (s != null && s.length() > 0) {
						i = Integer.valueOf(s);
					}

					currentPhysiologicalStatus.setCnsBeforeDive(i);
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		return cnsBeforeDiveTf;
	}

	private JComponent createInterPulmonaryShuntLabel() {
		ValuePanel v = new ValuePanel("interpulmonary.shunt", UIAgent.getInstance().getIcon(UIAgent.ICON_INTERPULMONARY_SHUNT_16));
		v.setComponent(createInterPulmonaryShuntComponent());
		return v;
	}

	private JComponent createCnsBeforeDiveLabel() {
		ValuePanel v = new ValuePanel("cns.before.dive");
		v.setComponent(createCnsBeforeDiveComponent());
		return v;
	}

	private JComponent createSaturationIndexAfterDiveComponent() {
		Character[] o = new Character[26];
		for (char i = 'A'; i <= 'Z'; i++) {
			o[i - 'A'] = i;
		}
		saturationIndexAfterDiveCb = new JComboBox(o);
		saturationIndexAfterDiveCb.setMinimumSize(new Dimension(30, 20));
		saturationIndexAfterDiveCb.setSelectedIndex(-1);
		saturationIndexAfterDiveCb.addKeyListener(new EmptyComboBoxKeyListener());
		saturationIndexAfterDiveCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPhysiologicalStatus != null) {

					if (enableChangePropagation) {
						Character c = (Character) saturationIndexAfterDiveCb
								.getSelectedItem();
						if (null == c) {
							currentPhysiologicalStatus
									.setSaturationIndexAfterDive((char) 0);
						} else {
							currentPhysiologicalStatus
									.setSaturationIndexAfterDive(c);
						}
						logBookManagerFacade.setDiveChanged(currentDive);
					}
				}
			}
		});
		return saturationIndexAfterDiveCb;
	}

	private JComponent createMaxPPO2Component() {
		SpinnerNumberModel snm = new SpinnerNumberModel(0d, 0d, 2.5d, 0.1d);
		maxPPO2Spinner = new JSpinner(snm);
		maxPPO2Spinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					currentPhysiologicalStatus
							.setMaxPPO2((Double) maxPPO2Spinner.getValue());
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		return maxPPO2Spinner;
	}

	private JComponent createSkinCoolTemperatureComponent() {
		SpinnerNumberModel snm = new SpinnerNumberModel(0d, 0d, 45d, 0.5d);
		skinCoolComponent = new JSpinner(snm);
		skinCoolComponent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					currentPhysiologicalStatus
							.setSkinCoolTemperature((Double) skinCoolComponent
									.getValue());
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		return skinCoolComponent;
	}

	private JComponent createArterialMicroBubbleComponent() {
		SpinnerNumberModel snm = new SpinnerNumberModel(0, 0, 7, 1);
		arterialMBSpinner = new JSpinner(snm);
		arterialMBSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (enableChangePropagation) {
					currentPhysiologicalStatus
							.setArterialMicrobubbleLevel((Integer) arterialMBSpinner
									.getValue());
					logBookManagerFacade.setDiveChanged(currentDive);
				}
			}
		});
		return arterialMBSpinner;
	}

	private JComponent createSaturationIndexLabel() {
		ValuePanel v = new ValuePanel("saturation.index");
		v.setComponent(createSaturationIndexAfterDiveComponent());
		return v;
	}

	private JComponent createMaxPPO2Label() {
		ValuePanel v = new ValuePanel("max.ppo2");
		v.setComponent(createMaxPPO2Component());
		return v;
	}

	private JComponent createSkinCoolTemperaturePanel() {
		ValuePanel v = new ValuePanel("temperature.skincool");
		v.setComponent(createSkinCoolTemperatureComponent());
		return v;
	}

	private JComponent createArterialMicroBubblePanel() {
		ValuePanel v = new ValuePanel("arterialmicrobubble" , UIAgent.getInstance().getIcon(UIAgent.ICON_ARTERIAL_MICROBUBBLES_16));
		v.setComponent(createArterialMicroBubbleComponent());
		return v;
	}

	public void setPhysiologicalStatus(PhysiologicalStatus physiologicalStatus,
			Dive currentDive) {
		enableChangePropagation(false);
		this.currentDive = currentDive;
		this.currentPhysiologicalStatus = physiologicalStatus;

		if (null != currentPhysiologicalStatus) {
			adaptValues();
		} else {
			clear();
		}
		enableChangePropagation(true);
	}

	private void adaptValues() {
		if (currentPhysiologicalStatus.getSaturationIndexAfterDive() == (char) 0) {
			saturationIndexAfterDiveCb.setSelectedIndex(-1);
		} else {
			saturationIndexAfterDiveCb.setSelectedItem(currentPhysiologicalStatus
					.getSaturationIndexAfterDive());
		}

		maxPPO2Spinner.setValue(currentPhysiologicalStatus.getMaxPPO2());
		skinCoolComponent.setValue(currentPhysiologicalStatus
				.getSkinCoolTemperature());
		arterialMBSpinner.setValue(currentPhysiologicalStatus
				.getArterialMicrobubbleLevel());
		interPulmonaryTf.setText(String.valueOf(currentPhysiologicalStatus
				.getInterPulmonaryShunt()));
		cnsBeforeDiveTf.setText(String.valueOf(currentPhysiologicalStatus
				.getCnsBeforeDive()));
	}

	private void clear() {
		saturationIndexAfterDiveCb.setSelectedIndex(-1);
		maxPPO2Spinner
				.setValue(((SpinnerNumberModel) maxPPO2Spinner.getModel())
						.getMinimum());
		skinCoolComponent.setValue(((SpinnerNumberModel) skinCoolComponent
				.getModel()).getMinimum());
		arterialMBSpinner.setValue(((SpinnerNumberModel) arterialMBSpinner
				.getModel()).getMinimum());
		interPulmonaryTf.setText(null);
		cnsBeforeDiveTf.setText(null);
	}

	private void enableChangePropagation(boolean b) {
		this.enableChangePropagation = b;
	}
}
