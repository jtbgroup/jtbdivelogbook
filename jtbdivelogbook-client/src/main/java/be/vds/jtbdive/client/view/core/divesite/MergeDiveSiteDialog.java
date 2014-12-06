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
package be.vds.jtbdive.client.view.core.divesite;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;

public class MergeDiveSiteDialog extends PromptDialog {

	private static final long serialVersionUID = 3379432074059899274L;

	private DiveSiteManagerFacade diveSiteManagerFacade;
	private DiveSiteChooser toDeleteDlChooser;
	private DiveSiteChooser toKeepDlChooser;

	public MergeDiveSiteDialog(DiveSiteManagerFacade diveSiteManagerFacade,
			JFrame jframe) {
		super(jframe, i18n.getString("divesites.merge"), i18n
				.getString("divesites.merge.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVE_SITE_MERGE_48), null);
		initCustom(diveSiteManagerFacade);
	}

	private void initCustom(DiveSiteManagerFacade diveSiteManagerFacade) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
		toDeleteDlChooser.setDiveSiteManagerFacade(diveSiteManagerFacade);
		toKeepDlChooser.setDiveSiteManagerFacade(diveSiteManagerFacade);
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new DetailPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		GridBagLayoutManager.addComponent(p, createToDelLabel(), c, 0, 0, 2, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, Box.createHorizontalStrut(20), c,
				0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createToDelComponent(), c, 1, 1,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createToKeepLabel(), c, 0, 2, 2,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, Box.createHorizontalStrut(20), c,
				0, 3, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createToKeepComponent(), c, 1, 3,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), c, 0, 4, 2, 1,
				1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.FIRST_LINE_START);
		return p;
	}

	private Component createToDelLabel() {
		return new I18nLabel("divesite.to.delete");
	}

	private Component createToDelComponent() {
		toDeleteDlChooser = new DiveSiteChooser(diveSiteManagerFacade);
		toDeleteDlChooser
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(
								DiveSiteChooser.DIVESITE_SITE_PROPERTY)) {
							adaptActions();
						}
					}
				});
		return toDeleteDlChooser;
	}

	private Component createToKeepLabel() {
		return new I18nLabel("divesite.to.keep");
	}

	private Component createToKeepComponent() {
		toKeepDlChooser = new DiveSiteChooser(diveSiteManagerFacade);
		toKeepDlChooser.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						DiveSiteChooser.DIVESITE_SITE_PROPERTY)) {
					adaptActions();
				}
			}

		});
		return toKeepDlChooser;
	}

	private void adaptActions() {
		setOkButtonEnabled(getDiveLocationToDelete() != null
				&& getDiveLocationToDelete() != null);
	}

	public DiveSite getDiveLocationToKeep() {
		return toKeepDlChooser.getDiveSite();
	}

	public DiveSite getDiveLocationToDelete() {
		return toDeleteDlChooser.getDiveSite();
	}

}
