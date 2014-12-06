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
package be.vds.jtbdive.client.view.core.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UserPreferences;

public class FormattingPreferrencePanel extends AbstractPreferrencePanel {

	private static final long serialVersionUID = -3689046291861107714L;
	private JComboBox dateHourFormattingCb;
	private DefaultComboBoxModel dateHourFormattingModel;
	private JLabel dateHourFormatLbl;
	
	private JLabel dateFormatLbl;
	private DefaultComboBoxModel dateFormattingModel;
	private JComboBox dateFormattingCb;

	public FormattingPreferrencePanel() {
		super();
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);

		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 3, 5, 3);
		int y = 0;

		GridBagLayoutManager.addComponent(p, createDateHourLabel(), c, 0, y, 1,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST);
		GridBagLayoutManager.addComponent(p,
				createDateHourFormattingComponent(), c, 1, y++, 1, 1, 1, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createDateLabel(), c, 0, y, 1, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST);
		GridBagLayoutManager.addComponent(p, createDateFormattingComponent(),
				c, 1, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), c, 0, y,
				1, 1, 0, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return p;
	}

	private Component createDateLabel() {
		return new I18nLabel("date");
	}

	private Component createDateHourLabel() {
		return new I18nLabel("date.hours");
	}

	private Component createDateHourFormattingComponent() {
		String[] formats = { "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss",
				"MM-dd-yyyy HH:mm:ss" };

		dateHourFormattingModel = new DefaultComboBoxModel(formats);
		dateHourFormattingCb = new JComboBox(dateHourFormattingModel);
		dateHourFormattingCb.setEditable(true);
		dateHourFormattingCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dateHourFormatLbl.setText(new SimpleDateFormat(
						(String) dateHourFormattingCb.getSelectedItem())
						.format(new Date()));
			}
		});

		dateHourFormatLbl = new JLabel();

		JPanel p = new JPanel(new BorderLayout());
		p.add(dateHourFormattingCb, BorderLayout.CENTER);
		p.add(dateHourFormatLbl, BorderLayout.SOUTH);
		return p;

	}

	private Component createDateFormattingComponent() {
		String[] formats = { "yyyy-MM-dd", "dd-MM-yyyy", "MM-dd-yyyy" };

		dateFormattingModel = new DefaultComboBoxModel(formats);
		dateFormattingCb = new JComboBox(dateFormattingModel);
		dateFormattingCb.setEditable(true);
		dateFormattingCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dateFormatLbl.setText(new SimpleDateFormat(
						(String) dateFormattingCb.getSelectedItem())
						.format(new Date()));
			}
		});

		dateFormatLbl = new JLabel();

		JPanel p = new JPanel(new BorderLayout());
		p.add(dateFormattingCb, BorderLayout.CENTER);
		p.add(dateFormatLbl, BorderLayout.SOUTH);
		return p;

	}

	@Override
	public void adaptUserPreferences() {
		UserPreferences up = UserPreferences.getInstance();
		up.setDateHoursFormat(new SimpleDateFormat(
				(String) dateHourFormattingCb.getSelectedItem()));
		up.setDateFormat(new SimpleDateFormat((String) dateFormattingCb
				.getSelectedItem()));
	}

	@Override
	public void setUserPreferences() {
		UserPreferences up = UserPreferences.getInstance();

		setDateHourFormat(up);
		setDateFormat(up);
	}

	private void setDateFormat(UserPreferences up) {
		SimpleDateFormat sdf = up.getPreferredDateFormat();
		String pattern = sdf.toPattern();
		int index = dateFormattingModel.getIndexOf(sdf.toPattern());

		if (index > -1) {
			dateFormattingCb.setSelectedIndex(index);
		} else {
			dateFormattingModel.addElement(pattern);
		}
	}

	private void setDateHourFormat(UserPreferences up) {
		SimpleDateFormat sdf = up.getPreferredDateHoursFormat();
		String pattern = sdf.toPattern();
		int index = dateHourFormattingModel.getIndexOf(sdf.toPattern());

		if (index > -1) {
			dateHourFormattingCb.setSelectedIndex(index);
		} else {
			dateHourFormattingModel.addElement(pattern);
		}
	}
}
