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
package be.vds.jtbdive.client.view.wizard.importation;

import java.awt.Color;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.WizardPanel;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.material.Material;

public class ImportFinishPanel extends WizardPanel {

	private static final long serialVersionUID = -3391269373786199058L;
	private JTextPane detailTextArea;
	private static SimpleAttributeSet ITALIC_GRAY = new SimpleAttributeSet();
	private static SimpleAttributeSet BOLD_BLACK = new SimpleAttributeSet();

	// Best to reuse attribute sets as much as possible.
	static {
		StyleConstants.setForeground(ITALIC_GRAY, Color.gray);
		StyleConstants.setItalic(ITALIC_GRAY, true);
		StyleConstants.setFontFamily(ITALIC_GRAY, "Helvetica");
		StyleConstants.setFontSize(ITALIC_GRAY, 12);

		StyleConstants.setForeground(BOLD_BLACK, Color.black);
		StyleConstants.setBold(BOLD_BLACK, true);
		StyleConstants.setFontFamily(BOLD_BLACK, "Helvetica");
		StyleConstants.setFontSize(BOLD_BLACK, 14);
	}



	@Override
	public JComponent createContentPanel() {
		detailTextArea = new JTextPane();
		detailTextArea.setBackground(UIAgent.getInstance()
				.getColorBaseBackground());
		detailTextArea.setEditorKit(new StyledEditorKit());
		detailTextArea.setOpaque(false);
		detailTextArea.setEnabled(true);
		detailTextArea.setEditable(false);
		adaptText(null);

		JScrollPane scroll = new JScrollPane(detailTextArea);
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
		return scroll;
	}



	public void adaptText(Map<Object, Object> dataMap) {
		detailTextArea.setText(null);
		if (dataMap != null) {

			insertPicture(UIAgent.ICON_DOCUMENT_16);
			appendText("  Source\n", BOLD_BLACK);
			appendFileInfo(dataMap);
			appendText("\n\n", BOLD_BLACK);

			insertPicture(UIAgent.ICON_GRAPH_16);
			appendText("  Dives\n", BOLD_BLACK);
			appendDivesInfo(dataMap);
			appendText("\n\n", BOLD_BLACK);

			if (dataMap.get(ImportWizard.IMPORT_DIVER_MAP) != null) {
				insertPicture(UIAgent.ICON_DIVER_16);
				appendText("  Divers\n", BOLD_BLACK);
				appendDiversInfo(dataMap);
				appendText("\n\n", BOLD_BLACK);
			}

			if (dataMap.get(ImportWizard.IMPORT_DIVESITE_MAP) != null) {
				insertPicture(UIAgent.ICON_DIVE_SITE_16);
				appendText("  Dive Sites\n", BOLD_BLACK);
				appendDiveSitesInfo(dataMap);
				appendText("\n\n", BOLD_BLACK);
			}

			if (dataMap.get(ImportWizard.IMPORT_MATERIAL_MAP) != null) {
				insertPicture(UIAgent.ICON_MASK_16);
				appendText("  Materials\n", BOLD_BLACK);
				appendMaterialsInfo(dataMap);
				appendText("\n\n", BOLD_BLACK);
			}

			insertPicture(UIAgent.ICON_LOGBOOK_16);
			appendText("  LogBook\n", BOLD_BLACK);
			appendLogBookInfo(dataMap);

			detailTextArea.setCaretPosition(0);
		}
	}

	private void insertPicture(String iconRef) {
		detailTextArea.insertIcon(UIAgent.getInstance().getIcon(iconRef));
	}

	private void appendLogBookInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		LogBook b = (LogBook) dataMap.get(ImportWizard.IMPORT_NEW_LOGBOOK);
		if (null == b) {
			text.append("Dives will be imported in the current logbook");
		} else {
			text.append("Dives will be imported in a new logbook called '")
					.append(b.getName()).append("'");
			if (b.getOwner() != null) {
				text.append(" with ").append(b.getOwner().getFullName())
						.append(" as owner.");
			} else {
				text.append(" with no owner.");
			}
		}

		appendText(text.toString(), ITALIC_GRAY);
	}

	private void appendFileInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		text.append("Import format : ").append(
				dataMap.get(ImportWizard.IMPORT_FORMAT));
		text.append("\n");
		text.append("Import file : ").append(
				((File) (dataMap.get(ImportWizard.IMPORT_FILE)))
						.getAbsolutePath());

		appendText(text.toString(), ITALIC_GRAY);

	}

	private void appendText(String text, AttributeSet a) {
		try {
			detailTextArea.getDocument().insertString(
					detailTextArea.getDocument().getLength(), text, a);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void appendDivesInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		text.append("Number of dives that will be imported : ").append(
				((List<Dive>) (dataMap.get(ImportWizard.IMPORT_DIVES))).size());

		appendText(text.toString(), ITALIC_GRAY);
	}

	private void appendDiversInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		@SuppressWarnings("unchecked")
		Map<Diver, Diver> diversMatch = (Map<Diver, Diver>) (dataMap
				.get(ImportWizard.IMPORT_DIVER_MAP));
		int i = 0;
		for (Diver diver : diversMatch.values()) {
			if (diver == null)
				i++;
		}

		int totalDivers = diversMatch.keySet().size();

		text.append("Number of divers that will be imported : ").append(i);
		text.append("\n");
		text.append("Number of divers that will be matched : ").append(
				totalDivers - i);
		appendText(text.toString(), ITALIC_GRAY);
	}

	@SuppressWarnings("unchecked")
	private void appendDiveSitesInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		Map<DiveSite, DiveSite> diveSitesMatch = (Map<DiveSite, DiveSite>) (dataMap
				.get(ImportWizard.IMPORT_DIVESITE_MAP));

		int j = 0;
		for (DiveSite diveSite : diveSitesMatch.values()) {
			if (diveSite == null)
				j++;
		}

		int totalSites = diveSitesMatch.keySet().size();
		text.append("Number of divesite that will be imported : ").append(j);
		text.append("\n");
		text.append("Number of divesite that will be matched : ").append(
				totalSites - j);
		appendText(text.toString(), ITALIC_GRAY);
	}

	@SuppressWarnings("unchecked")
	private void appendMaterialsInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		Map<Material, Material> matMatch = (Map<Material, Material>) (dataMap
				.get(ImportWizard.IMPORT_MATERIAL_MAP));

		int j = 0;
		for (Material mat : matMatch.values()) {
			if (mat == null)
				j++;
		}

		int total = matMatch.keySet().size();
		text.append("Number of materials that will be imported : ").append(j);
		text.append("\n");
		text.append("Number of materials that will be matched : ").append(
				total - j);
		appendText(text.toString(), ITALIC_GRAY);
	}


	@Override
	public String getMessage() {
		return "Go for import...";
	}
}
