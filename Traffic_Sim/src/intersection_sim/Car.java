package intersection_sim;

import custom_exceptions.PlateNumberFormatException;

public class Car extends Vehicle{

	public static final int STANDARD_CROSS_TIME = 10;
	public static final int STANDARD_LENGTH = 5;
	public static final int STANDARD_EMISSIONS = 2;

	public Car(String numberPlate, int crossTime, int direction, int length, int emissions, Boolean crossed, int segment, Phase phase) throws PlateNumberFormatException {
		super(numberPlate, "Car", crossTime, direction, length, emissions, crossed, segment, phase);
	}
	
	public Car(String numberPlate, int direction, Boolean crossed, int segment, Phase phase) throws PlateNumberFormatException {
		super(numberPlate, "Car", STANDARD_CROSS_TIME, direction, STANDARD_LENGTH, STANDARD_EMISSIONS, crossed, segment, phase);
	}
}