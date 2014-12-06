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
package be.vds.jtbdive.client.view.core.logbook.material;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLEditorKit;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.I18nTitlePanel;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.material.Material;

public class MaterialDetailPanel extends JPanel {

	private static final long serialVersionUID = -31174489565690459L;
	private I18nLabel titleLabel;
	private JEditorPane generalJEditor;
	private I18nTitlePanel specificTitlePanel;
	private DetailPanel specificPanel;
	private JEditorPane specificJEditor;
	private JLabel activityIcon;
	private Material currentMaterial;

	public MaterialDetailPanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		int y = 0;
		gc.insets = new Insets(2, 5, 2, 5);
		GridBagLayoutManager.addComponent(this, createHeader(), gc, 0, y, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createActivityIcon(), gc, 0, y,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.EAST);

		GridBagLayoutManager.addComponent(this, createGeneralTitle(), gc, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createGeneralPanel(), gc, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createSpecificTitle(), gc, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createSpecificPanel(), gc, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, Box.createHorizontalGlue(), gc,
				0, ++y, 1, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.WEST);
	}

	private Component createActivityIcon() {
		activityIcon = new JLabel();
		return activityIcon;
	}

	private Component createSpecificPanel() {
		specificPanel = new DetailPanel(new BorderLayout());
		specificJEditor = new JEditorPane();
		specificJEditor.setEditorKit(new HTMLEditorKit());
		SwingComponentHelper
				.displayEditorPaneWithNoBorderAndTranslucent(specificJEditor);
		specificJEditor.setEditable(false);
		specificPanel.add(specificJEditor);
		return specificPanel;
	}

	private Component createSpecificTitle() {
		specificTitlePanel = new I18nTitlePanel("specific");
		return specificTitlePanel;
	}

	private Component createHeader() {
		JPanel p = new JPanel(new FlowLayout());
		p.setOpaque(false);
		titleLabel = new I18nLabel();
		p.add(titleLabel);
		return p;
	}

	private Component createGeneralPanel() {
		JPanel p = new DetailPanel(new BorderLayout());
		generalJEditor = new JEditorPane();
		generalJEditor.setEditorKit(new HTMLEditorKit());
		SwingComponentHelper
				.displayEditorPaneWithNoBorderAndTranslucent(generalJEditor);
		generalJEditor.setEditable(false);
		p.add(generalJEditor);
		return p;
	}

	private Component createGeneralTitle() {
		I18nTitlePanel tp = new I18nTitlePanel("general");
		return tp;
	}

	public void setMaterial(Material material) {
		this.currentMaterial = material;
		if (null == material) {
			clear();
		} else {
			fillGeneralFields();
		}
	}

	private void clear() {
		// This check is done only to know whether the GUI components are
		// already created because this method is called on startup dur to the
		// updateUI method
		if (titleLabel != null) {
			titleLabel.setText(null);
			titleLabel.setIcon(null);
			activityIcon.setIcon(null);

			generalJEditor.setText(null);

			specificJEditor.setText(null);
			specificPanel.setVisible(false);
			specificTitlePanel.setVisible(false);
		}

	}

	private void fillGeneralFields() {
		Material material = this.currentMaterial;
		activityIcon.setIcon(material.isActive() ? UIAgent.getInstance()
				.getIcon(UIAgent.ICON_ACTIVE_24) : UIAgent.getInstance()
				.getIcon(UIAgent.ICON_INACTIVE_24));

		titleLabel.setTextBundleKey(material.getMaterialType().getKey());
		titleLabel.setIcon(MaterialHelper.getMaterialIcon(
				material.getMaterialType(), MaterialHelper.ICON_SIZE_24));

		generalJEditor.setText(MaterialHelper.getGeneralFieldsAsHTML(material));

		String html = MaterialHelper.getSpecificFieldsAsHTML(material);
		specificJEditor.setText(html);
		specificPanel.setVisible(null != html);
		specificTitlePanel.setVisible(null != html);
	}

	@Override
	public void updateUI() {
		super.updateUI();
		setMaterial(currentMaterial);
	}

}
