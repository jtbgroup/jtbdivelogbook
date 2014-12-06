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
package be.vds.jtbdive.client.core.businessdelegate;

import java.util.List;

import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiverUsedException;
import be.vds.jtbdive.core.integration.businessdelegate.interfaces.DiverBusinessDelegate;
import be.vds.jtbdive.core.utils.ObjectSerializer;
import be.vds.jtbdive.persistence.core.dao.interfaces.DaoFactory;

public class DAODirectDiverBusinessDelegate extends DiverBusinessDelegate {


    public DAODirectDiverBusinessDelegate( ) {
	}

	@Override
    public List<Diver> findAllDivers() throws DataStoreException {
        return DaoFactory.getFactory().createDiverDAO().findAllDivers();
    }

    @Override
    public List<Diver> findDiversByName(String firstName, String lastName) throws DataStoreException {
        return DaoFactory.getFactory().createDiverDAO().findDiversByName(firstName, lastName);
    }


    @Override
    public Diver saveDiver(Diver diver) throws DataStoreException {
    	Diver d  = (Diver) ObjectSerializer.cloneObject(diver);
        return DaoFactory.getFactory().createDiverDAO().saveDiver(d);
    }

    @Override
    public Diver updateDiver(Diver diver) throws DataStoreException {
    	Diver d  = (Diver) ObjectSerializer.cloneObject(diver);
        return DaoFactory.getFactory().createDiverDAO().updateDiver(d);
    }



	@Override
	public boolean deleteDiver(Diver diver) throws DataStoreException,
			DiverUsedException {
		return DaoFactory.getFactory().createDiverDAO().deleteDiver(diver);
	}

	@Override
	public List<Diver> findDiversByFirstOrLastName(String name) throws DataStoreException{
		return DaoFactory.getFactory().createDiverDAO().findDiversByFirstOrLastName(name);
	}

	@Override
	public boolean mergeDivers(Diver diverToKeep, Diver diverToDelete)
			throws DataStoreException, DiverUsedException {
		return DaoFactory.getFactory().createDiverDAO().mergeDivers(diverToKeep, diverToDelete);
	}


}
