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
package be.vds.jtbdive.core.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import be.vds.jtbdive.core.core.catalogs.DiverRole;

/**
 * An association class that associates a diver and a role
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class PalanqueeEntry implements Serializable {

	private static final long serialVersionUID = 6813302870451223079L;
	private Set<DiverRole> roles;
	private Diver diver;

	public PalanqueeEntry() {
	}

	public PalanqueeEntry(Diver diver, Set<DiverRole> roles) {
		this.diver = diver;
		this.roles = roles;
	}

	public Set<DiverRole> getRoles() {
		return roles;
	}

	public Diver getDiver() {
		return diver;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj){
			return false;
		}

		if (obj instanceof PalanqueeEntry) {
			PalanqueeEntry pe = (PalanqueeEntry) obj;
			return pe.getDiver().equals(diver);
		}
		return super.equals(obj);
	}

	public void setPalanqueeRole(Set<DiverRole> roles) {
		this.roles = roles;
	}

	public void setDiver(Diver diver) {
		this.diver = diver;
	}

	public static Set<DiverRole> getBasicRole() {
		Set<DiverRole> roles = new HashSet<DiverRole>();
		roles.add(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER);
		return roles;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(diver.getFullName());
		sb.append("[");
		for (DiverRole role : roles) {
			sb.append(role).append(", ");
		}
		sb.substring(0, sb.length()-3);
		sb.append("]");
		return sb.toString();
	}

	public int hashCode() {
		return diver.hashCode() + roles.hashCode();
	};
	
}
