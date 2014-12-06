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
package be.vds.jtbdive.client.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.util.XslTransformer;
import be.vds.jtbdive.core.logging.Syslog;

/**
 * 
 * @author gautier
 */
public class ShowRevisionAction extends AbstractAction {

	private static final long serialVersionUID = 8731380951830582652L;
	private static final Syslog LOGGER = Syslog
			.getLogger(ShowRevisionAction.class);

	public ShowRevisionAction() {
		super("revision.show");
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FileOutputStream fos = null;
		try {
			InputStream xmlIs = ResourceManager.getInstance()
					.getResourceAsInputStream("resources/revisions.xml");
			InputStream xslIs = ResourceManager.getInstance()
					.getResourceAsInputStream("resources/xsl/revisions.xsl");
			File tempFile = File.createTempFile("revisionTemp", ".html");
			fos = new FileOutputStream(tempFile);
			XslTransformer t = new XslTransformer();
			boolean b = t.transformThrowingExceptions(xmlIs, xslIs, fos);

			if (b) {
				Desktop.getDesktop().open(tempFile);
			}

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			ExceptionDialog.showDialog(ex, null);
		} finally {
			try {
				fos.close();
			} catch (IOException ex) {
				LOGGER.error(ex.getMessage());
				ExceptionDialog.showDialog(ex, null);
			}
		}

	}
}
