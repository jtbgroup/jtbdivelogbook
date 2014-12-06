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

import java.awt.Component;
import java.awt.Frame;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Version;

public class VersionUpgraderDialog extends PromptDialog {
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();

	public VersionUpgraderDialog(Version oldVersion, Version newVersion) {
		super(i18n.getString("warning"), i18n
				.getMessage("configuration.outdated.message.confirm.params", new Object[]{oldVersion.toString(), newVersion.toString()}), UIAgent
				.getInstance().getBufferedImage(UIAgent.ICON_CONVERT_48),
				MODE_YES_NO, null);
		initDialog();
	}

	public VersionUpgraderDialog(Frame parentFrame, Version oldVersion, Version newVersion) {
		super(parentFrame, i18n.getString("warning"), i18n
				.getMessage("configuration.outdated.message.confirm.params", new Object[]{oldVersion.toString(), newVersion.toString()}), UIAgent
				.getInstance().getBufferedImage(UIAgent.ICON_CONVERT_48),
				MODE_YES_NO, null);
		initDialog();
	}

	private void initDialog() {
		this.setIconImage(UIAgent.getInstance().getBufferedImage(UIAgent.ICON_IMPORTANT_48));
	}

	@Override
	protected Component createContentPanel() {
		return null;
	}
}
