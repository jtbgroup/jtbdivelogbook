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
package be.vds.jtbdive.client.view.core.logbook;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.logging.Syslog;

public class LogBookChooserDialog extends PromptDialog {

	private static final long serialVersionUID = -7379417073493950323L;
	private static final Syslog LOGGER = Syslog
			.getLogger(LogBookChooserDialog.class);
	private List<LogBook> logBooks;
	private JXList list;
	private DefaultListModel listModel;

	public LogBookChooserDialog(JFrame parentFrame,
			LogBookManagerFacade logBookManagerFacade) {
		super(parentFrame, i18n.getString("logbook"), i18n
				.getString("logbook.selection.message"));
		initComponent(parentFrame, logBookManagerFacade);
	}

	private void initComponent(JFrame parentFrame,
			LogBookManagerFacade logBookManagerFacade) {
		try {
			logBooks = logBookManagerFacade.getLogBookNames();
			if (logBooks == null)
				logBooks = new ArrayList<LogBook>();
		} catch (DataStoreException e) {
			LOGGER.error(e);
			ExceptionDialog.showDialog(e, parentFrame);
		}
		setLogBookList(logBooks);
		list.grabFocus();
		setOkButtonEnabled(false);
	}

	@Override
	protected Component createContentPanel() {
		listModel = new DefaultListModel();
		list = new JXList(listModel);
		list.setCellRenderer(new ListCellRenderer() {
			protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) defaultRenderer
						.getListCellRendererComponent(list, value, index,
								isSelected, cellHasFocus);
				renderer.setOpaque(isSelected);
				renderer.setText(((LogBook) value).getName());
				return renderer;
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && -1 < e.getFirstIndex()) {
					// openButton.setEnabled(true);
					setOkButtonEnabled(true);
				} else {
					// openButton.setEnabled(false);
					setOkButtonEnabled(false);
				}
			}
		});
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (list.getSelectedIndex() > -1 && e.getClickCount() == 2) {
					// returnValue = OPTION_OPEN;
					setReturnOption(OPTION_OK);
					dispose();
				}
			}
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (list.getSelectedIndex() > -1
						&& e.getKeyCode() == KeyEvent.VK_ENTER) {
					// returnValue = OPTION_OPEN;
					setReturnOption(OPTION_OK);
					dispose();
				}
			}
		});
//		list.setOpaque(false);
		
		JScrollPane scroll = new JScrollPane(list);
//		SwingComponentHelper.displayJScrollPane(scroll);

		DetailPanel d = new DetailPanel(new BorderLayout());
		d.add(scroll, BorderLayout.CENTER);
		return d;
	}

	public void setLogBookList(List<LogBook> logbooks) {
		listModel.clear();
		for (LogBook lb : logbooks) {
			listModel.addElement(lb);
		}
	}

	public LogBook getSelectedLogBook() {
		return (LogBook) list.getSelectedValue();
	}
}
