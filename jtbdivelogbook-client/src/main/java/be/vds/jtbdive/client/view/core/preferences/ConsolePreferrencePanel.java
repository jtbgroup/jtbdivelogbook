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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import be.smd.i18n.swing.I18nCheckBox;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.core.logging.Syslog;

public class ConsolePreferrencePanel extends AbstractPreferrencePanel {

	private static final long serialVersionUID = -3689046291861107714L;
	private JComboBox loggingLevelCb;
	private JSpinner bufferSizeSpinner;
	private I18nCheckBox sortCheckBox;

	public ConsolePreferrencePanel() {
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

		GridBagLayoutManager.addComponent(p, createLoggerLevelLabel(), c, 0, y,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createLoggerLevelComponent(), c,
				1, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createBufferSizeLabel(), c, 0, y,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createBufferSizeComponent(), c, 1,
				y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		//Sorting
		GridBagLayoutManager.addComponent(p, createSortComponent(), c, 0, y++,
				2, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), c, 0, y,
				1, 1, 0, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return p;
	}

	private Component createBufferSizeLabel() {
		I18nLabel label = new I18nLabel("buffer.size");
		SwingComponentHelper.addI18nToolTip(label, "tooltip.buffer.size");
		return label;
	}

	private Component createBufferSizeComponent() {
		bufferSizeSpinner = new JSpinner(new SpinnerNumberModel(100, 10, 1000,
				5));
		return bufferSizeSpinner;
	}

	private Component createSortComponent() {
		sortCheckBox = new I18nCheckBox("add.on.top");
		SwingComponentHelper.addI18nToolTip(sortCheckBox, "tooltip.add.on.top");
		return sortCheckBox;
	}

	private Component createLoggerLevelLabel() {
		I18nLabel label = new I18nLabel("logging.level");
		SwingComponentHelper.addI18nToolTip(label, "tooltip.logging.level");
		return 	label;
	}

	private Component createLoggerLevelComponent() {
		loggingLevelCb = new JComboBox(Syslog.LEVELS);
		loggingLevelCb.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -2458894027557435437L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if (value != null) {
					Integer i = (Integer) value;
					if (i == Syslog.INT_DEBUG) {
						setText("DEBUG");
					} else if (i == Syslog.INT_INFO) {
						setText("INFO");
					} else if (i == Syslog.INT_WARN) {
						setText("WARN");
					} else if (i == Syslog.INT_ERROR) {
						setText("ERROR");
					} else if (i == Syslog.INT_FATAL) {
						setText("FATAL");
					}
				}
				return this;
			}
		});
		return loggingLevelCb;
	}

	@Override
	public void adaptUserPreferences() {
		UserPreferences up = UserPreferences.getInstance();
		up.setDefaultLoggingLevel((Integer) loggingLevelCb.getSelectedItem());
		up.setLoggingBufferSize((Integer) bufferSizeSpinner.getValue());
		up.setLogOnTop(sortCheckBox.isSelected());
	}

	@Override
	public void setUserPreferences() {
		UserPreferences up = UserPreferences.getInstance();

		if (up.getDefaultLoggingLevel() == null) {
			loggingLevelCb.setSelectedItem(Syslog.getLevelInt());
		} else {
			loggingLevelCb.setSelectedItem(up.getDefaultLoggingLevel());
		}

		bufferSizeSpinner.setValue(up.getLoggingBufferSize());
		sortCheckBox.setSelected(up.getLogOnTop());

	}
}
