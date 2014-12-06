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
package be.vds.jtbdive.client.actions.download;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.Action;

import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.core.download.DiveComputerDownloadDialog;
import be.vds.jtbdive.client.view.core.download.DownloadConstants;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.comparator.DiveDateComparator;
import be.vds.jtbdive.core.core.divecomputer.binary.BinaryLogBook;
import be.vds.jtbdive.core.core.divecomputer.parser.ComputerDataParser;
import be.vds.jtbdive.xml.parsers.BinaryDataFileParser;

public class DownloadDiveFromDiveComputerAction extends DownloadDiveAction {

	private static final long serialVersionUID = -476028563533879297L;

	public DownloadDiveFromDiveComputerAction(LogBookApplFrame logBookApplFrame) {
		super(logBookApplFrame);
		setEnabled(false);
		putValue(Action.SMALL_ICON,
				UIAgent.getInstance().getIcon(UIAgent.ICON_DIVECOMPUTER_16));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiveComputerDownloadDialog dlg = new DiveComputerDownloadDialog(
				logBookApplFrame);
		dlg.setMatCave(logBookApplFrame.getLogBookManagerFacade()
				.getCurrentLogBook().getMatCave());
		dlg.setEnableStandaloneOptions(logBookApplFrame
				.getLogBookManagerFacade().getCurrentLogBook() != null);

		int i = dlg.showDialog(true);
		if (i == DiveComputerDownloadDialog.OPTION_OK) {

			List<Dive> dives = dlg.getSelectedDives();
			Collections.sort(dives, new DiveDateComparator());

			// OPTION - Save Binary files
			Map<String, Object> options = dlg.getOptions();
			if (null != options.get(DownloadConstants.OPTION_SAVE_BINARY_FILE)
					&& ((Boolean) options
							.get(DownloadConstants.OPTION_SAVE_BINARY_FILE))) {
				saveBinary(
						(File) options
								.get(DownloadConstants.OPTION_BINARY_FILE),
						(Class<?>) options
								.get(DownloadConstants.OPTION_COMPUTER_DATA_PARSER_CLASS),
						(int[]) options
								.get(DownloadConstants.OPTION_BINARY_DATA));
			}

			// OPTION - Auto number dives
			Boolean autoNumber = (Boolean) options
					.get(DownloadConstants.OPTION_AUTONUMBER_DIVE);
			if (null != autoNumber && autoNumber)
				renumberDives(dives);

			// OPTION - Auto add Owner
			Boolean autoAddOwner = (Boolean) options
					.get(DownloadConstants.OPTION_AUTO_ADD_OWNER);
			if (null != autoAddOwner && autoAddOwner) {
				Diver owner = logBookApplFrame.getLogBookManagerFacade()
						.getCurrentLogBook().getOwner();
				if (owner != null) {
					addDiverToDives(dives, owner);
				}
			}

			// Save Dive
			addDives(dives);
		}
	}

	private void saveBinary(File file, Class<?> parserClass, int[] binaries) {
		try {
			BinaryLogBook bl = new BinaryLogBook();
			bl.setBinaries(binaries);
			bl.setDate(new Date());
			bl.setParser(ComputerDataParser.getParser(parserClass));
			BinaryDataFileParser p = new BinaryDataFileParser();
			List<BinaryLogBook> lbs = null;
			if (file.exists()) {
				lbs = p.read(new FileInputStream(file));
			}

			if (null == lbs) {
				lbs = new ArrayList<BinaryLogBook>();
			}
			lbs.add(bl);

			p.write(new FileOutputStream(file), lbs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
