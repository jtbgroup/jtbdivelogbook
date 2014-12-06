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
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.material.DiveComputerEquipment;

public class DiveComputerEquipmentPanel extends EquipmentPanel {

	public DiveComputerEquipmentPanel(LogBookManagerFacade logBookManagerFacade, DiveEquipment diveEquipment) {
		super(logBookManagerFacade, diveEquipment);
	}

	private static final long serialVersionUID = 504236853311708827L;
	private JLabel batteryLabel;
	private JSpinner batteryComponent;

	@Override
	protected Component createCentralPanel() {
		batteryLabel = new I18nLabel("battery");

		SpinnerModel sm = new SpinnerNumberModel(0, 0, 100, 1);
		batteryComponent = new JSpinner(sm);

		batteryComponent.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				((DiveComputerEquipment) getEquipment())
						.setRemainingBattery((Integer) batteryComponent
								.getValue());
				notifyModificationListeners(true);
			}
		});
		

		JLabel unitLabel = new JLabel("%");
		

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		p.add(batteryLabel);
		p.add(batteryComponent);
		p.add(unitLabel);
		return p;
	}

	protected void setEquipmentComponents() {
		batteryComponent.setValue(((DiveComputerEquipment) equipment)
				.getRemainingBattery());
	}


}
