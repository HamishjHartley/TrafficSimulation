package custom_exceptions;

public class PlateNumberFormatException extends Exception{

	public PlateNumberFormatException(String plateNumber) {
		super("The plate number format is not supported ("+plateNumber+"). \n"
				+ "Please, use a plate number in the format (XX23 XXX)");
	}
	
}