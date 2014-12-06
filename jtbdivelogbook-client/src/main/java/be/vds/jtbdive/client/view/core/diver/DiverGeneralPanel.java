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
package be.vds.jtbdive.client.view.core.diver;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Diver;

public class DiverGeneralPanel extends JPanel {

	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JXDatePicker birthdatePicker;

	
	public DiverGeneralPanel() {
		init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		this.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		int y = 0;
		GridBagLayoutManager
				.addComponent(this, createFirstNameLabel(), c, 0, y, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(this, createFirstNameTextField(), c,
				1, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, createLastNameLabel(), c, 0, y,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(this, createLastNameTextField(), c,
				1, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, new I18nLabel("birthdate"), c,
				0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(this, createBirthDateComponent(), c,
				1, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(this, Box.createVerticalGlue(), c, 0,
				y, 1, 1, 0, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

	}

	private Component createBirthDateComponent() {
		birthdatePicker = new JXDatePicker();
		birthdatePicker.setFormats(UIAgent.getInstance().getFormatDateShort()
				.toPattern());
		return birthdatePicker;
	}

	private Component createFirstNameLabel() {
		JLabel nameLabel = new I18nLabel("firstname");
		return nameLabel;
	}

	private Component createFirstNameTextField() {
		firstNameTextField = new JTextField(20);
		return firstNameTextField;
	}

	private Component createLastNameLabel() {
		JLabel depthLabel = new I18nLabel("lastname");
		return depthLabel;
	}

	private Component createLastNameTextField() {
		lastNameTextField = new JTextField();
		return lastNameTextField;
	}

	public void reset() {
		firstNameTextField.setText(null);
		lastNameTextField.setText(null);
		birthdatePicker.setDate(null);
	}

	public void fillDiverWithValues(Diver diver) {
		diver.setFirstName(firstNameTextField.getText().trim());
		diver.setLastName(lastNameTextField.getText().trim());
		diver.setBirthDate(birthdatePicker.getDate());		
	}

	public void setEditable(boolean b) {
		firstNameTextField.setEditable(b);
		lastNameTextField.setEditable(b);
		birthdatePicker.setEditable(b);		
	}

	public void setDiver(Diver diver) {
		firstNameTextField.setText(diver.getFirstName());
		lastNameTextField.setText(diver.getLastName());
		birthdatePicker.setDate(diver.getBirthDate());		
	}
}
