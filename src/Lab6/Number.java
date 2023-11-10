package Lab6;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class Number extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNumber;
	private int i = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Number frame = new Number();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Number() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 416, 243);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblNumber = new JLabel("");
		lblNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumber.setFont(new Font("Poppins", Font.BOLD, 20));
		lblNumber.setBounds(121, 72, 171, 72);
		lblNumber.setText(i + "");
		panel.add(lblNumber);

		// Create a SwingWorker to update the label
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (true) {
					++i;
					Thread.sleep(3000);

					// Update the label on the EDT
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							lblNumber.setText(i + "");
						}
					});
				}
			}
		};

		// Start the SwingWorker
		worker.execute();
	}
}
