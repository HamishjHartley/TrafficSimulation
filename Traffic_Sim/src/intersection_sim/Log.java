package intersection_sim;

import java.io.File;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter; 
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Log {
	
	//Singleton instantiation 
	private static Log instance = new Log();
	private ArrayList<String> logList = new ArrayList<String>();

	
	private Log() {
		//private to prevent any more Log objects from being instantiated
	}
	
	
	/**
	 * public access to singleton instance
	 * @return
	 */
	public static Log getInstance() {
		return instance;
	}
	
	
	/**
	 * @param v
	 */
	public void logVehicleAdded(Vehicle v) {
		logList.add(v.getNumberPlate() + " added "+java.time.LocalTime.now());
	}
	
	/**
	 * @param v
	 */
	public void logVehicleRemoved(Vehicle v) {
		logList.add(v.getNumberPlate() + " removed "+java.time.LocalTime.now());
	}
	
	/**
	 * @param v
	 */
	public void logVehicleCrossed(Vehicle v) {
		logList.add(v.getNumberPlate() + " crossed "+java.time.LocalTime.now());
	}
	
	/**
	 * @param phaseNumber
	 */
	public void logPhaseChanged(int phaseNumber) {
		logList.add("Phase:" + phaseNumber + " "+java.time.LocalTime.now());
	}
	
	/**
	 * Generates log file which contains information about events during simulation runtime.
	 * @throws IOException
	 */
	public void generateLogFile() throws IOException {
		try {
			// Create statistics_files directory
			final File directory = new File("log_files");
			directory.mkdir();
			
			//Creates a new file with time stamped name, then writes logged data to .txt file
			FileWriter writer = new FileWriter("log_files/log" + String.valueOf(System.currentTimeMillis()) + ".txt");
			for(String str: logList) {
				  writer.write(str + System.lineSeparator());
			}
			System.out.println("Log File created succesfully");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}


