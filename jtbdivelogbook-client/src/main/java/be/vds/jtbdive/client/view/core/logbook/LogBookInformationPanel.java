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
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.LogBookUtilities;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.view.table.I18nStringMapTableModel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.LogBook;

public class LogBookInformationPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 5869245206018091008L;
	private I18nStringMapTableModel dataTableModel;
	private JXTable dataTable;
	private LogBookManagerFacade logBookManagerFacade;

	public LogBookInformationPanel(LogBookManagerFacade logBookManagerFacade) {
		init();
		this.logBookManagerFacade = logBookManagerFacade;
		logBookManagerFacade.addObserver(this);
		UIAgent.getInstance().addObserver(this);
		UnitsAgent.getInstance().addObserver(this);
	}

	private void init() {
		this.setBackground(UIAgent.getInstance().getColorBaseBackground());
		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		this.add(createDataPanel(), BorderLayout.CENTER);
	}

	private Component createDataPanel() {
		dataTableModel = new I18nStringMapTableModel();
		dataTable = new JXTable(dataTableModel);
		dataTable.setOpaque(false);
		dataTable.setHighlighters(HighlighterFactory.createAlternateStriping());

		JScrollPane scroll = new JScrollPane(dataTable);
		// scroll.getColumnHeader().setVisible(false);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		return scroll;
	}

	public void setData(Map<String, Object> logBookData) {
		dataTableModel.setData(logBookData);
		dataTable.setSortOrder(0, SortOrder.ASCENDING);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_META_SAVED)
					|| event.getType().equals(LogBookEvent.DIVE_DELETED)
					|| event.getType().equals(LogBookEvent.DIVE_UPDATE)
					|| event.getType().equals(LogBookEvent.DIVE_SAVE)
					|| event.getType().equals(LogBookEvent.DIVES_SAVE)
					|| event.getType().equals(LogBookEvent.DIVES_DELETED)) {
				synchronizeWithLogBook();
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_DELETED)) {
				synchronizeWithLogBook();
			}
		} else if (o.equals(UIAgent.getInstance())
				&& arg.equals(UIAgent.CHANGE_DATE_FORMAT_DAY_HOUR)) {
			synchronizeWithLogBook();
		} else if (o.equals(UnitsAgent.getInstance())
				&& arg.equals(UnitsAgent.UNITS_CHANGED)) {
			synchronizeWithLogBook();
		}
	}

	public void synchronizeWithLogBook() {
		final LogBook lb = logBookManagerFacade.getCurrentLogBook();
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (lb == null) {
					clear();
				} else {
					setData(LogBookUtilities.getLogBookData(lb));
				}
			}
		}).start();
	}

	private void clear() {
		setData(null);
	}
}
