package intersection_sim;

import custom_exceptions.PlateNumberFormatException;

public class Bus extends Vehicle{
	
	public static final int STANDARD_CROSS_TIME = 15;
	public static final int STANDARD_LENGTH = 7;
	public static final int STANDARD_EMISSIONS = 5;

	public Bus(String numberPlate, int crossTime, int direction, int length, int emissions, Boolean crossed, int segment, Phase phase) throws PlateNumberFormatException {
		super(numberPlate, "Bus", crossTime, direction, length, emissions, crossed, segment, phase);
	}
	
	public Bus(String numberPlate, int direction, Boolean crossed, int segment, Phase phase) throws PlateNumberFormatException {
		super(numberPlate, "Bus", STANDARD_CROSS_TIME, direction, STANDARD_LENGTH, STANDARD_EMISSIONS, crossed, segment, phase);
	}
}