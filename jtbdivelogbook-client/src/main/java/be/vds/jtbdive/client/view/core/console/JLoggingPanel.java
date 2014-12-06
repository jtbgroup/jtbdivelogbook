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
package be.vds.jtbdive.client.view.core.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.logging.ListenerAppender;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * This component listens a logger appender and write the log in a console. The
 * bufferMaxSize corresponds to the number of log record that is cached and
 * displayed.
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class JLoggingPanel extends DetailPanel {

	private static final long serialVersionUID = -2374783387943115919L;
	public static final String LEVEL_CHANGED = "level.changed";
	private static final Syslog LOGGER = Syslog.getLogger(JLoggingPanel.class);
	private static final Integer[] levels = new Integer[] { LogEntry.LVL_DEBUG,
			LogEntry.LVL_INFO, LogEntry.LVL_WARN, LogEntry.LVL_ERROR,
			LogEntry.LVL_FATAL };
	public static final String[] LVL_NAMES = { "DEBUG", "INFO", "WARN",
			"ERROR", "FATAL" };
	private JList loggingComponent;
	private boolean isEmpty = true;
	private JButton garbageButton;
	private int bufferMaxSize = 100;
	private DefaultListModel loggingListModel;
	private Map<Integer, Color> foregroundColors = new HashMap<Integer, Color>();
	private boolean logOnTop;
	private JComboBox levelCombo;
	private JScrollPane loggingScroll;
	private JButton logOrderButton;
	private Color consoleBackground = Color.WHITE;
	private JSpinner lineSpinner;
	private JLabel numberOfLineLabel;
	{
		foregroundColors.put(LogEntry.LVL_DEBUG, Color.GREEN);
		foregroundColors.put(LogEntry.LVL_INFO, Color.BLACK);
		foregroundColors.put(LogEntry.LVL_WARN, Color.ORANGE);
		foregroundColors.put(LogEntry.LVL_ERROR, Color.RED);
		foregroundColors.put(LogEntry.LVL_FATAL, Color.RED);
	}

	/**
	 * A Contructor that has no appender and a buffer size of 100.
	 * 
	 * @author Gautier Vanderslyen
	 */
	public JLoggingPanel() {
		this(100, true);
	}

	/**
	 * A constructor that listens on a specific {@link ListenerAppender} and has
	 * a defined buffer size.
	 * 
	 * @param bufferMaxSize
	 * @param appender
	 * @author Gautier Vanderslyen
	 */
	public JLoggingPanel(int bufferMaxSize, boolean logOnTop) {
		this.bufferMaxSize = bufferMaxSize;
		this.logOnTop = logOnTop;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		add(createHeader(), BorderLayout.NORTH);
		add(createLoggingScroll(), BorderLayout.CENTER);
		// add(createNumberOfLine(), BorderLayout.SOUTH);
	}

	private Component createNumberOfLine() {
		numberOfLineLabel = new JLabel("0");
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.setOpaque(false);
		p.add(numberOfLineLabel);
		return p;
	}

	private Component createHeader() {
		JPanel header = new JPanel();

		header.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		int x = 0;
		GridBagLayoutManager.addComponent(header, createLevelCombobox(), c, x,
				0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.WEST);

		GridBagLayoutManager.addComponent(header, Box.createHorizontalGlue(),
				c, ++x, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(header,
				createCopyClipBoardComponent(), c, ++x, 0, 1, 1, 0, 0,
				GridBagConstraints.NONE, GridBagConstraints.EAST);

		GridBagLayoutManager.addComponent(header, createOrderComponent(), c,
				++x, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.EAST);

		GridBagLayoutManager.addComponent(header, createNumberOfLine(), c, ++x,
				0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(header, new JLabel("/"), c, ++x, 0,
				1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(header, createLineBufferComponent(),
				c, ++x, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);

		GridBagLayoutManager.addComponent(header, createGarbageButton(), c,
				++x, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		return header;
	}

	private Component createOrderComponent() {
		logOrderButton = new JButton(new AbstractAction() {
			private static final long serialVersionUID = 2795119383637481885L;

			@Override
			public void actionPerformed(ActionEvent e) {
				switchSorting();
			}
		});
		logOrderButton.setIcon(UIAgent.getInstance().getIcon(
				logOnTop ? UIAgent.ICON_ARROW_UP_16
						: UIAgent.ICON_ARROW_DOWN_16));
		logOrderButton.setBorderPainted(false);
		logOrderButton.setContentAreaFilled(true);
		logOrderButton.setFocusable(false);
		return logOrderButton;
	}

	public void switchSorting() {
		logOrderButton.setIcon(logOnTop ? UIAgent.getInstance().getIcon(
				UIAgent.ICON_ARROW_DOWN_16) : UIAgent.getInstance().getIcon(
				UIAgent.ICON_ARROW_UP_16));
		logOnTop = !logOnTop;
		reverseOrderMessages();
		LOGGER.debug("Log on top = " + logOnTop);
	}

	private void reverseOrderMessages() {
		LogEntry[] events = new LogEntry[loggingListModel.size()];
		for (int i = 0; i < loggingListModel.size(); i++) {
			events[i] = (LogEntry) loggingListModel.get(i);
		}

		int length = events.length;
		loggingListModel.removeAllElements();
		for (int i = 0; i < length; i++) {
			loggingListModel.addElement(events[length - 1 - i]);
		}

	}

	private Component createCopyClipBoardComponent() {

		JButton b = new JButton(new AbstractAction() {

			private static final long serialVersionUID = 5472723666283110629L;

			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < loggingListModel.getSize(); i++) {
					LogEntry event = (LogEntry) loggingListModel
							.getElementAt(i);
					sb.append(event.getText());
				}
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(new StringSelection(sb.toString()), null);
			}
		});
		b.setIcon(UIAgent.getInstance().getIcon(UIAgent.ICON_CLIPBOARD_16));
		b.setBorderPainted(false);
		b.setContentAreaFilled(true);
		b.setFocusable(false);
		return b;
	}

	/**
	 * Sets the level of the logger in the combobox.
	 * 
	 * @param level
	 *            the LogEntry level
	 */
	public void setLevel(int level) {
		levelCombo.setSelectedItem(level);
	}

	private Component createLevelCombobox() {
		levelCombo = new JComboBox(levels);
		levelCombo.setBackground(UIAgent.getInstance().getColorConsoleBkg());

		levelCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -4782584345096700134L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list,
						value, index, isSelected, cellHasFocus);
				Integer level = (Integer) value;
				l.setText(LVL_NAMES[level]);
				l.setForeground(foregroundColors.get(level));

				if (!isSelected) {
					l.setBackground(consoleBackground);
				}

				return this;
			}
		});
		levelCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adaptLevel((Integer) levelCombo.getSelectedItem());
			}
		});

		return levelCombo;
	}

	private Component createLineBufferComponent() {

		lineSpinner = new JSpinner(new SpinnerNumberModel(bufferMaxSize, 10,
				1000, 5));
		lineSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				bufferMaxSize = (Integer) ((JSpinner) (e.getSource()))
						.getValue();
				adaptRowSize();
			}
		});

		return lineSpinner;
	}

	private Component createGarbageButton() {
		garbageButton = new JButton(new AbstractAction() {
			private static final long serialVersionUID = -3827757978409448981L;

			public void actionPerformed(ActionEvent e) {
				loggingListModel.removeAllElements();
				setIsEmpty(true);
				adaptNumberOfRowLabel();
			}
		});
		garbageButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_TRASH_EMPTY_16));
		garbageButton.setBorderPainted(false);
		garbageButton.setFocusPainted(false);
		garbageButton.setContentAreaFilled(true);
		garbageButton.setFocusable(false);
		return garbageButton;
	}

	private Component createLoggingScroll() {
		loggingListModel = new DefaultListModel();
		loggingComponent = new JList(loggingListModel) {
			public String getToolTipText(MouseEvent evt) {
				// Get item index
				int index = locationToIndex(evt.getPoint());

				// Get item
				LogEntry item = (LogEntry) (getModel().getElementAt(index));

				// Return the tool tip text
				return item.getText();
			}
		};
		loggingComponent.setCellRenderer(new LogginCellRenderer());
		loggingComponent.setBackground(consoleBackground);

		loggingScroll = new JScrollPane(loggingComponent);
		loggingScroll.setPreferredSize(new Dimension(300, 120));
		return loggingScroll;
	}

	private void adaptLevel(int level) {
		firePropertyChange(LEVEL_CHANGED, null, level);
	}

	public void log(final String s, final int level) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (logOnTop) {
					loggingListModel.add(0, new LogEntry(s, level));
					loggingComponent.ensureIndexIsVisible(0);
				} else {
					int row = loggingListModel.getSize();
					loggingListModel.add(row, new LogEntry(s, level));
					try {
						loggingComponent.ensureIndexIsVisible(row);
					} catch (ArrayIndexOutOfBoundsException e) {
						LOGGER.error(e);
					}
				}
				adaptRowSize();
				setIsEmpty(false);
			}
		});
	}

	private void adaptRowSize() {
		while (loggingListModel.size() > bufferMaxSize) {
			if (logOnTop) {
				loggingListModel.remove(loggingListModel.size() - 1);
			} else {
				loggingListModel.remove(0);
			}
		}
		adaptNumberOfRowLabel();
	};

	private void adaptNumberOfRowLabel() {
		numberOfLineLabel.setText(String.valueOf(loggingListModel.size()));
	}

	private void setIsEmpty(boolean b) {
		if (isEmpty && !b) {
			isEmpty = b;
			garbageButton.setIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_TRASH_FULL_16));
		} else if (!isEmpty && b) {
			isEmpty = b;
			garbageButton.setIcon(UIAgent.getInstance().getIcon(
					UIAgent.ICON_TRASH_EMPTY_16));
		}
	}

	/**
	 * Changes the buffer size.
	 * 
	 * @param bufferMaxSize
	 * @author Gautier Vanderslyen
	 */
	public void setBufferMaxSize(int bufferMaxSize) {
		this.bufferMaxSize = bufferMaxSize;
		lineSpinner.setValue(bufferMaxSize);
	}

	/**
	 * Sets the background color of the text panel and the logging level combobox
	 * 
	 * @param color
	 * @author Gautier Vanderslyen
	 */
	public void setConsoleBackgroundColor(Color color) {
		this.consoleBackground = color;
		loggingComponent.setBackground(color);
		levelCombo.setBackground(color);
	}

	/**
	 * Sets the foreground colors of the text panel. Levels are constants in the
	 * class {@link Syslog};
	 * 
	 * @param color
	 * @author Gautier Vanderslyen
	 */
	public void setConsoleForegroundColors(Map<Integer, Color> colors) {
		for (Integer level : colors.keySet()) {
			foregroundColors.put(level, colors.get(level));
		}
	}

	private class LogginCellRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = -3854220970400695888L;

		public LogginCellRenderer() {
			this.setOpaque(false);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			LogEntry event = (LogEntry) value;

			if (event.getLevel() >= LogEntry.LVL_FATAL) {
				setForeground(foregroundColors.get(LogEntry.LVL_FATAL));
			} else if (event.getLevel() >= LogEntry.LVL_ERROR) {
				setForeground(foregroundColors.get(LogEntry.LVL_ERROR));
			} else if (event.getLevel() >= LogEntry.LVL_WARN) {
				setForeground(foregroundColors.get(LogEntry.LVL_WARN));
			} else if (event.getLevel() >= LogEntry.LVL_INFO) {
				setForeground(foregroundColors.get(LogEntry.LVL_INFO));
			} else if (event.getLevel() >= LogEntry.LVL_DEBUG) {
				setForeground(foregroundColors.get(LogEntry.LVL_DEBUG));
			} else {
				setForeground(foregroundColors.get(LogEntry.LVL_INFO));
			}
			setText(event.getText());

			return this;
		}
	}

	public class LogEntry {
		public static final int LVL_DEBUG = 0;
		public static final int LVL_INFO = 1;
		public static final int LVL_WARN = 2;
		public static final int LVL_ERROR = 3;
		public static final int LVL_FATAL = 4;

		private int level;
		private String text;

		public int getLevel() {
			return level;
		}

		public String getText() {
			return text;
		}

		public LogEntry(String text, int level) {
			this.text = text;
			this.level = level;
		}
	}

	public boolean isLogOnTop() {
		return logOnTop;
	}
}
