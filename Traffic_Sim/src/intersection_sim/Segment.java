package intersection_sim;

import java.util.Iterator;
import java.util.LinkedList;

public class Segment {

	private int numberSegment, totalEmissions, numberOfVehiclesWaiting, leftQueueLength, leftWaitingTime, straightRightQueueLength, straightRightWaitingTime;
	private double averageWaitingTime, averageWaitingLength, averageCrossingTime;
	private Lane laneLeft, laneStraightRight;
	private LinkedList<Vehicle> leftQueue, straightRightQueue;
	private Iterator<Vehicle> itLeft, itStraightRight;
	private Vehicle current;

	/**
	 *  Sets up the Segment with the number of segment it is.
	 * 
	 * @param numberSegment number of the segment
	 */
	public Segment(int numberSegment, Lane laneLeft, Lane laneStraightRight) {
		//Check if numberSegment is between the allowed values 1 to 4.
		if (numberSegment < 1 || numberSegment > 4) {
			throw new IllegalArgumentException("Segment number must be between 1 and 4");
		}

		this.numberSegment = numberSegment;
		this.laneLeft = laneLeft;
		this.laneStraightRight = laneStraightRight;
	}
	
	public void addVehicle(Vehicle v) {
		switch (v.getDirection()) {
		case 1: // Left direction
			laneLeft.addVehicle(v);
			System.out.println("Vehicle added left");
			break;
		case 2: // Straight direction
			laneStraightRight.addVehicle(v);
			System.out.println("Vehicle added straight");
			break;
		case 3: // Right direction
			laneStraightRight.addVehicle(v);
			System.out.println("Vehicle added right");
			break;
		default:
			throw new IllegalArgumentException("That direction is not available");
		}
	}
	
	/**
	 * Gets the lane located in the left side of the segment.
	 * 
	 * @return lane
	 */
	public Lane getLaneLeft() {
		return laneLeft;
	}
	
	/**
	 * Gets the lane located in the straight/right side of the segment-
	 * 
	 * @return lane
	 */
	public Lane getLaneStraightRight() {
		return laneStraightRight;
	}

	/**
	 * Gets the segment number.
	 * 
	 * @return numberSegment
	 */
	public int getNumberSegment() {
		return numberSegment;
	}

	/**
	 * Calculates the latest average waiting time of the vehicles.
	 * 
	 * @return averageWaitingTime
	 */
	public double getAverageWaitingTime() {
		// Gets all the information needed from the left lane
		leftQueue = laneLeft.getVehicleQueue();
		leftQueueLength = leftQueue.size(); leftWaitingTime = 0; 
		
		// Iterates through the left lane vehicle queue and adds up all the crossing times
		itLeft = leftQueue.iterator();
		while(itLeft.hasNext()) {
			current = itLeft.next();
			leftWaitingTime += current.getCrossTime();
		}

		// Gets all the information needed from the right lane
		straightRightQueue = laneStraightRight.getVehicleQueue();
		straightRightQueueLength = straightRightQueue.size(); straightRightWaitingTime = 0; 

		// Iterates through the right lane vehicle queue and adds up all the crossing times
		itStraightRight = straightRightQueue.iterator();
		while(itStraightRight.hasNext()) {
			current = itStraightRight.next();
			straightRightWaitingTime += current.getCrossTime(); 
		}
		
		// Calculates the mean waiting time and returns it
		averageWaitingTime = (leftWaitingTime + straightRightWaitingTime)/2.0;

		return averageWaitingTime;
	}

	/**
	 * Calculates the latest average waiting length of the vehicles.
	 * 
	 * @return averageWaitingLength
	 */
	public double getAverageWaitingLength() {
		// Gets all the information needed from the left lane
		leftQueue = laneLeft.getVehicleQueue();
		leftQueueLength = leftQueue.size();

		// Iterates through the left lane vehicle queue and adds up all the vehicles' length
		int lengthLeft = 0;
		itLeft = leftQueue.iterator();
		while(itLeft.hasNext()) {
			current = itLeft.next();
			lengthLeft += current.getLength();
		}

		// Gets all the information needed from the right lane
		straightRightQueue = laneStraightRight.getVehicleQueue();
		straightRightQueueLength = straightRightQueue.size(); 

		// Iterates through the right lane vehicle queue and adds up all the vehicles' length
		int lengthStraightRight = 0;
		itStraightRight = straightRightQueue.iterator();
		while(itStraightRight.hasNext()) {
			Vehicle current = itStraightRight.next();
			lengthStraightRight += current.getLength();
		}

		// Calculates the average vehicle length and returns it
		if(leftQueueLength != 0 && straightRightQueueLength != 0) {
			averageWaitingLength = (double) (lengthLeft + lengthStraightRight)/(leftQueueLength + straightRightQueueLength);
		}
		else if(leftQueueLength == 0 && straightRightQueueLength != 0) {
			averageWaitingLength = (double) lengthStraightRight/straightRightQueueLength;
		}
		else if(leftQueueLength != 0 && straightRightQueueLength == 0) {
			averageWaitingLength = (double) lengthLeft/leftQueueLength;
		}
		else {
			averageWaitingLength = 0;
		}

		return averageWaitingLength;
	}

	/**
	 * Calculates the latest average crossing time of the vehicles
	 * 
	 * @return averageCrossingTime
	 */
	public double getAverageCrossingTime() {
		// Gets all the information needed from the left lane
		leftQueue = laneLeft.getVehicleQueue();
		leftQueueLength = leftQueue.size();
		
		// Iterates through the left lane vehicle queue and adds up all the crossing times
		int crossingTimeLeft = 0;
		itLeft = leftQueue.iterator();
		while(itLeft.hasNext()) {
			current = itLeft.next();
			crossingTimeLeft += current.getCrossTime();
		}

		// Gets all the information needed from the right lane
		straightRightQueue = laneStraightRight.getVehicleQueue();
		straightRightQueueLength = straightRightQueue.size(); 
		
		// Iterates through the right lane vehicle queue and adds up all the crossing times
		int crossingTimeStraightRight = 0;
		itStraightRight = straightRightQueue.iterator();
		while(itStraightRight.hasNext()) {
			Vehicle current = itStraightRight.next();
			crossingTimeStraightRight += current.getCrossTime();
		}
		
		// Calculates the average vehicle length and returns it
		if(leftQueueLength != 0 && straightRightQueueLength != 0) {
			averageCrossingTime = (double) (crossingTimeLeft + crossingTimeStraightRight)/(leftQueueLength + straightRightQueueLength);
		}
		else if(leftQueueLength == 0 && straightRightQueueLength != 0) {
			averageCrossingTime = (double) crossingTimeStraightRight/straightRightQueueLength;
		}
		else if(leftQueueLength != 0 && straightRightQueueLength == 0){
			averageCrossingTime = (double) crossingTimeLeft/leftQueueLength;
		}
		else {
			averageCrossingTime = 0;
		}

		return averageCrossingTime;
	}

	/**
	 * Calculates the latest total amount of emissions of the vehicles
	 * 
	 * @return totalEmissions
	 */
	public int getTotalEmissions() {
		// Gets all the information needed from the left lane
		leftQueue = laneLeft.getVehicleQueue();
		int emissionsLeft = 0;

		// Iterates through the left lane vehicle queue and adds up all the crossing times
		itLeft = leftQueue.iterator();
		while(itLeft.hasNext()) {
			current = itLeft.next();
			emissionsLeft += current.getCO2Emitted();
		}

		// Gets all the information needed from the right lane
		straightRightQueue = laneStraightRight.getVehicleQueue();
		int emissionsStraightRight = 0;

		// Iterates through the right lane vehicle queue and adds up all the crossing times
		itStraightRight = straightRightQueue.iterator();
		while(itStraightRight.hasNext()) {
			Vehicle current = itStraightRight.next();
			emissionsStraightRight += current.getCO2Emitted();
		}
		
		// Calculates the total emissions amount of the segment and returns it
		totalEmissions = emissionsLeft + emissionsStraightRight;
		return totalEmissions;
	}

	/**
	 * Calculates the latest amount of vehicles waiting in each segment
	 * 
	 * @return numberOfVehiclesWaiting
	 */
	public int getAmountOfVehiclesWaiting() {
		// Gets all the information needed from the left lane
		leftQueue = laneLeft.getVehicleQueue();
		leftQueueLength = leftQueue.size();

		// Gets all the information needed from the right lane
		straightRightQueue = laneStraightRight.getVehicleQueue();
		straightRightQueueLength = straightRightQueue.size(); 
		
		// Calculates the total amount vehicles still in the segment and returns it
		numberOfVehiclesWaiting = leftQueueLength + straightRightQueueLength;
		return numberOfVehiclesWaiting;
	}
}