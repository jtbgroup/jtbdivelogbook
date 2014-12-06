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
package be.vds.jtbdive.client.view.utils;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.java.balloontip.utils.ToolTipUtils;
import be.smd.i18n.I18nResourceManager;
import be.vds.wizard.Wizard;

public class SwingComponentHelper {
	private static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private static final BalloonTipStyle balloonTipStyle = new RoundedBalloonStyle(
			5, 5, UIAgent.getInstance().getColorDetailPanelTop(), UIAgent
					.getInstance().getColorFormatLabelBorder());

	// private static final BalloonTipStyle balloonTipStyle = new
	// ModernBalloonStyle(5, 5, UIAgent.getInstance().getColorDetailPanelTop(),
	// UIAgent.getInstance().getColorDetailPanelBottom(),
	// UIAgent.getInstance().getColorFormatLabelBorder());

	public static JTextArea createTransclucentJTextAreaWithNoBorder(
			String text, boolean editable, boolean lineWrap,
			boolean wrapStyleWord) {
		JTextArea ta = new JTextArea(text);
		ta.setEditable(editable);
		ta.setLineWrap(lineWrap);
		ta.setWrapStyleWord(wrapStyleWord);

		ta.setOpaque(false);
		ta.setBorder(null);
		ta.setBorder(BorderFactory.createEmptyBorder());
		ta.setBackground(new Color(0, 0, 0, 0));

		return ta;
	}

	public static void displayEditorPaneWithNoBorderAndTranslucent(
			JEditorPane editorPane) {
		editorPane.setOpaque(false);
		editorPane.setBorder(null);
		editorPane.setBorder(BorderFactory.createEmptyBorder());
		editorPane.setBackground(new Color(0, 0, 0, 0));
	}

	public static void addI18nToolTip(JComponent component, String bundleKey) {
		addToolTip(component, i18n.getString(bundleKey));
	}

	public static void addToolTip(JComponent component, String text) {
		BalloonTip tooltipBalloon = new BalloonTip(component, text,
				new RoundedBalloonStyle(5, 5, UIAgent.getInstance()
						.getColorBaseBackground(), UIAgent.getInstance()
						.getColorFormatLabelBorder()), false);
		ToolTipUtils.balloonToToolTip(tooltipBalloon, 500, 3000);
	}

	public static void addToolTip(JComponent component, JComponent content) {
		BalloonTip tooltipBalloon = new BalloonTip(component, content,
				new RoundedBalloonStyle(5, 5, UIAgent.getInstance()
						.getColorBaseBackground(), UIAgent.getInstance()
						.getColorFormatLabelBorder()), false);
		ToolTipUtils.balloonToToolTip(tooltipBalloon, 500, 3000);
	}


	public static void displayJScrollPane(JScrollPane scroll) {
		displayJScrollPane(scroll, false);
	}

	public static void displayJScrollPane(JScrollPane scroll, boolean opaque) {
		if (!opaque) {
			scroll.getViewport().setOpaque(false);
			scroll.setViewportBorder(BorderFactory.createEmptyBorder());
			scroll.setOpaque(false);
		}
		scroll.getVerticalScrollBar().setUnitIncrement(
				UIAgent.VERTICAL_UNIT_SCROLL);
	}

	public static void displayWizard(String titleBundleKey, Wizard wizard) {
		wizard.setWizardSize(500, 400);
		wizard.setWizardCentered(true);
		wizard.setTitle(i18n.getString(titleBundleKey));
		wizard.setDefaultIcons();
		wizard.setBackText(i18n.getString("back"));
		wizard.setNextText(i18n.getString("next"));
		wizard.setFinishText(i18n.getString("finish"));
		wizard.setCancelText(i18n.getString("cancel"));
	}

}
