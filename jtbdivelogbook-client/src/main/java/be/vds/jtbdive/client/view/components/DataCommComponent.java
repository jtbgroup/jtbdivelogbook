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
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.interfaces.SerialDataCommInterface;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.interfaces.DataCommInterface;
import be.vds.jtbdive.core.interfaces.DataCommInterfaceType;

public class DataCommComponent extends JPanel {

	private static final long serialVersionUID = -2207262013801392213L;
	public static final String PROPERTY_INTERFACE_CHANGED = "interface.changed";
	private JPanel contentPanel;

	private DataCommInterface datacomInterface;
	private JLabel imageLabel;
	private JLabel dataCommTypeLabel;

	public DataCommComponent() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.add(createContentPanel(), BorderLayout.CENTER);
		this.add(createButtons(), BorderLayout.SOUTH);
		refreshContent();
	}

	private Component createContentPanel() {
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setOpaque(false);
		contentPanel.add(createImageLabel(), BorderLayout.WEST);
		contentPanel.add(createDataCommType(), BorderLayout.CENTER);
		return contentPanel;
	}

	private Component createImageLabel() {
		imageLabel = new JLabel();
		return imageLabel;
	}

	private Component createDataCommType() {
		dataCommTypeLabel = new JLabel();
		dataCommTypeLabel.setFont(UIAgent.getInstance()
				.getFontTitleDetail());
		dataCommTypeLabel.setHorizontalTextPosition(JLabel.LEFT);
		return dataCommTypeLabel;
	}

	private Component createButtons() {
		I18nButton modifyButton = new I18nButton(
				new AbstractAction(null, UIAgent.getInstance().getIcon(
						UIAgent.ICON_TOOLS_16)) {

					private static final long serialVersionUID = 2172081435017611002L;

					@Override
					public void actionPerformed(ActionEvent e) {
						Window w = WindowUtils
								.getTopLevelWindow(DataCommComponent.this);
						DataCommDialog dlg = null;
						if (w instanceof Frame) {
							dlg = new DataCommDialog((Frame) w);
						} else if (w instanceof Dialog) {
							dlg = new DataCommDialog((Dialog) w);
						} else {
							dlg = new DataCommDialog();
						}

						dlg.setDataCommInterface(datacomInterface);

						int i = dlg.showDialog(350, 350);
						if (i == DataCommDialog.OPTION_OK) {
							DataCommInterface old = datacomInterface;
							datacomInterface = dlg.getDataCommInterface();
							refreshContent();
							fireInterfaceChanged(old, datacomInterface);
						}
					}

				});
		modifyButton.setTooltipTextBundleKey("modify");

		I18nButton resetButton = new I18nButton(
				new AbstractAction(null, UIAgent.getInstance().getIcon(
						UIAgent.ICON_CANCEL_16)) {

					private static final long serialVersionUID = -1544258509063378031L;

					@Override
					public void actionPerformed(ActionEvent e) {
						setDatacomInterface(null);
					}
				});
		resetButton.setTooltipTextBundleKey("reset");

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(modifyButton);
		p.add(resetButton);
		p.setOpaque(false);
		return p;
	}

	private void fireInterfaceChanged(DataCommInterface oldIf,
			DataCommInterface newIf) {
		firePropertyChange(PROPERTY_INTERFACE_CHANGED, oldIf, newIf);
	}

	private void refreshContent() {
		if (null == datacomInterface) {
			imageLabel.setIcon(null);
			dataCommTypeLabel.setText(I18nResourceManager.sharedInstance()
					.getString("no.datacomm.interface"));
			dataCommTypeLabel.setToolTipText(null);
			dataCommTypeLabel.setIcon(null);
		} else {
			imageLabel.setIcon(getIconFor(datacomInterface
					.getDataCommInterfaceType()));
			String detail = formatDataCommInterfaceDetail(datacomInterface);
			dataCommTypeLabel.setText(I18nResourceManager.sharedInstance()
					.getString(
							datacomInterface.getDataCommInterfaceType()
									.getKey()));
			dataCommTypeLabel.setToolTipText(detail);
			dataCommTypeLabel.setIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_INFO_16));
		}
	}

	private String formatDataCommInterfaceDetail(
			DataCommInterface datacomInterface) {
		switch (datacomInterface.getDataCommInterfaceType()) {
		case SERIAL:
			return formatSerialInterface((SerialDataCommInterface) datacomInterface);
		default:
			return datacomInterface.toString();
		}
	}

	private String formatSerialInterface(
			SerialDataCommInterface datacomInterface) {
		StringBuilder sb = new StringBuilder();
		sb.append("Port : ").append(datacomInterface.getPort());
		sb.append(" - Baud rate : ").append(datacomInterface.getBaudRate());
		sb.append(" - Data bits : ").append(datacomInterface.getDataBits());
		sb.append(" - Parity : ")
				.append(SerialDataCommInterface.getParity(datacomInterface
						.getParity()));
		sb.append(" - Stop bits : ").append(datacomInterface.getStopBits());
		return sb.toString();
	}

	private Icon getIconFor(DataCommInterfaceType dataCommInterfaceType) {
		switch (datacomInterface.getDataCommInterfaceType()) {
		case SERIAL:
			return UIAgent.getInstance().getIcon(
					UIAgent.ICON_SERIAL_32);
		default:
			return UIAgent.getInstance().getIcon(UIAgent.ICON_USB_32);
		}
	}

	public void setDatacomInterface(DataCommInterface datacomInterface) {
		this.datacomInterface = datacomInterface;
		refreshContent();
	}

	public DataCommInterface getDataCommInterface() {
		return datacomInterface;
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SerialDataCommInterface interf = new SerialDataCommInterface("COM1",
				SerialDataCommInterface.BAUD_RATE_2400,
				SerialDataCommInterface.DATA_BITS_6,
				SerialDataCommInterface.PARITY_MARK,
				SerialDataCommInterface.STOP_BIT_2);

		DataCommComponent dcc = new DataCommComponent();
		dcc.setDatacomInterface(interf);
		f.getContentPane().add(dcc);
		f.setSize(300, 300);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

}
