package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import custom_exceptions.PlateNumberFormatException;
import intersection_sim.Lane;
import intersection_sim.Vehicle;

class LaneConstructorTest {
	
	Lane lane;
	int laneNumber, phaseNumber;
	LinkedList<Vehicle> vehicleQueue;
	IllegalArgumentException thrown;
	
	@BeforeEach
	void setUp() throws PlateNumberFormatException {
		vehicleQueue = new LinkedList<Vehicle>();
	}
	
	@Test
	void testConstructorCorrectData() {
		phaseNumber = 4;
		laneNumber = 1;
		try {
			lane = new Lane(laneNumber, phaseNumber, vehicleQueue);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		phaseNumber = 1;
		laneNumber = 1;
		try {
			lane = new Lane(laneNumber, phaseNumber, vehicleQueue);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		phaseNumber = 4;
		laneNumber = 8;
		try {
			lane = new Lane(laneNumber, phaseNumber, vehicleQueue);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	void testIncorrectLaneNumber() {
		phaseNumber = 1;
		laneNumber = 0;
		thrown = assertThrows(IllegalArgumentException.class, () -> lane = new Lane(laneNumber, phaseNumber, vehicleQueue));
		assertEquals("Lane number must be between 1 and 8", thrown.getMessage());
		
		phaseNumber = 1;
		laneNumber = 10;
		thrown = assertThrows(IllegalArgumentException.class, () -> lane = new Lane(laneNumber, phaseNumber, vehicleQueue));
		assertEquals("Lane number must be between 1 and 8", thrown.getMessage());
	}
	
	@Test
	void testIncorrectPhaseNumber() {
		phaseNumber = 0;
		laneNumber = 1;
		thrown = assertThrows(IllegalArgumentException.class, () -> lane = new Lane(laneNumber, phaseNumber, vehicleQueue));
		assertEquals("Phase number must be between 1 and 4", thrown.getMessage());
		
		phaseNumber = 5;
		laneNumber = 1;
		thrown = assertThrows(IllegalArgumentException.class, () -> lane = new Lane(laneNumber, phaseNumber, vehicleQueue));
		assertEquals("Phase number must be between 1 and 4", thrown.getMessage());
	}

}