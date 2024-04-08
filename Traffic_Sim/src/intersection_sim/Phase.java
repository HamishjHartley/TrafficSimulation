package intersection_sim;

import java.util.ArrayList;
import java.util.Iterator;


public class Phase extends Thread {

	private int phaseNumber, duration;
	private ArrayList<Lane> laneList = new ArrayList<Lane>();
	private long targetTimerTime;
	private Boolean timerRunning = false;
	private ArrayList<String> vehiclesCrossedLastIteration = new ArrayList<String>();

	public Phase(int phaseNumber, int duration) {
		// Check if the cycle number is 0 or negative
		if (phaseNumber <= 0) {
			throw new IllegalStateException("Phase number must be greater than 0");
		}
		// Check if the duration is negative
		if (duration < 0) {
			throw new IllegalStateException("Duration must be a positive number");
		}

		this.phaseNumber = phaseNumber;
		this.duration = duration;
	}

	/**
	 * Return phase number.
	 * 
	 * @return phase number
	 */
	public int getPhaseNumber() {
		return phaseNumber;
	}
	
	/**
	 * Return duration of the phase.
	 * 
	 * @return phase duration
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Modify the phase duration.
	 * 
	 * @param new phase duration
	 */
	public void setDuration(int newDuration) {
		if (newDuration < 1) {
			throw new IllegalArgumentException("Duration can only be a positive number");
		}
		this.duration = newDuration;
	}
	
	/**
	 * Add a lane to the phase.
	 * 
	 * @param lane
	 */
	public void addLane(Lane lane) {
		if(laneList.contains(lane)) {
			throw new IllegalArgumentException("Cannot have duplicated lanes in the same phase");
		}
		else {
			laneList.add(lane);
		}
	}
	
	/**
	 * Get the lanes assigned to this phase.
	 * 
	 * @return lanes of this phase
	 */
	public ArrayList<Lane> getLanes() {
		return laneList;
	}
	
	/**
	 * Start the phase timer according to the phase duration.
	 */
	public void startTimer() {
		// Set all the lanes assigned to this phase to green light, so the vehicles know they can 
		// cross.
		Iterator<Lane> itr = laneList.iterator();
		while(itr.hasNext()) {
			itr.next().setGreenStreetLight(true);
		}
		// Get current time and compute the target time
		long startTimerTime = System.currentTimeMillis();
		targetTimerTime = startTimerTime / 1000 + duration;
		timerRunning = true;
	}
	
	/**
	 * Returns a Boolean indicating if the phase's timer is running or not. And if the timer is finished,
	 * it changes the lane's street light color back to red.
	 * 
	 * @return true for timer running
	 */
	public Boolean isTimerOn() {
		if(!timerRunning) {
			throw new IllegalStateException("Phase timer not running");
		}
		else {
			long currentTime = System.currentTimeMillis() / 1000;
			// Check if current time is greater than timer target time. If true, change lane's street light
			// status, stop the timer, and return false.
			if(currentTime >= targetTimerTime) {
				Iterator<Lane> itr = laneList.iterator();
				while(itr.hasNext()) {
					itr.next().setGreenStreetLight(false);
				}
				timerRunning = false;
				return false;
			}
			// If still running return true
			else {
				return true;
			}
		}
	}
	
	/**
	 * Adds vehicle's plate number to a temporary buffer of vehicles that just crossed the intersection on 
	 * the last iteration of this phase.
	 * 
	 * @param plateNumber
	 */
	public void addVehicleAsCrossed(String plateNumber) {
		vehiclesCrossedLastIteration.add(plateNumber);
	}
	
	/**
	 * Return the temporary array list of the plate numbers of vehicles that crossed the intersection on
	 * the last iteration of this phase. This buffer gets cleared every time this list is retrieved with
	 * this method.
	 * 
	 * @return vehicles crossed plate number list
	 */
	public ArrayList<String> getVehiclesCrossedLastPhase() {
		ArrayList<String> vehicleArr = new ArrayList<String>();
		// Copy the buffer to a temporary array list
		for(String numberPlate: vehiclesCrossedLastIteration) {
			vehicleArr.add(numberPlate);
		}
		// Clear buffer
		vehiclesCrossedLastIteration.clear();
		
		return vehicleArr;
	}
	
}
