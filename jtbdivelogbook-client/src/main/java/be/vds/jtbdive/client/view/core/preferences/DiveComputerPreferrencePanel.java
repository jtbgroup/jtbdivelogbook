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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.view.components.DataCommComponent;
import be.vds.jtbdive.client.view.events.EmptyComboBoxKeyListener;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.catalogs.DiveComputerType;

public class DiveComputerPreferrencePanel extends AbstractPreferrencePanel {

	private static final long serialVersionUID = -4753316582141576156L;
	private DataCommComponent dataCommComponent;
	private JComboBox computerTypeCb;
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();

	public DiveComputerPreferrencePanel() {
		super(false);
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		int y = 0;

		GridBagLayoutManager.addComponent(p, createDiveComputerTypeLabel(),
				c, 0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createDiveComputerComponent(),
				c, 1, y++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, createInterfaceTypeLabel(), c,
				0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTHWEST);
		GridBagLayoutManager.addComponent(p, createInterfaceComponent(), c,
				1, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(p, Box.createVerticalGlue(), c, 0,
				y, 1, 1, 0, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		
		return p;
	}

	private Component createDiveComputerComponent() {
		computerTypeCb = new JComboBox(DiveComputerType.values());
		computerTypeCb.addKeyListener(new EmptyComboBoxKeyListener());
		computerTypeCb.setRenderer(new ListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) new DefaultListCellRenderer()
						.getListCellRendererComponent(list, value, index,
								isSelected, cellHasFocus);
				if (null == value) {
					renderer.setText(null);
				} else {
					renderer.setText(i18n.getString(((DiveComputerType) value)
							.getKey()));
				}
				return renderer;
			}
		});
		return computerTypeCb;
	}

	private Component createInterfaceComponent() {
		dataCommComponent = new DataCommComponent();
		dataCommComponent.setBorder(new LineBorder(UIAgent.getInstance()
				.getColorPanelUnselected()));
		return dataCommComponent;
	}

	private Component createDiveComputerTypeLabel() {
		return new I18nLabel("dive.computer");
	}

	private Component createInterfaceTypeLabel() {
		return new I18nLabel("datacomm.interface.type");
	}

	@Override
	public void adaptUserPreferences() {
		UserPreferences.getInstance()
				.setPreferredDiveComputerDataCommInterface(
						dataCommComponent.getDataCommInterface());

		UserPreferences.getInstance().setPreferredDiveComputer(
				(DiveComputerType) computerTypeCb.getSelectedItem());
	}

	@Override
	public void setUserPreferences() {
		dataCommComponent.setDatacomInterface(UserPreferences.getInstance()
				.getPreferredDiveComputerDataCommInterface());

		computerTypeCb.setSelectedItem(UserPreferences.getInstance()
				.getPreferredDiveComputer());
	}
}
