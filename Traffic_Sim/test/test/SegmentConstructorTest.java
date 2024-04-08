package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import intersection_sim.Lane;
import intersection_sim.Segment;
import intersection_sim.Vehicle;

class SegmentConstructorTest {
	
	Segment segment;
	int numberSegment;
	Lane laneLeft, laneStraightRight;
	LinkedList<Vehicle> leftQueue, straightRightQueue;
	IllegalArgumentException thrown;
	
	@BeforeEach
	void setUp() throws Exception {
		leftQueue = new LinkedList<>();
		straightRightQueue = new LinkedList<>();
		laneLeft = new Lane(1, 3, leftQueue);
		laneStraightRight = new Lane(2, 1, straightRightQueue);
	}

	@Test
	void testConstructorCorrectData() {
		numberSegment = 3;
		try {
			segment = new Segment(numberSegment, laneLeft, laneStraightRight);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		numberSegment = 1;
		try {
			segment = new Segment(numberSegment, laneLeft, laneStraightRight);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		numberSegment = 4;
		try {
			segment = new Segment(numberSegment, laneLeft, laneStraightRight);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	void testIncorrectData() {
		numberSegment = 0;
		thrown = assertThrows(IllegalArgumentException.class, () -> segment = new Segment(numberSegment, laneLeft, laneStraightRight));
		assertEquals("Segment number must be between 1 and 4", thrown.getMessage());
		
		numberSegment = 5;
		thrown = assertThrows(IllegalArgumentException.class, () -> segment = new Segment(numberSegment, laneLeft, laneStraightRight));
		assertEquals("Segment number must be between 1 and 4", thrown.getMessage());
	}

}
