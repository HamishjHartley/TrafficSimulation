package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import custom_exceptions.PlateNumberFormatException;
import intersection_sim.Phase;
import intersection_sim.Vehicle;

class VehicleConstructorTest {

	@Test
	public void VehicleConstructorCorrectData() {
		try {
			Phase p = new Phase(1, 15);
			Vehicle v = new Vehicle("XX23 XXX", "Car", 10, 3, 5, 2, false, 4, p);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void wrongPlateNumber() {
		Phase p = new Phase(1, 15);
		// Wrong format => Space missing
		assertThrows(PlateNumberFormatException.class, () -> new Vehicle("XX23XXX", "Car", 10, 3, 5, 2, false, 4, p),
				"Expected to throw an PlateNumberFormatException, but it didn�t");

		// Wrong format ==> Letters instead of numbers or spaces
		String plateNumber = "XX23 XXX";
		for (int i = 2; i < 5; i++) {
			plateNumber = "XX23 XXX";
			char[] temporaryArr = plateNumber.toCharArray();
			temporaryArr[i] = 'A';

			assertThrows(PlateNumberFormatException.class,
					() -> new Vehicle(new String(temporaryArr), "Car", 10, 3, 5, 2, false, 4, p),
					"Expected to throw an PlateNumberFormatException, but it didn�t");
		}

		// Wrong format ==> Numbers instead of letters or spaces
		for (int i = 0; i < 2; i++) {
			plateNumber = "XX23 XXX";
			char[] temporaryArr = plateNumber.toCharArray();
			temporaryArr[i] = '1';

			assertThrows(PlateNumberFormatException.class,
					() -> new Vehicle(new String(temporaryArr), "Car", 10, 3, 5, 2, false, 4, p),
					"Expected to throw an PlateNumberFormatException, but it didn�t");
		}
		for (int i = 4; i < 8; i++) {
			plateNumber = "XX23 XXX";
			char[] temporaryArr = plateNumber.toCharArray();
			temporaryArr[i] = '1';

			assertThrows(PlateNumberFormatException.class,
					() -> new Vehicle(new String(temporaryArr), "Car", 10, 3, 5, 2, false, 4, p),
					"Expected to throw an PlateNumberFormatException, but it didn�t");
		}

		// Wrong format ==> String is bigger than 8 chars in length
		assertThrows(PlateNumberFormatException.class, () -> new Vehicle("XX23 XXXX", "Car", 10, 3, 5, 2, false, 4, p),
				"Expected to throw an PlateNumberFormatException, but it didn�t");

		// Wrong format ==> String is smaller than 8 chars in length
		assertThrows(PlateNumberFormatException.class, () -> new Vehicle("XX23 X", "Car", 10, 3, 5, 2, false, 4, p),
				"Expected to throw an PlateNumberFormatException, but it didn�t");

		// Wrong format ==> Empty string
		assertThrows(IllegalStateException.class, () -> new Vehicle("", "Car", 10, 3, 5, 2, false, 4, p),
				"Expected to throw an IllegalStateException, but it didn�t");

		// Wrong format ==> Null string
		assertThrows(IllegalArgumentException.class, () -> new Vehicle(null, "Car", 10, 3, 5, 2, false, 4, p),
				"Expected to throw an IllegalArgumentException, but it didn�t");
	}

	@Test
	void wrongVehicleType() {
		Phase p = new Phase(1, 15);
		// Wrong format ==> Null string
		assertThrows(IllegalArgumentException.class, () -> new Vehicle("XX23 XXX", null, 10, 3, 5, 2, false, 4, p),
				"Expected to throw an IllegalArgumentException, but it didn�t");

		// Wrong format ==> Empty string
		assertThrows(IllegalStateException.class, () -> new Vehicle("XX23 XXX", "", 10, 3, 5, 2, false, 4, p),
				"Expected to throw an IllegalStateException, but it didn�t");
	}

	@Test
	public void wrongCrossTime() {
		int crossTime = 0;
		Phase p = new Phase(1, 15);
		IllegalStateException thrown = assertThrows(IllegalStateException.class,
				() -> new Vehicle("XX23 XXX", "Car", crossTime, 3, 5, 2, false, 4, p),
				"Expected to throw an IllegalStateException, but it didn�t");

		assertTrue(thrown.getMessage().contentEquals("Cross time must be greater than 0s"));
	}

	@Test
	public void negativeDirection() {
		int direction = -56;
		Phase p = new Phase(1, 15);
		IllegalStateException thrown = assertThrows(IllegalStateException.class,
				() -> new Vehicle("XX23 XXX", "Car", 10, direction, 5, 2, false, 4, p),
				"Expected to throw an IllegalStateException, but it didn�t");

		assertTrue(thrown.getMessage().contentEquals("Directions can only be a positive int"));
	}

	@Test
	public void wrongLenght() {
		int length = -2;
		Phase p = new Phase(1, 15);
		IllegalStateException thrown = assertThrows(IllegalStateException.class,
				() -> new Vehicle("XX23 XXX", "Car", 10, 3, length, 2, false, 4, p),
				"Expected to throw an IllegalStateException, but it didn�t");

		assertTrue(thrown.getMessage().contentEquals("Length can only be a positive int"));
	}

	@Test
	public void negativeEmissions() {
		int emissions = -3;
		Phase p = new Phase(1, 15);
		IllegalStateException thrown = assertThrows(IllegalStateException.class,
				() -> new Vehicle("XX23 XXX", "Car", 10, 3, 5, emissions, false, 4, p),
				"Expected to throw an IllegalStateException, but it didn�t");

		assertTrue(thrown.getMessage().contentEquals("Emissions must be greater or equal than 0"));
	}
	
	@Test
	public void negativeSegment() {
		int segment = -3;
		Phase p = new Phase(1, 15);
		IllegalStateException thrown = assertThrows(IllegalStateException.class,
				() -> new Vehicle("XX23 XXX", "Car", 10, 3, 5, 80, false, segment, p),
				"Expected to throw an IllegalStateException, but it didn�t");

		assertTrue(thrown.getMessage().contentEquals("Segment must be greater or equal than 0"));
	}
	
	@Test
	public void C02EmissionsPerSecondCarWaiting() {
		try {
			Phase p = new Phase(1, 15);
			Vehicle vehicle = new Vehicle("XX23 XXX", "Car", 20, 3, 5, 20, false, 2, p);
			TimeUnit.SECONDS.sleep(2);
			int emissions = vehicle.getCO2Emitted();
			
			assertEquals(40, emissions); //2s * 20g/s = 40
		} catch (PlateNumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void C02EmissionsPerSecondCarCrossedFromInitialisation() {
		try {
			Phase p = new Phase(1, 15);
			Vehicle vehicle = new Vehicle("XX23 XXX", "Car", 20, 3, 5, 20, true, 2, p);
			TimeUnit.SECONDS.sleep(2);
			int emissions = vehicle.getCO2Emitted();
			
			assertEquals(0, emissions); //0s * 20g/s = 0
		} catch (PlateNumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void C02EmissionsPerSecondCarCrossed() {
		try {
			Phase p = new Phase(1, 15);
			Vehicle vehicle = new Vehicle("XX23 XXX", "Car", 20, 3, 5, 20, false, 2, p);
			TimeUnit.SECONDS.sleep(2);
			int emissions = vehicle.getCO2Emitted();
			TimeUnit.SECONDS.sleep(2);
			
			assertEquals(40, emissions); //2s * 20g/s = 40
		} catch (PlateNumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void C02EmissionsPerSecondChangeCrossingStatus() {
		try {
			Phase p = new Phase(1, 15);
			Vehicle vehicle = new Vehicle("XX23 XXX", "Car", 20, 3, 5, 20, false, 2, p);
			TimeUnit.SECONDS.sleep(2);
			vehicle.setCrossingStatus(false);
			TimeUnit.SECONDS.sleep(2);
			int emissions = vehicle.getCO2Emitted();
			
			assertEquals(80, emissions); //4s * 20g/s = 80
		} catch (PlateNumberFormatException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}