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
package be.vds.jtbdive.client.view.core.logbook.materialset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.comparator.MaterialSetComparator;
import be.vds.jtbdive.core.core.material.MaterialSet;
import be.vds.jtbdive.core.exceptions.DataStoreException;

public class MaterialSetPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 5774101360125198388L;
	private LogBookManagerFacade logBookManagerFacade;
	private MaterialSetTreePanel materialSetTreePanel;
	private I18nButton addButton;
	private MaterialSetContainerPanel materialSetContainerPanel;
	private I18nButton deleteButton;
	private I18nButton editButton;

	public MaterialSetPanel(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createButtonsPanel(), BorderLayout.NORTH);
		this.add(createCentralPanel(), BorderLayout.CENTER);
		this.logBookManagerFacade.addObserver(this);
	}

	private Component createButtonsPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setOpaque(false);
		p.add(createAddMaterialSetButton());
		p.add(createEditMaterialSetButton());
		p.add(createDeleteMaterialSetButton());
		return p;
	}

	private Component createEditMaterialSetButton() {
		editButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = 8007006025256277476L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doEditMaterialSet(materialSetTreePanel.getSelectedMaterialSet());
			}

		});
		editButton.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_EDIT_16));
		editButton.setTooltipTextBundleKey("edit");
		editButton.setEnabled(false);
		return editButton;
	}

	private void doEditMaterialSet(MaterialSet materialSet) {
		MaterialSetDialog dlg = null;
		JFrame parentFrame = WindowUtils.getParentFrame(this);
		if (null == materialSet) {
			dlg = MaterialSetDialog.createNewMaterialSetDialog(parentFrame);
		} else {
			dlg = MaterialSetDialog.createEditMaterialSetDialog(parentFrame);
			dlg.setMaterialSet(materialSet);
		}
		dlg.setLogBookManagerFacade(logBookManagerFacade);
		
		int i = dlg.showDialog();
		if (i == MaterialSetDialog.OPTION_YES) {
			try {
				logBookManagerFacade.saveMaterialSet(dlg
						.getDisplayedMaterialSet());
			} catch (DataStoreException e) {
				ExceptionDialog.showDialog(e.getMessage(), this);
			}
		}
	}

	private Component createDeleteMaterialSetButton() {
		deleteButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = 1825074051166578970L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doDeleteMaterialSet();
			}
		});
		deleteButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_CANCEL_16));
		deleteButton.setTooltipTextBundleKey("delete");
		deleteButton.setEnabled(false);
		return deleteButton;
	}

	private Component createAddMaterialSetButton() {
		addButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = -8054102902444829658L;

			@Override
			public void actionPerformed(ActionEvent e) {
				doEditMaterialSet(null);
			}
		});
		addButton.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_ADD_16));
		addButton.setTooltipTextBundleKey("add");
		addButton.setEnabled(false);
		return addButton;
	}

	private Component createMaterialSetContentPanel() {
		materialSetContainerPanel = new MaterialSetContainerPanel(
				logBookManagerFacade);
		return materialSetContainerPanel;
	}

	private Component createMaterialSetTreePanel() {
		materialSetTreePanel = new MaterialSetTreePanel();
		materialSetTreePanel
				.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		materialSetTreePanel
				.addTreeSelectionListener(new TreeSelectionListener() {

					@Override
					public void valueChanged(TreeSelectionEvent e) {
						MaterialSet ms = materialSetTreePanel
								.getSelectedMaterialSet();
						deleteButton.setEnabled(ms != null);
						editButton.setEnabled(ms != null);
						displayMaterialSet(ms);
					}
				});

		materialSetTreePanel.addTreeKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					doDeleteMaterialSet();
				}
			}

		});
		return materialSetTreePanel;
	}

	private void doDeleteMaterialSet() {
		MaterialSet materialSet = materialSetTreePanel.getSelectedMaterialSet();
		int i = JOptionPane.showConfirmDialog(MaterialSetPanel.this,
				I18nResourceManager.sharedInstance()
						.getString("delete.confirm"));
		if (i == JOptionPane.YES_OPTION) {
			deleteMaterialSet(materialSet);
		}
	}

	private void displayMaterialSet(MaterialSet materialSet) {
		materialSetContainerPanel.setMaterialSet(materialSet);
	}

	private Component createCentralPanel() {
		JSplitPane split = new JSplitPane();
		split.setLeftComponent(createMaterialSetTreePanel());
		split.setRightComponent(createMaterialSetContentPanel());
		split.setDividerLocation(200);
		split.setOpaque(false);
		return split;

	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(logBookManagerFacade) && arg instanceof LogBookEvent) {
			LogBookEvent evt = (LogBookEvent) arg;
			if (evt.getType().equals(LogBookEvent.MATERIALSET_SAVED)) {
				MaterialSet newMatSet = (MaterialSet) evt.getNewValue();
				addMaterialSet(newMatSet, true);
				synchAddButton();
			} else if (evt.getType().equals(LogBookEvent.MATERIALSET_DELETED)) {
				MaterialSet oldMat = (MaterialSet) evt.getOldValue();
				doAfterDelete(oldMat);
			} else if (evt.getType().equals(LogBookEvent.LOGBOOK_LOADED)) {
				clear();
				loadAllMaterial();
				synchAddButton();
			} else if (evt.getType().equals(LogBookEvent.LOGBOOK_DELETED)
					|| evt.getType().equals(LogBookEvent.LOGBOOK_CLOSED)) {
				clear();
				synchAddButton();
			}
		}
	}

	private void doAfterDelete(MaterialSet materialSet) {
		removeMaterialSet(materialSet);
		materialSetContainerPanel.clear();
	}

	private void removeMaterialSet(MaterialSet materialSet) {
		materialSetTreePanel.removeMaterialSet(materialSet);
	}

	private void synchAddButton() {
		LogBook lb = logBookManagerFacade.getCurrentLogBook();
		boolean b = false;
		if (lb != null && lb.getMatCave() != null) {
			b = lb.getMatCave().getAllMaterials().size() > 0;
		}

		addButton.setEnabled(b);
	}

	private void loadAllMaterial() {
		MatCave mc = logBookManagerFacade.getCurrentMatCave();

		Set<MaterialSet> oSets = mc.getMaterialSets();
		if (null != oSets) {
			List<MaterialSet> sets = new ArrayList<MaterialSet>(oSets);
			Collections.sort(sets, new MaterialSetComparator());
			for (MaterialSet ms : sets) {
				addMaterialSet(ms, false);
			}
		}
	}

	private void addMaterialSet(MaterialSet materialSet, boolean scrollToVisible) {
		materialSetTreePanel.addMaterialSet(materialSet, scrollToVisible);
		if (scrollToVisible) {
			materialSetContainerPanel.setMaterialSet(materialSet);
		}
	}

	public void clear() {
		materialSetTreePanel.clear();
		materialSetContainerPanel.clear();
	}

	public void synchronizeWithLogBook() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				clear();
				if (null != logBookManagerFacade.getCurrentLogBook()) {
					loadAllMaterial();
					addButton.setEnabled(true);
				}
			}
		});
	}

	private void deleteMaterialSet(MaterialSet materialSet) {
		try {
			logBookManagerFacade.removeMaterialSet(materialSet);
		} catch (DataStoreException e) {
			ExceptionDialog.showDialog(e.getMessage(), this);
		}
	}

}
