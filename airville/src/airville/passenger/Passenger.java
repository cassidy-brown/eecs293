package airville.passenger;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import airville.CheckInTime;
import airville.Settings;

/**
 * Each instance is a single passenger that needs to be checked in
 * All Passengers are created via a static factory method, which assigns
 * them an arbitrary set of Circumstances
 * @author Cassidy
 *
 */
public class Passenger implements CheckInTime {
	
	private Set<Circumstance> circumSet;
	private boolean busy;
	
	/**
	 * This private constructor (accessed via static generate()) creates a new passenger
	 * @param circumSet set of circumstances this passenger has
	 */
	private Passenger(Set<Circumstance> circumSet){
		this.circumSet = circumSet;
		busy = false;
	}
	
	public Set<Circumstance> getCircumstanceSet(){
		return circumSet;
	}

	@Override
	public double getTimeMultiplier() {
		double d = circumSet.stream()
				.mapToDouble(c -> c.getTimeMultiple())
				.reduce(1.0, (a, b) -> a * b);
		return d;
	}

	@Override
	public boolean isBusy() {
		return busy;
	}

	@Override
	public void setBusy(boolean b){
		busy = b;
	}
		
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("Passenger: { ");
		circumSet.stream().forEach(c -> b.append(c + " "));
		b.append("}");
		return b.toString();
	}
	
	/**
	 * Factory method for making Passengers
	 * @return new Passenger instance with a random set of circumstances according to their frequency
	 */
	public static Passenger generate(){
		return new Passenger(Circumstance.randomSet());
	}
	
	/**
	 * This enum represents different circumstances a passenger may be under which would
	 * affect their check-in requirements and time 
	 * @author Cassidy
	 */
	public enum Circumstance{		
		FREQUENT_FLYER(Settings.FF_TIME, Settings.FF_FREQ), 
		EXCESS_BAGGAGE(Settings.EB_TIME, Settings.EB_FREQ), 
		REROUTED(Settings.RR_TIME, Settings.RR_FREQ), 
		COLLECTING_BONUS(Settings.CB_TIME, Settings.CB_FREQ);
		
		private double timeMultiple; 
		private double frequency;
		private static EnumSet<Circumstance> valueSet = EnumSet.allOf(Circumstance.class);
		
		Circumstance(double timeMultiple, double frequency){
			this.timeMultiple = timeMultiple;
			this.frequency = frequency;
		}
		
		public double getTimeMultiple(){
			return timeMultiple;
		}
	
		private double getFrequency(){
			return frequency;
		}
		
		/**
		 * @return a set of enum values selected based on each value's frequency
		 */
		public static Set<Circumstance> randomSet(){
			return valueSet.stream()
					.filter(c -> Math.random() <= c.getFrequency())
					.collect(Collectors.toSet());
		}
	}
	
	/**
	 * Test class that allows access to private methods, namely the constructor
	 * @author Cassidy
	 */
	public static class Test{
		/**
		 * Allows you to create a Passenger with specified traits
		 * @param set set of circumstances this passenger has
		 * @return new passenger with given set stored
		 */
		public Passenger newPassenger(Set<Circumstance> set){
			Set<Circumstance> newSet = new HashSet<>();
			newSet.addAll(set);
			return new Passenger(newSet);
		}
	}

}
