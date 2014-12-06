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
package be.vds.jtbdive.client.view.core.logbook.material;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.core.diver.MergeDiverDialog;
import be.vds.jtbdive.client.view.core.logbook.material.edit.EditMaterialDialog;
import be.vds.jtbdive.client.view.utils.MaterialHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.material.Material;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class MaterialPanel extends JPanel implements Observer {
	private static final long serialVersionUID = -6632838013712978564L;
	private static final Syslog LOGGER = Syslog.getLogger(MaterialPanel.class);
	private static final String CARD_SUMMARY = "summary";
	private static final String CARD_DEFAULT = "default";
	private static final String CARD_MATERIAL = "material";
	private LogBookManagerFacade logBookManagerFacade;
	private I18nButton deleteMaterialButton;
	private MaterialTreePanel materialTreePanel;
	private I18nButton mergeMaterialButton;
	private I18nButton editMaterialButton;
	private I18nButton addMaterialButton;
	private CardLayout materialCardLayout;
	private JPanel materialsPanel;
	private MaterialDetailPanel materialDetail;
	private MaterialSummaryPanel summaryPanel;

	public MaterialPanel(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		this.logBookManagerFacade.addObserver(this);
		inti();
	}

	private void inti() {
		setLayout(new BorderLayout());
		setOpaque(false);
		add(createButtonsPanel(), BorderLayout.NORTH);
		add(createCentralPanel(), BorderLayout.CENTER);
	}

	private Component createCentralPanel() {
		JSplitPane split = new JSplitPane();
		split.setOpaque(false);
		split.setDividerLocation(250);
		split.setLeftComponent(createMaterialTreePanel());
		split.setRightComponent(createMaterialScrollPanel());
		return split;
	}

	private JComponent createMaterialScrollPanel() {
		materialCardLayout = new CardLayout();
		materialsPanel = new DetailPanel();
		materialsPanel.setOpaque(false);
		materialsPanel.setLayout(materialCardLayout);

		createCardsPanel();

		return materialsPanel;
	}

	private void createCardsPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		materialsPanel.add(p, CARD_DEFAULT);

		summaryPanel = new MaterialSummaryPanel();
		materialsPanel.add(summaryPanel, CARD_SUMMARY);

		materialDetail = new MaterialDetailPanel();
		materialsPanel.add(materialDetail, CARD_MATERIAL);
	}

	public Component createMaterialTreePanel() {
		materialTreePanel = new MaterialTreePanel();
		materialTreePanel
				.addTreeSelectionListener(createTreeSelectionListener());
		return materialTreePanel;
	}

	private TreeSelectionListener createTreeSelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (e.getNewLeadSelectionPath() != null) {
					List<Material> mats = materialTreePanel
							.getSelectedMaterials();
					if (mats.size() == 1) {
						displayMaterial(mats.get(0),
								materialTreePanel.hasOnlyMaterialsSelected());
					} else {
						displayMaterialList(mats);
					}

				}
			}

		};
	}

	private void displayMaterialList(List<Material> materials) {
		summaryPanel.setMaterials(materials);
		materialCardLayout.show(materialsPanel, CARD_SUMMARY);
		editMaterialButton.setEnabled(false);
		deleteMaterialButton.setEnabled(true);
	}

	private Component createButtonsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setOpaque(false);
		panel.add(createAddMaterialButton());
		panel.add(createEditMaterialButton());
		panel.add(createDeleteMaterialButton());
		panel.add(createMergeMaterialsButton());

		return panel;
	}

	private Component createMergeMaterialsButton() {
		mergeMaterialButton = new I18nButton(new AbstractAction(null, UIAgent
				.getInstance().getIcon(UIAgent.ICON_MERGE_16)) {

			private static final long serialVersionUID = 5560375004201332540L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame f = WindowUtils.getParentFrame(MaterialPanel.this);
				MergeMaterialDialog dlg = new MergeMaterialDialog(f);
				dlg.setLogBookManagerFacade(logBookManagerFacade);
				int i = dlg.showDialog(300, 250);
				if (i == MergeDiverDialog.OPTION_OK) {
					Material mtk = dlg.getMaterialToKeep();
					Material mtd = dlg.getMaterialToDelete();
					try {
						logBookManagerFacade.mergeMaterial(mtk, mtd);
					} catch (DataStoreException e1) {
						LOGGER.error(e1);
						ExceptionDialog.showDialog(e1, null);
					}
				}
			}
		});
		mergeMaterialButton.setTooltipTextBundleKey("merge");
		mergeMaterialButton.setEnabled(false);
		return mergeMaterialButton;
	}

	private Component createDeleteMaterialButton() {
		deleteMaterialButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = 5560375004201332540L;

			@Override
			public void actionPerformed(ActionEvent e) {
				List<Material> mat = materialTreePanel.getSelectedMaterials();
				if (null != mat) {
					StringBuilder message = buildConfirmationMessage(mat);
					int i = JOptionPane.showConfirmDialog(
							WindowUtils.getParentFrame(MaterialPanel.this),
							message,
							I18nResourceManager.sharedInstance().getString(
									"confirmation"), JOptionPane.YES_NO_OPTION);
					if (i == JOptionPane.YES_OPTION) {
						try {
							logBookManagerFacade.deleteMaterials(mat);
						} catch (DataStoreException e1) {
							LOGGER.error(e1);
							ExceptionDialog.showDialog(e1, null);
						}
					}
				}
			}

			private StringBuilder buildConfirmationMessage(
					List<Material> materials) {
				StringBuilder message = new StringBuilder();
				message.append(I18nResourceManager.sharedInstance().getString(
						"items.about.to.delete"));
				message.append("\r\n");

				for (Material material : materials) {
					message.append("\t- ")
							.append(material.getShortDescription())
							.append("\r\n");
				}

				message.append(I18nResourceManager.sharedInstance().getString(
						"continue.confirm.message"));
				return message;
			}
		});
		deleteMaterialButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_CANCEL_16));
		deleteMaterialButton.setTooltipTextBundleKey("delete");
		deleteMaterialButton.setEnabled(false);
		return deleteMaterialButton;
	}

	private Component createEditMaterialButton() {
		editMaterialButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = 6277926836449312023L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Material mat = materialTreePanel.getSelectedMaterial();
				editMaterial(mat);
			}
		});
		editMaterialButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_EDIT_16));
		editMaterialButton.setTooltipTextBundleKey("edit");
		editMaterialButton.setEnabled(false);
		return editMaterialButton;
	}

	private Component createAddMaterialButton() {
		addMaterialButton = new I18nButton(new AbstractAction() {

			private static final long serialVersionUID = -1355092302919058869L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame f = WindowUtils.getParentFrame(MaterialPanel.this);
				MaterialChooserDialog dlg = new MaterialChooserDialog(f);
				int i = dlg.showDialog(300, 300);
				if (i == MaterialChooserDialog.OPTION_OK) {
					Material mat = MaterialHelper
							.createMaterialForMaterialType(dlg
									.getMaterialType());
					editMaterial(mat);
				}
			}
		});
		addMaterialButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_ADD_16));
		addMaterialButton.setTooltipTextBundleKey("add");
		addMaterialButton.setEnabled(false);
		return addMaterialButton;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.MATERIAL_SAVED)) {
				if (event.getOldValue() == null) {
					addMaterial((Material) event.getNewValue(), true);
				} else {
					replaceMaterial((Material) event.getOldValue(),
							(Material) event.getNewValue());
				}
			} else if (event.getType().equals(LogBookEvent.MATERIAL_DELETED)) {
				removeMaterial((Material) event.getOldValue());
			} else if (event.getType().equals(LogBookEvent.MATERIALS_DELETED)) {
				removeMaterials((List<Material>) event.getOldValue());
			} else if (event.getType().equals(LogBookEvent.MATERIAL_MERGED)) {
				removeMaterial((Material) event.getOldValue());
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED)) {
				synchronizeWithLogBook();
				mergeMaterialButton.setEnabled(true);
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)) {
				clear();
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_DELETED)) {
				if (logBookManagerFacade.getCurrentLogBook() == null) {
					clear();
				}
			}
		}
	}

	public void reloadCurrentMaterial() {
		Material material = materialTreePanel.getSelectedMaterial();
		if (material != null) {
			displayMaterial(material,
					materialTreePanel.hasOnlyMaterialsSelected());
		}
	}

	private void displayMaterial(Material material, boolean canBeEdited) {
		materialDetail.setMaterial(material);
		materialCardLayout.show(materialsPanel, CARD_MATERIAL);

		editMaterialButton.setEnabled(canBeEdited);
		deleteMaterialButton.setEnabled(true);
	}

	private void editMaterial(Material mat) {
		EditMaterialDialog editDlg = new EditMaterialDialog(mat);
		int j = editDlg.showDialog(500, 450);
		if (j == EditMaterialDialog.OPTION_OK) {
			Material material = editDlg.getEditedMaterial();
			try {
				logBookManagerFacade.saveMaterial(material);
			} catch (DataStoreException e1) {
				LOGGER.error(e1);
				ExceptionDialog.showDialog(e1, MaterialPanel.this);
			}
		}
	}

	private void addMaterial(Material material, boolean scrollPathToVisible) {
		materialTreePanel.addMaterial(material, scrollPathToVisible);
	}

	private void removeMaterial(Material material) {
		materialTreePanel.removeMaterial(material);
		displayDefault();
	}

	private void removeMaterials(List<Material> materials) {
		for (Material material : materials) {
			materialTreePanel.removeMaterial(material);
		}
		displayDefault();
	}

	private void displayDefault() {
		materialCardLayout.show(materialsPanel, CARD_DEFAULT);
	}

	private void replaceMaterial(Material oldValue, Material newValue) {
		materialTreePanel.replaceMaterial(oldValue, newValue);
		displayMaterial(newValue, materialTreePanel.hasOnlyMaterialsSelected());
	}

	public void synchronizeWithLogBook() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				clear();
				if (null != logBookManagerFacade.getCurrentLogBook()) {
					loadMatCave(logBookManagerFacade.getCurrentLogBook()
							.getMatCave());
					addMaterialButton.setEnabled(true);
				}
			}
		});
	}

	private void clear() {
		materialTreePanel.clear();
		removeMaterialCardPanels();
		addMaterialButton.setEnabled(false);
		deleteMaterialButton.setEnabled(false);
		editMaterialButton.setEnabled(false);
		mergeMaterialButton.setEnabled(false);
	}

	private void removeMaterialCardPanels() {
		materialsPanel.removeAll();
		createCardsPanel();
		materialCardLayout.show(materialsPanel, CARD_DEFAULT);
	}

	private void loadMatCave(MatCave matCave) {
		for (Material material : matCave.getAllMaterials(true)) {
			addMaterial(material, false);
		}

	}
}
