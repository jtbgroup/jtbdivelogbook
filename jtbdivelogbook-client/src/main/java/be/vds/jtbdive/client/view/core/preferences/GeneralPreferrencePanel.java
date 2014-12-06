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
import java.util.Locale;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nCheckBox;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.util.LanguageHelper;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookSorting;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class GeneralPreferrencePanel extends AbstractPreferrencePanel {

	private static final long serialVersionUID = -3689046291861107714L;
	private JComboBox localeCb;
	private I18nCheckBox skipWelcomeMessageCb;
	private I18nCheckBox skipDiveTimeModificationInfluenceMessageCb;
	private I18nCheckBox skipDiveDepthModificationInfluenceMessageCb;
	private JComboBox logbookDefaultSortingCb;
	private I18nCheckBox notifyUpdateCb;

	public GeneralPreferrencePanel() {
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
		GridBagLayoutManager.addComponent(p, createLanguageLabel(), c, 0, y, 1,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createLanguageComponent(), c, 1,
				y++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager
				.addComponent(p, createLogBookSortingLabel(), c, 0, y, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createLogBookSortingComponent(),
				c, 1, y++, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		// a gap of 50
		GridBagLayoutManager.addComponent(p, Box.createHorizontalStrut(50), c,
				0, y++, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_START);

		// all checkboxes
		GridBagLayoutManager.addComponent(p,
				createSkipStartupMessageComponent(), c, 0, y++, 2, 1, 0, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createSkipCheckUpdateComponent(),
				c, 0, y++, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p,
				createSkipDiveTimeModificationInfluenceMessageComponent(), c,
				0, y++, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p,
				createSkipDiveDepthModificationInfluenceMessageComponent(), c,
				0, y++, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		//gap
		GridBagLayoutManager.addComponent(p, Box.createGlue(), c, 2, y,
				2, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		return p;
	}

	private Component createLogBookSortingLabel() {
		return new I18nLabel("logbook.default.sorting");
	}

	private Component createLogBookSortingComponent() {
		logbookDefaultSortingCb = new JComboBox(LogBookSorting.values());
		logbookDefaultSortingCb.setRenderer(new KeyedCatalogRenderer());
		return logbookDefaultSortingCb;
	}

	private Component createSkipStartupMessageComponent() {
		skipWelcomeMessageCb = new I18nCheckBox();
		return createPanelForChackBox(skipWelcomeMessageCb, "startup.message");
	}

	private Component createSkipDiveDepthModificationInfluenceMessageComponent() {
		skipDiveDepthModificationInfluenceMessageCb = new I18nCheckBox();
		return createPanelForChackBox(
				skipDiveDepthModificationInfluenceMessageCb,
				"dive.depth.modification.influence.skip");
	}

	private Component createPanelForChackBox(I18nCheckBox checkBox,
			String bundleKey) {
		JTextArea label = SwingComponentHelper
				.createTransclucentJTextAreaWithNoBorder(I18nResourceManager
						.sharedInstance().getString(bundleKey), false, true,
						true);

		JPanel p = new JPanel(new BorderLayout());
//		JPanel p = new JPanel(new GridBagLayout());
//		p.setBorder(new LineBorder(Color.RED));
		p.setOpaque(false);
//		GridBagConstraints gc = new GridBagConstraints();
//		GridBagLayoutManager.addComponent(p, checkBox, gc, 0, 0, 1, 1, 0, 0,
//				GridBagConstraints.NONE, GridBagConstraints.CENTER);
//		GridBagLayoutManager.addComponent(p, label, gc, 1, 0, 1, 1, 1, 0,
//				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		p.add(checkBox, BorderLayout.WEST);
		p.add(label, BorderLayout.CENTER);
		
		return p;
	}

	private Component createSkipDiveTimeModificationInfluenceMessageComponent() {
		skipDiveTimeModificationInfluenceMessageCb = new I18nCheckBox();
		return createPanelForChackBox(
				skipDiveTimeModificationInfluenceMessageCb,
				"dive.time.modification.influence.skip");
	}

	private Component createSkipCheckUpdateComponent() {
		notifyUpdateCb = new I18nCheckBox();
		return createPanelForChackBox(notifyUpdateCb,
				"update.check.startup.notify");
	}

	private Component createLanguageComponent() {
		localeCb = new JComboBox(LanguageHelper.getKnownLocales().toArray());
		localeCb.setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 3777523076033538405L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Locale locale = (Locale) value;
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, cellHasFocus);
				label.setText(I18nResourceManager.sharedInstance().getString(
						"locale." + locale.getLanguage() + "."
								+ locale.getCountry()));

				return this;
			}
		});
		return localeCb;
	}

	private Component createLanguageLabel() {
		JLabel label = new I18nLabel("startup.language");
		label.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_MOUTH_16));
		return label;
	}

	@Override
	public void adaptUserPreferences() {
		UserPreferences up = UserPreferences.getInstance();
		up.setPreferredLocale((Locale) localeCb.getSelectedItem());
		up.setSkipStartupMessage(skipWelcomeMessageCb.isSelected());
		up.setCheckUpdatesOnStartUp(notifyUpdateCb.isSelected());
		up.setDefaultLogbookSorting((LogBookSorting) logbookDefaultSortingCb
				.getSelectedItem());

		up.setSkipDiveDepthmodificationInfluenceMessage(skipDiveDepthModificationInfluenceMessageCb
				.isSelected());
		up.setSkipDiveTimeInfluenceMessage(skipDiveTimeModificationInfluenceMessageCb
				.isSelected());
	}

	@Override
	public void setUserPreferences() {
		UserPreferences up = UserPreferences.getInstance();
		if (null != up.getPreferredLocale()) {
			localeCb.setSelectedItem(up.getPreferredLocale());
		}

		skipWelcomeMessageCb.setSelected(up.skipStartupMessage());
		skipDiveDepthModificationInfluenceMessageCb.setSelected(up
				.skipDiveDepthModificationInfluenceMessage());
		skipDiveTimeModificationInfluenceMessageCb.setSelected(up
				.skipDiveTimeInfluenceMessage());
		notifyUpdateCb.setSelected(up.checkUpdatesOnStartup());

		logbookDefaultSortingCb.setSelectedItem(up.getDefaultLogbookSorting());

	}
}
