package Lab6;

import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private static CRUD lab6;
	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private JButton btnAdd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 1234);

			// lay ra object
			lab6 = (CRUD) registry.lookup("thaotaccsdl");

			String jsonData = lab6.getData();
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Client frame = new Client(jsonData);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No Connect To Server", "Server", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	/**
	 * Create the frame.
	 */
	public Client(String jsonData) {
		
		setTitle("Client1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 436, 263);
		contentPane.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 436, 263);
		panel.add(scrollPane);
		
		JPanel panelAdd = new JPanel();
		panelAdd.setBounds(0, 265, 436, 48);
		contentPane.add(panelAdd);
		panelAdd.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(30, 10, 249, 28);
		panelAdd.add(textField);
		textField.setColumns(10);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dulieu = textField.getText();
				try {
					lab6.add(dulieu);
					JOptionPane.showMessageDialog(null, "Thêm thành công");

					// Lấy dữ liệu mới từ server sau khi thêm thành công
					String jsonData = lab6.getData();
					Gson updatedGson = new GsonBuilder().create();
					Student[] updatedKhachHangs = updatedGson.fromJson(jsonData, Student[].class);

					// Cập nhật dữ liệu mới cho bảng
					String[][] updatedDataArray = new String[updatedKhachHangs.length][2];
					for (int i = 0; i < updatedKhachHangs.length; i++) {
						updatedDataArray[i][0] = String.valueOf(updatedKhachHangs[i].getId());
						updatedDataArray[i][1] = updatedKhachHangs[i].getName();
					}

					// Cập nhật bảng hiển thị với dữ liệu mới
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.setDataVector(updatedDataArray,  new String[] { "ID", "Name" });
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnAdd.setBounds(289, 9, 85, 28);
		panelAdd.add(btnAdd);

		// Create a SwingWorker to update the label
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				while (true) {
					// Get the latest data from the server
					String jsonData = lab6.getData();

					// Convert the JSON data to Student objects
					Gson gson = new GsonBuilder().create();
					Student[] student = gson.fromJson(jsonData, Student[].class);

					// Create a new table model
					String[][] data = new String[student.length][2];
					for (int i = 0; i < student.length; i++) {
						data[i][0] = String.valueOf(student[i].getId());
						data[i][1] = student[i].getName();
					}

					// Update the table on the EDT
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							table = new JTable();
							table.setModel(new DefaultTableModel(data, new String[] { "ID", "Name" }));
							scrollPane.setViewportView(table);
						}
					});

					// Sleep for 3 seconds before updating the table again
					Thread.sleep(3000);
				}
			}
		};

		// Start the SwingWorker
		worker.execute();
	}
}