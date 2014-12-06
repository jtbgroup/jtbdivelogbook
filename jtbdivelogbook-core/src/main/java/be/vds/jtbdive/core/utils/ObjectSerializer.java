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
package be.vds.jtbdive.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public final class ObjectSerializer {
	
	private ObjectSerializer() {
	}

	public static byte[] serialize(Object object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.close();
			return bos.toByteArray();
		} catch (IOException ex) {
			Logger.getLogger(ObjectSerializer.class.getName()).severe(
					ex.getMessage());
		}
		return null;
	}

	public static Object deserialize(byte[] serializedObject) {
		Object result = null;
		if (serializedObject != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(
					serializedObject);
			try {
				ObjectInputStream ois = new ObjectInputStream(bis);
				result = ois.readObject();
				bis.close();

			} catch (ClassNotFoundException ex) {
				Logger.getLogger(ObjectSerializer.class.getName()).severe(
						ex.getMessage());
			} catch (IOException ex) {
				Logger.getLogger(ObjectSerializer.class.getName()).severe(
						ex.getMessage());
			}
		}
		return result;
	}

	public static Object cloneObject(Object object) {
		Object clone = null;
		byte[] copy = serialize(object);
		clone = deserialize(copy);
		return clone;
	}

}
