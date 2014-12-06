package be.vds.jtbdive.client.view.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import be.smd.i18n.swing.I18nCheckBox;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.swing.component.DetailPanel;
import be.vds.jtbdive.client.view.core.diver.DiverChooser;
import be.vds.jtbdive.core.core.Diver;

public class DiverMatchComponent extends JPanel {

	private static final long serialVersionUID = -5645815717519540041L;
	private JLabel diverNameTf;
	private I18nCheckBox matchCb;
	private DiverChooser diverChooser;
	private DiverManagerFacade diverManagerFacade;
	private Diver originalDiver;
	private JCheckBox selectComboBox;

	public DiverMatchComponent(DiverManagerFacade diverManagerFacade) {
		this.diverManagerFacade = diverManagerFacade;
		init();
	}

	private void init() {
		this.setOpaque(false);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		GridBagLayoutManager.addComponent(this, createSelectionComponent(), gc,
				0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.CENTER);
		GridBagLayoutManager.addComponent(this, createNameTf(), gc, 1, 0, 1, 1,
				1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);

		GridBagLayoutManager.addComponent(this, Box.createHorizontalStrut(30),
				gc, 0, 1, 1, 2, 0, 0, GridBagConstraints.NONE,
				GridBagConstraints.NORTH);

		GridBagLayoutManager.addComponent(this, createMatchChoicePanel(), gc,
				1, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.EAST);

		selectComboBox.setSelected(true);
		matchCb.setSelected(false);
		handleMatch(false);
	}

	private Component createMatchChoicePanel() {
		JPanel p = new DetailPanel(new BorderLayout());
		p.add(createMatchChoice(), BorderLayout.NORTH);
		p.add(createMatchValue(), BorderLayout.CENTER);
		return p;
	}

	private Component createSelectionComponent() {
		selectComboBox = new JCheckBox(new AbstractAction() {
			private static final long serialVersionUID = 2005631770158613958L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSelection(selectComboBox.isSelected());
			}
		});
		return selectComboBox;
	}

	private Component createNameTf() {
		diverNameTf = new JLabel();
		return diverNameTf;
	}

	private Component createMatchValue() {
		diverChooser = new DiverChooser(diverManagerFacade);
		return diverChooser;
	}

	private Component createMatchChoice() {
		matchCb = new I18nCheckBox(new AbstractAction("diver.map") {

			private static final long serialVersionUID = 6202048294644235868L;

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMatch(matchCb.isSelected());
			}

		});

		matchCb.setOpaque(false);
		matchCb.setBorder(null);
		return matchCb;
	}

	private void handleSelection(boolean enabled) {
		diverNameTf.setEnabled(enabled);
		matchCb.setEnabled(enabled);
		
		if (!enabled) {
			matchCb.setSelected(false);
			handleMatch(false);
		}
	}

	private void handleMatch(boolean enabled) {
		diverChooser.setEditable(enabled);

		if (!enabled)
			diverChooser.setDiver(null);
	}

	public void setOriginalDiver(Diver diver) {
		this.originalDiver = diver;
		diverNameTf.setText(this.originalDiver.getFullName());
	}

	public Diver getSelectedDiver() {
		return diverChooser.getDiver();
	}

	public Diver getOriginalDiver() {
		return originalDiver;
	}

	public void setSelected(boolean selected) {
		selectComboBox.setSelected(selected);
		handleSelection(selected);
	}

	public boolean isSelected() {
		return selectComboBox.isSelected();
	}

	public void setSelectedDiver(Diver diver) {
		if (!isSelected()) {
			setSelected(diver != null);
		}

		setMatch(diver != null);
		diverChooser.setDiver(diver);
		diverChooser.setEditable(diver != null);
	}

	public void setMatch(boolean match) {
		if (matchCb.isEnabled())
			matchCb.setSelected(match);
	}

}
