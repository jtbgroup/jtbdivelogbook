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
package be.vds.jtbdive.client.actions;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.util.LanguageHelper;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;

public class SwitchLanguageAction extends AbstractAction {
	private static final long serialVersionUID = -3482174442287392458L;
	private Locale locale;
	private LogBookApplFrame logBookApplicationFrame;

	public SwitchLanguageAction(LogBookApplFrame logBookApplicationFrame,
			Locale locale) {
		this.locale = locale;
		this.logBookApplicationFrame = logBookApplicationFrame;
		this.putValue(Action.SMALL_ICON, ResourceManager.getInstance()
				.getCountryImageIcon(LanguageHelper.getLocaleIconName16(locale)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		I18nResourceManager.sharedInstance().setDefaultLocale(locale);
		logBookApplicationFrame.validate();
		logBookApplicationFrame.repaint();
		SwingUtilities.updateComponentTreeUI(logBookApplicationFrame);
		logBookApplicationFrame.adaptLanguage();
	}
}
