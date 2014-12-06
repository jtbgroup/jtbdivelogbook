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
package be.vds.jtbdive.xml.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import be.vds.jtbdive.core.core.Country;

public class CountriesParser {
	private static final String EL_COUNTRY = "ISO_3166-1_Entry";
	private static final String EL_COUNTRY_NAME = "ISO_3166-1_Country_name";
	private static final String EL_COUNTRY_CODE = "ISO_3166-1_Alpha-2_Code_element";
	
	
	public List<Country> readCountriesList(Element root) {
		List<Country> countries = new ArrayList<Country>();
		// no inspection unchecked
		for (Iterator iterator = root.getChildren(EL_COUNTRY).iterator(); iterator
				.hasNext();) {
			Element countryElement = (Element) iterator.next();

			Element countryName = countryElement.getChild(EL_COUNTRY_NAME);
			Element countryCode = countryElement.getChild(EL_COUNTRY_CODE);

			String countryname = countryName.getText();
			countries.add(new Country(countryname, countryCode.getText()));
		}
		return countries;
	}
}
