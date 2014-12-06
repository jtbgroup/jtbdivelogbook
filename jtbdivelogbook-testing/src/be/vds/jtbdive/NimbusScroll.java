package be.vds.jtbdive;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import be.vds.jtbdive.client.swing.component.DetailPanel;

public class NimbusScroll {
	public static void main(String[] args) {
		 initLAF();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = new JPanel();
		JPanel listPanel = new JPanel();

		contentPane.setLayout(new BorderLayout());

		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));
		for (int i = 0; i < 20; i++) {
			JCheckBox cb = new JCheckBox(String.valueOf(i));
			listPanel.add(cb);
		}

		JScrollPane sp = new JScrollPane(listPanel);
//		sp.setBorder(BorderFactory.createEmptyBorder());
		
		JPanel detailPane = new DetailPanel(new BorderLayout());
		detailPane.add(sp, BorderLayout.CENTER);
		
		contentPane.add(new JLabel("NORTH"), BorderLayout.NORTH);
		contentPane.add(detailPane, BorderLayout.CENTER);
		
		frame.add(contentPane);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void initLAF() {
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
			}
		}
	}
}
