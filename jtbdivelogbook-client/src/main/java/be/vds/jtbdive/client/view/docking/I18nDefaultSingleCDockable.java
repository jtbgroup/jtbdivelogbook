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
package be.vds.jtbdive.client.view.docking;

import java.awt.Component;

import javax.swing.Icon;

import be.smd.i18n.I18nResourceManager;
import bibliothek.gui.dock.common.DefaultSingleCDockable;

public class I18nDefaultSingleCDockable extends DefaultSingleCDockable {

	private String bundleTitle;

	public I18nDefaultSingleCDockable(String id, String bundleTitle, Icon icon,
			Component component) {
		super(id, icon, I18nResourceManager.sharedInstance().getString(
				bundleTitle), component, null);
		this.bundleTitle = bundleTitle;
	}

	public void updateLanguage() {
		setTitleText(I18nResourceManager.sharedInstance()
				.getString(bundleTitle));
	}

}
