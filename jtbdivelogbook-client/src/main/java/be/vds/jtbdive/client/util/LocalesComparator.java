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
package be.vds.jtbdive.client.util;

import java.util.Comparator;
import java.util.Locale;

public class LocalesComparator implements Comparator<Locale> {

	@Override
	public int compare(Locale o1, Locale o2) {
		int i = o1.getLanguage().compareTo(o2.getLanguage());
		if(i == 0){
			i = o1.getCountry().compareTo(o2.getCountry());
		}
		return i;
	}
	
	

}
