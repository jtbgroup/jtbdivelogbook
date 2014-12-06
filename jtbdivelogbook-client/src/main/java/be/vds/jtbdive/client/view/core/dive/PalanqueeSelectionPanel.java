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
package be.vds.jtbdive.client.view.core.dive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import be.smd.i18n.I18nResourceManager;
import be.smd.i18n.swing.I18nCheckBox;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.components.PromptDialog;
import be.vds.jtbdive.client.view.core.diver.DiverChooser;
import be.vds.jtbdive.client.view.utils.DiverRoleImageMapper;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.catalogs.DiverRole;

public class PalanqueeSelectionPanel extends PromptDialog {

	private static final long serialVersionUID = -4213102608890701687L;
	public static final int MODE_NEW = 1;
	public static final int MODE_EDIT = 2;

	private JButton selectButton;
	private DiverManagerFacade diverManagerFacade;
	private DiverChooser diverChooser;
	private Set<JCheckBox> rolesCheckBoxes;
	private int mode;
	private TitledBorder roleBorder;

	public PalanqueeSelectionPanel(JFrame parentFrame,
			DiverManagerFacade diverManagerFacade) {
		super(parentFrame, i18n.getString("diver"), i18n
				.getString("diver.selection"));
		setDiverManagerFacade(diverManagerFacade);
	}

	public PalanqueeSelectionPanel(DiverManagerFacade diverManagerFacade) {
		super(i18n.getString("palanquee"), i18n
				.getString("palanquee.selection"));
		setDiverManagerFacade(diverManagerFacade);
	}

	private void setDiverManagerFacade(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		diverChooser.setDiveManagerFacade(this.diverManagerFacade);
	}

	@Override
	protected Component createContentPanel() {
		rolesCheckBoxes = new HashSet<JCheckBox>();
		
		JPanel p = new DetailPanel(new BorderLayout());
		p.add(createDiverChooser(), BorderLayout.NORTH);
		p.add(createDiverRolePanel(), BorderLayout.CENTER);
		return p;
	}

	private JComponent createDiverRolePanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		roleBorder = new TitledBorder(I18nResourceManager.sharedInstance()
				.getString("roles"));
		panel.setBorder(roleBorder);

		JCheckBox checkBox = null;
		for (DiverRole role : DiverRole.values()) {
			if (!role.equals(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER)) {

				checkBox = new I18nCheckBox(role.getKey());
				checkBox.setOpaque(false);
				checkBox.setActionCommand(role.getKey());
				rolesCheckBoxes.add(checkBox);

				JPanel rolePanel = new JPanel();
				rolePanel.setOpaque(false);
				rolePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel iconLabel = new JLabel(
						DiverRoleImageMapper.ICON_ROLES.get(role));
				rolePanel.add(iconLabel);
				rolePanel.add(checkBox);

				panel.add(rolePanel);
			}
		}
		panel.add(Box.createVerticalStrut(10));

		return panel;
	}

	private JComponent createDiverChooser() {
		diverChooser = new DiverChooser(diverManagerFacade);
		diverChooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(
						DiverChooser.DIVER_CHANGED_PROPERTY)) {
					selectButton.setEnabled(null != evt.getNewValue());
				}
			}
		});

		if (mode == MODE_NEW) {
			diverChooser.setEditable(true);
		} else {
			diverChooser.setEditable(false);
		}

		return diverChooser;
	}

	public PalanqueeEntry getPalanqueeEntry() {
		return new PalanqueeEntry(diverChooser.getDiver(), getRoles());
	}

	public void setPalanqueeEntry(PalanqueeEntry palanqueeEntry) {
		diverChooser.setDiver(palanqueeEntry.getDiver());
		setRoles(palanqueeEntry.getRoles());
	}

	private void setRoles(Set<DiverRole> roles) {
		for (DiverRole diverRole : roles) {
			for (JCheckBox cb : rolesCheckBoxes) {
				if (cb.getActionCommand().equals(diverRole.getKey())) {
					cb.setSelected(true);
				}
			}
		}
	}

	private Set<DiverRole> getRoles() {
		Set<DiverRole> roles = new HashSet<DiverRole>();
		roles.add(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER);
		for (JCheckBox cb : rolesCheckBoxes) {
			if (cb.isSelected()) {
				DiverRole r = DiverRole.getRoleForKey(cb.getActionCommand());
				roles.add(r);
			}
		}
		return roles;
	}

	public void enableDiverSelection(boolean enabled) {
		diverChooser.setEditable(enabled);
	}

}
