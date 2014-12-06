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
package be.vds.jtbdive.client.view.core.download;

import java.awt.Component;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.MatCave;

public class DiveComputerDownloadDialog extends PromptDialog {

	private static final long serialVersionUID = -7861092506685650094L;
	private DiveComputerDownloadPanel dcPanel;

	public DiveComputerDownloadDialog(JFrame parentFrame) {
		super(parentFrame, i18n.getString("divecomputer.download"), null);
	}

	@Override
	protected Component createContentPanel() {
		dcPanel = new DiveComputerDownloadPanel();
		return dcPanel;
	}
	
	public void setEnableStandaloneOptions(boolean enable){
		dcPanel.setEnableStandaloneOptions(enable);
	}
	
	public List<Dive> getSelectedDives() {
		return dcPanel.getSelectedDives();
	}

	public Map<String, Object> getOptions() {
		return dcPanel.getOptions();
	}

	public void setMatCave(MatCave matCave) {
		dcPanel.setMatCave(matCave);
	}

}
