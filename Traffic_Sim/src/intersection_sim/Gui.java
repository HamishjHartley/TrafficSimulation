package intersection_sim;

import javax.swing.JFrame;

import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import custom_exceptions.InvalidVehicleException;
import custom_exceptions.PlateNumberFormatException;

import custom_exceptions.InvalidVehicleException;
import custom_exceptions.PlateNumberFormatException;

import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;
import java.util.stream.IntStream;
import java.io.IOException;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.ListSelectionModel;


public class Gui {
	private JTable vehicleTable;
	private JTable laneTable;
	private JTable statisticsTable;
	private JTextField searchTxt;
	private JLabel intersectionLabel;
	private JFrame exportPopUp;
	private JTextPane airQualityIndex;
	private JTextPane co2Index;
	private JPanel panel2;
	private Boolean cars = true;
	private Boolean time = true;
	private Boolean co2 = true;
	private Boolean air = false;
	private int phaseCounter = 0;

	// Stores 8 images which represent each phase
	private ImageIcon[] intersectionIcons = new ImageIcon[5];

	Intersection intersection;

	Add addPage = new Add(intersection);
	Edit editPage = new Edit(intersection);

	/**
	 * Adds a vehicle to the GUI vehicles table
	 * 
	 * @param v vehicle that needs to be added
	 */
	public void addVehicle(Vehicle v) {
		String status;
		// Convert from Boolean to string as easier on back-end to handle
		if (v.isCrossed()) {
			status = "Crossed";
		} else {
			status = "Waiting";
		}
		DefaultTableModel model = (DefaultTableModel) vehicleTable.getModel();
		model.addRow(new Object[] { v.getNumberPlate(), v.getVehicleType(), v.getCrossTime(), v.getDirection(),
				v.getLength(), v.getEmissions(), status, Integer.toString(v.getSegment()) });
	}

	/**
	 * Removes a vehicle from the GUI vehicles table
	 * 
	 * @param v vehicle that needs to be removed
	 */
	public void removeVehicle(String numberPlate) {
		DefaultTableModel model = (DefaultTableModel) vehicleTable.getModel();
		// Loop through entries in first column to check if matching numberplates
		Iterator<Vector> iter = model.getDataVector().listIterator();
		int i = 0;
		while (iter.hasNext()) {
			Vector row = iter.next();
			if (row.get(0).equals(numberPlate)) {
				model.removeRow(i);
				break;
			}
			i++;
		}
	}
	
	private void removeHighlightedVehicle() {
		DefaultTableModel model = (DefaultTableModel) vehicleTable.getModel();
		int select = vehicleTable.convertRowIndexToModel(vehicleTable.getSelectedRow());
			intersection.removeVehicle(model.getDataVector().elementAt(select).elementAt(0).toString());
	}
	
	public void searchVehicle(String numberPlate) {
		DefaultTableModel model = (DefaultTableModel) vehicleTable.getModel();

		Iterator<Vector> iter = model.getDataVector().listIterator();
		int i = 0;
		while (iter.hasNext()) {
			Vector row = iter.next();
			if (row.get(0).equals(numberPlate)) {
				int select = vehicleTable.convertRowIndexToView(i);
				vehicleTable.changeSelection(select, 0, false, false);
				vehicleTable.changeSelection(select, 3, false, true);
				vehicleTable.setSelectionBackground(new Color(255, 123, 100));
			}
			i++;
		}
	}
	
	public void updateVehicle(String numberPlate, boolean crossed) {
		DefaultTableModel model = (DefaultTableModel) vehicleTable.getModel();
		
		String status;
		// Convert from Boolean to string as easier on back-end to handle
		if (crossed) {
			status = "Crossed";
		} else {
			status = "Waiting";
		}
		
		//iterate through and find row with matching numberplate
		Iterator<Vector> iter = model.getDataVector().listIterator();
		int i = 0;
		while (iter.hasNext()) {
			Vector row = iter.next();
			if (row.get(0).equals(numberPlate)) {
				model.setValueAt(status, i, 6);
				break;
			}
			i++;
		}
		
	}

	/**
	 * Adds the lanes into their respective table
	 * 
	 * @param laneNumber number of the current lane being added
	 * @param p lane being added
	 */
	public void addPhase(int phaseNumber, Phase p) {
		DefaultTableModel model = (DefaultTableModel) laneTable.getModel();
		model.addRow(new Object[] { phaseNumber,"", p.getDuration() });
	}
	
	public void updatePhase(int phaseNumber, Phase p) {
		DefaultTableModel model = (DefaultTableModel) laneTable.getModel();
		model.setValueAt(p.getDuration(), phaseNumber-1, 2);
	}
	
	public void addLane(int laneNumber, Lane p) {
		DefaultTableModel model = (DefaultTableModel) laneTable.getModel();
		String lanes = "";
		String currentLane = (String)model.getValueAt(p.getPhase()-1, 1);
		lanes = lanes+ currentLane;
		if (!currentLane.contentEquals("")) {
			lanes = lanes + ", ";
		}
		lanes = lanes + p.getNumberLane();
		model.setValueAt(lanes, p.getPhase()-1, 1);
	}
	
	public void setCurrentPhase(int phaseNumber, Color c) {
		laneTable.changeSelection(phaseNumber, 0, false, false);
		laneTable.changeSelection(phaseNumber, 1, false, true);
		laneTable.setSelectionBackground(c);
	}
	/**
	 *  Adds the segments into their respective table
	 * 
	 * @param numberSegment number of the current segment being added
	 * @param s segment being added
	 */
	public void addSegment(int numberSegment, Segment s) {
		DefaultTableModel model = (DefaultTableModel) statisticsTable.getModel();
		model.addRow(new Object[] { numberSegment, s.getAverageWaitingTime(), s.getAverageWaitingLength(),
				s.getAverageCrossingTime(), s.getAmountOfVehiclesWaiting() });
	}

	/**
	 * Clears the GUI lanes table to be able to update them later on
	 */
	public void clearPhases() {
		DefaultTableModel model = (DefaultTableModel) laneTable.getModel();
		int rows = model.getRowCount();
		while (rows > 0) {
			model.removeRow(rows - 1);
			rows--;
		}
	}

	/**
	 * Clears the GUI segments table to be able to update them later on
	 */
	public void clearSegments() {
		DefaultTableModel model = (DefaultTableModel) statisticsTable.getModel();
		int rows = model.getRowCount();
		while (rows > 0) {
			model.removeRow(rows - 1);
			rows--;
		}
	}
	
	    //Air quality text pane output
		public void updateAirQualityIndex() {
			airQualityIndex.setText(intersection.getAirQualityIndex());

		}
		//Co2 text pane output
		public void updateC02Index() {
			// Total Co2 text pane output
			co2Index.setText(String.valueOf(intersection.getCO2Index()));
			
		}
	
    
    public void updateIcons(int phaseNumber) {
    	intersectionLabel.setIcon(intersectionIcons[phaseNumber]);
    }
    

	/**
	 * Sets up the Gui
	 * 
	 * @param intersection intersection in which the Gui is based on
	 */
	public Gui(Intersection intersection) {
		
		this.intersection = intersection;
		
		//TODO:Re-name and edit these images to represent the actual lanes, i.e (2,6) 
		//Only need four images
    	ImageIcon phase1 = new ImageIcon(Gui.class.getResource("2,6.png"), "phase1");
    	ImageIcon phase2 = new ImageIcon(Gui.class.getResource("4,8.png"), "phase2");
    	ImageIcon phase3 = new ImageIcon(Gui.class.getResource("1,5.png"), "phase3");
    	ImageIcon phase4 = new ImageIcon(Gui.class.getResource("3,7.png"), "phase4");
    	
    	intersectionIcons[0] = phase1;
    	intersectionIcons[1] = phase2;
    	intersectionIcons[2] = phase3;
    	intersectionIcons[3] = phase4;

    	JFrame frame = new JFrame("Intersection");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.getContentPane().setLayout(new MigLayout("", "[1254px]", "[672px]"));
       
          //code to add custom exit behaviour
        frame.addWindowListener(new WindowAdapter() {
            @Override
             public void windowClosing(WindowEvent e) {
                 intersection.generateLog();
                 System.exit(0);
             }
        });
		//Tabbed pane which contains genInfo, advFeatures and export 
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setToolTipText("Advanced features");
		frame.getContentPane().add(tabbedPane, "cell 0 0,grow");
		
		/*
		 * General information tab
		 */
		JPanel genInfoPane = new JPanel();
		tabbedPane.addTab("General Information", null, genInfoPane, null);
		genInfoPane.setLayout(new MigLayout("", "[687.00px,grow][115.00px,grow][409px,grow]",
				"[][107.00px][337.00][50.00][47.00,grow][51.00,grow][][]"));
		
		//Extra JPanel to contain plate number browser
		JPanel panel_1 = new JPanel();
		genInfoPane.add(panel_1, "cell 0 4,grow");


		//ScrollPane to contain the vehicleTable
		JScrollPane scrollPane1 = new JScrollPane();
		genInfoPane.add(scrollPane1, "cell 0 1 1 2,growx,aligny top");
	
		vehicleTable = new JTable();
		vehicleTable.setEnabled(true);
		vehicleTable.setAutoCreateRowSorter(true);
		vehicleTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Vehicles*", "Type*",
				"Crossing time", "Direction*", "Length", "Emissions", "Status*", "Segment*" }) {
			Class[] columnTypes = new Class[] { String.class, String.class, Integer.class, Object.class, Integer.class,
					Integer.class, Object.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		    public boolean isCellEditable(int row, int column){  
		          return false;  
		    }
		});
		vehicleTable.getColumnModel().getColumn(0).setResizable(false);
		vehicleTable.getColumnModel().getColumn(1).setResizable(false);
		vehicleTable.getColumnModel().getColumn(2).setResizable(false);
		vehicleTable.getColumnModel().getColumn(3).setResizable(false);
		vehicleTable.getColumnModel().getColumn(4).setResizable(false);
		vehicleTable.getColumnModel().getColumn(5).setResizable(false);
		vehicleTable.getColumnModel().getColumn(6).setResizable(false);
		scrollPane1.setViewportView(vehicleTable);

		//ScrollPane to contain the laneTable
		JScrollPane scrollPane2 = new JScrollPane();
		genInfoPane.add(scrollPane2, "cell 1 1 1 2,growx,aligny top");
		
		laneTable = new JTable();
//		laneTable.setAutoCreateRowSorter(true);
		laneTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Phase", "Lanes", "Duration"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		laneTable.getColumnModel().getColumn(0).setResizable(false);
		laneTable.getColumnModel().getColumn(1).setResizable(false);
		scrollPane2.setViewportView(laneTable);
		
		//ScrollPane to contain the statistics Table
		JScrollPane scrollPane3 = new JScrollPane();
		genInfoPane.add(scrollPane3, "cell 2 1 1 2,growx,aligny top");

		statisticsTable = new JTable();
		statisticsTable.setAutoCreateRowSorter(true);
		statisticsTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Segment", "Waiting time",
				"Waiting length", "Crossing time", "Vehicles waiting to cross" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		statisticsTable.getColumnModel().getColumn(0).setResizable(false);
		statisticsTable.getColumnModel().getColumn(1).setResizable(false);
		statisticsTable.getColumnModel().getColumn(2).setResizable(false);
		statisticsTable.getColumnModel().getColumn(3).setResizable(false);
		statisticsTable.getColumnModel().getColumn(4).setResizable(false);
		scrollPane3.setViewportView(statisticsTable);

		//Plate number browser
		searchTxt = new JTextField();
		panel_1.add(searchTxt);
		searchTxt.setText("Type here");
		searchTxt.setColumns(10);

		// Air quality text pane output
	    airQualityIndex = new JTextPane();
	    airQualityIndex.setEditable(false);
	    airQualityIndex.setText("High");
		genInfoPane.add(airQualityIndex, "cell 1 4,grow");

		// Total Co2 text pane output
		co2Index = new JTextPane();
		co2Index.setEditable(false);
		co2Index.setText("0"); //inital Co2 value
		genInfoPane.add(co2Index, "cell 1 3,grow");
		//JButtons
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchVehicle(searchTxt.getText());
			}
		});
		genInfoPane.add(searchButton, "flowx,cell 0 6");

		JButton newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		newButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Opens add vehicle pop-up
				Add.mainGui(intersection);
			}
		});
		genInfoPane.add(newButton, "flowx,cell 2 6");

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vehicleTable.clearSelection();
			}
		});
		genInfoPane.add(clearButton, "cell 0 6");

		JButton editButton = new JButton("Edit");
		editButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Opens edit vehicle page pop-up
				Edit.mainGui(intersection);
			}
		});
		genInfoPane.add(editButton, "cell 2 6");

		//JLabels
		JLabel vehicleLabel = new JLabel("Vehicles");
		genInfoPane.add(vehicleLabel, "cell 0 0");

		JLabel phaseLabel = new JLabel("Phases");
		genInfoPane.add(phaseLabel, "cell 1 0");

		JLabel statsLabel = new JLabel("Statistics");
		genInfoPane.add(statsLabel, "cell 2 0");
		
		JLabel airQualLabel = new JLabel("Air quality \r\nindex");
		genInfoPane.add(airQualLabel, "flowx,cell 1 4");
		
		JLabel numberPlateLabel = new JLabel("Plate number browser");
		panel_1.add(numberPlateLabel);
		
		JLabel co2IndexLabel = new JLabel("CO2 index" + " \u00B5" +"g");
		genInfoPane.add(co2IndexLabel, "flowx,cell 1 3");
		
		JButton randomButton = new JButton("Random Vehicle");
		randomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					intersection.addNRandomVehicles(1);
			}
		});
		genInfoPane.add(randomButton, "cell 2 6");
		
		JButton removeBtn = new JButton("Remove"); 
		removeBtn.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				removeHighlightedVehicle(); 
			} 
		}); 
		genInfoPane.add(removeBtn, "cell 0 6"); 

		
		/*
		 * Advanced Features tab 
		 */
		JScrollPane advFeaturesPane = new JScrollPane();
		advFeaturesPane.setToolTipText("Advanced features");
		tabbedPane.addTab("Advanced features", null, advFeaturesPane, null);

		panel2 = new JPanel();
		advFeaturesPane.setViewportView(panel2);

		//Graphical output of current intersection state 
		intersectionLabel = new JLabel("Intersection State");
		intersectionLabel.setIcon(new ImageIcon(Gui.class.getResource("/intersection_sim/intersection.png")));
		panel2.add(intersectionLabel);
		
		/*
		 * Export Settings tab
		 */
		JPanel exportPane = new JPanel();
		tabbedPane.addTab("Export settings", null, exportPane, null);
		exportPane.setLayout(new MigLayout("",
				"[1px][1px][1px][1px][1px][1px][105px][5px][39px][5px][87px][5px][51px][5px][73px][5px][111px]",
				"[23px][23px][][][][][][][][][]"));

		JLabel featuresSelectionLabel = new JLabel("Select features to be exported");
		exportPane.add(featuresSelectionLabel, "cell 6 1 3 1,alignx left,aligny center");

		
		//CheckBoxes
		JCheckBox carsCheckBox = new JCheckBox("Cars passed through intersection");
		carsCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					cars = true;
				} else
					cars = false;
			}
		});

		carsCheckBox.setSelected(true);
		exportPane.add(carsCheckBox, "cell 6 2,alignx left,aligny top");

		JCheckBox timeCheckBox = new JCheckBox("Average waiting time");
		timeCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					time = true;
				} else
					time = false;
			}
		});
		timeCheckBox.setSelected(true);
		exportPane.add(timeCheckBox, "cell 6 4,alignx left,aligny top");

		JCheckBox co2CheckBox = new JCheckBox("Average CO2 emissions");
		co2CheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					co2 = true;
				} else
					co2 = false;
			}
		});
		co2CheckBox.setSelected(true);
		exportPane.add(co2CheckBox, "cell 6 6,alignx left,aligny top");

		JCheckBox airCheckBox = new JCheckBox("Air quality index");
		airCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					air = true;
				} else
					air = false;
			}
		});
		exportPane.add(airCheckBox, "cell 6 8,alignx left,aligny top");


		//JButtons
		JButton exitButton = new JButton("Exit");
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				intersection.generateLog();
				System.exit(0);
			}
		});
		exportPane.add(exitButton, "cell 14 10,alignx left,aligny top");
		
		JButton reportButton = new JButton("Generate report file");
		reportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					intersection.generateReport(cars, time, co2, air);
					exportPopUp = new JFrame();
					JOptionPane.showMessageDialog(exportPopUp, "Report generated!");

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		exportPane.add(reportButton, "cell 10 10,alignx left,aligny top");
		frame.setVisible(true);
		

}
}
