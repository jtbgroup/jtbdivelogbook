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

import be.vds.jtbdive.client.view.utils.UIAgent;

public class ChangeConfigDialog extends PromptDialog {

	public ChangeConfigDialog(String message) {
		super(i18n.getString("warning"), message, UIAgent.getInstance()
				.getBufferedImage(UIAgent.ICON_CONNECT_16),
				PromptDialog.MODE_YES_NO, null);
	}

	@Override
	protected Component createContentPanel() {
		return null;
	}

}
