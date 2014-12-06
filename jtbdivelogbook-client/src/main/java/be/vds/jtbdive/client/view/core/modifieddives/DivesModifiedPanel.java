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
package be.vds.jtbdive.client.view.core.modifieddives;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import be.smd.i18n.I18nResourceManager;
import be.vds.jtbdive.client.actions.ActionType;
import be.vds.jtbdive.client.actions.LogBookApplActionsContoller;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.renderer.DiveListCellRenderer;
import be.vds.jtbdive.core.core.Dive;

/**
 * 
 * @author Gautier Vanderslyen
 */
public class DivesModifiedPanel extends DetailPanel implements Observer {

	private static final long serialVersionUID = 7924196823555624859L;
	private JXList list;
	private DefaultListModel model;
	private final LogBookApplActionsContoller ctrl;
	private Action saveAllAction;
	private LogBookManagerFacade logBookManagerFacade;
	private JLabel rowCountLabel;

	public DivesModifiedPanel(LogBookApplActionsContoller ctrl,
			LogBookManagerFacade logBookManagerFacade) {
		this.ctrl = ctrl;
		this.logBookManagerFacade = logBookManagerFacade;
		init();
	}

	private void init() {
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.add(createButtonPanel(), BorderLayout.NORTH);
		this.add(createListPanel(), BorderLayout.CENTER);

		logBookManagerFacade.addObserver(this);
	}

	public Component createButtonPanel() {
		rowCountLabel = new JLabel("# 0");

		saveAllAction = ctrl.getAction(ActionType.SAVE_ALL_DIVE);
		JButton saveAllButton = new JButton(saveAllAction);
		saveAllButton.setText(null);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		p.setOpaque(false);
		p.add(rowCountLabel);
		p.add(Box.createHorizontalGlue());
		p.add(Box.createHorizontalStrut(5));
		p.add(Box.createHorizontalGlue());
		p.add(saveAllButton);
		return p;
	}

	public Component createListPanel() {
		model = new DefaultListModel();
		list = new JXList(model);
		list.setBackground(Color.WHITE);
		list.addHighlighter(HighlighterFactory.createAlternateStriping());
		list.setCellRenderer(new DiveListCellRenderer());

		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() >= 2) {
					Dive dive = (Dive) ((JList) list).getSelectedValue();
					if (dive != null) {
						logBookManagerFacade.setCurrentDive(dive);
					}
				}
			}
		});

		JScrollPane scroll = new JScrollPane(list);
		scroll.getViewport().setBackground(Color.WHITE);
		scroll.getViewport().setOpaque(true);

		return scroll;
	}

	public void clearChangedDives() {
		model.clear();
		saveAllAction.setEnabled(false);
		adaptRowCount(0);
	}

	private void adaptRowCount(int rows) {
		if (rows == 0)
			rowCountLabel.setText(null);
		else {
			String message = I18nResourceManager.sharedInstance().getMessage(
					"dives.numberof.param", new Object[] { rows });
			rowCountLabel.setText(message);
		}
	}

	public void reloadDiveChanged() {
		model.removeAllElements();
		Collection<Dive> dives = logBookManagerFacade.getAllModifiedDives();
		if (dives != null && dives.size() > 0) {
			for (Dive dive : dives) {
				model.addElement(dive);
			}
		}
		adaptRowCount(dives.size());
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_LOADED)) {
				clearChangedDives();
			} else if (event.getType().equals(LogBookEvent.DIVE_ADDED)
					|| event.getType().equals(LogBookEvent.DIVE_DELETED)
					|| event.getType().equals(LogBookEvent.DIVE_SAVE)
					|| event.getType().equals(LogBookEvent.DIVE_RELOAD)
					|| event.getType().equals(LogBookEvent.DIVES_SAVE)
					|| event.getType().equals(LogBookEvent.DIVE_UPDATE)
					|| event.getType().equals(LogBookEvent.DIVE_MODIFIED)) {
				reloadDiveChanged();
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_DELETED)) {
				reloadDiveChanged();
			}
		}
	}
}
