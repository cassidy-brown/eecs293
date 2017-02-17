package airville;

/** This is a pseudoclass providing time information that would 
 * be given by the Real Time team. I think...
 * @author Cassidy
 *
 */
public class RealTime {

	/**
	 * This method takes a time multiplier aggregated from Counter, Employees, and Groups
	 * and determines how long to wait as a passenger is checked in based on the multiplier.
	 * It waits and returns the current Time when it completes
	 * @param timeMultiplier factor affecting amount of time it will take to check in a passenger
	 * @return the time check-in is completed
	 */
	public static Time waitForTimeOfCheckin(double timeMultiplier){
		return Time.current();
	}
	
	/**
	 * This class represents time as used by the real time team.
	 * It is used to track how long it takes for a passenger to be checked in
	 * and how long since a passenger was checked in
	 * @author Cassidy
	 *
	 */
	public static class Time{
		
		private int seconds;
		
		/** 
		 * @param sec Time in seconds
		 */
		private Time(int sec){
			seconds = sec;
		}
		
		/**
		 * @return The value of the time object, that is, the seconds
		 */
		public int getValue(){
			return seconds;
		}
		
		/** 
		 * @return a Time object representing the current time
		 */
		public static Time current(){
			return new Time(6000);		//arbitrary filler number
		}
		
		
		/**
		 * Converts the specified long (representing seconds)
		 * to a Time object
		 * @param sec seconds stored in Time object
		 * @return new Time object
		 */
		public static Time valueOf(int sec){
			return new Time(sec);
		}
		
		public int compareTo(Time t){
			return seconds - t.getValue();
		}
		
	}
}
