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
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.LogBookMeta;

public class LogBookEditionDialog extends PromptDialog {

	private static final long serialVersionUID = -7539278158859766343L;
	private LogBookMetaDataPanel metadataPanel;

	private LogBookEditionDialog(JFrame parentWindow,

	DiverManagerFacade diverManagerFacade, String message, Image titleImage,
			Image messageImage) {
		super(parentWindow, i18n.getString("logbook"), message, messageImage);
		loadValues(diverManagerFacade);
	}

	private void loadValues(DiverManagerFacade diverManagerFacade) {
		metadataPanel.setDiverManagerFacade(diverManagerFacade);
		setOkButtonEnabled(false);
	}

	public static LogBookEditionDialog createNewDialog(JFrame parentWindow,
			DiverManagerFacade diverManagerFacade) {
		LogBookEditionDialog dlg = new LogBookEditionDialog(parentWindow,
				diverManagerFacade, i18n.getString("logbookmeta.new.message"),
				UIAgent.getInstance().getBufferedImage(
						UIAgent.ICON_LOGBOOK_ADD_16), UIAgent.getInstance()
						.getBufferedImage(UIAgent.ICON_LOGBOOK_ADD_16));
		return dlg;
	}

	public static LogBookEditionDialog createEditDialog(JFrame parentWindow,
			DiverManagerFacade diverManagerFacade) {
		LogBookEditionDialog dlg = new LogBookEditionDialog(parentWindow,
				diverManagerFacade, i18n.getString("logbookmeta.edit.message"),
				UIAgent.getInstance().getBufferedImage(
						UIAgent.ICON_LOGBOOK_EDIT_16), UIAgent.getInstance()
						.getBufferedImage(UIAgent.ICON_LOGBOOK_EDIT_16));
		return dlg;
	}


	@Override
	protected Component createContentPanel() {
		JPanel contentPane = new JPanel();
		contentPane.setBackground(UIAgent.getInstance()
				.getColorBaseBackground());
		contentPane.setLayout(new BorderLayout(0, 10));

		contentPane.add(createLogBookMetadataPanel(), BorderLayout.CENTER);
		return contentPane;
	}

	private Component createLogBookMetadataPanel() {
		metadataPanel = new LogBookMetaDataPanel();
		metadataPanel.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						LogBookMetaDataPanel.LOGBOOKMETA_CHANGED)) {
					synchronizeSaveButton();
				}
			}
		});
		return metadataPanel;
	}

	public void setLogBookMetadata(LogBookMeta logBookMeta) {
		metadataPanel.setLogBookMetadata(logBookMeta);
	}

	private void synchronizeSaveButton() {
		setOkButtonEnabled(metadataPanel.isComplete());
	}

	public LogBookMeta getDisplayedLogBookMetadata() {
		return metadataPanel.getDisplayedLogBookMetadata();
	}

}
