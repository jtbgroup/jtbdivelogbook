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
package be.vds.jtbdive.client.view.core.browser;

import be.vds.jtbdive.client.view.core.browser.nodeconstructor.DiveDateNodeConstructor;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.DiveNumberNodeConstructor;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.DiveSiteNodeConstructor;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.DiveYearNodeConstructor;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookBrowserNodeConstructor;
import be.vds.jtbdive.client.view.core.browser.nodeconstructor.LogBookSorting;

public class LogBookSortingHelper {

	public static LogBookBrowserNodeConstructor getNodeConstructor(
			LogBookSorting sorting) {
		switch (sorting) {
		case DIVE_DATE:
			return new DiveDateNodeConstructor();
		case DIVE_SITE:
			return new DiveSiteNodeConstructor();
		case DIVE_YEAR:
			return new DiveYearNodeConstructor();
		default:
			return new DiveNumberNodeConstructor();
		}

	}

}
