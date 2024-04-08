package custom_exceptions;

public class InvalidVehicleException extends Exception {
		public InvalidVehicleException() {
			super("Invalid vehicle type");
		}
}