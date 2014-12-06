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
package be.vds.jtbdive.client.view.wizard.export;

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
import be.vds.jtbdive.core.logging.Syslog;

public class ExportFinishPanel extends WizardPanel {

	private static final long serialVersionUID = -3391269373786199058L;
	private static final Syslog LOGGER = Syslog.getLogger(ExportFinishPanel.class);
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

	@Override
	public String getMessage() {
		return i18n.getString("wizard.export.message.finish");
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
		}

		detailTextArea.setCaretPosition(0);
	}

	private void appendText(String text, AttributeSet a) {
		try {
			detailTextArea.getDocument().insertString(
					detailTextArea.getDocument().getLength(), text, a);
		} catch (BadLocationException e) {
			LOGGER.error(e);
		}
	}

	private void insertPicture(String iconRef) {
		detailTextArea.insertIcon(UIAgent.getInstance().getIcon(iconRef));
	}

	private void appendFileInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		text.append("Export format : ").append(
				dataMap.get(ExportWizard.EXPORT_FORMAT));
		text.append("\n");
		text.append("Export file : ").append(
				((File) (dataMap.get(ExportWizard.EXPORT_FILE)))
						.getAbsolutePath());

		appendText(text.toString(), ITALIC_GRAY);

	}

	@SuppressWarnings("unchecked")
	private void appendDivesInfo(Map<Object, Object> dataMap) {
		StringBuilder text = new StringBuilder();
		text.append("Number of dives that will be exported : ").append(
				((List<Dive>) (dataMap.get(ExportWizard.EXPORT_DIVES))).size());

		appendText(text.toString(), ITALIC_GRAY);
	}

}
