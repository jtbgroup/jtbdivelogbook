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
package be.vds.jtbdive.client.view.components;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.core.core.Dive;

public class SaveModifiedDivesDialog extends PromptDialog {

	private static final long serialVersionUID = -9174554746972473669L;
	private DiveSelectionDialog dlg;

	public SaveModifiedDivesDialog(LogBookManagerFacade logBookManagerFacade,
			Frame frame, String title, String message) {
		super(frame, title, message, PromptDialog.MODE_YES_NO_CANCEL);
		initCustom(logBookManagerFacade);
	}

	private void initCustom(LogBookManagerFacade logBookManagerFacade) {
		I18nResourceManager i18n = I18nResourceManager.sharedInstance();
		List<Dive> dives = new ArrayList<Dive>(
				logBookManagerFacade.getAllModifiedDives());
		dlg = new DiveSelectionDialog(i18n.getString("dives.modified"),
				i18n.getString("dives.modified"), dives);
	}

	@Override
	protected Component createContentPanel() {
		return null;
	}

	@Override
	protected void performYesAction() {
		dispose();
		int i = dlg.showDialog();

		if (i == DiveSelectionDialog.OPTION_OK) {
			setReturnOption(OPTION_YES);
		} else {
			setReturnOption(OPTION_CANCEL);
		}
	}

	public List<Dive> getSelectedDives() {
		List<Dive> selection = dlg.getSelectedDives();
		return selection;
	}

}
