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
package be.vds.jtbdive.client.view.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import be.smd.i18n.I18nResourceManager;

public abstract class WizardPanel extends JPanel {

	private static final long serialVersionUID = -639155434340697865L;
	protected static final I18nResourceManager i18n = I18nResourceManager
			.sharedInstance();
	private Set<CompletionListener> listeners = new HashSet<CompletionListener>();

	public void addCompletionListener(CompletionListener completionListener) {
		this.listeners.add(completionListener);
	}

	public void removeCompletionListener(CompletionListener completionListener) {
		this.listeners.remove(completionListener);
	}

	public void fireCompletionChanged(boolean isComplete) {
		for (CompletionListener listener : listeners) {
			listener.completionChanged(isComplete);
		}
	}

	public WizardPanel() {
		initWizardPanel();
	}

	private void initWizardPanel() {
		this.setLayout(new BorderLayout(0, 20));
		String text = getMessage();
		if (null != text) {
			this.add(createMessagePanel(text), BorderLayout.NORTH);
		}

		JComponent content = createContentPanel();
		if (null != content) {
			this.add(content, BorderLayout.CENTER);
		}
	}

	private Component createMessagePanel(String message) {
		JTextArea ta = new JTextArea();
		ta.setOpaque(false);
		// ta.setEnabled(false);
		ta.setEditable(false);
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);
		ta.setText(message);
		ta.setFont(new Font("Arial", Font.ITALIC, 12));
		return ta;
	}

	public abstract JComponent createContentPanel();

	public abstract String getMessage();

	public boolean isComplete() {
		return true;
	}

}
