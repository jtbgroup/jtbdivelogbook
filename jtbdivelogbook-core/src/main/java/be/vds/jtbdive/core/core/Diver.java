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
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * This class is an implementation of a Diver. The way to identify a diver is
 * it's id (see equals() method)
 * 
 * @author Gautier Vanderslyen
 */
public class Diver implements Serializable {

	private static final long serialVersionUID = -370943611366150934L;
	private String firstName;
	private String lastName;
	private long id = -1;
	private List<Contact> contacts;
	private Date birthDate;

	public Diver() {
		this(-1);
	}

	public Diver(long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the full name of the diver. The full name is defined by the
	 * concatenation of the first name and the last name
	 * 
	 * @return the full name of the diver
	 */
	public String getFullName() {
		StringBuilder sb = new StringBuilder();
		if (null != firstName) {
			sb.append(firstName).append(" ");
		}

		if (null != lastName) {
			sb.append(lastName);
		}

		return sb.toString();
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (obj == this){
			return true;
		}

		if (obj instanceof Diver) {
			Diver diver = (Diver) obj;
			if (id == -1 && diver.getId() == -1){
				return this == diver;
			}

			if (id == diver.getId()) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		if (-1 == id) {
			return this.hashCode();
		} else {
			return Long.valueOf(id).hashCode();
		}
	};

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(firstName).append(" ");
		sb.append(lastName);
		sb.append(" (id : ").append(id).append(")");
		return sb.toString();
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	/**
	 * A comparator based on the last name + first name
	 * 
	 */
	public static class DiverNameComparator implements Comparator<Diver> {

		public int compare(Diver d1, Diver d2) {
			String s1 = d1.getLastName().toLowerCase() + " "
					+ d1.getFirstName().toLowerCase();
			String s2 = d2.getLastName().toLowerCase() + " "
					+ d2.getFirstName().toLowerCase();
			return s1.compareTo(s2);
		}

	}

	/**
	 * A comparator based on the id
	 * 
	 */
	public static class DiverIdComparator implements Comparator<Diver> {
		public int compare(Diver d1, Diver d2) {
			if (d1.getId() > d2.getId()) {
				return 1;
			} else if (d1.getId() < d2.getId()) {
				return -1;
			} else {
				return 0;
			}

		}
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

}
