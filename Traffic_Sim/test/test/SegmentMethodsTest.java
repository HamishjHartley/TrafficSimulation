package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import custom_exceptions.PlateNumberFormatException;
import intersection_sim.Lane;
import intersection_sim.Phase;
import intersection_sim.Segment;
import intersection_sim.Vehicle;

class SegmentMethodsTest {

	Segment segment;
	int numberSegment, phaseNumber, duration;
	Lane laneLeft, laneStraightRight;
	LinkedList<Vehicle> leftQueue, straightRightQueue;
	Vehicle v1, v2, v3;
	Phase p;

	@BeforeEach
	void setUp() throws PlateNumberFormatException {
		numberSegment = 1;
		phaseNumber = 1;
		duration = 15;
		leftQueue = new LinkedList<>();
		straightRightQueue = new LinkedList<>();
		laneLeft = new Lane(1, phaseNumber, leftQueue);
		laneStraightRight = new Lane(2, phaseNumber, straightRightQueue);
		segment = new Segment(numberSegment, laneLeft, laneStraightRight);
		p = new Phase(phaseNumber, duration);
		v1 = new Vehicle("AB12 CDE", "Car", 5, 2, 10, 7, false, 1, p); 
		v2 = new Vehicle("FG34 HIJ", "Bus", 8, 2, 14, 15, false, 1, p);
		v3 = new Vehicle("KL56 MNO", "Truck", 7, 2, 13, 17, false, 1, p); 
	}

	@Test
	void testGetAverageWaitingTimeNonDecimal() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		int totalTime = v1.getCrossTime() + v2.getCrossTime();
		double averageTime = totalTime/2.0;
		assertEquals(averageTime, segment.getAverageWaitingTime());
	}

	@Test
	void testGetAverageWaitingTimeDecimal() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		laneLeft.addVehicle(v3);
		int totalTime = v1.getCrossTime() + v2.getCrossTime() + v3.getCrossTime();
		double averageTime = totalTime/2.0;
		assertEquals(averageTime, segment.getAverageWaitingTime());
	}

	@Test
	void testGetAverageWaitingLengthNonDecimal() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		int totalLength = v1.getLength() + v2.getLength();
		double averageLength = totalLength/2.0;
		assertEquals(averageLength, segment.getAverageWaitingLength());
	}

	@Test
	void testGetAverageWaitingLengthDecimal() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		laneLeft.addVehicle(v3);
		int totalLength = v1.getLength() + v2.getLength() + v3.getLength();
		double averageLength = totalLength/3.0;
		assertEquals(averageLength, segment.getAverageWaitingLength());
	}

	@Test
	void testGetAverageWaitingLengthEmptyLeftNonDecimal() {
		laneStraightRight.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		int totalLength = v1.getLength() + v2.getLength();
		double averageLength = totalLength/2.0;
		assertEquals(averageLength, segment.getAverageWaitingLength());
	}

	@Test
	void testGetAverageWaitingLengthEmptyLeftDecimal() {
		laneStraightRight.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		laneStraightRight.addVehicle(v3);
		int totalLength = v1.getLength() + v2.getLength() + v3.getLength();
		double averageLength = totalLength/3.0;
		assertEquals(averageLength, segment.getAverageWaitingLength());
	}

	@Test
	void testGetAverageWaitingLengthEmptyStraightRightNonDecimal() {
		laneLeft.addVehicle(v1);
		laneLeft.addVehicle(v2);
		int totalLength = v1.getLength() + v2.getLength();
		double averageLength = totalLength/2.0;
		assertEquals(averageLength, segment.getAverageWaitingLength());
	}

	@Test
	void testGetAverageWaitingLengthEmptyStraightRightLeftDecimal() {
		laneLeft.addVehicle(v1);
		laneLeft.addVehicle(v2);
		laneLeft.addVehicle(v3);
		int totalLength = v1.getLength() + v2.getLength() + v3.getLength();
		double averageLength = totalLength/3.0;
		assertEquals(averageLength, segment.getAverageWaitingLength());
	}

	@Test
	void testGetAverageWaitingLengthEmpty() {
		assertEquals(0.0, segment.getAverageWaitingLength());
	}

	@Test
	void testGetAverageCrossingLengthNonDecimal() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		int totalLength = v1.getCrossTime() + v2.getCrossTime();
		double averageLength = totalLength/2.0;
		assertEquals(averageLength, segment.getAverageCrossingTime());
	}

	@Test
	void testGetAverageCrossingLengthDecimal() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		laneLeft.addVehicle(v3);
		int totalLength = v1.getCrossTime() + v2.getCrossTime() + v3.getCrossTime();
		double averageLength = totalLength/3.0;
		assertEquals(averageLength, segment.getAverageCrossingTime());
	}

	@Test
	void testGetAverageCrossingLengthEmptyLeftNonDecimal() {
		laneStraightRight.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		int totalLength = v1.getCrossTime() + v2.getCrossTime();
		double averageLength = totalLength/2.0;
		assertEquals(averageLength, segment.getAverageCrossingTime());
	}

	@Test
	void testGetAverageCrossingLengthEmptyLeftDecimal() {
		laneStraightRight.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		laneStraightRight.addVehicle(v3);
		int totalLength = v1.getCrossTime() + v2.getCrossTime() + v3.getCrossTime();
		double averageLength = totalLength/3.0;
		assertEquals(averageLength, segment.getAverageCrossingTime());
	}

	@Test
	void testGetAverageCrossingLengthEmptyStraightRightNonDecimal() {
		laneLeft.addVehicle(v1);
		laneLeft.addVehicle(v2);
		int totalLength = v1.getCrossTime() + v2.getCrossTime();
		double averageLength = totalLength/2.0;
		assertEquals(averageLength, segment.getAverageCrossingTime());
	}

	@Test
	void testGetAverageCrossingLengthEmptyStraightRightLeftDecimal() {
		laneLeft.addVehicle(v1);
		laneLeft.addVehicle(v2);
		laneLeft.addVehicle(v3);
		int totalLength = v1.getCrossTime() + v2.getCrossTime() + v3.getCrossTime();
		double averageLength = totalLength/3.0;
		assertEquals(averageLength, segment.getAverageCrossingTime());
	}

	@Test
	void testGetAverageCrossingLengthEmpty() {
		assertEquals(0.0, segment.getAverageCrossingTime());
	}

	@Test
	void testGetTotalEmissions() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		laneLeft.addVehicle(v3);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int totalEmissions = v1.getCO2Emitted() + v2.getCO2Emitted() +v3.getCO2Emitted();
		assertEquals(totalEmissions, segment.getTotalEmissions());
	}

	@Test
	void testGetTotalEmissionsEmpty() {
		assertEquals(0, segment.getTotalEmissions());
	}

	@Test
	void testGetAmountOfVehiclesWaiting() {
		laneLeft.addVehicle(v1);
		laneStraightRight.addVehicle(v2);
		laneLeft.addVehicle(v3);
		int totalAmountOfCars = laneLeft.getVehicleQueue().size() + laneStraightRight.getVehicleQueue().size();
		assertEquals(totalAmountOfCars, segment.getAmountOfVehiclesWaiting());
		laneLeft.removeVehicle(v1.getNumberPlate());
		totalAmountOfCars = laneLeft.getVehicleQueue().size() + laneStraightRight.getVehicleQueue().size();
		assertEquals(totalAmountOfCars, segment.getAmountOfVehiclesWaiting());
	}

	@Test
	void testGetAmountOfVehiclesWaitingEmpty() {
		assertEquals(0, segment.getAmountOfVehiclesWaiting());
	}
}