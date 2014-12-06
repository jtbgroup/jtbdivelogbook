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
package be.vds.jtbdive.core.core.catalogs;

public enum ContactType implements KeyedCatalog, OrderedCatalog {

	EMAIL(1, "email"),
	MOBILE(2, "mobile"), 
	PHONE(3, "phone"), 
	;

	private String key;
	private int id;

	public String getKey() {
		return key;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public int getOrder() {
		return id;
	}

	private ContactType(int id, String key) {
		this.id = id;
		this.key = key;
	}

	public static ContactType getContactTypeForId(int id) {
		for (ContactType ct : values()) {
			if (ct.getId() == id){
				return ct;
			}
		}
		return null;
	}

}
