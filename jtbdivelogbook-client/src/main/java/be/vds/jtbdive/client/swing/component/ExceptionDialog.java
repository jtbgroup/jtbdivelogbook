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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtb.swing.utils.WindowUtils;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class ExceptionDialog extends JDialog {

	private static final long serialVersionUID = -3134282004722432760L;
	private Throwable exception;
	private String additionnalMessage;
	private JScrollPane stackTraceScroll;
	private Font font = new Font("Arial", Font.PLAIN, 11);
	private boolean expandStackTrace;
	private String title;

	private ExceptionDialog(JDialog parentDialog, Throwable exception,
			String title, String additionnalMessage, boolean expandStackTrace) {
		super(parentDialog, true);
		initDialog(exception, title, additionnalMessage, expandStackTrace);
	}

	private ExceptionDialog(JFrame parentFrame, Throwable exception,
			String title, String additionnalMessage, boolean expandStackTrace) {
		super(parentFrame, true);
		initDialog(exception, title, additionnalMessage, expandStackTrace);
	}

	private ExceptionDialog(Throwable exception, String title,
			String additionnalMessage, boolean expandStackTrace) {
		this.setModal(true);
		initDialog(exception, title, additionnalMessage, expandStackTrace);
	}

	private void initDialog(Throwable exception, String title,
			String additionnalMessage, boolean expandStackTrace) {
		this.title = title;
		this.exception = exception;
		this.additionnalMessage = additionnalMessage;
		this.expandStackTrace = expandStackTrace;
		init();
	}

	private void init() {
		this.setTitle(this.title == null ? "Exception" : this.title);
		this.getContentPane().add(createContentPane());
		this.pack();
		if (getOwner() != null) {
			WindowUtils.centerWindow(ExceptionDialog.this, getOwner());
		} else {
			WindowUtils.centerWindow(ExceptionDialog.this);
		}
	}

	private Component createContentPane() {
		JPanel contentPane = new JPanel() {

			private static final long serialVersionUID = -4293519287353073463L;

			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setPaint(Color.WHITE);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setPaint(new GradientPaint(getWidth() / 2, 0,
						Color.LIGHT_GRAY, getWidth() / 2, 50, Color.WHITE));
				g2.fillRect(0, 0, getWidth(), 50);
			}
		};
		JLabel iconLabel = new JLabel(UIAgent.getInstance().getIcon(
				UIAgent.ICON_CHECKMARK_RED_32));
		JPanel iconPanel = new JPanel(new BorderLayout());
		iconPanel.setOpaque(false);
		iconPanel.add(iconLabel, BorderLayout.NORTH);

		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		contentPane.add(createExceptionPanel(), BorderLayout.CENTER);
		contentPane.add(createButtonsPanel(), BorderLayout.SOUTH);
		contentPane.add(iconPanel, BorderLayout.WEST);

		return contentPane;
	}

	private Component createExceptionPanel() {
		JPanel exceptionPanel = new JPanel();
		exceptionPanel.setOpaque(false);
		exceptionPanel.setLayout(new BorderLayout(3, 10));
		exceptionPanel.add(createTopPanel(), BorderLayout.NORTH);

		if (null != exception) {
			exceptionPanel.add(createStackPanel(), BorderLayout.CENTER);
		}

		return exceptionPanel;
	}

	private Component createTopPanel() {
		JPanel p = new JPanel(new BorderLayout(5, 0));
		p.setOpaque(false);
		p.add(createAdditionalTextArea(), BorderLayout.CENTER);
		return p;
	}

	public Component createAdditionalTextArea() {
		Font font = new Font("Arial", Font.PLAIN, 13);
		JTextArea descriptionArea = new JTextArea();
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setText(getDescriptionText());
		descriptionArea.setFont(font);
		descriptionArea.setColumns(35);
		descriptionArea.setEditable(false);

		descriptionArea.setOpaque(false);
		descriptionArea.setBorder(null);
		descriptionArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		descriptionArea.setBackground(new Color(0, 0, 0, 0));

		return descriptionArea;
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			pack();
		}
		super.setVisible(b);
	}

	private String getDescriptionText() {
		if (additionnalMessage != null) {
			return additionnalMessage;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(exception.getClass().getName());
			if (exception.getMessage() != null) {
				sb.append("\r\n");
				sb.append(exception.getMessage());
			}
			return sb.toString();
		}
	}

	private Component createButtonsPanel() {
		JButton b = new JButton(new AbstractAction("Close") {

			private static final long serialVersionUID = 466678502484594699L;

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton mailButton = new JButton(
				new AbstractAction("Copy to clipboard") {

					private static final long serialVersionUID = -9067905592710470394L;

					public void actionPerformed(ActionEvent e) {
						copyException();
					}
				});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
		p.setOpaque(false);
		p.add(mailButton);
		p.add(b);

		return p;
	}

	private void copyException() {
		StringBuilder sb = new StringBuilder();
		if (null != additionnalMessage) {
			sb.append("Additionnal message :\r\n");
			sb.append("=====================\r\n");
			sb.append(additionnalMessage);
			sb.append("\r\n");
			sb.append("\r\n");
		}
		if (null != exception) {
			sb.append(getExceptionStack(exception, 0));
		}
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(sb.toString()), null);
	}

	private String getExceptionStack(Throwable throwable, int level) {
		StringBuilder sb = new StringBuilder();
		String tabs = "";
		for (int i = 0; i < level; i++) {
			tabs = tabs + "   ";
		}

		sb.append(tabs);
		sb.append("Exception: ");
		sb.append(throwable.getClass().getName());
		sb.append("\r\n");
		if (throwable.getMessage() != null) {
			sb.append(tabs);
			sb.append("Message: ");
			sb.append(throwable.getMessage());
			sb.append("\r\n");
		}

		StackTraceElement[] lines = throwable.getStackTrace();
		for (StackTraceElement stackTraceElement : lines) {
			sb.append(tabs).append(" ");
			sb.append(stackTraceElement.toString());
			sb.append("\r\n");
		}

		if (throwable.getCause() != null) {
			sb.append("\r\n");
			sb.append(getExceptionStack(throwable.getCause(), level + 1));
		}
		return sb.toString();
	}

	public static void showDialog(Throwable ex, Component parentComponent) {
		showDialog(ex, parentComponent, null, true);
	}

	public static void showDialog(String message, Component parentComponent) {
		showDialog(null, parentComponent, message, true);
	}

	public static void showDialog(Throwable ex, Component parentComponent,
			String additionnalMessage) {
		showDialog(ex, parentComponent, additionnalMessage, true);
	}

	public static void showDialog(Throwable ex, Component parentComponent,
			String additionnalMessage, boolean expandStackTrace) {
		ExceptionDialog d = null;
		if (null == parentComponent) {
			d = new ExceptionDialog(ex, null, additionnalMessage,
					expandStackTrace);
		} else {
			Window w = WindowUtils.getTopLevelWindow(parentComponent);
			if (w instanceof JFrame) {
				d = new ExceptionDialog((JFrame) w, ex, null,
						additionnalMessage, expandStackTrace);
			} else if (w instanceof JDialog) {
				d = new ExceptionDialog((JDialog) w, ex, null,
						additionnalMessage, expandStackTrace);
			} else {
				d = new ExceptionDialog(ex, null, additionnalMessage,
						expandStackTrace);
			}
		}

		WindowUtils.centerWindow(d);
		d.setVisible(true);
	}

	private Component createStackPanel() {
		stackTraceScroll = new JScrollPane(constructStackTraceTextArea());
		stackTraceScroll.setVisible(expandStackTrace);

		JLabel nestedLabel = new JLabel("Stack Trace");
		nestedLabel.setFont(font);

		final JLabel buttonLabel = new JLabel(expandStackTrace ? UIAgent
				.getInstance().getIcon(UIAgent.ICON_BULLET_ARROW_UP) : UIAgent
				.getInstance().getIcon(UIAgent.ICON_BULLET_ARROW_DOWN));
		buttonLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				boolean b = stackTraceScroll.isVisible();
				stackTraceScroll.setVisible(!b);
				buttonLabel.setIcon(!b ? UIAgent.getInstance().getIcon(
						UIAgent.ICON_BULLET_ARROW_UP) : UIAgent.getInstance()
						.getIcon(UIAgent.ICON_BULLET_ARROW_DOWN));
				pack();
			}
		});

		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1, 0, 0, 0);
		JComponent box = new JComponent() {
			private static final long serialVersionUID = -279587274636579148L;
		};
		box.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0,
				Color.LIGHT_GRAY));

		GridBagLayoutManager.addComponent(panel, box, c, 0, 0, 2, 1, 1, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

		GridBagLayoutManager
				.addComponent(panel, nestedLabel, c, 0, 1, 1, 1, 1, 0,
						GridBagConstraints.NONE,
						GridBagConstraints.FIRST_LINE_START);

		GridBagLayoutManager.addComponent(panel, buttonLabel, c, 1, 1, 1, 1, 0,
				0, GridBagConstraints.NONE, GridBagConstraints.FIRST_LINE_END);

		GridBagLayoutManager.addComponent(panel, stackTraceScroll, c, 0, 2, 2,
				1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		return panel;
	}

	private JTextArea constructStackTraceTextArea() {
		JTextArea inboundTextArea = new JTextArea();
		inboundTextArea.setFont(font);
		inboundTextArea.setEditable(false);

		StringBuilder sb = new StringBuilder();
		sb.append(getExceptionStack(exception, 0));

		inboundTextArea.setText(sb.toString());
		inboundTextArea.setCaretPosition(0);
		inboundTextArea.setRows(7);
		inboundTextArea.setColumns(35);
		return inboundTextArea;
	}

	
}
