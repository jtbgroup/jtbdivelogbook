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
package be.vds.jtbdive.client.util;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Filtre de fichiers selon l'extension de leur nom. Un type de fichier
 * correspond � une ou plusieurs extensions.
 * <p>
 * Enhanced to have a detailed description or not. By default, the description
 * is detailed
 * 
 * @see javax.swing.JFileChooser#addChoosableFileFilter(javax.swing.filechooser.FileFilter)
 * @author S�bastien Wautelet
 */
public class ExtensionFilter extends javax.swing.filechooser.FileFilter {
	private Set<String> extensions;
	private String name;
	private boolean detailedDescription = true;

	public ExtensionFilter(String nom) {
		this(null, nom);
	}

	/**
	 * Cr�e un filtre qui accepte uniquement les fichiers dont l'extension est
	 * pass�e en param�tre.
	 * 
	 * @param extension
	 *            Extension (sans le .) Par exemple "txt" pour les fichiers
	 *            texte.
	 * @param nom
	 *            Nom du format qui sera utilis�e par la fonction
	 *            getDescription.
	 */
	public ExtensionFilter(String extension, String nom) {
		this.extensions = new HashSet<String>();
		this.name = nom;
		if (null != extension)
			this.extensions.add(extension);
	}

	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		for (String ext : extensions) {
			if (f.getPath().toLowerCase().endsWith("." + ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Renvoie la description qui s'affichera dans la ListBox du JFileChooser.
	 * 
	 * @return Description du filtre (par exemple : <b>Fichiers C++ (*.cpp,
	 *         *.cxx)</b> si le filtre a �t� cr�� avec le nom "Fichiers C++" et
	 *         l'extension "cpp", et qu'on lui a rajout� l'extension "cxx").
	 */
	public String getDescription() {
		if (!detailedDescription)
			return name;

		StringBuilder descr = new StringBuilder();
		descr.append(name).append(" (");

		for (String ext : extensions) {
			descr.append("*.").append(ext).append(", ");
		}

		return descr.substring(0, descr.lastIndexOf(",")) + ")";
	}

	/**
	 * Rajoute une extension � la liste des extensions qui seront accept�e pour
	 * ce fichier (dans le cas des formats � plusieurs extensions, par exemple
	 * "htm" ou "html" pour les fichiers HTML).
	 * 
	 * @param extension
	 *            L'extension � rajouter, toujours sans le .
	 */
	public void addExtension(String extension) {
		extensions.add(extension);
	}

	public Set<String> getExtensions() {
		return extensions;
	}

	public void setDetailedDescription(boolean detailedDescription) {
		this.detailedDescription = detailedDescription;
	}
}