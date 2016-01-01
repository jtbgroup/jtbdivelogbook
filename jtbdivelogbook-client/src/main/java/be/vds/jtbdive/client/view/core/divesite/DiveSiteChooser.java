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

import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class DiveSiteChooser extends JPanel {

	private static final long serialVersionUID = -5744779590962263755L;
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteChooser.class);
	private static final Dimension BUTTONS_DIM = new Dimension(20, 20);
	public static final String DIVESITE_SITE_PROPERTY = "divesite.changed";
	private DiveSite diveSite;
	private JTextField nameTf;
	private DiveSiteManagerFacade diveSiteManagerFacade;
	private JButton searchButton;
	private JButton resetButton;

	public DiveSiteChooser(DiveSiteManagerFacade diveLocationManagerFacade) {
		this.diveSiteManagerFacade = diveLocationManagerFacade;
		init();
	}

	private void init() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayoutManager.addComponent(this, createNameTf(), c, 0, 0, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createSearchButton(), c, 1, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createResetButton(), c, 2, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
		
		super.setOpaque(false);
	}

	private Component createNameTf() {
		nameTf = new JTextField(10);
		nameTf.setEditable(false);
		nameTf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					lookup(true);
				}
			}
		});
		return nameTf;
	}

	private Component createSearchButton() {
		Action action = new AbstractAction() {
			private static final long serialVersionUID = -4963261725727371705L;

			@Override
			public void actionPerformed(ActionEvent e) {
				lookup(false);
			}

		};
		action.putValue(Action.LARGE_ICON_KEY, UIAgent.getInstance()
				.getIcon(UIAgent.ICON_SEARCH_16));
		searchButton = new JButton(action);
		searchButton.setPreferredSize(BUTTONS_DIM);
		searchButton.setContentAreaFilled(false);

		return searchButton;
	}

	private Component createResetButton() {
		Action resetAction = new AbstractAction() {
			private static final long serialVersionUID = 4483675063779611365L;

			@Override
			public void actionPerformed(ActionEvent e) {
				setDiveSiteAndNotify(null);
			}
		};
		resetAction.putValue(Action.LARGE_ICON_KEY, UIAgent.getInstance()
				.getIcon(UIAgent.ICON_CANCEL_16));
		resetButton = new JButton(resetAction);
		resetButton.setPreferredSize(BUTTONS_DIM);
		resetButton.setContentAreaFilled(false);
		return resetButton;
	}

	private void lookup(final boolean byPassIfOneResult) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Window parentWindow = WindowUtils.getTopLevelWindow(DiveSiteChooser.this);
				DiveSiteChooserDialog dcd = null;
				if (parentWindow instanceof Frame) {
					dcd = new DiveSiteChooserDialog((Frame) parentWindow,
							diveSiteManagerFacade);
				}else if (parentWindow instanceof Dialog) {
					dcd = new DiveSiteChooserDialog((Dialog) parentWindow,
							diveSiteManagerFacade);
				} else {
					dcd = new DiveSiteChooserDialog(diveSiteManagerFacade);
				}

				List<DiveSite> listOfDiveSites = new ArrayList<DiveSite>();
				if (diveSite != null && diveSite.getName().equals(nameTf.getText())) {
					listOfDiveSites.add(diveSite);
				} else {
					String nameCrit = nameTf.getText().trim();
					if (nameCrit != null && nameCrit.length() > 0) {
						try {
							listOfDiveSites = diveSiteManagerFacade
									.findDiveSitesByName(nameCrit);
						} catch (DataStoreException e) {
							LOGGER.error(e.getMessage());
						}
					}
				}

				boolean showDialog = true;
				if (byPassIfOneResult) {
					if (listOfDiveSites.size() == 1) {
						showDialog = false;
						setDiveSiteAndNotify(listOfDiveSites.get(0));
					}
				}

				if (showDialog) {
					dcd.setDiveSites(listOfDiveSites);
					dcd.setSearchCriteria(nameTf.getText());
					int i = dcd.showDialog(500, 400);
					if (i == DiveSiteChooserDialog.OPTION_OK) {
						setDiveSiteAndNotify(dcd.getSelectedDiveSite());
					}
				}
			}

		});
	}
	
	public DiveSite getDiveSite() {
		return diveSite;
	}

	public void setDiveSite(DiveSite diveSite) {
		this.diveSite = diveSite;
		if (null != diveSite) {
			nameTf.setText(diveSite.getName());
			nameTf.setCaretPosition(0);
		} else {
			nameTf.setText(null);
		}
	}


	public void setEditable(boolean enabled) {
		nameTf.setEditable(enabled);
		searchButton.setEnabled(enabled);
		resetButton.setEnabled(enabled);
	}

	private void setDiveSiteAndNotify(DiveSite diveSite) {
		setDiveSite(diveSite);
		firePropertyChange(DIVESITE_SITE_PROPERTY, null, diveSite);
	}
	
	public void setDiveSiteManagerFacade(
			DiveSiteManagerFacade diveSiteManagerFacade) {
		this.diveSiteManagerFacade = diveSiteManagerFacade;
	}

}
