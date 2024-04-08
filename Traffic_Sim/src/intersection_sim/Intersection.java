package intersection_sim;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import custom_exceptions.InvalidVehicleException;
import custom_exceptions.PlateNumberFormatException;

public class Intersection extends Thread{

	static final long GUI_REFRESH_RATE = 500;  // In milliseconds (pattern)

	private int currentLane,segmentNumber;
	private int emissionTotal = 0, co2 = 0;
	private String airQuality;
	private int amberLightDuration = 0; // Set to 0 by default
	private Random rand  = new Random();

	private HashMap<String, Vehicle> vehicleList = new HashMap<String, Vehicle>();
	private ArrayList<Lane> laneList = new ArrayList<Lane>();
	private ArrayList<Segment> segmentList = new ArrayList<Segment>();
	private ArrayList<Phase> phaseList = new ArrayList<Phase>();
	
	private Log log = Log.getInstance();
	
	private enum Phases{
		FIRST(1),
		SECOND(2),
		THIRD(3),
		FOURTH(4);

		private int i;
		
		/**
		 * Sets the value of each Phases.
		 * 
		 * @param i value to which it belongs to.
		 */
		Phases(int i) {
			this.i = i;
		}
	}
	
	private Gui gui;

	/**
	 * Sets the intersection.
	 */
	public Intersection() {
		gui = new Gui(this);
	}

	/**
	 * Provides a user friendly descriptor of air Quality.
	 * Based on UK government standard: https://uk-air.defra.gov.uk/air-pollution/daqi.
	 * 
	 * @return airQuality String holding the state of the CO2 emissions.
	 */
	public String getAirQualityIndex() {
		if (co2 <= 1000) {
			airQuality ="Very high";
		} else if (co2 > 1000 && co2 <= 6000) {
			airQuality ="High";
		} else if (co2 > 6000 && co2 <=10000) {
			airQuality ="Moderate";
		} else if (co2 > 10000) {
			airQuality ="Low";
		}
		return airQuality;
	}

	/**
	 * Provides the GUI with the amount of total emissions that have crossed the intersection.
	 * 
	 * @return co2 integer holding the amount of CO2 emissions that have passed through the intersection.
	 */
	public int getCO2Index() {
		int sum = 0;
		// Iterates through segmentList summing total emissions from each segment
		for(int i = 0; i < segmentList.size(); i++){
			sum += segmentList.get(i).getTotalEmissions();
		}
		// Add sum to global CO2 variable
		co2 += sum;
		// Returns current CO2 value since the start of the simulation
		return co2; 
	}

	/**
	 * Gets the respective segment from the ArrayList given the number of the segment.
	 * 
	 * @param segmentNumber number of the segment wanted.
	 * @return Segment Object from the ArrayList<Segment>.
	 */
	public Segment getSegment(int segmentNumber) {
		return segmentList.get(segmentNumber-1);
	}

	/**
	 * This function is used by Main and Gui. 
	 * It creates a Vehicle Object and stores it in the global list. After this, it is
	 * assigned to Segment/Lane for statistics tracking. Finally, it is added to the Gui.
	 * 
	 * @param numberPlate plate number of the vehicle.
	 * @param vehicleType type of vehicle (Car, Bus or Truck).
	 * @param crossTime vehicle's crossing time.
	 * @param direction vehicle's direction (left, straight or right).
	 * @param length vehicle's length in metres.
	 * @param emissions amount of emissions the vehicle generates.
	 * @param crossed status of the vehicle (crossed or waiting).
	 * @param segment segment in which the vehicle is located.
	 * @throws PlateNumberFormatException exception which is thrown when the plate number doesn't 
	 * follow the specified format.
	 */
	public void addVehicle(String numberPlate, String vehicleType, int crossTime, int direction, int length,
			int emissions, Boolean crossed, int segment) throws PlateNumberFormatException {
		Vehicle vehicleObject;
		// Gets the phase in which the vehicle is found
		Phase phase = getPhase(segment, direction);
		// Depending on the type of the vehicle, different constructors are used
		switch(vehicleType) {
		case "Car":
			Car carObject = new Car(numberPlate, crossTime, direction, length, emissions, crossed, segment, phase);
			vehicleObject = carObject;
			break;
		case "Bus":
			Bus busObject = new Bus(numberPlate, crossTime, direction, length, emissions, crossed, segment, phase);
			vehicleObject = busObject;
			break;
		case "Truck":
			Truck truckObject = new Truck(numberPlate, crossTime, direction, length, emissions, crossed, segment, phase);
			vehicleObject = truckObject;
			break;
		default:
			vehicleObject = new Vehicle(numberPlate, vehicleType, crossTime, direction, length, emissions, crossed, segment, phase);
		}

		// If the vehicle is not already in the ArrayList<Vehicle>, then
		if (!vehicleList.containsKey(numberPlate)) {
			// Adds vehicle to segmentList, which also adds the vehicle to the phase
			if ((segment-1 >= segmentList.size())||(segment <=0)) {
				throw new IndexOutOfBoundsException("Segment must be between 1 and " + segmentList.size());
			}
			// Start the vehicle thread
			vehicleObject.start();
			segmentList.get(segment - 1).addVehicle(vehicleObject);
			// Adds vehicle to vehicleList and GUI
			vehicleList.put(numberPlate,vehicleObject);
			gui.addVehicle(vehicleObject);
			log.logVehicleAdded(vehicleObject);
		}
		else throw new IllegalStateException("This vehicle already exists");
	}

	/**
	 * 
	 * Used by Main and Gui, 
	 * It creates a random vehicle and stores it in the global list. After this, it is
	 * assigned to Segment/Lane for statistics tracking. Finally, it is added to the Gui.
	 * 
	 * @throws InvalidVehicleException
	 * @throws PlateNumberFormatException
	 */
	public void addNRandomVehicles(int num) {
		for(int i=0; i <num; i++) {
			//Defining the available possible parameters for a vehicle to have
			String[] type = {"Truck", "Car", "Bus"};
			int[] direction = {1,2,3};
			int[] segment = {1,2,3,4};
			Boolean crossed = false;
			
			//Random seed to select the type of vehicle 
			int rand = ThreadLocalRandom.current().nextInt(0, 2 + 1);
			//Random seed to select segment which vehicle will be added to
			int randSeg = ThreadLocalRandom.current().nextInt(0, 3 + 1);
			//Random seed to select the direction of the vehicle
			int randDir = ThreadLocalRandom.current().nextInt(0, 2 + 1);
			
			//Random numberPlate generator, 
			//generates numberPlate of format AA00 AAA
			//Reference to unicode Upper-case Latin alphabet range(65-90)
		    int leftLimit = 65; // letter 'a'
		    int rightLimit = 90; // letter 'z'
		    Random firstRandChars = new Random();
		    Random secondRandChars = new Random();

		  //Random string generation code adapted from tutorial: https://www.baeldung.com/java-random-string
		    String firstNumberChars = firstRandChars.ints(leftLimit, rightLimit + 1)
		      .limit(2)
		      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		      .toString();
			
		    String secondNumberChars = secondRandChars.ints(leftLimit, rightLimit + 1)
		  	      .limit(3)
		  	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  	      .toString();
		
			int randomNumPlateInt = new Random().nextInt(89)+10;
			//Appending randomly generated Strings and integer into a number plate
			String numberPlate = firstNumberChars + String.valueOf(randomNumPlateInt) + " " + secondNumberChars;
			
			try {
			switch(type[rand]) {
			case "Car":
				addVehicle(numberPlate, "Car", Car.STANDARD_CROSS_TIME, direction[randDir], Car.STANDARD_LENGTH, Car.STANDARD_EMISSIONS, crossed, segment[randSeg]);
				break;
			case "Bus":
				addVehicle(numberPlate, "Bus", Bus.STANDARD_CROSS_TIME, direction[randDir], Bus.STANDARD_LENGTH, Bus.STANDARD_EMISSIONS, crossed, segment[randSeg]);
				break;
			case "Truck":
				addVehicle(numberPlate, "Truck", Truck.STANDARD_CROSS_TIME, direction[randDir], Truck.STANDARD_LENGTH, Truck.STANDARD_EMISSIONS, crossed, segment[randSeg]);

				break;
			}
			} catch (PlateNumberFormatException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Calculates the phase in which the lane is found.
	 * 
	 * @param segment segment to which the lane belongs to.
	 * @param direction direction in which the vehicle wants to move.
	 * @return Phase Object from the ArrayList<Phase>.
	 */
	public Phase getPhase(int segment, int direction) {
		int phaseNumber;
		switch (direction) {
		case 1: //left
			phaseNumber = segmentList.get(segment-1).getLaneLeft().getPhase();
			System.out.println("Phase: "+phaseNumber);
			return phaseList.get(phaseNumber - 1);
		case 2: //straight
			phaseNumber = segmentList.get(segment-1).getLaneStraightRight().getPhase();
			System.out.println("Phase: "+phaseNumber);
			return phaseList.get(phaseNumber - 1);
		case 3: //right
			phaseNumber = segmentList.get(segment-1).getLaneStraightRight().getPhase();
			System.out.println("Phase: "+phaseNumber);
			return phaseList.get(phaseNumber - 1);
		default:
			throw new IllegalArgumentException("That direction is not available");
		}
	}


	/**
	 * This function is used by Main and Gui.
	 * It locates a Vehicle Object by numberplate String. Once located, it removes it from 
	 * the vehicleList, as well as from the GUI.
	 * 
	 * @param numberPlate plate number of the vehicle being deleted.
	 */

	public void removeVehicle(String numberPlate) {
		// Iterates through the vehicleList
		Vehicle vehicleObject = vehicleList.get(numberPlate);
		System.out.println("removing vehicle: " + vehicleObject.getNumberPlate());
		vehicleList.remove(numberPlate);
		gui.removeVehicle(numberPlate);

		// Determine segment number, then determine direction >> then we know Lane
		// Finds current segment with index (segmentNumber)
		Segment currentSegment = segmentList.get(vehicleObject.getSegment()-1);
		// Gets vehicle direction
		int currentDirection = vehicleObject.getDirection();
		for(int i = 1; i <= segmentList.size(); i++){
			// If the segment is the correct one then the vehicle is deleted from its specific 
			// Lane depending on its direction
			if(currentSegment.getNumberSegment() == i) {
				// Left
				if(currentDirection == 1 ) {
					currentSegment.getLaneLeft().removeVehicle(numberPlate);
					System.out.println("Removed left");
				}
				// Straight or right
				else if(currentDirection == 2 | currentDirection == 3) {
					currentSegment.getLaneStraightRight().removeVehicle(numberPlate);
					System.out.println("Removed straight/right");
				}
				log.logVehicleRemoved(vehicleObject);
			}
		}
	}

	/**
	 * Updates the Vehicle Object status in the GUI table.
	 * 
	 * @param numberPlate plate number of the Vehicle being checked.
	 * @param crossed Boolean that stores if the Vehicle has crossed or not.
	 */
	public void updateVehicleStatus(String numberPlate, boolean crossed) {
		// Find vehicle in GUI.
		gui.updateVehicle(numberPlate, crossed);
		
		//update C02 index and Air quality
		gui.updateAirQualityIndex();
		gui.updateC02Index();
		
		//can be done better
		this.updateSegments();
		log.logVehicleCrossed(vehicleList.get(numberPlate));
	}

	/**
	 * Used to initially load in the Lanes.
	 * 
	 * @param LaneNumber number of current Lane.
	 * @param phase phase of the cycle to which the Lane belongs to.
	 */
	public void addLane(int laneNumber, int phaseNumber) {
		LinkedList<Vehicle> vehicleQueue = new LinkedList<Vehicle>();
		Lane laneObject = new Lane(laneNumber, phaseNumber, vehicleQueue);
		laneList.add(laneObject);

		Phase phaseObject = phaseList.get(phaseNumber-1);
		phaseObject.addLane(laneObject);
		gui.addLane(laneNumber, laneObject);
	}

	/**
	 * Used to initially load in the Phases.
	 * 
	 * @param phaseNumber number of current Phase.
	 * @param duration duration of the phase measured in seconds.
	 */
	public void addPhase(int phaseNumber, int duration) {
		Phase phaseObject = new Phase(phaseNumber, duration);
		phaseList.add(phaseNumber-1, phaseObject);
		gui.addPhase(phaseNumber, phaseObject);
	}

	/**
	 * Used to initially load in the Segments, straight after the Lanes.
	 * 
	 * @param numberSegment number of the current Segment.
	 * @param LaneLeft Lane found in the left and is linked to the current Segment.
	 * @param LaneStraightRight Lane found in the straight/right and is linked to the current Segment.
	 */
	public void addSegment(int numberSegment, int laneLeft, int laneStraightRight) {
		Lane leftLaneObject = laneList.get(laneLeft - 1);
		Lane straightRightLaneObject = laneList.get(laneStraightRight - 1);
		Segment segmentObject = new Segment(numberSegment, leftLaneObject, straightRightLaneObject);
		segmentList.add(segmentObject);
		gui.addSegment(numberSegment, segmentObject);
	}

	/**
	 * Updates the segment's statistics after any modification is done to them.
	 */
	public void updateSegments() {
		gui.clearSegments();
		Iterator<Segment> iter = segmentList.iterator();
		while (iter.hasNext()) {
			Segment segmentObject = iter.next();
			gui.addSegment(segmentObject.getNumberSegment(), segmentObject);
		}
	}
	/**
	 * Updates Gui indexes for both air quality and CO2 index
	 */
	private void updateGuiIndex() {
		gui.updateAirQualityIndex();
		gui.updateC02Index();
	}


	/**
	 * Sets the current phase in the GUI to highlighted.
	 * @param phaseNumber number of the Phase that needs to be highlighted.
	 */
	public void selectPhase(Phase p, Color c) {
		int phaseNum = p.getPhaseNumber();
		phaseNum--;
		gui.setCurrentPhase(phaseNum, c);
	}

	/**
	 * Changes the Phases found in the GUI table.
	 * 
	 * @param phaseNumber number of phase being change.
	 * @param newDuration new duration of the phase in seconds.
	 */
	public void changePhase(int phaseNumber, int newDuration) {
		try {
			Phase phaseObject = phaseList.get(phaseNumber-1);
			phaseObject.setDuration(newDuration);
			gui.updatePhase(phaseNumber, phaseObject);
		} catch (IndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException("Phase number must be between 1 and " + phaseList.size());
		}
		
		updateSegments();
	}
	
	public void generateLog() {
		try {
			log.generateLogFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a report which is then exported when the user specifies so.
	 * The booleans represent checkboxes on the export setting page..
	 * 
	 * @param vehicles checkbox to save the vehicles which have passed through the intersection.
	 * @param time checkbox to save the total waiting time.
	 * @param co2 checbox to save the total CO2 emissions.
	 * @param air checkbox to save the air quality.
	 * @throws IOException excepton thrown if the txt file is not saved successfully.
	 */
	public void generateReport(boolean vehicles, boolean time, boolean co2, boolean air) throws IOException {
		try {
			// Create statistics_files directory
			final File directory = new File("statistics_files");
			directory.mkdir();

			//Creates a new file with time stamped name, then writes data structure to .txt file
			FileWriter writer = new FileWriter("statistics_files/statisitcs" + String.valueOf(System.currentTimeMillis()) + ".txt");
			System.out.println("File created succesfully");

			if(vehicles == true) {
				writer.write("Vehicles passed through intersection: " + "\n");
			} if (time == true) {
				writer.write("Total waiting time: " + "\n");
			}  if (co2 == true) {
				writer.write("Total CO2 emmisions: " + String.valueOf(co2) + "\n");
			} if (air == true) {
				writer.write("Air quality: " + airQuality + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Initially sets the amber light duration in Main with the data found in the CSV.
	 * 
	 * @param duration duration of the amber traffic light.
	 */
	public void setAmberLightDuration(int duration) {
		if(duration < 0) {
			throw new IllegalArgumentException("Amber light duration can't be negative");
		}
		else {
			amberLightDuration = duration;
		}
	}

	/**
	 * Activates the Phase in which the Vehicle Objects will start moving in their respective Lanes.
	 * 
	 * @param currPhase phase in which the simulation is currently on.
	 */
	private void activatePhase(Phase currPhase) {
		int i = 0;
		//log phase switch
		log.logPhaseChanged(currPhase.getPhaseNumber());
		// Start the timer for the Phase duration
		currPhase.startTimer();
		this.selectPhase(currPhase, Color.GREEN);
		while(currPhase.isTimerOn()) {
			// While the Phase timer is ON, then the threaded vehicles are notified and their status is updated in the GUI
			synchronized(currPhase) {
				if(i == 0) {
					currPhase.notifyAll();
					i++;
				}
				ArrayList<String> vehicleCrossedList = currPhase.getVehiclesCrossedLastPhase();
				for (String vehicle : vehicleCrossedList) {
					this.updateVehicleStatus(vehicle, true);			
				}
			}
		}

		// Set an extra target time for the amber duration in the traffic light
		long targetTime = System.currentTimeMillis() + amberLightDuration * 1000;
		this.selectPhase(currPhase, Color.YELLOW);
		while(System.currentTimeMillis() < targetTime) {
			// While the target time is not achieved the last crossing vehicles status are updated in the GUI
			ArrayList<String> vehicleCrossedList;
			synchronized(currPhase) {
				vehicleCrossedList = currPhase.getVehiclesCrossedLastPhase();
			}
			for (String vehicle : vehicleCrossedList) {
				this.updateVehicleStatus(vehicle, true);			
			}
			try {
				Thread.sleep(GUI_REFRESH_RATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Simulation of the road intersection given the 4 phases in which one cycle is divide into.
	 */
	public void run() {
		while(true) {
			Phase phaseObject;
			for (Phases currentPhase : Phases.values()) { 
				switch(currentPhase) {
				//Lane 2 and 6
				case FIRST:
					System.out.println("First cycle");
					gui.updateIcons(0);
						this.addNRandomVehicles(rand.nextInt(10));
					// Activates Phase and threaded vehicles
					phaseObject = phaseList.get(currentPhase.i-1);
					activatePhase(phaseObject);
					updateGuiIndex();
					break;
				//Lane 4 and 8
				case SECOND:
					System.out.println("Second cycle");
					gui.updateIcons(1);
						this.addNRandomVehicles(rand.nextInt(10));
					
					// Activates Phase and threaded vehicles
					phaseObject = phaseList.get(currentPhase.i-1);
					activatePhase(phaseObject);
					updateGuiIndex();
					break;
				//Lane 1 and 5
				case THIRD:
					System.out.println("Third cycle");
					gui.updateIcons(2);
						this.addNRandomVehicles(rand.nextInt(10));
					// Activates Phase and threaded vehicles
					phaseObject = phaseList.get(currentPhase.i-1);
					activatePhase(phaseObject);
					updateGuiIndex();
					break;
				//Lane 3 and 7
				case FOURTH:
					System.out.println("Fourth cycle");
					gui.updateIcons(3);
						this.addNRandomVehicles(rand.nextInt(10));
					// Activates Phase and threaded vehicles
					phaseObject = phaseList.get(currentPhase.i-1);
					activatePhase(phaseObject);
					updateGuiIndex();
					break;
				default:
					throw new IllegalStateException("Cannot have a Phase Object of type: "+currentPhase);
				}
			}
		}
	}
}
