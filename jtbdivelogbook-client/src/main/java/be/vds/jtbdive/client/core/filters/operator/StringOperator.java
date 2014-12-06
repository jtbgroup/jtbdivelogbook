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
package be.vds.jtbdive.client.core.filters.operator;

public enum StringOperator implements DiveFilterOperator {

	CONTAINS("contains", 0), //
	EQUALS("equals", 1), //
	;

	private String key;
	private int index;

	private StringOperator(String key, int index) {
		this.key = key;
		this.index = index;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getOrder() {
		return index;
	}

}
