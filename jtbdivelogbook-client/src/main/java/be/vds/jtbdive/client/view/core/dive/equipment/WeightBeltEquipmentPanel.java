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
package be.vds.jtbdive.client.view.core.dive.equipment;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.material.WeightBeltEquipment;

public class WeightBeltEquipmentPanel extends EquipmentPanel {

	public WeightBeltEquipmentPanel(LogBookManagerFacade logBookManagerFacade, DiveEquipment diveEquipment) {
		super(logBookManagerFacade, diveEquipment);
	}

	private static final long serialVersionUID = -5018350688536684694L;
	private JLabel weightLabel;
	private JSpinner weightComponent;

	@Override
	protected Component createCentralPanel() {
		weightLabel = new I18nLabel("weight");

		SpinnerModel sm = new SpinnerNumberModel(0d, 0d, 100d, 0.5d);
		weightComponent = new JSpinner(sm);

		weightComponent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				((WeightBeltEquipment) getEquipment()).setWeight((Double) weightComponent
						.getValue());
				notifyModificationListeners(true);
			}
		});

		JLabel weightUnitLabel = new JLabel(UnitsAgent.getInstance()
				.getWeightUnit().getSymbol());

		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		p.setOpaque(false);
		p.add(weightLabel);
		p.add(weightComponent);
		p.add(weightUnitLabel);
		return p;
	}

	protected void setEquipmentComponents() {
		weightComponent.setValue(((WeightBeltEquipment) equipment).getWeight());
	}
}
