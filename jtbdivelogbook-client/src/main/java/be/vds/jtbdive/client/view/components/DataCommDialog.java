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
package be.vds.jtbdive.client.view.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.core.download.DataCommDetailPanel;
import be.vds.jtbdive.client.view.core.download.SerialDataComDetailPanel;
import be.vds.jtbdive.client.view.renderer.KeyedCatalogRenderer;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.interfaces.DataCommInterface;
import be.vds.jtbdive.core.interfaces.DataCommInterfaceType;

public class DataCommDialog extends PromptDialog {

	private static final long serialVersionUID = -4155311590074703454L;
	private JComboBox dataCommTypeCb;
	private CardLayout dataComCardLayout;
	private JPanel dataComDetailPanel;
	private Map<String, DataCommDetailPanel> dataComPanels;
	private DataCommDetailPanel currentDataCommInterfacePanel;
	private I18nLabel dataCommTypeLabel;

	public DataCommDialog() {
		super(i18n.getString("datacomm.interface"), i18n
				.getString("datacomm.interface.message"));
	}

	public DataCommDialog(Frame parentFrame) {
		super(parentFrame, i18n.getString("datacomm.interface"), i18n
				.getString("datacomm.interface.message"), UIAgent.getInstance().getBufferedImage(
						UIAgent.ICON_SERIAL_32), null);
	}

	public DataCommDialog(Dialog parentDialog) {
		super(parentDialog, i18n.getString("datacomm.interface"), i18n
				.getString("datacomm.interface.message"), UIAgent.getInstance().getBufferedImage(
						UIAgent.ICON_SERIAL_32), null);
	}

	protected void doBeforeInit() {
		dataComPanels = new HashMap<String, DataCommDetailPanel>();
	}


	@Override
	protected Component createContentPanel() {
		JPanel p = new JPanel(new BorderLayout(5, 5));
		p.setOpaque(false);
		p.add(createDataCommInterfaceType(), BorderLayout.NORTH);
		p.add(createDataCommInterfacePanel(), BorderLayout.CENTER);
		return p;
	}

	private Component createDataCommInterfaceType() {
		dataCommTypeLabel = new I18nLabel("datacomm.interface.type");

		dataCommTypeCb = new JComboBox(DataCommInterfaceType.values());
		dataCommTypeCb.setSelectedIndex(-1);
		dataCommTypeCb.setRenderer(new KeyedCatalogRenderer());

		dataCommTypeCb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataCommInterfaceType dci = ((DataCommInterfaceType) dataCommTypeCb
						.getSelectedItem());
				if (dci != null) {
					dataComCardLayout.show(dataComDetailPanel, dci.getKey());
					currentDataCommInterfacePanel = dataComPanels
							.get(((DataCommInterfaceType) dataCommTypeCb
									.getSelectedItem()).getKey());
				} else {
					dataComCardLayout.show(dataComDetailPanel, "nothing");
					currentDataCommInterfacePanel = dataComPanels
							.get("nothing");
				}
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(dataCommTypeLabel);
		p.add(dataCommTypeCb);
		p.setOpaque(false);
		return p;
	}

	private Component createDataCommInterfacePanel() {
		dataComDetailPanel = new JPanel();
		dataComDetailPanel.setOpaque(false);
		dataComCardLayout = new CardLayout();

		dataComDetailPanel.setLayout(dataComCardLayout);

		JPanel nothingPanel = new JPanel();
		nothingPanel.setOpaque(false);
		dataComDetailPanel.add(nothingPanel, "nothing");
		currentDataCommInterfacePanel = null;

		for (DataCommInterfaceType ty : DataCommInterfaceType.values()) {
			addTypeToPanel(ty);
		}

		JScrollPane scroll = new JScrollPane(dataComDetailPanel);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		scroll.setViewportBorder(null);

		DetailPanel p = new DetailPanel(new BorderLayout());
		p.add(scroll, BorderLayout.CENTER);
		return p;
	}

	private void addTypeToPanel(DataCommInterfaceType dataCommInterfaceType) {
		DataCommDetailPanel dcdp = createDataCommDetailPanel(dataCommInterfaceType);
		dataComDetailPanel.add(dcdp, dataCommInterfaceType.getKey());
		dataComPanels.put(dataCommInterfaceType.getKey(), dcdp);
	}

	private DataCommDetailPanel createDataCommDetailPanel(
			DataCommInterfaceType dataCommInterfaceType) {
		switch (dataCommInterfaceType) {
		case SERIAL:
			return new SerialDataComDetailPanel();

		default:
			return createNoOptionPanel();
		}

	}

	private DataCommDetailPanel createNoOptionPanel() {
		DataCommDetailPanel p = new DataCommDetailPanel() {
			private static final long serialVersionUID = -7723792498003743011L;

			@Override
			public DataCommInterface getDataCommInterface() {
				return null;
			}

			@Override
			public void setDataComInterface(DataCommInterface datacomInterface) {
			}
		};
		p.setOpaque(false);
		p.add(new I18nLabel("no.option"));
		return p;
	}

	public DataCommInterface getDataCommInterface() {
		return currentDataCommInterfacePanel.getDataCommInterface();
	}

	public void setDataCommInterface(DataCommInterface datacomInterface) {
		if (datacomInterface == null) {
			clear();
		} else {
			dataCommTypeCb.setSelectedItem(datacomInterface
					.getDataCommInterfaceType());
			currentDataCommInterfacePanel.setDataComInterface(datacomInterface);
		}
	}

	private void clear() {
		dataCommTypeCb.setSelectedItem(null);
	}

}
