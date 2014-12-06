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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nCheckBox;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.util.LanguageHelper;
import be.vds.jtbdive.client.util.LocalesComparator;
import be.vds.jtbdive.client.util.ReportHelper;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.units.LengthUnit;
import be.vds.jtbdive.core.core.units.TemperatureUnit;

public class ReportDialog extends PromptDialog {

	private static final long serialVersionUID = -7574643204355407104L;
	private JCheckBox includeDiveProfileCb;
	private JCheckBox includeDiveDocumentCb;
	private JComboBox languageCb;
	private JComboBox lengthUnitCb;
	private JComboBox temperatureUnitCb;
	private DiveSelectionComponent diveSelector;
	private LogBook logBook;
	private I18nCheckBox includeDiveSiteCb;
	private I18nCheckBox includeDiveSiteDocumentsCb;
	private I18nCheckBox includeDiveDetailsCb;
	private I18nCheckBox includePhysiologicalStatusCb;
	

	public ReportDialog(LogBook logBook) {
		super(i18n.getString("report"), i18n
				.getString("report.options.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_REPORT_48));
		this.logBook = logBook;
		initComponent();
	}

	private void initComponent() {
		initValues();
		diveSelector.setDives(logBook.getDives());
	}

	@Override
	protected Component createContentPanel() {
		JPanel optionsPanel = new DetailPanel();
		optionsPanel.setLayout(new GridBagLayout());
		optionsPanel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		int y = 0;

		// language
		GridBagLayoutManager.addComponent(optionsPanel, createLanguageLabel(),
				c, 0, y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(optionsPanel, createLanguageCb(), c,
				1, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		// Temperature unit
		GridBagLayoutManager.addComponent(optionsPanel,
				createTemperatureUnitLabel(), c, 0, y, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(optionsPanel,
				createTemperatureUnitCb(), c, 1, y++, 1, 1, 1, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);

		// Length unit
		GridBagLayoutManager.addComponent(optionsPanel,
				createLengthUnitLabel(), c, 0, y, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(optionsPanel, createLengthUnitCb(),
				c, 1, y++, 1, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);

		// Dive selection
		GridBagLayoutManager.addComponent(optionsPanel,
				createDiveSelectionLabel(), c, 0, y, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(optionsPanel,
				createSelectDiveComponent(), c, 1, y++, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		// Include dive details
		GridBagLayoutManager.addComponent(optionsPanel,
				createIncludePhysiologicalStatusCb(), c, 0, y++, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		// Include dive details
		GridBagLayoutManager.addComponent(optionsPanel,
				createIncludeDiveDetailsCb(), c, 0, y++, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		// Include dive profiles
		GridBagLayoutManager.addComponent(optionsPanel,
				createIncludeDiveProfileCb(), c, 0, y++, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		// Include dive pictures
		GridBagLayoutManager.addComponent(optionsPanel,
				createIncludeDivePicturesCb(), c, 0, y++, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		// Include dive sites
		GridBagLayoutManager.addComponent(optionsPanel,
				createIncludeDiveSiteCb(), c, 0, y++, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		// Include dive sites pictures
		GridBagLayoutManager.addComponent(optionsPanel,
				createIncludeDiveSitePictureCb(), c, 0, y++, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		// Gap
		GridBagLayoutManager.addComponent(optionsPanel, Box.createGlue(), c, 0,
				y, 2, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);

		return optionsPanel;
	}

	private Component createSelectDiveComponent() {
		diveSelector = new DiveSelectionComponent(null);
		diveSelector.setPreferredSize(new Dimension(200, 20));
		return diveSelector;
	}

	private Component createLanguageLabel() {
		return new I18nLabel("language");
	}

	private Component createLengthUnitLabel() {
		return new I18nLabel("length");
	}

	private Component createTemperatureUnitLabel() {
		return new I18nLabel("temperature");
	}

	private Component createDiveSelectionLabel() {
		return new I18nLabel("dives.selection");
	}

	private Component createTemperatureUnitCb() {
		temperatureUnitCb = new JComboBox(TemperatureUnit.values());
		temperatureUnitCb.setRenderer(new KeyedCatalogRenderer());
		return temperatureUnitCb;
	}

	private Component createLengthUnitCb() {
		lengthUnitCb = new JComboBox(LengthUnit.values());
		lengthUnitCb.setRenderer(new KeyedCatalogRenderer());
		return lengthUnitCb;
	}

	private Component createLanguageCb() {
		List<Locale> locales = LanguageHelper.getKnownLocales();
		Collections.sort(locales, new LocalesComparator());
		languageCb = new JComboBox(locales.toArray());
		languageCb.setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1419437662957077797L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list,
						value, index, isSelected, cellHasFocus);
				Locale locale = (Locale) value;
				l.setText(I18nResourceManager.sharedInstance().getString(
						"locale." + locale.getLanguage() + "."
								+ locale.getCountry()));
				return this;
			}
		});
		return languageCb;
	}

	private Component createIncludeDiveProfileCb() {
		includeDiveProfileCb = new I18nCheckBox("dive.profile.include");
		includeDiveProfileCb.setOpaque(false);
		return includeDiveProfileCb;
	}
	
	private Component createIncludePhysiologicalStatusCb() {
		includePhysiologicalStatusCb = new I18nCheckBox("dive.physiological.status.include");
		includePhysiologicalStatusCb.setOpaque(false);
		return includePhysiologicalStatusCb;
	}
	private Component createIncludeDiveDetailsCb() {
		includeDiveDetailsCb = new I18nCheckBox("dive.details.include");
		includeDiveDetailsCb.setOpaque(false);
		return includeDiveDetailsCb;
	}

	private Component createIncludeDiveSiteCb() {
		includeDiveSiteCb = new I18nCheckBox("include.dive.site");
		includeDiveSiteCb.setOpaque(false);
		includeDiveSiteCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!includeDiveSiteCb.isSelected()) {
					includeDiveSiteDocumentsCb.setEnabled(false);
					includeDiveSiteDocumentsCb.setSelected(false);
				} else {
					includeDiveSiteDocumentsCb.setEnabled(true);
				}
			}
		});
		return includeDiveSiteCb;
	}

	private Component createIncludeDiveSitePictureCb() {
		includeDiveSiteDocumentsCb = new I18nCheckBox(
				"documents.include.divesite");
		includeDiveSiteDocumentsCb.setOpaque(false);
		return includeDiveSiteDocumentsCb;
	}

	private Component createIncludeDivePicturesCb() {
		includeDiveDocumentCb = new I18nCheckBox("documents.include.dive");
		includeDiveDocumentCb.setOpaque(false);
		return includeDiveDocumentCb;
	}

	public Map<String, Object> getOptions() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(ReportHelper.INCLUDE_DIVE_PROFILE,
				includeDiveProfileCb.isSelected());
		options.put(ReportHelper.INCLUDE_DIVE_PHYSIOLOGICAL_STATUS,
				includePhysiologicalStatusCb.isSelected());
		options.put(ReportHelper.INCLUDE_DIVE_DETAILS,
				includeDiveDetailsCb.isSelected());
		options.put(ReportHelper.INCLUDE_DIVE_DOCUMENT,
				includeDiveDocumentCb.isSelected());
		options.put(ReportHelper.DIVES, diveSelector.getSelectedDives());
		options.put(ReportHelper.LOCALE, languageCb.getSelectedItem());
		options.put(ReportHelper.UNIT_LENGTH, lengthUnitCb.getSelectedItem());
		options.put(ReportHelper.UNIT_TEMPERATURE,
				temperatureUnitCb.getSelectedItem());
		options.put(ReportHelper.INCLUDE_DIVE_SITE,
				includeDiveSiteCb.isSelected());
		options.put(ReportHelper.INCLUDE_DIVE_SITE_DOCUMENT,
				includeDiveSiteDocumentsCb.isSelected());

		return options;
	}

	private void initValues() {
		temperatureUnitCb.setSelectedItem(UnitsAgent.getInstance()
				.getTemperatureUnit());
		lengthUnitCb.setSelectedItem(UnitsAgent.getInstance().getLengthUnit());
		languageCb.setSelectedItem(I18nResourceManager.sharedInstance()
				.getDefaultLocale());

		includePhysiologicalStatusCb.setSelected(false);
		includeDiveDetailsCb.setSelected(true);
		includeDiveProfileCb.setSelected(true);
		includeDiveDocumentCb.setSelected(false);

		includeDiveSiteCb.setSelected(false);
		includeDiveSiteDocumentsCb.setSelected(false);
		includeDiveSiteDocumentsCb.setEnabled(false);
	}
}
