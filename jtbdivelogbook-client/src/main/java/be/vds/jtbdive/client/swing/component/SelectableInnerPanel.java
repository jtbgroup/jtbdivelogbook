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
package be.vds.jtbdive.client.swing.component;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import be.vds.jtb.swing.component.ScrollableFlowPanel;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class SelectableInnerPanel extends ScrollableFlowPanel {
	private static final long serialVersionUID = 6079069755625144674L;
	private static final Border BORDER_SELECTED = new LineBorder(UIAgent
			.getInstance().getColorPanelSelected(), 1);
	private static final Border BORDER_UNSELECTED = new LineBorder(UIAgent
			.getInstance().getColorPanelUnselected(), 1);
	private static final Border BORDER_LABEL_UNSELECTED = BorderFactory
			.createEmptyBorder(1, 1, 1, 1);

	private List<SelectablePanel> registeredComponents = new ArrayList<SelectablePanel>();
	private List<SelectablePanel> selectedComponents = new ArrayList<SelectablePanel>();

	private Set<SelectionListener> selectionListeners = new HashSet<SelectionListener>();
	private SelectorMouseListener selectionAdapter;
	private DoubleClickHandler doubleClickHandler;
	private int titleMaxLength = 15;

	public SelectableInnerPanel() {
		init();
	}

	private void init() {
		this.setOpaque(false);
		selectionAdapter = new SelectorMouseListener();
		this.addMouseListener(selectionAdapter);
	}

	public void addSelectableComponent(JComponent component, String title) {
		addSelectableComponent(component, title, false);
	}

	public void addSelectableComponent(JComponent component, String title,
			boolean autoSelect) {

		SelectablePanel selectComp = createSelectablePanel(component, title);
		selectComp.addMouseListener(selectionAdapter);
		for (int i = 0; i < component.getComponentCount(); i++) {
			component.getComponent(i).addMouseListener(selectionAdapter);
		}

		setSelected(selectComp, autoSelect);

		registeredComponents.add(selectComp);
		this.add(selectComp);
		this.invalidate();
		this.repaint();
		this.validate();
	}

	private SelectablePanel createSelectablePanel(JComponent component,
			String title) {
		return new SelectablePanel(component, title);
	}

	private void deregisterComponent(JComponent component) {
		component.removeMouseListener(selectionAdapter);

		for (int i = 0; i < component.getComponentCount(); i++) {
			component.getComponent(i).removeMouseListener(selectionAdapter);
		}

		SelectablePanel sp = getSelectableForComponent(component);
		registeredComponents.remove(sp);
		if (sp != null) {
			this.remove(sp);
		}
	}

	private SelectablePanel getSelectableForComponent(JComponent component) {
		for (SelectablePanel sp : registeredComponents) {
			if (sp.getInnerComponent().equals(component))
				return sp;
		}
		return null;
	}

	public void removeComponents(List<JComponent> components) {
		for (JComponent component : components) {
			deregisterComponent(component);
		}
		clearSelection();
		this.invalidate();
		this.repaint();
		this.validate();
	}

	public void removeComponent(JComponent component) {
		deregisterComponent(component);
		clearSelection();
		this.invalidate();
		this.repaint();
		this.validate();
	}

	public void clearSelection() {
		for (SelectablePanel c : selectedComponents) {
			setSelected(c, false);
		}
		selectedComponents.clear();
		fireSelectionChanged();
	}

	public void addSelectionListener(SelectionListener selectionListener) {
		selectionListeners.add(selectionListener);
	}

	public void removeSelectionListener(SelectionListener selectionListener) {
		selectionListeners.remove(selectionListener);
	}

	private void fireSelectionChanged() {
		for (SelectionListener selectionListener : selectionListeners) {
			selectionListener.selectionChanged(this);
		}
	}

	public void setTitleMaxLength(int titleMaxLength) {
		this.titleMaxLength = titleMaxLength;
	}

	public class SelectorMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().equals(SelectableInnerPanel.this)) {
				if (e.getModifiersEx() != KeyEvent.CTRL_DOWN_MASK
						&& selectedComponents.size() > 0) {
					clearSelection();
				}
			} else {
				SelectablePanel c = getTopSelectableComponent(e.getComponent());

				if (e.getClickCount() == 2) {
					doDoubleClick();
				} else {
					doSimpleClick(e, c);
				}
			}
		}

		private void doDoubleClick() {
			if (null != doubleClickHandler)
				doubleClickHandler.handleDoubleClick();
		}

		private void doSimpleClick(MouseEvent e, SelectablePanel c) {
			if (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK) {
				// SelectablePanel c = (SelectablePanel)
				// e.getComponent();
				if (selectedComponents.contains(c)) {
					selectedComponents.remove(c);
					setSelected(c, false);
				} else {
					selectedComponents.add(c);
					setSelected(c, true);
				}
			} else {
				if (selectedComponents.size() > 0) {
					for (SelectablePanel comp : selectedComponents) {
						setSelected(comp, false);
					}
					selectedComponents.clear();
				}
				setSelected(c, true);
				selectedComponents.add(c);
			}
			fireSelectionChanged();
			repaint();
			validate();
		}

		private SelectablePanel getTopSelectableComponent(Component component) {
			if (component instanceof SelectablePanel) {
				return (SelectablePanel) component;
			}
			if (component.getParent() == null)
				return null;
			return getTopSelectableComponent(component.getParent());
		}

	}

	public void setDoubleClickHandler(DoubleClickHandler doubleClickHandler) {
		this.doubleClickHandler = doubleClickHandler;
	}

	public List<JComponent> getSelectedComponents() {
		List<JComponent> comps = new ArrayList<JComponent>();
		for (SelectablePanel sp : selectedComponents) {
			comps.add(sp.getInnerComponent());
		}
		return comps;
	}

	private void setSelected(SelectablePanel selectedComponent, boolean selected) {
		selectedComponent.setSelected(selected);
		if (selected)
			selectedComponents.add(selectedComponent);
	}

	public List<JComponent> getRegisteredComponents() {
		List<JComponent> comps = new ArrayList<JComponent>();
		for (SelectablePanel sp : registeredComponents) {
			comps.add(sp.getInnerComponent());
		}
		return comps;
	}

	public void clear() {
		this.removeAll();
		registeredComponents.clear();
		selectedComponents.clear();
		fireSelectionChanged();

		this.repaint();
		this.revalidate();
	}

	public SelectablePanel getSelectedPanel() {
		return selectedComponents.size() == 0 ? null : selectedComponents
				.get(0);
	}

	public class SelectablePanel extends JPanel {

		private static final long serialVersionUID = -1402382399184791864L;
		private String title;
		private JComponent component;
		private JLabel titleLabel;

		public SelectablePanel(JComponent component, String title) {
			this.component = component;
			setTitle(title);
			init();
		}

		private void setTitle(String title) {
			if (title != null && title.length() >= titleMaxLength) {
				this.title = title.substring(0, 12) + "...";
			} else {
				this.title = title;
			}
		}

		public void updateTitle(String title) {
			setTitle(title);
			titleLabel.setText(this.title);
			titleLabel
					.setVisible(this.title != null && this.title.length() > 0);
		}

		public void setSelected(boolean selected) {
			component.setBorder(selected ? BORDER_SELECTED : BORDER_UNSELECTED);
			if (selected) {
				titleLabel.setBorder(BORDER_SELECTED);
				titleLabel.setBackground(UIAgent.getInstance()
						.getColorPanelSelectedBackground());
				titleLabel.setOpaque(true);
			} else {
				titleLabel.setBorder(BORDER_LABEL_UNSELECTED);
				titleLabel.setBackground(null);
				titleLabel.setOpaque(false);
			}
		}

		public JComponent getInnerComponent() {
			return component;
		}

		private void init() {
			LayoutManager lm = new GridBagLayout();
			this.setLayout(lm);
			this.setOpaque(false);
			GridBagConstraints c = new GridBagConstraints();

			GridBagLayoutManager.addComponent(this, component, c, 0, 0, 1, 1,
					0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

			titleLabel = new JLabel(title);
			GridBagLayoutManager.addComponent(this, Box.createVerticalStrut(3),
					c, 0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
					GridBagConstraints.CENTER);
			GridBagLayoutManager.addComponent(this, titleLabel, c, 0, 2, 1, 1,
					0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);

			titleLabel.setVisible(title != null);
		}

	}

}
