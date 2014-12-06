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
package be.vds.jtbdive.client.core.filters.inspector;

import java.util.ArrayList;
import java.util.List;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.catalogs.DivePlatform;
import be.vds.jtbdive.core.core.catalogs.KeyedCatalog;


public class DivePlatformInspector implements KeyedCatalogFilterInspector{
	@Override
	public KeyedCatalog[] getCatalogValues(){
		return DivePlatform.values();
	}
	
	
	@Override
	public Object getFilterParameter(Dive dive) {
		List<DivePlatform> l =  new ArrayList<DivePlatform>();
		l.add(dive.getDivePlatform());
		return l;
	}

}
