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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Diver;

public class MergeDiverDialog extends PromptDialog {

	private static final long serialVersionUID = -2772870697419695446L;
	private DiverManagerFacade diverManagerFacade;
	private DiverChooser toDeleteDlChooser;
	private DiverChooser toKeepDlChooser;

	public MergeDiverDialog(DiverManagerFacade diverManagerFacade,
			JFrame jframe) {
		super(jframe, i18n.getString("divers.merge"), i18n
				.getString("divers.merge.message"),UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVER_BLACK_MERGE_48), null);
		initCustom(diverManagerFacade);
	}
	
	private void initCustom(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		toDeleteDlChooser.setDiveManagerFacade(diverManagerFacade);
		toKeepDlChooser.setDiveManagerFacade(diverManagerFacade);
	}

	protected Component createContentPanel() {
		JPanel p = new DetailPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		GridBagLayoutManager.addComponent(p, createToDelLabel(), c, 0, 0, 2, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, Box.createHorizontalStrut(20), c, 0, 1, 1, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createToDelComponent(), c, 1, 1,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createToKeepLabel(), c, 0, 2, 2,
				1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, Box.createHorizontalStrut(20), c, 0, 3, 1, 1,
				0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(p, createToKeepComponent(), c, 1, 3,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, Box.createGlue(), c, 0, 4, 2, 1,
				1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.FIRST_LINE_START);
		return p;
	}

	private Component createToDelLabel() {
		return new I18nLabel("diver.to.delete");
	}

	private Component createToDelComponent() {
		toDeleteDlChooser = new DiverChooser(diverManagerFacade);
		toDeleteDlChooser
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (evt.getPropertyName().equals(
								DiverChooser.DIVER_CHANGED_PROPERTY)) {
							adaptActions();
						}
					}
				});
		return toDeleteDlChooser;
	}

	private Component createToKeepLabel() {
		return new I18nLabel("diver.to.keep");
	}

	private Component createToKeepComponent() {
		toKeepDlChooser = new DiverChooser(diverManagerFacade);
		toKeepDlChooser.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						DiverChooser.DIVER_CHANGED_PROPERTY)) {
					adaptActions();
				}
			}
		});
		return toKeepDlChooser;
	}

	private void adaptActions() {
		setOkButtonEnabled(getDiverToDelete() != null
				&& getDiverToKeep() != null);
	}

	public Diver getDiverToKeep() {
		return toKeepDlChooser.getDiver();
	}

	public Diver getDiverToDelete() {
		return toDeleteDlChooser.getDiver();
	}

	private Component createExplanationText() {
		JTextArea ta = new JTextArea();
		ta.setFont(UIAgent.getInstance().getFontNormalItalic());
		ta.setLineWrap(true);
		ta.setOpaque(false);
		ta.setEditable(false);
		ta.setForeground(UIAgent.getInstance().getColorBaseForeground());
		ta.setText(I18nResourceManager.sharedInstance().getString(
				"diver.merge.message"));
		return ta;
	}
}
