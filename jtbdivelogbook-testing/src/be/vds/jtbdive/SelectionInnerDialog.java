package be.vds.jtbdive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import be.vds.jtbdive.client.swing.component.SelectableInnerPanel;
import be.vds.jtbdive.client.swing.component.SelectionListener;

public class SelectionInnerDialog extends JFrame {

	private static final long serialVersionUID = 2234315817741060739L;
	private SelectableInnerPanel selectPanel;

	public SelectionInnerDialog() {
		init();
	}

	private void init() {
		this.getContentPane().add(createContentPane());
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private Component createContentPane() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(createButtonsPanel(), BorderLayout.EAST);
		p.add(createCenterPanel(), BorderLayout.CENTER);
		return p;
	}

	private Component createButtonsPanel() {
		JButton addButton = new JButton(new AbstractAction("add") {

			private static final long serialVersionUID = -1840506507371645585L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JLabel l = new JLabel();
				l.setText(String.valueOf(System.currentTimeMillis()));
				selectPanel.addSelectableComponent(l, "blabla");
			}
		});

		JPanel p = new JPanel();
		p.add(addButton);
		return p;
	}

	private Component createCenterPanel() {
		selectPanel = new SelectableInnerPanel();
		selectPanel.setBorder(new LineBorder(Color.GREEN));
		selectPanel.addSelectionListener(new SelectionListener() {

			@Override
			public void selectionChanged(JComponent component) {
				System.out.println("Component is " + component.toString());
			}
		});

		return selectPanel;
	}

	public static void main(String[] args) {
		SelectionInnerDialog d = new SelectionInnerDialog();
		d.setVisible(true);
	}
}
