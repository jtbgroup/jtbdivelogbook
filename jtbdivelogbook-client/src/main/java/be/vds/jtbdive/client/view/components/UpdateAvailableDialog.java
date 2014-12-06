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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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

import org.jdesktop.swingx.JXBusyLabel;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.smd.i18n.swing.I18nCheckBox;
import be.vds.jtbdive.client.core.UserPreferences;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class UpdateAvailableDialog extends JDialog {

	private static final long serialVersionUID = -3590185298828382341L;
	private static final Syslog LOGGER = Syslog
			.getLogger(UpdateAvailableDialog.class);
	private I18nCheckBox skipCb;
	private I18nButton closeButton;
	private JLabel titleLabel;
	private JTextArea messageTextArea;
	private CardLayout cardLayout;
	private DetailPanel cardPanel;
	private JXBusyLabel busy;
	private boolean skipEnable;

	public UpdateAvailableDialog(JFrame parentFrame, String newVersion,
			boolean skipEnable) {
		super(parentFrame, I18nResourceManager.sharedInstance().getString(
				"message"), true);
		this.skipEnable = skipEnable;
		init();

		loadMessage(newVersion);
	}

	private void loadMessage(final String newVersion) {

		new Thread(new VersionRunnable(newVersion)).start();
	}

	private void createWarningMessage() {
		messageTextArea.setText(I18nResourceManager.sharedInstance().getString(
				"connection.problem.message"));
		titleLabel.setText(I18nResourceManager.sharedInstance().getString(
				"connection.problem"));
	}

	private void createUpdateMessage(String newVersion) {
		titleLabel.setText(I18nResourceManager.sharedInstance().getString(
				"new.version"));
		String s = I18nResourceManager.sharedInstance().getMessage(
				"new.version.message.args", new Object[]{newVersion});
		messageTextArea.setText(s);
	}

	private void createNoUpdateMessage(String newVersion) {
		titleLabel.setText(I18nResourceManager.sharedInstance().getString(
				"no.update"));
		String s = I18nResourceManager.sharedInstance().getMessage(
				"no.update.message.args", new Object[]{newVersion});
		messageTextArea.setText(s);
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
		titleLabel = new JLabel();
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		Font basicFont = titleLabel.getFont();
		titleLabel.setFont(new Font(basicFont.getFamily(), Font.ITALIC
				+ Font.BOLD, 20));

		messageTextArea = new JTextArea();
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setEditable(false);
		messageTextArea.setOpaque(false);
		messageTextArea
				.setFont(new Font(basicFont.getFamily(), Font.ITALIC, 12));
		messageTextArea.setPreferredSize(new Dimension(500, 100));

		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BorderLayout());
		p.add(titleLabel, BorderLayout.NORTH);
		p.add(messageTextArea, BorderLayout.CENTER);

		cardLayout = new CardLayout();
		cardPanel = new DetailPanel(cardLayout);
		busy = new JXBusyLabel();
		busy.setBusy(true);
		cardPanel.add(busy, "card.busy");
		cardPanel.add(p, "card.message");

		return cardPanel;
	}

	public JComponent createButtonsPanel() {
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(createCloseButton(), BorderLayout.EAST);
		if (skipEnable) {
			bottom.add(createSkipButton(), BorderLayout.WEST);
		}
		bottom.setOpaque(false);
		return bottom;

	}

	private Component createSkipButton() {
		skipCb = new I18nCheckBox("hide.next.time");
		skipCb.setOpaque(false);
		skipCb.setFocusable(false);
		skipCb.setContentAreaFilled(false);
		return skipCb;
	}

	private Component createCloseButton() {
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
		return p;
	}

	private void closeDialog() {
		if (skipEnable) {
			boolean skip = skipCb.isSelected();
			boolean mustUpdate = UserPreferences.getInstance().checkUpdatesOnStartup();
			if (skip == mustUpdate) {
				UserPreferences.getInstance().setCheckUpdatesOnStartUp(!skip);
				UserPreferences.getInstance().savePreferences(false);
			}
		}
		this.dispose();
	}

	public boolean canSkip() {
		return skipCb == null ? false : skipCb.isSelected();
	}

	class VersionRunnable implements Runnable {

		private String newVersion;

		public VersionRunnable(String newVersion) {
			this.newVersion = newVersion;
		}

		@Override
		public void run() {
			boolean connectionFailed = false;
			if (null == newVersion) {
				try {
					newVersion = ResourceManager.getInstance()
							.getLastReleaseVersion();
					if(newVersion == null)
						newVersion = Version.getCurrentVersion().toString();
				} catch (TransferException e) {
					connectionFailed = true;
					LOGGER.error(e);
				}
			}

			if (!connectionFailed) {
				if (Version.getCurrentVersion().isLowerThan(newVersion)) {
					createUpdateMessage(newVersion);
				} else {
					createNoUpdateMessage(newVersion);
				}
			} else {
				createWarningMessage();
			}

			cardLayout.show(cardPanel, "card.message");
			busy.setBusy(false);
		}
	}
}
