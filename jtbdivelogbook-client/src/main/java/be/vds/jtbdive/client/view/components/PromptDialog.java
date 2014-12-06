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
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.html.HTMLEditorKit;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nButton;
import be.vds.jtb.swing.component.reflection.ReflectionPane;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.util.ImageUtils;
import be.vds.jtbdive.client.view.utils.UIAgent;

public abstract class PromptDialog extends JDialog {

	protected static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private static final long serialVersionUID = -3618751238322181878L;
	public static final int OPTION_ERROR = 0;
	public static final int OPTION_CANCEL = 1;
	public static final int OPTION_OK = 2;
	public static final int OPTION_YES = 2;
	public static final int OPTION_NO = 3;
	public static final int MODE_OK_CANCEL = 0;
	public static final int MODE_YES_NO = 1;
	public static final int MODE_YES_NO_CANCEL = 2;
	private int returnOption;
	private I18nButton okButton;
	private ReflectionPane iconComponent;
	private JCheckBox skipCb;
	private I18nButton cancelButton;
	private I18nButton yesButton;
	private JPanel messagePanel;

	public PromptDialog(String title, String message) {
		this(title, message, MODE_OK_CANCEL);
	}

	public PromptDialog(String title, String message, int options) {
		this(title, message, null, options, null);
	}

	public PromptDialog(String title, String message, String skipMessage) {
		this(title, message, null, MODE_OK_CANCEL, skipMessage);
	}

	public PromptDialog(String title, String message, Image image) {
		this(title, message, image, MODE_OK_CANCEL, null);
	}

	public PromptDialog(String title, String message, Image image,
			int buttonsOptions, String skipMessage) {
		init(title, message, image, buttonsOptions, skipMessage);
	}

	public PromptDialog(Frame parentFrame, String title, String message,
			String skipMessage) {
		this(parentFrame, title, message, null, MODE_OK_CANCEL, skipMessage);
	}

	public PromptDialog(Frame parentFrame, String title, String message) {
		this(parentFrame, title, message, null, MODE_OK_CANCEL, null);
	}

	public PromptDialog(Frame parentFrame, String title, String message,
			int buttonsOptions) {
		this(parentFrame, title, message, null, buttonsOptions, null);
	}

	public PromptDialog(Frame parentFrame, String title, String message,
			Image image, String skipMessage) {
		this(parentFrame, title, message, image, MODE_OK_CANCEL, skipMessage);
	}

	public PromptDialog(Frame parentFrame, String title, String message,
			Image image) {
		this(parentFrame, title, message, image, MODE_OK_CANCEL, null);
	}

	public PromptDialog(Frame parentFrame, String title, String message,
			Image image, int buttonsOptions, String skipMessage) {
		super(parentFrame);
		init(title, message, image, buttonsOptions, skipMessage);
	}

	public PromptDialog(Dialog parentDialog, String title, String message) {
		this(parentDialog, title, message, null, MODE_OK_CANCEL, null);
	}

	public PromptDialog(Dialog dialog, String title, String message,
			Image image) {
		this(dialog, title, message, image, MODE_OK_CANCEL, null);
	}
	
	public PromptDialog(Dialog parentDialog, String title, String message,
			Image image, String skipMessage) {
		this(parentDialog, title, message, image, MODE_OK_CANCEL, skipMessage);
	}


	public PromptDialog(Dialog parentDialog, String title, String message,
			Image image, int buttonsOptions, String skipMessage) {
		super(parentDialog);
		init(title, message, image, buttonsOptions, skipMessage);
	}



	private void init(String title, String message, Image image,
			int buttonsOptions, String skipMessage) {
		doBeforeInit();
		this.setTitle(title);
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().add(
				createFullPane(message, image, buttonsOptions, skipMessage));

		this.setAlwaysOnTop(true);
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					dispose();
			}
		});
	}

	protected void doBeforeInit() {
	}

	private Component createFullPane(String message, Image image,
			int buttonsOptions, String skipMessage) {
		JPanel fullPanel = new JPanel();
		fullPanel.setBackground(UIAgent.getInstance().getColorBaseBackground());
		fullPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		Component c = createContentPanel();

		GridBagLayoutManager.addComponent(fullPanel,
				createMessagePanel(message, image), gc, 0, 0, 1, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		if (c == null) {
			c = Box.createGlue();
		}
		GridBagLayoutManager.addComponent(fullPanel, c, gc, 0, 1, 1, 1, 1, 1,
				GridBagConstraints.BOTH, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(fullPanel,
				createButtonsPanel(buttonsOptions, skipMessage), gc, 0, 2, 1,
				1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		messagePanel.setVisible(null != message || null != image);

		return fullPanel;
	}

	protected abstract Component createContentPanel();

	private Component createMessagePanel(String message, Image image) {
		BorderLayout messageBorderLayout = new BorderLayout(0, 0);
		messagePanel = new JPanel(messageBorderLayout);
		messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		messagePanel.setOpaque(false);

		JComponent messageComp = createMessageComponent(message);
		messagePanel.add(messageComp, BorderLayout.CENTER);

		iconComponent = new ReflectionPane();
		if (image != null) {
			BufferedImage bi = ImageUtils.getResizedBufferedImage(image, 48);
			iconComponent.setBufferedImage(bi);
			Dimension d = new Dimension(48, 72);
			iconComponent.setPreferredSize(d);
			iconComponent.setMinimumSize(d);
			iconComponent.setMinimumSize(d);
			messageBorderLayout.setHgap(10);
		}
		messagePanel.add(iconComponent, BorderLayout.WEST);

		return messagePanel;
	}


	private JComponent createMessageComponent(String message) {
		if (message != null && message.startsWith("<html>")
				&& message.endsWith("</html>")) {
			JEditorPane editor = new JEditorPane();
			editor.setEditorKit(new HTMLEditorKit());
			editor.setText(message);
			editor.setEditable(false);
			return editor;
		}

		JTextArea messageTextArea = new JTextArea();
		messageTextArea.setText(message);
		messageTextArea.setFont(UIAgent.getInstance().getFontPromptMessage());
		messageTextArea.setBorder(null);
		messageTextArea.setOpaque(false);
		messageTextArea.setEditable(false);
		messageTextArea.setLineWrap(true);
		messageTextArea.setWrapStyleWord(true);
		return messageTextArea;
	}

	private Component createButtonsPanel(int buttonsOptions, String skipMessage) {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPanel.setOpaque(false);

		if (buttonsOptions == MODE_OK_CANCEL) {
			JButton b = createOkButton();
			buttonsPanel.add(b);
			b = createCancelButton();
			buttonsPanel.add(b);
		} else if (buttonsOptions == MODE_YES_NO) {
			JButton b = createYesButton();
			buttonsPanel.add(b);
			b = createNoButton();
			buttonsPanel.add(b);
		} else if (buttonsOptions == MODE_YES_NO_CANCEL) {
			JButton b = createYesButton();
			buttonsPanel.add(b);
			b = createNoButton();
			buttonsPanel.add(b);
			buttonsPanel.add(Box.createHorizontalStrut(10));
			b = createCancelButton();
			buttonsPanel.add(b);

		}
		;

		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setOpaque(false);

		if (null != skipMessage) {
			skipCb = new JCheckBox(skipMessage);
			skipCb.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					performSkipAction();
				}

			});
			skipCb.setOpaque(false);
			skipCb.setFocusable(false);
			southPanel.add(skipCb, BorderLayout.NORTH);
		}
		southPanel.add(buttonsPanel, BorderLayout.SOUTH);
		return southPanel;
	}

	public boolean skipSelected() {
		return skipCb == null ? false : skipCb.isSelected();
	}

	private JButton createCancelButton() {
		Action cancelAction = new AbstractAction("cancel") {

			private static final long serialVersionUID = 921672782122434129L;

			@Override
			public void actionPerformed(ActionEvent e) {
				returnOption = OPTION_CANCEL;
				dispose();
			}
		};

		cancelButton = new I18nButton(cancelAction);
		return cancelButton;
	}

	private JButton createNoButton() {
		Action noAction = new AbstractAction("no") {

			private static final long serialVersionUID = 921672782122434129L;

			@Override
			public void actionPerformed(ActionEvent e) {
				returnOption = OPTION_NO;
				dispose();
			}
		};

		I18nButton noButton = new I18nButton(noAction);
		return noButton;
	}

	private JButton createOkButton() {
		Action okAction = new AbstractAction("ok") {

			private static final long serialVersionUID = 5676836530417104046L;

			@Override
			public void actionPerformed(ActionEvent e) {
				performOkAction();
			}
		};

		okButton = new I18nButton(okAction);
		return okButton;
	}

	private JButton createYesButton() {
		Action yesAction = new AbstractAction("yes") {

			private static final long serialVersionUID = 5676836530417104046L;

			@Override
			public void actionPerformed(ActionEvent e) {
				performYesAction();
			}
		};

		yesButton = new I18nButton(yesAction);
		return yesButton;
	}

	protected void performSkipAction() {
	}

	protected void performOkAction() {
		returnOption = OPTION_OK;
		dispose();
	}

	protected void performYesAction() {
		returnOption = OPTION_YES;
		dispose();
	}

	public int showDialog() {
		return showDialog(false, -1, -1, true);
	}

	public int showDialog(int width, int height) {
		return showDialog(false, width, height, true);
	}

	public int showDialog(boolean pack) {
		return showDialog(pack, -1, -1, true);
	}

	private int showDialog(boolean pack, int width, int height,
			boolean centerWindow) {
		if (pack) {
			this.pack();
		} else if (width > -1 && height > -1) {
			this.setSize(width, height);
		} else {
			this.setSize(350, 450);
		}

		if (centerWindow) {
			this.setLocationRelativeTo(null);
		}

		this.setVisible(true);
		return returnOption;
	}

	protected void setOkButtonEnabled(boolean enabled) {
		okButton.setEnabled(enabled);
	}

	protected void setYesButtonEnabled(boolean enabled) {
		yesButton.setEnabled(enabled);
	}

	protected void setCancelButtonEnabled(boolean enabled) {
		cancelButton.setEnabled(enabled);
	}

	protected void setReturnOption(int returnOption) {
		this.returnOption = returnOption;
	}

}
