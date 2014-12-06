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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.text.RegexPlainDocument;
import be.vds.jtbdive.client.view.core.map.MapPinDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Address;
import be.vds.jtbdive.core.core.Coordinates;
import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.SimplePinableObject;
import be.vds.jtbdive.core.logging.Syslog;

public class DiveSiteLocationPanel extends JPanel {

	private static final long serialVersionUID = -5670200477360153834L;
	private static final Syslog LOGGER = Syslog
			.getLogger(DiveSiteLocationPanel.class);
	private JTextField longitudeComponent;
	private JTextField latitudeComponent;
	private JSpinner altitudeSpinner;
	private AddressPanel addressPanel;

	public DiveSiteLocationPanel(List<Country> countries) {
		init(countries);
	}

	private void init(List<Country> countries) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setOpaque(false);
		this.add(createCoordinatesPanel());
		this.add(createAddressPanel(countries));
		this.add(createAltitudePanel());

		this.add(Box.createVerticalGlue());
	}

	private Component createAddressPanel(List<Country> countries) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(I18nResourceManager.sharedInstance()
				.getString("address")));
		p.setOpaque(false);
		addressPanel = new AddressPanel(countries);
		p.add(addressPanel);
		return p;
	}

	private JPanel createAltitudePanel() {
		JPanel p = new JPanel();
		p.setBorder(new TitledBorder(I18nResourceManager.sharedInstance()
				.getString("altitude")));
		p.setLayout(new GridBagLayout());
		p.setOpaque(false);
		int y = -1;
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 3, 3, 3);

		// Altitude
		GridBagLayoutManager.addComponent(p, createAltitudeLabel(), c, 0, ++y,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(p, createAltitudeComponent(), c, 1,
				y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		return p;
	}

	private JPanel createCoordinatesPanel() {
		JPanel p = new JPanel();
		p.setBorder(new TitledBorder(I18nResourceManager.sharedInstance()
				.getString("coordinates")));
		p.setLayout(new GridBagLayout());
		p.setOpaque(false);
		int y = -1;
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 3, 3, 3);
		// Latitude - longitude
		GridBagLayoutManager.addComponent(p, new I18nLabel("latitude"), c, 0,
				++y, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(p, createLatitudeComponent(), c, 1,
				y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager
				.addComponent(p, new I18nLabel("longitude"), c, 2, y, 1, 1, 0,
						0, GridBagConstraints.NONE, GridBagConstraints.EAST);
		GridBagLayoutManager.addComponent(p, createLongitudeComponent(), c, 3,
				y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(p, createCoordinatesButtons(), c, 0,
				++y, 4, 1, 1, 0, GridBagConstraints.NONE,
				GridBagConstraints.EAST);

		return p;
	}

	private Component createCoordinatesButtons() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		p.add(createPinOnMapComponent());
		p.add(createClearCoordinatesComponent());
		return p;
	}

	private Component createClearCoordinatesComponent() {
		I18nButton btn = new I18nButton(new AbstractAction("delete", UIAgent
				.getInstance().getIcon(UIAgent.ICON_CANCEL_16)) {

			@Override
			public void actionPerformed(ActionEvent e) {
				latitudeComponent.setText(null);
				longitudeComponent.setText(null);
			}
		});
		return btn;
	}

	private Component createPinOnMapComponent() {
		I18nButton btn = new I18nButton(new AbstractAction("map.pin") {

			@Override
			public void actionPerformed(ActionEvent e) {
				MapPinDialog dlg = new MapPinDialog();

				Coordinates c = getDisplayedCoordinates();
				if (null != c) {
					dlg.addPin(new SimplePinableObject(c));
					dlg.setZoomPercent(0.5f);
					dlg.centerOnCoordinates(c);
				}

				int i = dlg.showDialog(800, 700);
				if (i == MapPinDialog.OPTION_OK) {
					Collection<Coordinates> pins = dlg.getAllPins();
					if (pins.size() > 0) {
						Coordinates obj = (Coordinates) pins.toArray()[0];
						latitudeComponent.setText(String.valueOf(obj
								.getLatitude()));
						longitudeComponent.setText(String.valueOf(obj
								.getLongitude()));
					}
				}
			}

			private Coordinates getDisplayedCoordinates() {
				String lati = latitudeComponent.getText();
				String longi = longitudeComponent.getText();
				if ((lati == null || lati.length() == 0)
						&& (longi == null || longi.length() == 0)) {
					return null;
				}

				try {

					double longiL = 0;
					double latiL = 0;

					if (longi != null && longi.length() > 0) {
						longiL = Double.parseDouble(longi);
					}

					if (lati != null && lati.length() > 0) {
						latiL = Double.parseDouble(lati);
					}

					return new Coordinates(latiL, longiL);
				} catch (NumberFormatException e) {
					LOGGER.error("Error with the coordinates format : " + lati
							+ " / " + longi);
					return null;
				}
			}
		});
		btn.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_TARGET_16));
		return btn;
	}

	private Component createAltitudeLabel() {
		I18nLabel label = new I18nLabel("altitude");
		return label;
	}

	private Component createAltitudeComponent() {
		SpinnerNumberModel sm = new SpinnerNumberModel(0d, -50d, 5000d, 0.5);
		altitudeSpinner = new JSpinner(sm);
		return altitudeSpinner;
	}

	private Component createLongitudeComponent() {
		longitudeComponent = new JTextField(10);
		longitudeComponent
				.setDocument(createPlainDoc(Coordinates.MAX_LONGITUDE));
		return longitudeComponent;
	}

	private Document createPlainDoc(final double max) {
		RegexPlainDocument d = new RegexPlainDocument(
				new String[] { RegexPlainDocument.DOUBLE }) {
			@Override
			protected void insertNormally(int offs, String str, AttributeSet a,
					String fullText) throws BadLocationException {
				Double d = Double.valueOf(fullText);
				if (Math.abs(d) <= max) {
					insertNewChar(offs, str, a);
				} else {
					doNotInsertNewChar(offs, str, a);
				}
			}
		};
		return d;
	}

	private Component createLatitudeComponent() {
		latitudeComponent = new JTextField(10);
		latitudeComponent.setDocument(createPlainDoc(Coordinates.MAX_LATITUDE));
		return latitudeComponent;
	}

	public void reset() {
		latitudeComponent.setText(null);
		longitudeComponent.setText(null);
		altitudeSpinner.setValue(0);
		addressPanel.clear();
	}

	public void setValue(DiveSite diveSite) {
		if (diveSite.hasCoordinates()) {
			Coordinates c = diveSite.getCoordinates();
			latitudeComponent.setText(String.valueOf(c.getLatitude()));
			longitudeComponent.setText(String.valueOf(c.getLongitude()));
		}
		altitudeSpinner.setValue(diveSite.getAltitude());
		addressPanel.setAddress(diveSite.getAddress());
	}

	public void setEditable(boolean b) {
		longitudeComponent.setEnabled(b);
		latitudeComponent.setEnabled(b);
		altitudeSpinner.setEnabled(b);
	}

	public Coordinates getDiveSiteCoordinates() {
		Coordinates c = null;
		String latS = latitudeComponent.getText();
		String lonS = longitudeComponent.getText();
		if (null != latS && null != lonS && latS.length() > 0
				&& lonS.length() > 0) {
			c = new Coordinates(Double.parseDouble(latS),
					Double.parseDouble(lonS));
		}

		return c;
	}

	public Double getDiveSiteAltitude() {
		return (Double) altitudeSpinner.getValue();
	}

	public Address getDiveSiteAddress() {
		return addressPanel.getAddress();
	}
}
