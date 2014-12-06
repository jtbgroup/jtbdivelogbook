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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import be.smd.i18n.swing.I18nButton;
import be.smd.i18n.swing.I18nCheckBox;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.swing.component.DetailPanel;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class StartupMessageDialog extends JDialog {

	private static final long serialVersionUID = -3590185298828382341L;
	private I18nCheckBox skipCb;
	private final String messageTitle = "Thanks for using JTB Dive LogBook";
	private final String welcomeMessage = "If you want to contribute, report a bug, ask new features or just give your opinion about this software, please visit the sourceforge page at http://jtbdivelogbook.sourceforge.net";
	private I18nButton closeButton;

	public StartupMessageDialog(JFrame parentFrame) {
		super(parentFrame, "Message", true);
		init();
	}

	private void init() {
		this.getContentPane().add(createContentPanel());

		this.getRootPane().setDefaultButton(closeButton);
		closeButton.requestFocus();
		this.pack();
	}

	public JComponent createContentPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(createMessagePanel(), BorderLayout.CENTER);
		p.add(createButtonsPanel(), BorderLayout.SOUTH);
		p.setBackground(Color.WHITE);
		p.setOpaque(true);
		return p;
	}

	public JComponent createMessagePanel() {
		JLabel titleLabel = new JLabel(messageTitle);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		Font basicFont = titleLabel.getFont();
		titleLabel.setFont(new Font(basicFont.getFamily(), Font.ITALIC
				+ Font.BOLD, 20));

		JTextArea messageTextArea = new JTextArea();
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setText(welcomeMessage);
		messageTextArea.setEditable(false);
		messageTextArea.setOpaque(false);
		messageTextArea
				.setFont(new Font(basicFont.getFamily(), Font.ITALIC, 12));
		messageTextArea.setPreferredSize(new Dimension(500, 100));

		DetailPanel p = new DetailPanel();
		p.setLayout(new BorderLayout());
		p.add(titleLabel, BorderLayout.NORTH);
		p.add(messageTextArea, BorderLayout.CENTER);

		return p;
	}

	public JComponent createButtonsPanel() {
		closeButton = new I18nButton(new AbstractAction("close") {

			private static final long serialVersionUID = -6449434406644549261L;

			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.setOpaque(false);
		p.add(closeButton);

		skipCb = new I18nCheckBox("hide.next.time");
		skipCb.setOpaque(false);

		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(p, BorderLayout.EAST);
		bottom.add(skipCb, BorderLayout.WEST);
		bottom.setOpaque(false);
		return bottom;

	}

	private void closeDialog() {
		boolean skip = skipCb.isSelected();
		boolean skipProp = UserPreferences.getInstance().skipStartupMessage();
		if (skip != skipProp) {
			UserPreferences.getInstance().setSkipStartupMessage(skip);
			UserPreferences.getInstance().savePreferences(false);
		}
		this.dispose();
	}
}
