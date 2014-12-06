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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.interfaces.SerialDataCommInterface;
import be.vds.jtbdive.core.interfaces.DataCommInterface;

public class SerialDataComDetailPanel extends DataCommDetailPanel {

	private static final long serialVersionUID = -684168648237680136L;
	private JComboBox portCb;
	private JComboBox stopBitCb;
	private JComboBox parityBitCb;
	private JComboBox dataBitCb;
	private JComboBox baudRateCb;
	private DefaultComboBoxModel portComboBoxModel;

	public SerialDataComDetailPanel() {
		super();
		init();
	}

	private void init() {

		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		GridBagLayoutManager
				.addComponent(this, createSerialPortLabel(), c, 0, 0, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createSerialPortComponent(), c,
				1, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createBaudRateLabel(), c, 0, 1,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createBaudRateComponent(), c,
				1, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createDataBitLabel(), c, 0, 2,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager
				.addComponent(this, createDataBitComponent(), c, 1, 2, 1, 1, 1,
						0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager
				.addComponent(this, createParityBitLabel(), c, 0, 3, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager.addComponent(this, createParityBitComponent(), c,
				1, 3, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(this, createStopBitLabel(), c, 0, 4,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		GridBagLayoutManager
				.addComponent(this, createStopBitComponent(), c, 1, 4, 1, 1, 1,
						0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		GridBagLayoutManager
				.addComponent(this, Box.createVerticalGlue(), c, 1, 5, 1, 1, 1,
						1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
	}

	private Component createBaudRateComponent() {
		baudRateCb = new JComboBox(SerialDataCommInterface.BAUD_RATE.toArray());
		baudRateCb.setRenderer(new DefaultListCellRenderer());
		return baudRateCb;
	}

	private Component createDataBitComponent() {
		dataBitCb = new JComboBox(SerialDataCommInterface.DATA_BITS.keySet()
				.toArray());
		dataBitCb.setRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) new DefaultListCellRenderer()
						.getListCellRendererComponent(list, value, index,
								isSelected, cellHasFocus);
				renderer.setText(SerialDataCommInterface
						.getDataBit((Integer) value));
				return renderer;
			}
		});
		return dataBitCb;
	}

	private Component createParityBitComponent() {
		parityBitCb = new JComboBox(SerialDataCommInterface.PARITY.keySet()
				.toArray());
		parityBitCb.setRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) new DefaultListCellRenderer()
						.getListCellRendererComponent(list, value, index,
								isSelected, cellHasFocus);
				renderer.setText(SerialDataCommInterface
						.getParity((Integer) value));
				return renderer;
			}
		});
		return parityBitCb;
	}

	private Component createStopBitComponent() {
		stopBitCb = new JComboBox(SerialDataCommInterface.STOP_BITS.keySet()
				.toArray());
		stopBitCb.setRenderer(new ListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) new DefaultListCellRenderer()
						.getListCellRendererComponent(list, value, index,
								isSelected, cellHasFocus);
				renderer.setText(SerialDataCommInterface
						.getStopBit((Integer) value));
				return renderer;
			}
		});
		return stopBitCb;
	}

	private Component createSerialPortComponent() {
		portComboBoxModel = new DefaultComboBoxModel(SerialDataCommInterface
				.getAllPorts().toArray());
		portCb = new JComboBox(portComboBoxModel);
		portCb.setEditable(true);
		portCb.setRenderer(new SerialPortListCellRenderer());
		return portCb;
	}

	private Component createStopBitLabel() {
		JLabel portLabel = new I18nLabel("stop.bit");
		return portLabel;
	}

	private Component createSerialPortLabel() {
		JLabel portLabel = new I18nLabel("port");
		return portLabel;
	}

	private Component createBaudRateLabel() {
		JLabel label = new I18nLabel("baud.rate");
		return label;
	}

	private Component createDataBitLabel() {
		JLabel label = new I18nLabel("data.bit");
		return label;
	}

	private Component createParityBitLabel() {
		JLabel label = new I18nLabel("parity");
		return label;
	}

	@Override
	public DataCommInterface getDataCommInterface() {
		SerialDataCommInterface serialInterface = new SerialDataCommInterface();
		serialInterface.setPort((String) portCb.getSelectedItem());
		serialInterface.setBaudRate((Integer) baudRateCb.getSelectedItem());
		serialInterface.setDataBits((Integer) dataBitCb.getSelectedItem());
		serialInterface.setParity((Integer) parityBitCb.getSelectedItem());
		serialInterface.setStopBits((Integer) stopBitCb.getSelectedItem());
		return serialInterface;
	}

	@Override
	public void setDataComInterface(DataCommInterface datacomInterface) {
		SerialDataCommInterface sd = (SerialDataCommInterface) datacomInterface;
		baudRateCb.setSelectedItem(sd.getBaudRate());
		parityBitCb.setSelectedItem(sd.getParity());
		dataBitCb.setSelectedItem(sd.getDataBits());
		stopBitCb.setSelectedItem(sd.getStopBits());

		int index = portComboBoxModel.getIndexOf(sd.getPort());
		if (index == -1) {
			index = 0;
			portComboBoxModel.insertElementAt(sd.getPort(), index);
		}
		portCb.setSelectedIndex(index);
	}
}
