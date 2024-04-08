package intersection_sim;

import java.awt.EventQueue;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.NoSuchElementException;

public class Edit {

	private JFrame frame;
	private JFrame popUp;
	private JTable table;
	private JTable table1;
	private Intersection intersection;


	//hi
	/**
	 * Launches the GUI application
	 * 
	 * @param intersection 
	 */
	public static void mainGui(Intersection intersection) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Edit window = new Edit(intersection);
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
	public Edit(Intersection intersection) {
		this.intersection = intersection;
		initialize();
	}

	/**
	 * Removes a vehicle from the table.
	 * If the vehicle suggested by the user is not in the table, it throws a NoSuchElementException
	 * In the way it is implemented it calls intersection to remove said car
	 */
	private void removeVehicle() {
		DefaultTableModel model = (DefaultTableModel) table1.getModel();
		try {
			System.out.println(model);
			System.out.println(model.getDataVector().elementAt(0).elementAt(0).toString());
			intersection.removeVehicle(model.getDataVector().elementAt(0).elementAt(0).toString());
		} catch (NoSuchElementException e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, e.getMessage());
		} catch (NullPointerException e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp,"please enter a value");
		} catch (Exception e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, "something went wrong: " + e.getMessage());
		}
	}

	/**
	 * This function allows the modification of the Lanes duration during the simulation.
	 * It will throw an exception is the duration isn't a positive int or if the lane number is not inside
	 * the ranges specified in the intersection.csv
	 */
	private void updatePhases(){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		try {
			int phaseNumber = Integer.parseInt((String) model.getValueAt(0, 0));
			int newDuration = Integer.parseInt((String) model.getValueAt(0, 1));
			intersection.changePhase(phaseNumber, newDuration);
		} catch (NumberFormatException e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp,"please enter a value");
		} catch (IllegalArgumentException e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, e.getMessage());
		} catch (IndexOutOfBoundsException e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, e.getMessage());
		} catch (Exception e) {
			popUp = new JFrame();
			JOptionPane.showMessageDialog(popUp, "Something went wrong: " + e.getMessage());
		}
		
	}

	/**
	 * Initializes the contents of the Edit GUI frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(720, 480, 720, 480);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[][][][][523.00,grow][][][214.00,grow][][][][]", "[][][][34.00][116.00][77.00][118.00,grow][330.00,grow]"));

		JLabel lblNewLabel = new JLabel("Edit lane");
		frame.getContentPane().add(lblNewLabel, "cell 4 3");

		JLabel lblNewLabel1 = new JLabel("Remove vehicle");
		frame.getContentPane().add(lblNewLabel1, "cell 7 3");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(scrollPane, "cell 4 4,grow");

		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
				},
				new String[] {
						"Phase", "Duration"
				}
				));
		scrollPane.setViewportView(table);

		JScrollPane scrollPane1 = new JScrollPane();
		frame.getContentPane().add(scrollPane1, "cell 7 4,grow");

		table1 = new JTable();
		table1.setModel(new DefaultTableModel(
				new Object[][] {
					{null},
				},
				new String[] {
						"Vehicle plate number"
				}
				));
		scrollPane1.setViewportView(table1);

		JButton btnNewButton = new JButton("Save");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updatePhases();
			}
		});
		frame.getContentPane().add(btnNewButton, "cell 4 6");

		JButton btnNewButton1 = new JButton("Remove");
		frame.getContentPane().add(btnNewButton1, "cell 7 6");
		btnNewButton1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeVehicle();
			}
		});

		JButton btnNewButton2 = new JButton("Back");
		btnNewButton2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
			}
		});

		frame.getContentPane().add(btnNewButton2, "cell 8 7");
	}
}