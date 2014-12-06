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
package be.vds.jtbdive.client.swing.component;

import javax.swing.JLabel;

import be.smd.i18n.swing.I18nLabel;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class I18nTitlePanel extends TitlePanel {

	private static final long serialVersionUID = -2948286199701098643L;

	public I18nTitlePanel(String title) {
		super(title);
	}

	@Override
	protected JLabel createJLabel() {
		return new I18nLabel();
	}

	@Override
	public void setTitle(String title) {
		((I18nLabel) label).setTextBundleKey(title);
	}

}
