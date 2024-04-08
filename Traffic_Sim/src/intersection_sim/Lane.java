package intersection_sim;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Lane {

	private int numberLane, phase;
	private LinkedList<Vehicle> vehicleQueue = new LinkedList<Vehicle>();

	/**
	 * Sets up the Lane with the number of lane it is and its phase.
	 * This constructor will be used to add the intersection configuration from intersection.csv
	 *  
	 * @param numberLane number of the Lane
	 * @param phase time the Lane will last in seconds
	 */
	public Lane (int numberLane, int phase, LinkedList<Vehicle> vehicleQueue) {
		//Check if numberLane is between the allowed values 1 to 8
		if (numberLane < 1 || numberLane > 8) {
			throw new IllegalArgumentException("Lane number must be between 1 and 8");
		}

		// Check if the phase is between the allowed values 1 to 4
		if (phase < 1 || phase > 4) {
			throw new IllegalArgumentException("Phase number must be between 1 and 4");
		}

		this.numberLane = numberLane;
		this.phase = phase;
		this.vehicleQueue = vehicleQueue;
	}


	/**
	 * Gets the Lane number
	 * 
	 * @return numberLane
	 */
	public int getNumberLane() {
		return numberLane;
	}

	/**
	 * Gets the Lane phase
	 * 
	 * @return phase
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * Sets the Lane phase to a new one being provided
	 * 
	 * @param newPhase new time the Lane will last in seconds
	 */
	public void setPhase(int newPhase) {
		if (newPhase < 1) {
			throw new IllegalArgumentException("Phase can only be a positive int");
		}
		this.phase = newPhase;
	}

	/**
	 * Adds a vehicle to the Lane's queue
	 * 
	 * @param automobile new vehicle in the Lane
	 */
	public void addVehicle(Vehicle automobile) {
		vehicleQueue.add(automobile);
	}

	/**
	 * Removes a vehicle that is still in the queue, from the GUI.
	 * Throws an exception in the case that the vehicle queue is empty and no automobile can be removed. 
	 * 
	 * @param plateNumber represents the plate number of the vehicle that needs to be deleted from the queue
	 */
	public void removeVehicle(String numberPlate) {
		if(vehicleQueue.isEmpty()) {
			throw new NoSuchElementException("There are no vehicles in this queue");
		}
		else {
			boolean removed = false;
			Iterator<Vehicle> it = vehicleQueue.iterator();
			while(it.hasNext()) {
				Vehicle current = it.next();
				if(current.getNumberPlate().equals(numberPlate)) {
					vehicleQueue.remove(current);
					removed = true;
					break;
				}
			}
			if(!removed)
				throw new NoSuchElementException("There are no vehicles with that number plate in this queue");
		}
	}
	
	/**
	 * Return the first vehicle in the FIFO queue
	 * 
	 * @return Vehicle
	 */
	public Vehicle getFirstVehicleInQueue() {
		if(vehicleQueue.isEmpty()) {
			throw new NoSuchElementException("There are no vehicles in this queue");
		}
		else {
			return vehicleQueue.getFirst();
		}
	}
	
	/**
	 * Remove the first vehicle in the FIFO queue
	 * @return Vehicle
	 */
	public void removeFirstVehicleInQueue() {
		if(vehicleQueue.isEmpty()) {
			throw new NoSuchElementException("There are no vehicles in this queue");
		}
		else {
			vehicleQueue.removeFirst();
		}
	}

	/**
	 * Gets the vehicleº queue of this Lane
	 * 
	 * @return vehicleQueue
	 */
	public LinkedList<Vehicle> getVehicleQueue(){
		return vehicleQueue;
	}
	
	public void setGreenStreetLight(Boolean isGreen) {
		Iterator<Vehicle> vehicles = vehicleQueue.iterator();
		while(vehicles.hasNext()) {
			vehicles.next().isGreenLight(isGreen);
		}
	}
}
