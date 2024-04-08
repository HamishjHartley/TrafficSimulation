package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import custom_exceptions.PlateNumberFormatException;
import intersection_sim.Lane;
import intersection_sim.Phase;
import intersection_sim.Vehicle;

class LaneMethodsTest {
	
	Lane lane;
	int newPhase, laneNumber, phaseNumber, duration;
	LinkedList<Vehicle> vehicleQueue, testVehicleQueue;
	Vehicle v1, v2;
	Phase p;
	
	@BeforeEach
	void setUp() throws PlateNumberFormatException {
		newPhase = 2;
		laneNumber = 3;
		phaseNumber = 1;
		duration =15;
		p = new Phase(phaseNumber, duration);
		v1 = new Vehicle("AB12 CDE", "Car", 5, 2, 10, 7, false, 1, p); 
		v2 = new Vehicle("FG34 HIJ", "Bus", 8, 2, 14, 15, false, 1, p);
		vehicleQueue = new LinkedList<Vehicle>();
		testVehicleQueue = new LinkedList<Vehicle>();
		lane = new Lane(laneNumber, phaseNumber, vehicleQueue);
	}
	
	@Test
	void testGetPhase() {
		assertEquals(phaseNumber, lane.getPhase());
	}
	
	@Test
	void testSetPhase() {
		assertEquals(phaseNumber, lane.getPhase());
		lane.setPhase(newPhase);
		assertEquals(newPhase, lane.getPhase());
		assertNotEquals(phaseNumber, lane.getPhase());
	}
	
	@Test
	void testAddCorrectVehicleAndGetVehicleQueue() {
		assertEquals(testVehicleQueue.size(), lane.getVehicleQueue().size());
		lane.addVehicle(v1);
		lane.addVehicle(v2);
		testVehicleQueue.add(v1);
		testVehicleQueue.add(v2);
		assertEquals(testVehicleQueue.size(), lane.getVehicleQueue().size());
		assertEquals(testVehicleQueue.getFirst(), lane.getVehicleQueue().getFirst());
	}
	
	@Test
	void testAddIncorrectVehicle() throws PlateNumberFormatException { 
		assertEquals(testVehicleQueue.size(), lane.getVehicleQueue().size());
		PlateNumberFormatException thrown = assertThrows(PlateNumberFormatException.class, () -> lane.addVehicle(v1 = new Vehicle("AB12CDE", "Car", 5, 2, 10, 7, false, 1, p)));
		assertEquals("The plate number format is not supported (AB12CDE). \nPlease, use a plate number in the format (XX23 XXX)", thrown.getMessage());
	}

	@Test
	void testRemoveCorrectVehicle() {
		lane.addVehicle(v1);
		lane.addVehicle(v2);
		testVehicleQueue.add(v1);
		testVehicleQueue.add(v2);
		assertEquals(testVehicleQueue.size(), lane.getVehicleQueue().size());
		lane.removeVehicle(v1.getNumberPlate());
		lane.removeVehicle(v2.getNumberPlate());
		testVehicleQueue.remove(v1);
		testVehicleQueue.remove(v2);
		assertEquals(testVehicleQueue.size(), lane.getVehicleQueue().size());
		assertEquals(testVehicleQueue, lane.getVehicleQueue());
	}
	
	@Test
	void testRemoveIncorrectVehicle() {
		testAddCorrectVehicleAndGetVehicleQueue();
		NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> lane.removeVehicle("XX23 XXX"));
		assertEquals("There are no vehicles with that number plate in this queue", thrown.getMessage());
	}
	
	@Test
	void testRemoveVehicleEmptyQueue() {
		NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> lane.removeVehicle(v1.getNumberPlate()));
		assertEquals("There are no vehicles in this queue", thrown.getMessage());
	}
}