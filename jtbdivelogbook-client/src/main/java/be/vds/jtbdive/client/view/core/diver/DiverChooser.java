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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXPanel;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class DiverChooser extends JXPanel {

	private static final long serialVersionUID = -2936125130478688459L;
	private static final Syslog LOGGER = Syslog.getLogger(DiverChooser.class);
	public static final String DIVER_CHANGED_PROPERTY = "diver.changed";
	private static final Dimension BUTTONS_DIM = new Dimension(20, 20);
	private DiverManagerFacade diverManagerFacade;
	private JTextField nameTextField;
	private Diver diver;
	private JButton searchButton;
	private JButton resetButton;

	public DiverChooser(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		this.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayoutManager.addComponent(this, createNameTextField(), c, 0, 0,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createSearchButton(), c, 1, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createResetButton(), c, 2, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
	}

	private Component createResetButton() {
		Action resetAction = new AbstractAction() {
			private static final long serialVersionUID = 4483675063779611365L;

			@Override
			public void actionPerformed(ActionEvent e) {
				setDiver(null);
				fireDiverSelected();
			}
		};
		resetAction.putValue(Action.LARGE_ICON_KEY, UIAgent.getInstance()
				.getIcon(UIAgent.ICON_CANCEL_16));
		resetButton = new JButton(resetAction);
		resetButton.setPreferredSize(BUTTONS_DIM);
		resetButton.setContentAreaFilled(false);
		return resetButton;
	}

	private void fireDiverSelected() {
		firePropertyChange(DIVER_CHANGED_PROPERTY, null, diver);
	}

	private Component createSearchButton() {
		AbstractAction action = new AbstractAction() {
			private static final long serialVersionUID = -2666905141510004110L;

			@Override
			public void actionPerformed(ActionEvent e) {
				lookup(false);
			}
		};
		searchButton = new JButton(action);
		searchButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_SEARCH_16));
		searchButton.setPreferredSize(new Dimension(18, 18));
		searchButton.setContentAreaFilled(false);
		searchButton.setBorderPainted(false);
		return searchButton;
	}

	private Component createNameTextField() {
		nameTextField = new JTextField(20);
		nameTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					lookup(true);
				}
			}
		});
		return nameTextField;
	}

	private void lookup() {
		lookup(false);
	}

	private void lookup(final boolean byPassIfOneResult) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DiverChooserDialog dcd = null;
				Window parentWindow = WindowUtils
						.getTopLevelWindow(DiverChooser.this);
				if (parentWindow instanceof Frame) {
					dcd = new DiverChooserDialog((Frame) parentWindow,
							diverManagerFacade);
				} else if (parentWindow instanceof Dialog) {
					dcd = new DiverChooserDialog((Dialog) parentWindow,
							diverManagerFacade);
				} else {
					dcd = new DiverChooserDialog(diverManagerFacade);
				}

				List<Diver> listOfDivers = new ArrayList<Diver>();
				if (diver != null
						&& diver.getFullName().equals(nameTextField.getText())) {
					listOfDivers.add(diver);
				} else {
					String nameCrit = nameTextField.getText().trim();
					if (nameCrit != null && nameCrit.length() > 0) {
						try {
							listOfDivers = diverManagerFacade
									.findDiversByFirstOrLastName(nameCrit);
						} catch (DataStoreException e) {
							LOGGER.error(e.getMessage());
						}
					}
				}

				boolean showDialog = true;
				if (byPassIfOneResult) {
					if (listOfDivers.size() == 1) {
						showDialog = false;
						setDiverAndNotify(listOfDivers.get(0));
					}
				}

				if (showDialog) {
					dcd.setDivers(listOfDivers);
					dcd.setSearchCriteria(nameTextField.getText());
					int i = dcd.showDialog(500, 400);
					if (i == DiverChooserDialog.OPTION_OK) {
						setDiverAndNotify(dcd.getDiver());
					}
				}
			}

		});
	}

	private void setDiverAndNotify(Diver diver) {
		setDiver(diver);
		firePropertyChange(DIVER_CHANGED_PROPERTY, null, diver);
	}

	public Diver getDiver() {
		return diver;
	}

	public void setEditable(boolean enabled) {
		nameTextField.setEditable(enabled);
		searchButton.setEnabled(enabled);
		resetButton.setEnabled(enabled);
	}

	public void setDiver(Diver diver) {
		this.diver = diver;
		if (diver != null) {
			nameTextField.setText(diver.getFullName());
		} else {
			nameTextField.setText(null);
		}
	}

	public void setDiveManagerFacade(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
	}

	public void setDiverManagerFacade(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
	}

}
