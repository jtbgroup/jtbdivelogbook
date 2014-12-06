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
package be.vds.jtbdive.client.view.core.logbook.material.edit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nCheckBox;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.catalogs.MaterialType;
import be.vds.jtbdive.core.core.material.Material;

public abstract class AbstractMaterialPanel extends JPanel {

	private static final long serialVersionUID = 549552224768873811L;
	protected static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private JTextField manufacturerTf;
	private JTextField modelTf;
	private JTextArea commentTextArea;
	private JXDatePicker purchaseDatePicker;
	private Material currentMaterial;
	private I18nCheckBox activeCb;
	private JFormattedTextField priceTf;
	private JLabel titleLabel;
	private DetailPanel specificContainerPanel;

	public AbstractMaterialPanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createHeader(), BorderLayout.NORTH);
		this.add(createContent(), BorderLayout.CENTER);

	}

	private Component createContent() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		JComponent c = createGeneralAttributes();
		p.add(c, BorderLayout.NORTH);

		c = createSpecificContainer();
		if (c != null) {
			p.add(c, BorderLayout.CENTER);
		}

		return p;
	}

	private JComponent createSpecificContainer() {
		JComponent c = createSpecific();
		if (c == null)
			return null;

		JScrollPane scroll = new JScrollPane(c);
		SwingComponentHelper.displayJScrollPane(scroll);

		specificContainerPanel = new DetailPanel(new BorderLayout());
		specificContainerPanel.add(scroll);

		return specificContainerPanel;
	}

	private JComponent createGeneralAttributes() {
		JPanel content = new JPanel(new GridBagLayout());
		content.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5, 5);
		int y = 0;

		GridBagLayoutManager.addComponent(content, createActiveComponent(), gc,
				3, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.EAST);

		GridBagLayoutManager.addComponent(content, createManufacturerLabel(),
				gc, 0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content,
				createManufacturerComponent(), gc, 1, y, 2, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(content, createModelLabel(), gc, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content, createModelComponent(), gc,
				1, y, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		// LINE 3
		GridBagLayoutManager.addComponent(content, createPuchaseDateLabel(),
				gc, 0, ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content,
				createPurchaseDateComponent(), gc, 1, y, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(content, createPuchasePriceLabel(),
				gc, 2, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(content,
				createPurchasePriceComponent(), gc, 3, y, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);

		// LINE 4
		GridBagLayoutManager.addComponent(content, createCommentLabel(), gc, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(content, createCommentComponent(),
				gc, 1, y, 4, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);


		JScrollPane scroll = new JScrollPane(content);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);

		JPanel topContainerPanel = new DetailPanel(new BorderLayout());
		topContainerPanel.add(scroll);

		return topContainerPanel;
	}

	private Component createPurchasePriceComponent() {
		priceTf = new JFormattedTextField(NumberFormat.getInstance());
		priceTf.setColumns(7);

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		p.add(priceTf);
		p.add(Box.createHorizontalStrut(5));
		p.add(new JLabel("\u20AC"));
		return p;
	}

	private Component createPuchasePriceLabel() {
		return new I18nLabel("purchase.price");
	}

	private Component createActiveComponent() {
		activeCb = new I18nCheckBox("active");
		activeCb.setOpaque(false);
		activeCb.setSelected(true);
		return activeCb;
	}

	private Component createPuchaseDateLabel() {
		return new I18nLabel("purchase.date");
	}

	private Component createPurchaseDateComponent() {
		purchaseDatePicker = new JXDatePicker();
		purchaseDatePicker.setFormats("yyyy-MM-dd");
		return purchaseDatePicker;
	}

	private Component createCommentLabel() {
		return new I18nLabel("comment");
	}

	private Component createCommentComponent() {
		commentTextArea = new JTextArea();
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setLineWrap(true);

		JScrollPane scroll = new JScrollPane(commentTextArea);
		scroll.setPreferredSize(new Dimension(300, 75));
		return scroll;
	}

	private Component createHeader() {
		JPanel p = new JPanel(new FlowLayout());
		p.setOpaque(false);

		titleLabel = new JLabel();
		p.add(titleLabel);
		return p;
	}

	private void refreshTitle() {
		titleLabel.setText(getTitle());
		titleLabel.setIcon(getIcon());
	}

	protected Icon getIcon() {
		MaterialType mt = null;
		if (null == currentMaterial)
			return null;

		if (null != currentMaterial.getMaterialType()) {
			mt = currentMaterial.getMaterialType();
		}
		return MaterialHelper.getMaterialIcon(mt, 16);
	}

	protected String getTitle() {
		MaterialType mt = null;
		if (null == currentMaterial)
			return null;

		if (null != currentMaterial.getMaterialType()) {
			mt = currentMaterial.getMaterialType();
		}
		return i18n.getString(mt == null ? "material" : mt.getKey());
	}

	protected abstract JComponent createSpecific();

	public abstract Material getMaterial();

	public void setMaterial(Material material) {
		this.currentMaterial = material;
		refreshTitle();
		fillGeneralFields(material);
		fillSpecificComponents(material);
	}

	protected abstract void fillSpecificComponents(Material material);

	/**
	 * Fills the GUI components with the values of the material caracteristics
	 * 
	 * @param material
	 */
	private void fillGeneralFields(Material material) {
		modelTf.setText(material.getModel());
		manufacturerTf.setText(material.getManufacturer());
		commentTextArea.setText(material.getComment());
		purchaseDatePicker.setDate(material.getPurchaseDate());
		activeCb.setSelected(material.isActive());
		priceTf.setText(String.valueOf(material.getPurchasePrice()));
	}

	/**
	 * Fills the common material variables
	 * 
	 * @param material
	 */
	public void setGeneralFields(Material material) {
		if (currentMaterial != null) {
			material.setId(currentMaterial.getId());
		}

		material.setActive(activeCb.isSelected());

		String value = modelTf.getText();
		if (null != value && value.length() > 0)
			material.setModel(value);

		value = manufacturerTf.getText();
		if (null != value && value.length() > 0)
			material.setManufacturer(value);

		value = commentTextArea.getText();
		if (null != value && value.length() > 0)
			material.setComment(value);

		Date purchaseDate = purchaseDatePicker.getDate();
		if (null != purchaseDate) {
			material.setPurchaseDate(purchaseDate);
		}

		String s = priceTf.getText();
		if (s != null && s.length() > 0) {
			material.setPurchasePrice(Double.parseDouble(priceTf.getText()));
		} else {
			material.setPurchasePrice(0d);
		}

	}

	private Component createModelLabel() {
		return new I18nLabel("model");
	}

	private Component createModelComponent() {
		modelTf = new JTextField(20);
		return modelTf;
	}

	private Component createManufacturerLabel() {
		return new I18nLabel("manufacturer");
	}

	private Component createManufacturerComponent() {
		manufacturerTf = new JTextField(20);
		return manufacturerTf;
	}


}
