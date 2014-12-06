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
package be.vds.jtbdive.client.view.core.download;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXTable;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.smd.i18n.swing.I18nCheckBox;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.component.FileSelector;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.DataCommComponent;
import be.vds.jtbdive.client.view.table.DiveTableModel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.core.catalogs.DiveComputerType;
import be.vds.jtbdive.core.core.divecomputer.parser.DiveComputerDataParser;
import be.vds.jtbdive.core.core.divecomputer.parser.UwatecDiveComputerDataParser;
import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.interfaces.DataCommInterface;
import be.vds.jtbdive.core.logging.Syslog;

public class DiveComputerDownloadPanel extends JPanel {

	private static final long serialVersionUID = 9153183764439111486L;
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveComputerDownloadPanel.class);
	private JComboBox computerTypeCb;
	private DiveTableModel diveTableModel;
	private JXTable diveTable;
	private I18nCheckBox saveBinaryCheckBox;
	private FileSelector binaryFileSelection;
	private DiveComputerDataParser parser;
	private I18nCheckBox autoNumberCheckBox;
	private I18nCheckBox autoAddOwnerInPalanqueeCheckBox;
	private I18nButton downloadButton;
	private MatCave matCave;
	private DataCommComponent dataCommComponent;

	public DiveComputerDownloadPanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		GridBagLayoutManager.addComponent(this, createConnectionPanel(), c, 0,
				0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createDiveTablePanel(), c, 0,
				1, 1, 1, 1, 1, GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createOptionsPanel(), c, 0, 2,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);
	}

	private Component createDiveComputerTypeCb() {
		computerTypeCb = new JComboBox(DiveComputerType.values());
		computerTypeCb.setSelectedItem(UserPreferences.getInstance()
				.getPreferredDiveComputer());

		computerTypeCb.setRenderer(new ListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) new DefaultListCellRenderer()
						.getListCellRendererComponent(list, value, index,
								isSelected, cellHasFocus);
				if (null == value) {
					renderer.setText(null);
				} else {
					renderer.setText(i18n.getString(((DiveComputerType) value)
							.getKey()));
				}
				return renderer;
			}
		});

		computerTypeCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				evaluateDownloadButton();
			}
		});

		return computerTypeCb;
	}

	private Component createConnectionPanel() {
		JPanel connectionPanel = new DetailPanel();

		connectionPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		GridBagLayoutManager.addComponent(connectionPanel,
				createDiveComputerLabel(), c, 0, 0, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.PAGE_START);
		GridBagLayoutManager.addComponent(connectionPanel,
				createDiveComputerTypeCb(), c, 1, 0, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(connectionPanel,
				createConnectionTypeLabel(), c, 0, 1, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.PAGE_START);
		GridBagLayoutManager.addComponent(connectionPanel,
				createDataComPanel(), c, 1, 1, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		return connectionPanel;
	}

	private Component createDataComPanel() {
		dataCommComponent = new DataCommComponent();
		dataCommComponent.setBorder(new LineBorder(UIAgent.getInstance()
				.getColorPanelUnselected()));
		dataCommComponent.setDatacomInterface(UserPreferences.getInstance()
				.getPreferredDiveComputerDataCommInterface());

		dataCommComponent
				.addPropertyChangeListener(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (DataCommComponent.PROPERTY_INTERFACE_CHANGED
								.equals(evt.getPropertyName())) {
							evaluateDownloadButton();
						}
					}
				});

		return dataCommComponent;
	}

	private Component createDiveComputerLabel() {
		return new I18nLabel("dive.computer");
	}

	private Component createConnectionTypeLabel() {
		return new I18nLabel("connection");
	}

	private Component createDiveTablePanel() {
		JPanel panel = new DetailPanel();
		panel.setLayout(new BorderLayout());

		diveTableModel = new DiveTableModel();
		diveTable = new JXTable(diveTableModel);
		diveTable.setColumnControlVisible(false);
		diveTable.getColumnExt(diveTableModel.getColumnName(DiveTableModel.INDEX_NUMBER)).setVisible(false);
		diveTable.getColumnExt(diveTableModel.getColumnName(DiveTableModel.INDEX_DIVE_SITE)).setVisible(false);
		diveTable.setOpaque(false);
		JScrollPane scroll = new JScrollPane(diveTable);
		scroll.setPreferredSize(new Dimension(500, 200));
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		panel.add(scroll, BorderLayout.CENTER);
		panel.add(createLoadDataButton(), BorderLayout.EAST);

		return panel;
	}

	private Component createLoadDataButton() {
		downloadButton = new I18nButton(new AbstractAction("download") {

			private static final long serialVersionUID = -7660480847840248726L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						downloadButton.setEnabled(false);
						DataCommInterface dcInterface = dataCommComponent
								.getDataCommInterface();
						parser = getParser();
						parser.setDataComInterface(dcInterface);
						List<Dive> dives;
						try {
							dives = parser.read(matCave);
							diveTableModel.setData(dives);
						} catch (TransferException e) {
							LOGGER.error(e);
							JOptionPane
									.showMessageDialog(
											null,
											"Transfer excpetion\r\n"
													+ e.getMessage(),
											"Transfer Error",
											JOptionPane.ERROR_MESSAGE);
						} finally {
							parser.close();
							downloadButton.setEnabled(true);
						}

					}
				}).start();
			}
		});

		downloadButton.setEnabled(false);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.add(downloadButton, BorderLayout.NORTH);
		return panel;
	}

	private void evaluateDownloadButton() {
		boolean b = dataCommComponent.getDataCommInterface() != null;
		boolean b2 = computerTypeCb.getSelectedIndex() > -1;
		downloadButton.setEnabled(b && b2);
	}

	private Component createOptionsPanel() {
		JPanel panel = new DetailPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayoutManager.addComponent(panel, createSaveBinariesComponent(),
				c, 0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START);

		GridBagLayoutManager.addComponent(panel, createAutonumberOption(), c,
				0, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START);

		GridBagLayoutManager.addComponent(panel,
				createAutoAddOwnerInPalanquee(), c, 0, 2, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_START);
		return panel;
	}

	private Component createAutoAddOwnerInPalanquee() {
		autoAddOwnerInPalanqueeCheckBox = new I18nCheckBox("auto.add.owner");
		return autoAddOwnerInPalanqueeCheckBox;
	}

	private Component createAutonumberOption() {
		autoNumberCheckBox = new I18nCheckBox("autonumber.dive");
		return autoNumberCheckBox;
	}

	private Component createSaveBinariesComponent() {
		saveBinaryCheckBox = new I18nCheckBox(new AbstractAction(
				"save.binaries") {
			private static final long serialVersionUID = -5921970693951999676L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				binaryFileSelection.setEnabled(saveBinaryCheckBox.isSelected());
			}
		});

		binaryFileSelection = new FileSelector();
		binaryFileSelection.setShowHiddenFiles(true);
		binaryFileSelection.setButtonText(null);
		binaryFileSelection.setButtonIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_FOLDER_OPEN_16));
		binaryFileSelection.setButtonBorderPainted(false);
		binaryFileSelection.setButtonContentAreaFilled(false);
		binaryFileSelection.setEnabled(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(saveBinaryCheckBox, BorderLayout.NORTH);
		panel.add(binaryFileSelection, BorderLayout.CENTER);

		return panel;
	}

	public Map<String, Object> getOptions() {
		Map<String, Object> options = new HashMap<String, Object>();

		options.put(DownloadConstants.OPTION_SAVE_BINARY_FILE,
				saveBinaryCheckBox.isSelected());
		if (saveBinaryCheckBox.isSelected()) {
			options.put(DownloadConstants.OPTION_BINARY_FILE,
					binaryFileSelection.getSelectedFile());
			options.put(DownloadConstants.OPTION_COMPUTER_DATA_PARSER_CLASS,
					parser.getClass());
			options.put(DownloadConstants.OPTION_BINARY_DATA,
					parser.getBinaries());
		}

		options.put(DownloadConstants.OPTION_AUTONUMBER_DIVE,
				autoNumberCheckBox.isSelected());

		options.put(DownloadConstants.OPTION_AUTO_ADD_OWNER,
				autoAddOwnerInPalanqueeCheckBox.isSelected());

		return options;
	}

	private DiveComputerDataParser getParser() {
		// Integer i = (Integer) computerTypeCb.getSelectedItem();
		// if (UwatecConstants.getAladinDiveComputers().containsKey(i)) {
		// return new UwatecDiveComputerDataParser();
		// }
		DiveComputerType dct = (DiveComputerType) computerTypeCb
				.getSelectedItem();
		if (null != dct) {
			if (dct.getParserType() == 1) {
				return new UwatecDiveComputerDataParser();
			}
		}
		return null;
	}

	public List<Dive> getSelectedDives() {
		List<Dive> list = new ArrayList<Dive>();
		int[] rows = diveTable.getSelectedRows();
		for (int row : rows) {
			Dive dive = diveTableModel.getDiveAt(diveTable
					.convertRowIndexToModel(row));
			list.add(dive);
		}
		return list;
	}

	public void setEnableStandaloneOptions(boolean enable) {
		autoNumberCheckBox.setEnabled(enable);
	}

	public void setMatCave(MatCave matCave) {
		this.matCave = matCave;
	}
}
