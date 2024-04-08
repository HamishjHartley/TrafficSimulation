package intersection_sim;

import java.awt.EventQueue;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import custom_exceptions.PlateNumberFormatException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Add {

	private JFrame frame;
	private JFrame popUp;
	private JTable table;
	private JLabel lblNewLabel;
	private JButton btnNewButton;
	private JButton btnNewButton_1;

	private Intersection intersection;

	/**
	 * Launches the GUI application
	 * 
	 * @param intersection
	 */
	public static void mainGui(Intersection intersection) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Add window = new Add(intersection);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creates the application by initializing it
	 * 
	 * @param intersection
	 */
	public Add(Intersection intersection) {
		this.intersection = intersection;
		initialize();
	}

	/**
	 * Adds a new vehicle into the table. If the plate number doesn't follow the
	 * specifications, then a PlateNumberFormatException is thrown After this, the
	 * information regarding the segments statistics is updated in the GUI
	 */
	private void addVehicle() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Vector<Object> row = model.getDataVector().elementAt(0);
		try {
			System.out.println(row.elementAt(2));
			intersection.addVehicle(row.elementAt(0).toString(), row.elementAt(1).toString(), (int)row.elementAt(2),
					(int) row.elementAt(3), (int) row.elementAt(4), (int) row.elementAt(5), false,
					(int) row.elementAt(6));
		} catch (PlateNumberFormatException e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, e.getMessage());
		} catch (NullPointerException e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, "Please fill in all parameters");
		}
		catch (Exception e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, "something went wrong: " + e.getMessage());
		}

		intersection.updateSegments();
	}

	/**
	 * Initializes the contents of the Add GUI frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(720, 480, 720, 480);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane()
				.setLayout(new MigLayout("", "[][658.00][76.00][35.00][][][][][][grow]", "[][44.00][][][grow]"));

		lblNewLabel = new JLabel("Add vehicle");
		frame.getContentPane().add(lblNewLabel, "cell 1 0");

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "cell 1 1,grow");

		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"Vehicles", "Type", "Crossing time", "Direction", "Length", "Emissions", "Segment"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});

		scrollPane.setViewportView(table);

		btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVehicle();
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getTableData(table);
				clearTable(table);
				userPopUp();
			}
		});

		frame.getContentPane().add(btnNewButton, "flowx,cell 1 3");

		btnNewButton_1 = new JButton("Back");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnNewButton_1, "cell 1 3");
	}

	// Gets table data and returns as an array
	/**
	 * Calls the table holding the data and returns it as an array
	 * 
	 * @param t JTable where the data is saved into
	 * @return data in the table in the shape of an array
	 */
	public ArrayList<String> getTableData(JTable t) {
		// ArrayList to store tableData when user presses 'add'
		ArrayList<String> tableData = new ArrayList<String>();
		// Loop across n rows of table, adding each value to array
		for (int i = 0; i > t.getRowCount(); i++) {
			tableData.add(t.getValueAt(0, i).toString());
		}
		return tableData;
	}

	/**
	 * Clears the JTable holding the data
	 * 
	 * @param t JTable where the data is saved into
	 */
	public void clearTable(JTable t) {
		DefaultTableModel dm = (DefaultTableModel) t.getModel();
		dm.setRowCount(0);
		dm.setRowCount(1);
	}

	/**
	 * Alerts the user when a vehicle has been added successfully in the way of a
	 * pop up window
	 */
	public void userPopUp() {
		popUp = new JFrame();
		JOptionPane.showMessageDialog(popUp, "Vehicle Added!");
	}
}
