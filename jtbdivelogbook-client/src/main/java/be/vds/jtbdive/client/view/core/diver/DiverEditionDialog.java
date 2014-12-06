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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.exceptions.DataStoreException;

public class DiverEditionDialog extends JDialog {

	private static final long serialVersionUID = 6458455556834726848L;
	public static final int MODE_VIEW = 0;
	public static final int MODE_EDIT = 1;
	public static final int MODE_SAVE = 2;

	private static final Logger LOGGER = Logger
			.getLogger(DiverEditionDialog.class);

	private DiverPanel diverPanel;
	private int mode;
	private DiverManagerFacade diverManagerFacade;

	public DiverEditionDialog(JFrame parentWindow,
			DiverManagerFacade diverManagerFacade, int mode) {
		super(parentWindow, I18nResourceManager.sharedInstance().getString(
				"diver"), true);
		this.diverManagerFacade = diverManagerFacade;
		this.mode = mode;
		init();
	}

	private void init() {

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().add(createContentPane());

		adaptContentPanelEdition();

		this.setResizable(true);
		this.setSize(300, 400);
	}

	private void adaptContentPanelEdition() {
		if (mode == MODE_EDIT) {
			diverPanel.setEditable(true);
			this.setIconImage(
					UIAgent.getInstance().getBufferedImage(UIAgent.ICON_DIVER_BLACK_EDIT_24));
		} else if (mode == MODE_SAVE) {
			diverPanel.setEditable(true);
			this.setIconImage(UIAgent.getInstance().getBufferedImage(UIAgent.ICON_DIVER_BLACK_ADD_24));
		}
	}

	private JComponent createContentPane() {
		JPanel contentPane = new JPanel();
		contentPane.setBackground(UIAgent.getInstance()
				.getColorBaseBackground());
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		contentPane.add(createMainPanel(), BorderLayout.CENTER);
		contentPane.add(createButtonsPanel(), BorderLayout.SOUTH);

		return contentPane;
	}

	private Component createMainPanel() {
		diverPanel = new DiverPanel();
		return diverPanel;
	}

	private Component createButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setOpaque(false);

		if (mode == MODE_EDIT)
			buttonsPanel.add(createEditButton());
		if (mode == MODE_SAVE)
			buttonsPanel.add(createSaveButton());

		buttonsPanel.add(createCancelButton());
		return buttonsPanel;
	}

	private Component createSaveButton() {
		JButton button = new I18nButton(new AbstractAction("save") {
			private static final long serialVersionUID = 934381585791216159L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					diverManagerFacade.saveDiver(diverPanel.getDiver());
				} catch (DataStoreException e1) {
					JOptionPane
							.showMessageDialog(
									DiverEditionDialog.this,
									"Logbook data not saved due to persistency layer problem.",
									"Error", JOptionPane.ERROR_MESSAGE);
					LOGGER.error("logbook not saved: " + e1.getMessage());
				} finally {
					dispose();
				}
			}
		});
		return button;
	}

	private Component createEditButton() {
		JButton button = new I18nButton(new AbstractAction("edit") {
			private static final long serialVersionUID = -7239025054353825086L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					diverManagerFacade.updateDiver(diverPanel.getDiver());
				} catch (DataStoreException e1) {
					ExceptionDialog
							.showDialog(e1, DiverEditionDialog.this,
									"Logbook data not saved due to persistency layer problem.");
					LOGGER.error("logbook not saved: " + e1.getMessage());
				} finally {
					dispose();
				}
			}
		});
		return button;
	}

	private Component createCancelButton() {
		JButton button = new I18nButton(new AbstractAction("cancel") {
			private static final long serialVersionUID = 9041031936592206957L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		return button;
	}

	public void setValue(Diver diver) {
		diverPanel.setDiver(diver);
	}

}
