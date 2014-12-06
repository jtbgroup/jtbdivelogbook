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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.renderer.CountryRenderer;
import be.vds.jtbdive.core.core.Address;
import be.vds.jtbdive.core.core.Country;

public class AddressPanel extends JPanel {

    private JTextField streetTf;
    private JTextField boxTf;
    private JTextField numberTf;
    private JTextField cityTf;
    private JTextField zipTf;
    private JComboBox countryCb;
    private DefaultComboBoxModel countryCbModel;
    private JTextField regionTf;
    private List<Country> countries;

    public AddressPanel(List<Country> countries) {
        this.countries = countries;
        init();
        setCountries(countries);
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        int y = 0;
        gc.insets = new Insets(3, 3, 3, 3);
        // Street
        GridBagLayoutManager.addComponent(this, new I18nLabel("street"), gc, 0, y, 1, 1, 0,
                0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(this, createStreetComponent(), gc, 1,
                y, 4, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);

        // StreeNumber - Box
        GridBagLayoutManager.addComponent(this, new I18nLabel("number"), gc, 0,
                ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(this, createNumberComponent(), gc, 1,
                y, 1, 1, 0.5, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);

        GridBagLayoutManager.addComponent(this, new I18nLabel("box"), gc, 3, y,
                1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(this, createBoxComponent(), gc, 4, y,
                1, 1, 0.5, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
        // ZIP
        GridBagLayoutManager.addComponent(this, new I18nLabel("zip"), gc, 0,
                ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(this, createZipComponent(), gc, 1, y,
                1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);

        GridBagLayoutManager.addComponent(this, new I18nLabel("city"), gc, 3, y, 1, 1, 0,
                0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(this, createCityComponent(), gc, 4,
                y, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);

        // Region
        GridBagLayoutManager.addComponent(this, new I18nLabel("region"), gc, 0,
                ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(this, createRegionComponent(), gc, 1,
                y, 4, 1, 0, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);

        // Country
        GridBagLayoutManager.addComponent(this, new I18nLabel("country"), gc,
                0, ++y, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(this, createCountryComponent(), gc,
                1, y, 4, 1, 0, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);

        // Glue
        GridBagLayoutManager.addComponent(this, Box.createVerticalGlue(), gc,
                0, ++y, 1, 1, 0, 1, GridBagConstraints.VERTICAL,
                GridBagConstraints.WEST);
        GridBagLayoutManager.addComponent(this, Box.createHorizontalStrut(10),
                gc, 2, y, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.CENTER);

    }

    private Component createRegionComponent() {
        regionTf = new JTextField();
        return regionTf;
    }

    private Component createCountryComponent() {
        countryCbModel = new DefaultComboBoxModel();
        countryCb = new JComboBox(countryCbModel);
        countryCb.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list,
                        value, index, isSelected, cellHasFocus);
                if (null == value) {
                    l.setText(null);
                } else {
                    l.setText(((Country) value).getName());
                }
                return this;
            }
        });
        countryCb.setRenderer(new CountryRenderer());
        return countryCb;
    }

    private Component createStreetComponent() {
        streetTf = new JTextField();
        return streetTf;
    }

    private Component createNumberComponent() {
        numberTf = new JTextField();
        return numberTf;
    }

    private Component createBoxComponent() {
        boxTf = new JTextField();
        return boxTf;
    }

    private Component createZipComponent() {
        zipTf = new JTextField();
        return zipTf;
    }

    private Component createCityComponent() {
        cityTf = new JTextField();
        return cityTf;
    }

    public void setAddress(Address address) {
        if (null == address) {
            clear();
        } else {
            streetTf.setText(address.getStreet());
            numberTf.setText(address.getNumber());
            boxTf.setText(address.getBox());
            cityTf.setText(address.getCity());
            zipTf.setText(address.getZipCode());
            regionTf.setText(address.getRegion());
            countryCb.setSelectedItem(address.getCountry());
        }
    }

    public Address getAddress() {
        Address address = new Address();
        address.setStreet(streetTf.getText());
        address.setNumber(numberTf.getText());
        address.setBox(boxTf.getText());
        address.setCity(cityTf.getText());
        address.setRegion(regionTf.getText());
        address.setCountry((Country) countryCb.getSelectedItem());
        return address;
    }


    public void setCountries(List<Country> countries) {
        this.countries = countries;
        Collections.sort(this.countries, new CountriesI18nComparator());
              
        countryCbModel.removeAllElements();
        for (Country country : countries) {
            countryCbModel.addElement(country);
        }
    }

    public void clear() {
        streetTf.setText(null);
        numberTf.setText(null);
        boxTf.setText(null);
        cityTf.setText(null);
        zipTf.setText(null);
        regionTf.setText(null);
        countryCb.setSelectedItem(null);
    }
}
