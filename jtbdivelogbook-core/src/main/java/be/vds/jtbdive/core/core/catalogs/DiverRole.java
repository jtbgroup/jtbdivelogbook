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

import java.util.HashSet;
import java.util.Set;


/**
 * The role of a diver in the {@link Palanquee}
 * 
 * @author Gautier Vanderslyen
 * 
 */
public enum DiverRole implements KeyedCatalog{

	ROLE_PALANQUEE_SIMPLE_DIVER(0, "diver.role.simplediver", 99), 
	ROLE_PALANQUEE_FIRST(1, "diver.role.chief", 1),
	ROLE_PALANQUEE_SECOND(2, "diver.role.second", 2), 
	ROLE_PALANQUEE_MEDICAL_SUPPORT(4, "diver.role.medicalsupport", 3), 
	ROLE_PALANQUEE_CAMERA(8, "diver.role.camera", 4), 
	;

	private int role;
	private String key;
	private int order;

	public int getOrder() {
		return order;
	}

	private DiverRole(int role, String key, int order) {
		this.role = role;
		this.key = key;
		this.order = order;
	}

	/**
	 * gets the role in the palanquee
	 * 
	 * @return
	 */
	public int getRole() {
		return role;
	}

	/**
	 * Sets the role in the palanquee
	 * 
	 * @param role
	 */
	public void setRole(int role) {
		this.role = role;
	}

	public String getKey() {
		return key;
	}

	public static int convertToInt(Set<DiverRole> roles) {
		int i = DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER.getRole();
		for (DiverRole diverRole : roles) {
			i = i + diverRole.getRole();
		}
		return i;
	}

	public static Set<DiverRole> convertToRoles(int role) {
		Set<DiverRole> roles = new HashSet<DiverRole>();
		roles.add(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER);
		
		for (DiverRole diverRole : DiverRole.values()) {
			if((role & diverRole.getRole()) == diverRole.getRole()){
				roles.add(diverRole);
			}
		}
		
		return roles;
	}

	public static DiverRole getRoleForKey(String key) {
		for (DiverRole role : DiverRole.values()) {
			if(role.getKey().equals(key)){
				return role;
			}
		}
		return null;
	}

}
