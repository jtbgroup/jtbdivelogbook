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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.component.SearchBox;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.event.DiverEvent;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.core.divesite.MergeDiveSiteDialog;
import be.vds.jtbdive.client.view.events.DiverSelectionListener;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiverUsedException;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class DiverManagerPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 2844909050629167142L;
	private static final Syslog LOGGER = Syslog
			.getLogger(DiverManagerPanel.class);
	private DiverManagerFacade diverManagerFacade;
	private DiverTableModel diverTableModel;
	private Window parentWindow;
	private List<DiverSelectionListener> listeners = new ArrayList<DiverSelectionListener>();
	private JXTable diverTable;
	private Action deleteAction;
	private Action updateAction;
	private SearchBox diverSearchBox;

	public DiverManagerPanel(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		diverManagerFacade.addObserver(this);
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());

		this.add(createControlPanel(), BorderLayout.NORTH);
		this.add(createListPanel(), BorderLayout.CENTER);
	}

	private Component createControlPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 0);

		GridBagLayoutManager.addComponent(panel, createButtonsPanel(), c, 0, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);

		GridBagLayoutManager
				.addComponent(panel, Box.createGlue(), c, 1, 0, 1, 1, 1, 0,
						GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(panel, createSearchPanel(), c, 2, 0,
				1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.EAST);

		return panel;
	}

	private Component createSearchPanel() {
		diverSearchBox = new SearchBox() {

			private static final long serialVersionUID = -3034847826231936451L;

			@Override
			protected String formatSelectedObject(Object object) {
				DiveSite dl = (DiveSite) object;
				return dl.getName();
			}

			@Override
			protected void lookup() {
				lookupDiver();
			}
		};
		diverSearchBox.setButtonIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_MAGNIFYING_GLASS_16));
		diverSearchBox.setButtonText(null);
		int h = (int) diverSearchBox
		.getPreferredSize().getHeight();
		diverSearchBox.setPreferredSize(new Dimension(200, h));
		diverSearchBox.setMinimumSize(new Dimension(50, h));
		return diverSearchBox;
	}

	private Component createListPanel() {
		JPanel listPanel = new JPanel(new BorderLayout());

		diverTableModel = new DiverTableModel();
		diverTable = new JXTable(diverTableModel);
		diverTableModel.setRenderer(diverTable);
		diverTable.addHighlighter(HighlighterFactory.createAlternateStriping());
		diverTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							deleteAction.setEnabled(diverTable
									.getSelectedRowCount() > 0);
							updateAction.setEnabled(diverTable
									.getSelectedRowCount() > 0);
						}
					}
				});
		JScrollPane scroll = new JScrollPane(diverTable);
		listPanel.add(scroll, BorderLayout.CENTER);

		return listPanel;
	}

	private Component createButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		JButton deleteButton = getButton(createDeleteAction(), UIAgent
				.getInstance().getIcon(UIAgent.ICON_DIVER_BLACK_DELETE_16), "delete");
		JButton updateButton = getButton(createUpdateAction(), UIAgent
				.getInstance().getIcon(UIAgent.ICON_DIVER_BLACK_EDIT_16), "update");
		JButton newButton = getButton(createNewAction(), UIAgent.getInstance()
				.getIcon(UIAgent.ICON_DIVER_BLACK_ADD_16), "new");
		JButton mergeButton = getButton(createMergeAction(), UIAgent
				.getInstance().getIcon(UIAgent.ICON_DIVER_BLACK_MERGE_16), "merge");

		buttonsPanel.add(newButton);
		buttonsPanel.add(updateButton);
		buttonsPanel.add(deleteButton);
		buttonsPanel.add(mergeButton);

		return buttonsPanel;
	}

	private Action createMergeAction() {
		Action mergeAction = new AbstractAction() {

			private static final long serialVersionUID = 8611186921543882068L;

			@Override
			public void actionPerformed(ActionEvent e) {
				MergeDiverDialog dlg = new MergeDiverDialog(diverManagerFacade,
						(JFrame) parentWindow);
				WindowUtils.centerWindow(dlg);
				int i = dlg.showDialog(400, 350);
				if (MergeDiveSiteDialog.OPTION_OK == i) {
					Diver keep = dlg.getDiverToKeep();
					Diver delete = dlg.getDiverToDelete();
					try {
						diverManagerFacade.mergeDivers(keep, delete);
					} catch (DataStoreException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (DiverUsedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			}
		};

		return mergeAction;
	}

	private Action createNewAction() {
		Action newAction = new AbstractAction() {

			private static final long serialVersionUID = 7005906269547111524L;

			@Override
			public void actionPerformed(ActionEvent e) {
				DiverEditionDialog dlg = new DiverEditionDialog(null,
						diverManagerFacade, DiverEditionDialog.MODE_SAVE);
				WindowUtils.centerWindow(dlg);
				dlg.setVisible(true);
			}
		};
		return newAction;
	}

	private Action createUpdateAction() {
		updateAction = new AbstractAction() {

			private static final long serialVersionUID = -7004063322812413015L;

			@Override
			public void actionPerformed(ActionEvent e) {
				DiverEditionDialog dlg = new DiverEditionDialog(null,
						diverManagerFacade, DiverEditionDialog.MODE_EDIT);
				dlg.setValue(getSelectedDivers().get(0));
				WindowUtils.centerWindow(dlg);
				dlg.setVisible(true);
			}
		};
		updateAction.setEnabled(false);
		return updateAction;
	}

	private Action createDeleteAction() {
		deleteAction = new AbstractAction() {

			private static final long serialVersionUID = -897189991342055494L;

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Diver diver : getSelectedDivers()) {
					try {
						diverManagerFacade.deleteDiver(diver);
					} catch (DataStoreException ex) {
						LOGGER.error(ex.getMessage());
						ExceptionDialog.showDialog(ex, parentWindow);
					} catch (DiverUsedException ex) {
						LOGGER.warn(ex.getMessage());
						JOptionPane.showMessageDialog(parentWindow, ex
								.getMessage(), I18nResourceManager
								.sharedInstance().getString("warning"),
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
		deleteAction.setEnabled(false);
		return deleteAction;
	}

	private List<Diver> getSelectedDivers() {
		List<Diver> dls = new ArrayList<Diver>();
		int[] rows = diverTable.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			int row = diverTable.convertRowIndexToModel(rows[i]);
			dls.add(diverTableModel.getDiverAt(row));
		}
		return dls;
	}

	private void lookupDiver() {
		try {
			String firstNameText = diverSearchBox.getSearchText().trim();
			if (firstNameText.length() > 0) {
				List<Diver> dls = diverManagerFacade
						.findDiversByFirstOrLastName(firstNameText);
				setSearchResult(dls);
			}
		} catch (DataStoreException e) {
			LOGGER.error("Problem accessing the data : " + e.getMessage());
			JOptionPane.showMessageDialog(parentWindow,
					"Problem accessing the data : " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void setSearchResult(List<Diver> divers) {
		diverTableModel.setData(divers);
	}

	public void addDiverSelectionListener(
			DiverSelectionListener diverSelectionListener) {
		listeners.add(diverSelectionListener);
	}

	@Override
	public void update(Observable o, Object arg) {
		DiverEvent event = (DiverEvent) arg;
		if (event.getEventType() == DiverEvent.DELETE) {
			diverTableModel.removeDiver(event.getOldValue());
		} else if (event.getEventType() == DiverEvent.SAVE) {
			diverTableModel.replaceOrAddDiver(event.getNewValue());
		} else if (event.getEventType() == DiverEvent.UPDATE) {
			diverTableModel.replaceOrAddDiver(event.getNewValue());
		} else if (event.getEventType() == DiverEvent.MERGE) {
			diverTableModel.removeDiver(event.getOldValue());
			diverTableModel.replaceOrAddDiver(event.getNewValue());
		}
	}

	@Override
	public String toString() {
		return "Diver Manager panel";
	}

	private JButton getButton(Action action, Icon image, String tooltip) {
		JButton button = new I18nButton(action);
		button.setIcon(image);
		button.setToolTipText(tooltip);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusable(false);

		button.setMinimumSize(UIAgent.DIMENSION_20_20);
		button.setPreferredSize(UIAgent.DIMENSION_20_20);

		return button;
	}

	@Override
	public void updateUI() {
		super.updateUI();
		if (diverTable != null) {
			repaintTableHeaders();
			diverTableModel.fireTableDataChanged();
		}
	}

	private void repaintTableHeaders() {
		for (int i = 0; i < diverTable.getColumnCount(); i++) {
			diverTable.getColumn(i).setHeaderValue(
					diverTableModel.getColumnName(i));
		}
	}
}
