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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import be.smd.i18n.swing.I18nLabel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.core.diver.DiverChooser;
import be.vds.jtbdive.core.core.LogBookMeta;

public class LogBookMetaDataPanel extends DetailPanel {

	private static final long serialVersionUID = -3679758094296141327L;
	public static final String LOGBOOKMETA_CHANGED = "logbookmeta.changed";
	private JLabel ownerLabel;
	private JTextField nameTf;
	private LogBookMeta currentLogBookMetadata;
	private DiverChooser diverChooser;

	public LogBookMetaDataPanel() {
		init();
		// setEditable(false);
	}

	private void init() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		GridBagLayoutManager.addComponent(this, createNameLabel(), c, 0, 0, 1,
				1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);
		GridBagLayoutManager.addComponent(this, createNameTextField(), c, 1, 0,
				1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_END);

		GridBagLayoutManager.addComponent(this, createOwnerLabel(), c, 0, 1, 1,
				1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.FIRST_LINE_END);
		GridBagLayoutManager.addComponent(this, createDiverComponent(), c, 1,
				1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.FIRST_LINE_END);

		GridBagLayoutManager.addComponent(this, Box.createGlue(), c, 1, 2, 1,
				1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);

	}

	private JComponent createDiverComponent() {
		diverChooser = new DiverChooser(null);
		diverChooser.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						DiverChooser.DIVER_CHANGED_PROPERTY)) {
					firePropertyChange(LOGBOOKMETA_CHANGED, null, null);
				}
			}
		});
		return diverChooser;
	}

	private JComponent createOwnerLabel() {
		ownerLabel = new I18nLabel("owner");
		return ownerLabel;
	}

	private JComponent createNameTextField() {
		nameTf = new JTextField(20);
		nameTf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				firePropertyChange(LOGBOOKMETA_CHANGED, null, null);
			}
		});
		return nameTf;
	}

	private JComponent createNameLabel() {
		ownerLabel = new I18nLabel("name");
		return ownerLabel;
	}

	public void setLogBookMetadata(LogBookMeta logbookMetadata) {
		currentLogBookMetadata = logbookMetadata;
		nameTf.setText(currentLogBookMetadata.getName());
		diverChooser.setDiver(currentLogBookMetadata.getOwner());
	}

	public LogBookMeta getDisplayedLogBookMetadata() {
		LogBookMeta metaData = new LogBookMeta();
		// currentLogBook;
		if (null != currentLogBookMetadata) {
			metaData.setId(currentLogBookMetadata.getId());
		}

		metaData.setName(nameTf.getText().trim());
		metaData.setOwner(diverChooser.getDiver());
		return metaData;
	}

	public void setEditable(boolean editable) {
		nameTf.setEditable(editable);
		diverChooser.setEditable(editable);
	}

	public boolean isComplete() {
		String name = nameTf.getText().trim();
		return name.length() > 0 && diverChooser.getDiver() != null;
	}

	public void setDiverManagerFacade(DiverManagerFacade diverManagerFacade) {
		diverChooser.setDiverManagerFacade(diverManagerFacade);
	}
}
