package be.vds.jtbdive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TABlack {
	
	private Map<Integer, Color> colors = new HashMap<Integer, Color>();
	{
		colors.put(1, Color.GREEN);
		colors.put(2, Color.YELLOW);
		colors.put(3, Color.RED);
	}

	public static void main(String[] args) {
		new TABlack().start();
	}

	private JTextArea ta;
	private JComboBox lvl;

	private void start() {
		JDialog d = new JDialog();
		d.getContentPane().add(createText());
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setSize(400, 400);
		d.setLocationRelativeTo(null);
		d.setVisible(true);
	}

	private Component createText() {
		JButton b = new JButton(new AbstractAction("add text") {

			private static final long serialVersionUID = 839873227078841705L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = colors.get(lvl.getSelectedItem());
				ta.setForeground(c);
				
				ta.append("It's time : " + System.currentTimeMillis());
				ta.append("\r\n");
			}
		});

		lvl = new JComboBox(new Integer[] { 1, 2, 3 });

		JPanel ctl = new JPanel();
		ctl.add(b);
		ctl.add(lvl);

		ta = new JTextArea();
		ta.setBackground(Color.BLACK);
		ta.setForeground(colors.get(1));

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(ta, BorderLayout.CENTER);
		p.add(ctl, BorderLayout.SOUTH);
		return p;
	}

}
