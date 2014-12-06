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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.SwingComponentHelper;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class DiverChooserDialog extends PromptDialog {
	private static final long serialVersionUID = 8408622745689456572L;

	private static final Syslog LOGGER = Syslog
			.getLogger(DiverChooserDialog.class);

	private DiverManagerFacade diverManagerFacade;
	private JTextField nameTf;
	private DiverTableModel diverTableModel;
	private JXTable diversTable;

	public DiverChooserDialog(DiverManagerFacade diverManagerFacade) {
		super(i18n.getString("diver.selection"), i18n
				.getString("diver.selection.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVER_BLACK_DETAIL_48),
				PromptDialog.MODE_OK_CANCEL, null);
		initValues(diverManagerFacade);
	}

	public DiverChooserDialog(Frame parentWindow,
			DiverManagerFacade diverManagerFacade) {
		super(parentWindow, i18n.getString("diver.selection"), i18n
				.getString("diver.selection.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVER_BLACK_DETAIL_48),
				PromptDialog.MODE_OK_CANCEL, null);
		initValues(diverManagerFacade);
	}

	public DiverChooserDialog(Dialog parentWindow,
			DiverManagerFacade diverManagerFacade) {
		super(parentWindow, i18n.getString("diver.selection"), i18n
				.getString("diver.selection.message"), UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_DIVER_BLACK_DETAIL_48),
				PromptDialog.MODE_OK_CANCEL, null);
		initValues(diverManagerFacade);
	}

	private void initValues(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		setOkButtonEnabled(false);
		this.setIconImage(UIAgent.getInstance().getBufferedImage(
				UIAgent.ICON_DIVER_BLACK_DETAIL_16));
	}

	private Component createDiverSelection() {
		JPanel selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setOpaque(false);
		selectionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayoutManager.addComponent(selectionPanel,
				createFirstNameComp(), c, 1, 0, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(selectionPanel, createSearchComp(),
				c, 2, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);

		return selectionPanel;
	}

	private Component createFirstNameComp() {
		nameTf = new JTextField(15);
		nameTf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					lookup();
				}
			}
		});
		return nameTf;
	}

	private Component createSearchComp() {
		Action action = new AbstractAction() {
			private static final long serialVersionUID = -4631378779032584897L;

			@Override
			public void actionPerformed(ActionEvent e) {
				lookup();
			}
		};
		action.putValue(Action.LARGE_ICON_KEY,
				UIAgent.getInstance().getIcon(UIAgent.ICON_SEARCH_16));
		JButton searchButton = new JButton(action);
		searchButton.setContentAreaFilled(false);
		return searchButton;
	}

	private Component createDiversTable() {
		diverTableModel = new DiverTableModel();
		diversTable = new JXTable(diverTableModel);
		diverTableModel.setRenderer(diversTable);
		diversTable.setOpaque(false);
		diversTable
				.addHighlighter(HighlighterFactory.createAlternateStriping());
		diversTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		diversTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()
								&& diversTable.getSelectedRow() > -1) {
							setOkButtonEnabled(true);
						}
					}
				});
		diversTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					dispose();
					setReturnOption(OPTION_OK);
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(diversTable);
		scrollPane.setMinimumSize(new Dimension(200, 100));
		SwingComponentHelper.displayJScrollPane(scrollPane);

		JPanel p = new DetailPanel(new BorderLayout());
		p.add(scrollPane, BorderLayout.CENTER);
		return p;
	}

	public Diver getDiver() {
		int selectedRow = diversTable.getSelectedRow();
		int realRow = diversTable.convertRowIndexToModel(selectedRow);
		return diverTableModel.getDiverAt(realRow);
	}

	private void lookup() {
		try {
			String name = nameTf.getText().trim();
			if (null != name && name.length() > 0) {
				List<Diver> l = diverManagerFacade
						.findDiversByFirstOrLastName(name);
				diverTableModel.setData(l);
			} else {
				diverTableModel.setData(null);
			}
		} catch (DataStoreException e) {
			LOGGER.error("Problem accessing the data : " + e.getMessage());
			JOptionPane.showMessageDialog(this, "Problem accessing the data : "
					+ e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setDivers(List<Diver> l) {
		diverTableModel.setData(l);
	}

	public void setSearchCriteria(String search) {
		nameTf.setText(search);
	}

	@Override
	protected Component createContentPanel() {
		JPanel p = new DetailPanel(new BorderLayout());
		// p.setOpaque(false);
		p.add(createDiverSelection(), BorderLayout.NORTH);
		p.add(createDiversTable(), BorderLayout.CENTER);
		return p;
	}

}
