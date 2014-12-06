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
 */package be.vds.jtbdive.core.core;

import java.io.Serializable;

/**
 * This is a general representation for an address.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class Address implements Serializable {

	private static final long serialVersionUID = 305549583186275345L;
	private String street;
	private String number;
	private String box;

	private String zipCode;
	private String city;
	private String region;

	private Country country;

	public void setCountry(Country country) {
		this.country = country;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Country getCountry() {
		return country;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getBox() {
		return box;
	}

	public String getCity() {
		return city;
	}

	public String getRegion() {
		return region;
	}

}
