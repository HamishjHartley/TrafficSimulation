package intersection_sim;

import custom_exceptions.PlateNumberFormatException;

public class Truck extends Vehicle{

	public static final int STANDARD_CROSS_TIME = 20;
	public static final int STANDARD_LENGTH = 6;
	public static final int STANDARD_EMISSIONS = 6;

	public Truck(String numberPlate, int crossTime, int direction, int length, int emissions, Boolean crossed, int segment, Phase phase) throws PlateNumberFormatException {
		super(numberPlate, "Truck", crossTime, direction, length, emissions, crossed, segment, phase);
	}
	
	public Truck(String numberPlate, int direction, Boolean crossed, int segment, Phase phase) throws PlateNumberFormatException {
		super(numberPlate, "Truck", STANDARD_CROSS_TIME, direction, STANDARD_LENGTH, STANDARD_EMISSIONS, crossed, segment, phase);
	}
}
