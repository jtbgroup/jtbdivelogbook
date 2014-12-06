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

import java.util.Comparator;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.core.core.Country;

/**
 *
 * @author vanderslyen.g
 */
public class CountriesI18nComparator implements Comparator<Country> {
    
    private static final I18nResourceManager i18n = I18nResourceManager.sharedInstance();
    
    @Override
    public int compare(Country o1, Country o2) {
        String s1 = i18n.getString("country.code." + o1.getCode());
        String s2 = i18n.getString("country.code." + o2.getCode());
        
        return (s1.startsWith("???") ? o1.getName() : s1).compareTo((s2.startsWith("???") ? o2.getName() : s2));
    }
}
