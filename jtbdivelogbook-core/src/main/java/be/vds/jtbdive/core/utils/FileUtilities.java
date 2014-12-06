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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.fuin.utils4j.Utils4J;

public final class FileUtilities {

	private FileUtilities() {
	}

	public static void replaceAllInFile(File file, String oldValue,
			String newValue) throws IOException {
		byte[] b = new byte[(int) file.length()];
		FileInputStream is = new FileInputStream(file);
		is.read(b);
		is.close();
		String s = new String(b);
		s = s.replaceAll(oldValue, newValue);
		FileOutputStream os = new FileOutputStream(file);
		os.write(s.getBytes());
		os.flush();
		os.close();
	}

	public static void replaceAllInFile(File file, String[] oldValues,
			String[] newValues) throws IOException {
		byte[] b = new byte[(int) file.length()];
		FileInputStream is = new FileInputStream(file);
		is.read(b);
		is.close();
		String s = new String(b);

		for (int i = 0; i < oldValues.length; i++) {
			s = s.replaceAll(oldValues[i], newValues[i]);
		}

		FileOutputStream os = new FileOutputStream(file);
		os.write(s.getBytes());
		os.flush();
		os.close();

	}

	public static String readFileContent(URI uri) throws IOException {
		File file = new File(uri);
		byte[] b = new byte[(int) file.length()];
		FileInputStream is = new FileInputStream(file);
		is.read(b);
		is.close();
		return new String(b);
	}

	public static byte[] readFileContent(File file) throws IOException {
		byte[] b = new byte[(int) file.length()];
		new FileInputStream(file).read(b);
		return b;
	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * Create a new temporary directory. Use something like
	 * {@link #recursiveDelete(File)} to clean this directory up since it isn't
	 * deleted automatically
	 * 
	 * @return the new directory
	 * @throws IOException
	 *             if there is an error creating the temporary directory
	 */
	public static File createTempDir() throws IOException {
		final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
		File newTempDir;
		final int maxAttempts = 9;
		int attemptCount = 0;
		do {
			attemptCount++;
			if (attemptCount > maxAttempts) {
				throw new IOException(
						"The highly improbable has occurred! Failed to "
								+ "create a unique temporary directory after "
								+ maxAttempts + " attempts.");
			}
			String dirName = String.valueOf(System.currentTimeMillis());
			newTempDir = new File(sysTempDir, dirName);
		} while (newTempDir.exists());

		if (newTempDir.mkdirs()) {
			return newTempDir;
		} else {
			throw new IOException("Failed to create temp dir named "
					+ newTempDir.getAbsolutePath());
		}
	}

	public static void zipDir(String srcPath, File dest) throws IOException {
		Utils4J.zipDir(new File(srcPath), "", dest);
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 */
	public static boolean deleteDir(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	public static void copy(File srcFile, File destFile) throws IOException {
		FileUtils.copyFile(srcFile, destFile);
	}

	public static void renameFile(File srcFile, File destFile)
			throws IOException {
		FileUtils.copyFile(srcFile, destFile);
		srcFile.delete();
	}

}
