package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import intersection_sim.Bus;
import intersection_sim.Car;
import intersection_sim.Phase;
import intersection_sim.Truck;

class VehicleSubclassConstructorTest {

	@Test
	public void BusConstructorCorrectData() {
		try {
			Phase p = new Phase(1, 15);
			Bus b = new Bus("XX23 XXX", 10, 3, 5, 2, false, 4, p);
			
			assertEquals("Bus", b.getVehicleType()); 
			assertEquals(10, b.getCrossTime()); 
			assertEquals(5, b.getLength()); 
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void TruckConstructorCorrectData() {
		try {
			Phase p = new Phase(1, 15);
			Truck t = new Truck("XX23 XXX", 10, 3, 5, 2, false, 4, p);
			
			assertEquals("Truck", t.getVehicleType()); 
			assertEquals(3, t.getDirection()); 
			assertEquals(4, t.getSegment()); 
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void CarConstructorCorrectData() {
		try {
			Phase p = new Phase(1, 15);
			Car c = new Car("XX23 XXX", 10, 3, 5, 2, false, 4, p);
			
			assertEquals("Car", c.getVehicleType()); 
			assertEquals(false, c.isCrossed()); 
			assertEquals(2, c.getEmissions()); 
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}