package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import custom_exceptions.PlateNumberFormatException;
import intersection_sim.Intersection;
import intersection_sim.Lane;
import intersection_sim.Vehicle;

class IntersectionMethodsTest {

	Intersection inter;

	void setupPhases() {
		inter.addPhase(1, 10);
		inter.addPhase(2, 20);
		inter.addPhase(3, 30);
		inter.addPhase(4, 40);
	}

	void setupLanes() {
		inter.addLane(1, 3);
		inter.addLane(2, 1);
		inter.addLane(3, 4);
		inter.addLane(4, 2);
		inter.addLane(5, 3);
		inter.addLane(6, 1);
		inter.addLane(7, 4);
		inter.addLane(8, 2);	
	}

	void setupSegments() {
		inter.addSegment(1, 1, 6);
		inter.addSegment(2, 3, 8);
		inter.addSegment(3, 5, 2);
		inter.addSegment(4, 7, 4);
	}

	@BeforeEach
	void setUp(){
		inter = new Intersection();
	}

	@Test
	void addLane() {
		// First - creates phases
		inter.addPhase(1, 10);
		inter.addPhase(2, 10);
		// Second - creates lanes, which are linked to the previous phases
		inter.addLane(1, 1);
		inter.addLane(2, 2);
		// Third - creates a segment, which holds the lanes
		inter.addSegment(1, 1, 2);
		
		// Checks if the lanes are saved in their corresponded phases
		assertEquals(inter.getSegment(1).getLaneLeft().getPhase(), 1);
		assertEquals(inter.getSegment(1).getLaneStraightRight().getPhase(), 2);
	}

	@Test
	void addSegment() {
		// First - creates phases
		inter.addPhase(1, 10);
		inter.addPhase(2, 10);
		// Second - creates lanes, which are linked to the previous phases
		inter.addLane(1, 1);
		inter.addLane(2, 2);
		// Third - creates a segment, which holds the lanes
		inter.addSegment(1, 1, 2);
		
		// Checks if the segment has been initialised properly
		assertEquals(inter.getSegment(1).getNumberSegment(), 1);
	}

	@Test
	void addVehicle() {
		//default phases
		setupPhases();	
		//default lanes
		setupLanes();		
		//default segments
		setupSegments();
		try {
			inter.addVehicle("AA23 AAA", "Car", 10, 3, 5, 2, false, 4);			
		} catch (PlateNumberFormatException e) {
			fail();
		}
	}

	@Test
	void removeVehicle() {
		//default phases
		setupPhases();	
		//default lanes	
		setupLanes();	
		//default segments
		setupSegments();
		try {
			inter.addVehicle("AA23 AAA", "Car", 10, 3, 5, 2, false, 4);			
		} catch (PlateNumberFormatException e) {
			fail();
		}
		inter.removeVehicle("AA23 AAA");
	}

	@Test
	void addDuplicateVehicle() {
		//default phases
		setupPhases();	
		//default lanes
		setupLanes();		
		//default segments
		setupSegments();
		try {
			inter.addVehicle("AA23 AAA", "Car", 10, 3, 5, 2, false, 4);		
			assertThrows(IllegalStateException.class, () -> inter.addVehicle("AA23 AAA", "Car", 10, 3, 5, 2, false, 4));	
		} catch (PlateNumberFormatException e) {
			fail();
		}
	}

	@Test
	void addIncorrectVehicle() {
		//default phases
		setupPhases();
		//default lanes
		setupLanes();		
		//default segments
		setupSegments();
		assertThrows(PlateNumberFormatException.class, () -> inter.addVehicle("AA3 AAA", "Car", 10, 3, 5, 2, false, 4));
	}

	@Test
	void updatePhase() {
		// First - creates phases
		inter.addPhase(1, 10);
		inter.addPhase(2, 10);
		// Second - creates lanes, which are linked to the previous phases
		inter.addLane(1, 1);
		inter.addLane(2, 2);
		// Third - creates a segment, which holds the lanes
		inter.addSegment(1, 1, 2);
		
		// Checks if the initial duration of the phase
		assertEquals(inter.getPhase(1, 1).getDuration(), 10);
		// Changes the duration of the phase
		inter.changePhase(1, 15);
		// Checks the new duration of the phase
		assertEquals(inter.getPhase(1, 1).getDuration(), 15);
	}

	@Test
	void updateIncorrectLane() {
		// First - creates phases
		inter.addPhase(1, 10);
		inter.addPhase(2, 10);
		// Second - creates lanes, which are linked to the previous phases
		inter.addLane(1, 1);
		inter.addLane(2, 2);
		// Third - creates a segment, which holds the lanes
		inter.addSegment(1, 1, 2);

		// Checks if the initial duration of the phase
		assertEquals(inter.getPhase(1, 1).getDuration(), 10);

		//modify Lane that does not exist
		assertThrows(IndexOutOfBoundsException.class, () -> inter.changePhase(3, 10));
		//negative phase
		assertThrows(IllegalArgumentException.class, () -> inter.changePhase(1, -1));

	}


	// Test functions for stage 2 functionality

	@Test
	void updateVehicle() throws InterruptedException {
		//default phases
		setupPhases();	
		//default lanes
		setupLanes();		
		//default segments
		setupSegments();

		//populate with some vehicles
		try {
			inter.addVehicle("AA11 AAA", "Car", 10, 3, 5, 2, false, 4);
			inter.addVehicle("AA22 AAB", "Truck", 20, 3, 5, 2, false, 4);
		} catch (PlateNumberFormatException e) {
			fail();
		}

		Thread.sleep(5000);
		inter.updateVehicleStatus("AA11 AAA", true);
		Thread.sleep(5000);
	}

	@Test
	void updateStatistics() throws InterruptedException {
		//default phases
		setupPhases();		
		//default lanes
		setupLanes();	
		//default segments
		setupSegments();
		
		//populate with some vehicles
		try {
			inter.addVehicle("AA11 AAA", "Car", 10, 3, 5, 2, false, 4);
			inter.addVehicle("AA22 AAB", "Truck", 20, 3, 5, 2, false, 4);
		} catch (PlateNumberFormatException e) {
			fail();
		}
		Thread.sleep(5000);
		inter.updateVehicleStatus("AA11 AAA", true);
		Thread.sleep(5000);
	}
	
	@Test
	void selectPhase() throws InterruptedException {
		//default phases
		setupPhases();	
		//default lanes
		setupLanes();	
		//default segments
		setupSegments();
		
		//populate with some vehicles
		try {
			inter.addVehicle("AA11 AAA", "Car", 10, 3, 5, 2, false, 4);
			inter.addVehicle("AA22 AAB", "Truck", 20, 3, 5, 2, false, 4);
		} catch (PlateNumberFormatException e) {
			fail();
		}
//		inter.selectPhase(0);
		System.out.println("select first phase");
		Thread.sleep(5000);
//		inter.selectPhase(1);
		System.out.println("select 2nd phase");
		Thread.sleep(5000);
//		inter.selectPhase(2);
		System.out.println("select 3rd phase");
		Thread.sleep(5000);
//		inter.selectPhase(3);
		
	}
}

