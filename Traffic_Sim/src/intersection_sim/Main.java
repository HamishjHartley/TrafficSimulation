package intersection_sim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import custom_exceptions.PlateNumberFormatException;

public class Main {

	/** 
	 * Reads the data stored in two CSV files containing the lanes and segments information, and the vehicle information.
	 * After this, save the appropriate information in the infrastructure object and return it.
	 * 
	 * @param intersectionCSV CSV file containing information regarding lanes and segments
	 * @param vehiclesCSV CSV files containing information regarding vehicles information.
	 * @return intersection
	 */
	private static Intersection setUpSimulationFromFile(String intersectionCSV, String vehiclesCSV) {
		Intersection inter = new Intersection();
		BufferedReader br = null;
		ArrayList<Integer> phaseIndexAvailable = new ArrayList<>();
		try {
			//			/** READ DATA FROM "intersection.csv" AND CREATE PHASES, SEGMENTS AND LANES **/
//						FileReader fr = new FileReader(intersectionCSV); // Comment for JAR
//						br = new BufferedReader(fr);					 // Comment for JAR

			InputStream textFileData = Main.class.getResourceAsStream(intersectionCSV); // Uncomment for JAR
			br = new BufferedReader(new InputStreamReader(textFileData, "UTF-8")); // Uncomment for JAR
			String line;

			// INITIALISE PHASES AND ASSIGN THEIR DURATION
			// Skip titles row
			br.readLine();
			while((line = br.readLine()) != null && !line.equals(",,,,")) {
				String[] phaseData = line.split(",");
				System.out.println(line);
				// Create the phase
				inter.addPhase(Integer.parseInt(phaseData[0]), Integer.parseInt(phaseData[1]));
				// Add the phase's index to the list of available phases
				phaseIndexAvailable.add(Integer.parseInt(phaseData[0]));
			}

			// INITIALISE SEGMENTS AND LANES AND ASSIGN THEM TO THEIR RESPECTIVE PHASES 
			// Skip titles row
			br.readLine();
			while((line = br.readLine()) != null && !line.equals(",,,,")) {
				String[] intersectionData = line.split(",");
				// Check that the segments' phases exists
				if(!phaseIndexAvailable.contains(Integer.parseInt(intersectionData[2])) || 
						!phaseIndexAvailable.contains(Integer.parseInt(intersectionData[4]))) {
					throw new IllegalStateException("Invalid phase number");
				}

				// Add lane left turn and its duration
				inter.addLane(Integer.parseInt(intersectionData[1]), Integer.parseInt(intersectionData[2]));
				// Add lane straight/right  and its duration
				inter.addLane(Integer.parseInt(intersectionData[3]), Integer.parseInt(intersectionData[4]));
				// Segment name, left turn lane, straight/right turn lane
				inter.addSegment(Integer.parseInt(intersectionData[0]), Integer.parseInt(intersectionData[1]),
						Integer.parseInt(intersectionData[3]));
			}

			// SET AMBER LIGHT DURATION
			// Skip titles row
			br.readLine();
			while((line = br.readLine()) != null) {
				String[] intersectionData = line.split(",");

				inter.setAmberLightDuration(Integer.parseInt(intersectionData[0]));
			}

			/** READ DATA FROM "vehicles.csv" AND CREATE VEHICLES **/
//			fr = new FileReader(vehiclesCSV); // Comment for JAR
//			br = new BufferedReader(fr);	  // Comment for JAR

			textFileData = Main.class.getResourceAsStream(vehiclesCSV); // Uncomment for JAR
			br = new BufferedReader(new InputStreamReader(textFileData, "UTF-8")); // Uncomment for JAR

			// Skip titles row
			br.readLine();
			// Start reading the data
			while ((line = br.readLine()) != null) {
				String[] intersectionData = line.split(",");
				String plateNumber = intersectionData[0];			
				String vehicleType = intersectionData[1];
				int crossingTime = Integer.parseInt(intersectionData[2]);
				int direction = getDirectionInteger(intersectionData[3]);
				int length = Integer.parseInt(intersectionData[4]);
				int emissions = Integer.parseInt(intersectionData[5]);
				Boolean status = statusToBoolean(intersectionData[6]);
				int segment = Integer.parseInt(intersectionData[7]);

				// Add vehicle object
				inter.addVehicle(plateNumber, vehicleType, crossingTime, direction, length, emissions, status, segment);
			}

		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found! " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println("IOException!");
			e.printStackTrace();
			System.exit(1);
		} catch (PlateNumberFormatException e) {
			e.printStackTrace();
			System.exit(1);
		} 
		//Uncomment for no JAr
		//			finally {
		//			try {
		//				br.close();
		//			} catch (IOException ioe) {
		//				System.out.println("IOException!");
		//				ioe.printStackTrace();
		//			}
		//		}

		return inter;
	}

	/** 
	 * Translate status in String form "Waiting" and "Crossed" to Boolean, false and true respectively.
	 * 
	 * @param status string holding the state of the car (crossed or waiting)
	 * @return crossedStatus boolean depending on the inputed status
	 */
	private static Boolean statusToBoolean(String status) {
		if (status.equals("Waiting")) {
			return false;
		} else if (status.equals("Crossed")) {
			return true;
		} else {
			throw new IllegalStateException("Status of the vehicle: '" + status + "' is not recognized");
		}
	}

	/** 
	 * Return an integer from 1 to 3 according to the Strings "Left", "Straight" and "Right"
	 * 
	 * @param direction vehicle's direction (left, straight or right)
	 * @return direction (1="Left", 2="Straight" and 3="Right")
	 */
	private static int getDirectionInteger(String direction) {
		if(direction.equals("Left")) {
			return 1;
		}
		else if(direction.equals("Straight")) {
			return 2;
		}
		else if(direction.equals("Right")) {
			return 3;
		}
		else {
			throw new IllegalStateException("Direction of the vehicle: '" + direction + "' is not recognized");
		}
	} 

	public static void main(String[] args) {
		// Set up the simulation environment
		Intersection inter = setUpSimulationFromFile("intersection.csv", "vehicles.csv");

		// Update elements in the GUI
		inter.updateSegments();

		// Create and start controller thread
		Thread th = new Thread(inter);
		th.run();
	}
}