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
package be.vds.jtbdive.client.view.core.dive.profile;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.utils.ObjectSerializer;

public class DiveProfileEditorDialog extends PromptDialog {

	private static final long serialVersionUID = -2811946070325097859L;
	private DiveProfileEditor diveProfileEditor;
	private DiveProfileChartPanelEditor chartEditor;
	private DiveProfileTableEditor tableEditor;

	public DiveProfileEditorDialog() {
		super(i18n.getString("dive.profile.edit"), null);
		setDiveProfileEditor(new DiveProfileEditor());
	}

	private void setDiveProfileEditor(DiveProfileEditor diveProfileEditor) {
		this.diveProfileEditor = diveProfileEditor;
		chartEditor.registerDiveProfileEditor(this.diveProfileEditor);
		tableEditor.registerDiveProfileEditor(this.diveProfileEditor);
	}

	public void setDiveProfile(DiveProfile diveProfile) {
		diveProfile = (DiveProfile) ObjectSerializer.cloneObject(diveProfile);
		diveProfileEditor.load(diveProfile);
	}

	public Component createContentPanel() {
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setOpaque(false);
		split.setRightComponent(createRightPanel());
		split.setLeftComponent(createLeftPanel());
		split.setOneTouchExpandable(true);
		split.setResizeWeight(0.3);
		return split;
	}

	private Component createLeftPanel() {
		tableEditor = new DiveProfileTableEditor(
				diveProfileEditor);

		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(tableEditor, BorderLayout.CENTER);
		p.add(chartEditor.getWarningControlPanel(), BorderLayout.SOUTH);

		return p;
	}

	private Component createRightPanel() {
		chartEditor = new DiveProfileChartPanelEditor(diveProfileEditor);
		return chartEditor;
	}

	public DiveProfile getDepthEntries() {
		return diveProfileEditor.getDiveProfile();
	}

	public DiveProfile getDiveProfile() {
		return diveProfileEditor.getDiveProfile();
	}

	public static void main(String[] args) {
		DiveProfileEditorDialog dlg = new DiveProfileEditorDialog();
		dlg.showDialog(800, 600);
	}

}
