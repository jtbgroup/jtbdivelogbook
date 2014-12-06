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
package be.vds.jtbdive.client.view.core.stats;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.client.core.UnitsAgent;
import be.vds.jtbdive.client.core.event.LogBookEvent;
import be.vds.jtbdive.client.core.stats.StatQueryObject;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.client.view.wizard.stats.StatWizard;

public class StatPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 827001315401416888L;
	private LogBookManagerFacade logBookManagerFacade;
	private ChartPanel chartPanel;
	private JButton statWizardButton;
	private CardLayout cardLayout;
	private JPanel centralPanel;
	private StatQueryObject statQueryObject;

	public StatPanel(LogBookManagerFacade logBookManagerFacade) {
		this.logBookManagerFacade = logBookManagerFacade;
		logBookManagerFacade.addObserver(this);
		UnitsAgent.getInstance().addObserver(this);
		init();
	}

	private void init() {
		this.setLayout(new BorderLayout());
		this.setBackground(UIAgent.getInstance().getColorBaseBackground());
		this.setOpaque(true);
		this.add(createHeader(), BorderLayout.NORTH);
		this.add(createCentralPanel(), BorderLayout.CENTER);
	}

	private Component createHeader() {
		statWizardButton = new JButton(new AbstractAction() {

			private static final long serialVersionUID = -5345284505247399133L;

			@Override
			public void actionPerformed(ActionEvent e) {
				StatWizard wizard = new StatWizard(logBookManagerFacade);
				statQueryObject = wizard.launchWizard();
				displayStatQueryObjectResult();
			}

		});
		statWizardButton.setIcon(UIAgent.getInstance().getIcon(
				UIAgent.ICON_STATISTICS_16));
		statWizardButton.setEnabled(false);

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setOpaque(false);
		p.add(statWizardButton);
		return p;
	}

	private void displayStatQueryObjectResult() {
		if (null != statQueryObject) {
			chartPanel.setChart(StatChartGenerator.buildChart(statQueryObject));
			showGraphPanel(true);
		} else {
			showGraphPanel(false);
		}
	}

	private JComponent createCentralPanel() {
		cardLayout = new CardLayout();
		centralPanel = new DetailPanel(cardLayout);

		centralPanel.add(createDefaultPanel(), "default");
		centralPanel.add(createGraphPanel(), "graph");
		return centralPanel;
	}

	private Component createDefaultPanel() {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.add(new I18nLabel("no.query.defined"));
		return p;
	}

	private Component createGraphPanel() {
		chartPanel = new ChartPanel(null);
		chartPanel.setOpaque(false);

		return chartPanel;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.equals(UnitsAgent.getInstance())) {
			displayStatQueryObjectResult();
		} else if (arg instanceof LogBookEvent) {
			LogBookEvent event = (LogBookEvent) arg;
			if (event.getType().equals(LogBookEvent.LOGBOOK_CLOSED)
					|| event.getType().equals(LogBookEvent.LOGBOOK_DELETED)) {
				statQueryObject = null;
				showGraphPanel(false);
				statWizardButton.setEnabled(false);
			} else if (event.getType().equals(LogBookEvent.LOGBOOK_LOADED)) {
				statWizardButton.setEnabled(true);
			}
		}

	}

	private void showGraphPanel(boolean b) {
		cardLayout.show(centralPanel, b ? "graph" : "default");
	}

	public void updateLanguage() {
		displayStatQueryObjectResult();
	}
}
