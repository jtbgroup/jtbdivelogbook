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
package be.vds.jtbdive.client.view.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.core.core.Country;

/**
 *
 * @author vanderslyen.g
 */
public class CountryRenderer extends DefaultListCellRenderer {

    private static final I18nResourceManager i18n = I18nResourceManager.sharedInstance();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value == null) {
            label.setText(null);
        } else {
            Country c = (Country) value;
            String s = i18n.getString("country.code." + c.getCode());
            label.setText(s.startsWith("???") ? c.getName() : s);
        }

        return label;

    }
}
