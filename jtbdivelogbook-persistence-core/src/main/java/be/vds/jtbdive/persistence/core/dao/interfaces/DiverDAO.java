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
package be.vds.jtbdive.persistence.core.dao.interfaces;

import java.util.List;

import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiverUsedException;

public interface DiverDAO {

	public List<Diver> findAllDivers() throws DataStoreException;

//	public Diver findDiverById(int id);

	public Diver saveDiver(Diver diver) throws DataStoreException;

	public Diver updateDiver(Diver diver) throws DataStoreException;

	public boolean deleteDiver(Diver diver) throws DataStoreException, DiverUsedException;

	public List<Diver> findDiversByName(String firstName, String lastName) throws DataStoreException;

	public List<Diver> findDiversByFirstOrLastName(String name) throws DataStoreException;

	public Diver findDiver(long parseLong) throws DataStoreException;

	public boolean mergeDivers(Diver diverToKeep, Diver diverToDelete)throws DataStoreException, DiverUsedException;

}
