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

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.events.LogBookEventAdapter;
import be.vds.jtbdive.client.view.events.LogBookUiEventHandler;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Diver;

public class DiverDetailPanel extends DetailPanel {

	private static final long serialVersionUID = -2641382261738391666L;
	private JLabel nameLabel;
	private CardLayout cardLayout;
	private JLabel birthDateLabel;

	public DiverDetailPanel() {
		init();
		initListener();
	}

	private void initListener() {
		LogBookEventAdapter adapter = new LogBookEventAdapter() {
			@Override
			public void diverSelected(Component source, Diver diver) {
				displayDiver(diver);
			}
		};
		LogBookUiEventHandler.getInstance().addEventListener(adapter);
	}

	private void init() {
		JPanel centralPanel = new JPanel(new GridBagLayout());
		centralPanel.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(3, 3, 3, 3);
		int y = 0;
		GridBagLayoutManager.addComponent(centralPanel, createNameComponent(),
				gc, 0, y, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(centralPanel,
				createBirthDateComponent(), gc, 0, ++y, 1, 1, 1, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(centralPanel,
				Box.createVerticalGlue(), gc, 0, ++y, 1, 1, 1, 1,
				GridBagConstraints.BOTH, GridBagConstraints.WEST);

		JPanel defaultPanel = new JPanel();
		defaultPanel.add(new I18nLabel("no.diver"));
		defaultPanel.setOpaque(false);

		cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		this.add(defaultPanel, "default");
		this.add(centralPanel, "diver");
	}

	private Component createBirthDateComponent() {
		birthDateLabel = new JLabel();
		birthDateLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return birthDateLabel;
	}

	private Component createNameComponent() {
		nameLabel = new JLabel();
		nameLabel.setFont(UIAgent.getInstance().getFontTitleDetail());
		nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return nameLabel;
	}

	private void displayDiver(Diver diver) {
		if (diver == null)
			clear();
		else
			fillData(diver);
	}

	private void clear() {
		nameLabel.setText(null);
		cardLayout.show(this, "default");
	}

	private void fillData(Diver diver) {
		nameLabel.setText(diver.getFullName());
		Date bd = diver.getBirthDate();
		if (null != bd) {
			birthDateLabel.setText(UIAgent.getInstance()
					.getFormatDateShort().format(bd)+ " ("+LogBookUtilities.getDiversAge(diver)+")");
		} else {
			birthDateLabel.setText(null);
		}
		
		
		cardLayout.show(this, "diver");
	}

}
