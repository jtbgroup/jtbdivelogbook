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
package be.vds.jtbdive.client.view.core.map;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import be.vds.jtbdive.client.view.events.LogBookUiEventHandler;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.PinableObject;
import be.vds.jtbdive.core.utils.StringManipulator;

public class CoordinatesButtonTableEditor extends AbstractCellEditor implements
		TableCellRenderer, TableCellEditor {

	private static final long serialVersionUID = 1L;
	private CompCellPanel renderer = new CompCellPanel();
	private CompCellPanel editor = new CompCellPanel();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		renderer.setPin((PinableObject) value);
		renderer.setOpaque(true);
		if (isSelected) {
			renderer.setBackground(table.getSelectionBackground());
		} else {
			renderer.setBackground(table.getBackground());
		}
		return renderer;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		editor.setPin((PinableObject) value);
		editor.setOpaque(true);
		if (isSelected) {
			editor.setBackground(table.getSelectionBackground());
		} else {
			editor.setBackground(table.getBackground());
		}
		return editor;
	}

	@Override
	public Object getCellEditorValue() {
		return editor.getPin();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	class CompCellPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private JButton button = new JButton(UIAgent.getInstance().getIcon(
				UIAgent.ICON_MAP_16));
		private JLabel coordLabel = new JLabel();
		private PinableObject pin;

		CompCellPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					LogBookUiEventHandler.getInstance()
							.notifyPinableSelected(pin);
				}
			});
			this.add(button);
			this.add(Box.createHorizontalStrut(3));
			this.add(coordLabel);
		}

		public void setPin(PinableObject pin) {
			this.pin = pin;
			if (this.pin == null) {
				coordLabel.setText(null);
			} else {
				coordLabel.setText(StringManipulator.formatCoordinates(
						this.pin.getCoordinates().getLatitude(), this.getPin()
								.getCoordinates().getLongitude(), 5));
			}
			checkActive();
		}

		private void checkActive() {
			button.setEnabled(pin != null);
		}

		public PinableObject getPin() {
			return pin;
		}
	}
}