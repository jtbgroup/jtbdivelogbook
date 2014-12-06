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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.GlossaryManagerFacade;
import be.vds.jtbdive.client.view.core.logbook.LogBookEditionDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.exceptions.DataStoreException;

public class DiveSiteEditionDialog extends JDialog {

	private static final long serialVersionUID = 4891377257168571083L;
	public static final int MODE_VIEW = 0;
	public static final int MODE_EDIT = 1;
	public static final int MODE_SAVE = 2;

	private static final Logger logger = Logger
			.getLogger(LogBookEditionDialog.class);

	private DiveSitePanel diveSitePanel;
	private int mode;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private GlossaryManagerFacade glossaryManagerFacade;

	public DiveSiteEditionDialog(JFrame parentWindow,
			DiveSiteManagerFacade diveLocationManagerFacade,
			GlossaryManagerFacade glossaryManagerFacade, int mode) {
		super(parentWindow);
		this.diveSiteManagerFacade = diveLocationManagerFacade;
		this.glossaryManagerFacade = glossaryManagerFacade;
		this.mode = mode;
		init();
	}

	private void init() {

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		if (mode == MODE_EDIT) {
			this.setIconImage(UIAgent.getInstance().getBufferedImage(
					UIAgent.ICON_DIVE_SITE_EDIT_16));
		} else if (mode == MODE_SAVE) {
			this.setIconImage(UIAgent.getInstance().getBufferedImage(
					UIAgent.ICON_DIVE_SITE_ADD_16));
		}
		this.getContentPane().add(createContentPane());

		adaptContentPanelEdition();
	}

	private void adaptContentPanelEdition() {
		if (mode == MODE_EDIT || mode == MODE_SAVE)
			diveSitePanel.setEditable(true);
	}

	private JComponent createContentPane() {
		JPanel contentPane = new JPanel();
		contentPane.setBackground(UIAgent.getInstance()
				.getColorBaseBackground());
		contentPane.setLayout(new BorderLayout(2, 2));

		contentPane.add(createCentralPanel(), BorderLayout.CENTER);
		contentPane.add(createButtonsPanel(), BorderLayout.SOUTH);
		return contentPane;
	}

	private Component createCentralPanel() {
		diveSitePanel = new DiveSitePanel(diveSiteManagerFacade,
				glossaryManagerFacade);
		return diveSitePanel;
	}

	private Component createButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setOpaque(false);
		if (mode == MODE_EDIT || mode == MODE_SAVE)
			buttonsPanel.add(createSaveButton());

		buttonsPanel.add(createCancelButton());
		return buttonsPanel;
	}

	private Component createSaveButton() {
		JButton button = new I18nButton(new AbstractAction("save") {
			private static final long serialVersionUID = -2566992068086661427L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mode == MODE_SAVE) {

					try {
						diveSiteManagerFacade.saveDiveSite(diveSitePanel
								.getValue());
					} catch (DataStoreException e1) {
						JOptionPane
								.showMessageDialog(
										DiveSiteEditionDialog.this,
										"Logbook data not saved due to persistency layer problem.",
										"Error", JOptionPane.ERROR_MESSAGE);
						logger.error("logbook not saved: " + e1.getMessage());
					} finally {
						dispose();
					}
				} else if (mode == MODE_EDIT) {
					try {
						diveSiteManagerFacade.updateDiveSite(diveSitePanel
								.getValue());
					} catch (DataStoreException e1) {
						JOptionPane
								.showMessageDialog(
										DiveSiteEditionDialog.this,
										"Logbook data not saved due to persistency layer problem.",
										"Error", JOptionPane.ERROR_MESSAGE);
						logger.error("logbook not saved: " + e1.getMessage());
					} finally {
						dispose();
					}
				}
			}
		});
		return button;
	}

	private Component createCancelButton() {
		JButton button = new I18nButton(new AbstractAction("cancel") {
			private static final long serialVersionUID = 7223868365688403731L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		return button;
	}

	public void setValue(DiveSite diveLocation) {
		diveSitePanel.setValue(diveLocation);
	}

}
