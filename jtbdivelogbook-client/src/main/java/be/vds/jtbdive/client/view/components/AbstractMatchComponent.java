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
package be.vds.jtbdive.client.view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nCheckBox;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;

public abstract class AbstractMatchComponent extends JPanel {

	private static final long serialVersionUID = -5327071167161653302L;
	private JLabel toMatchNameTf;
	private I18nCheckBox matchCb;
	private JCheckBox selectComboBox;
	private boolean allowMatch;
	

	private Object objectToMatch;

	public AbstractMatchComponent(boolean allowMatch) {
		this.allowMatch = allowMatch;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(this, createSelectionComponent(), gc,
				0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createToMatchNameTf(), gc, 1,
				0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.EAST);

		if (allowMatch) {
			GridBagLayoutManager.addComponent(this,
					Box.createHorizontalStrut(30), gc, 0, 1, 1, 2, 0, 0,
					GridBagConstraints.NONE, GridBagConstraints.NORTH);

			GridBagLayoutManager.addComponent(this, createMatchChoicePanel(),
					gc, 1, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.EAST);
		}

		initDefault();
	}

	private void initDefault() {
		selectComboBox.setSelected(true);

		if (allowMatch) {
			matchCb.setSelected(false);
		}
		handleMatch(false);
	}

	private Component createMatchChoicePanel() {
		JPanel p = new DetailPanel(new BorderLayout());
		p.add(createMatchChoice(), BorderLayout.NORTH);
		p.add(createMatchComponent(), BorderLayout.CENTER);
		return p;
	}

	private Component createSelectionComponent() {
		selectComboBox = new JCheckBox(new AbstractAction() {
			private static final long serialVersionUID = 2005631770158613958L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSelection(selectComboBox.isSelected());
			}
		});
		return selectComboBox;
	}

	private void handleSelection(boolean enabled) {
		if (!allowMatch) {
			return;
		}

		toMatchNameTf.setEnabled(enabled);
		matchCb.setEnabled(enabled);

		if (!enabled) {
			matchCb.setSelected(false);
			handleMatch(false);
		}
	}

	private Component createToMatchNameTf() {
		toMatchNameTf = new JLabel();
		return toMatchNameTf;
	}

	private Component createMatchChoice() {
		matchCb = new I18nCheckBox(new AbstractAction(getMatchBundleKey()) {

			private static final long serialVersionUID = 6202048294644235868L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMatch(matchCb.isSelected());
			}

		});

		matchCb.setOpaque(false);
		matchCb.setBorder(null);
		return matchCb;
	}

	protected abstract String getMatchBundleKey() ;

	private void handleMatch(boolean enabled) {
		if (allowMatch) {
			handleMatchComponent(enabled);
		}
	}

	protected abstract void handleMatchComponent(boolean enabled);

	protected abstract Component createMatchComponent();

	public void setObjectToMatch(Object object) {
		this.objectToMatch = object;
		toMatchNameTf.setText(getObjectToMatchDescription());
	}

	protected abstract String getObjectToMatchDescription();

	public Object getSelectedObject() {
		if (!allowMatch)
			return null;
		return getMatchingObjectFromComponent();
	}

	protected abstract Object getMatchingObjectFromComponent();

	public void setMatchingObject(Object matchingObject) {
		if (allowMatch) {

			if (!isSelected()) {
				setSelected(matchingObject != null);
			}

			setMatch(matchingObject != null);
			setMatchingObjectToCustomComponent(matchingObject);
		}
	}

	protected abstract void setMatchingObjectToCustomComponent(
			Object selectedObject);

	public void setMatch(boolean match) {
		if (matchCb.isEnabled())
			matchCb.setSelected(match);
	}

	public Object getObjectToMatch() {
		return objectToMatch;
	}

	public void setSelected(boolean selected) {
		selectComboBox.setSelected(selected);
		handleSelection(selected);
	}

	public boolean isSelected() {
		return selectComboBox.isSelected();
	}

	public boolean isAllowMatch() {
		return allowMatch;
	}
}
