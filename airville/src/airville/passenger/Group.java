package airville.passenger;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import airville.CheckInTime;
import airville.Settings;
import airville.passenger.Passenger.Circumstance;

/**
 * Represents a group of {@link Passenger} (which may be of size 1)
 * Instances can only be created by way of a static factory method
 * which produces a random set of passengers to make up the group
 * @author Cassidy
 *
 */
public class Group implements CheckInTime {
	
	private Set<Passenger> passengerSet;
	private Set<Circumstance> circumSet;
	private boolean busy;
	
	private Group(Set<Passenger> set){
		passengerSet = set;
		circumSet = set.stream()
				.flatMap(p -> p.getCircumstanceSet().stream())
				.collect(Collectors.toSet());
		busy = false;
	}

	public Set<Passenger> getPassengerSet(){
		return passengerSet;
	}
	
	public Set<Circumstance> getCircumstanceSet(){
		return circumSet;
	}
	
	/**
	 * This method examines the group's circumstances set to determine if it
	 * needs assistance checking in
	 * @ return <code>true</code> if group cannot successfully use automated queue by itself
	 */
	public boolean needsAssistance(){
		return !circumSet.stream()
				.filter(c -> !c.equals(Circumstance.FREQUENT_FLYER))
				.collect(Collectors.toSet())
				.isEmpty();
	}	
	
	//Product of all passengers' time multipliers and 0.95^n where n is the number of passengers
	@Override
	public double getTimeMultiplier() {
		double base = passengerSet.stream()
				.mapToDouble(p -> p.getTimeMultiplier())
				.reduce(1.0, (a, b) -> a * b);
		double groupSizeMultiplier = Math.pow(Settings.GROUP_SIZE_MULT, passengerSet.size());
		
		return base * groupSizeMultiplier;
	}

	@Override
	public boolean isBusy() {
		return busy;
	}

	@Override
	public void setBusy(boolean b){
		busy = b;
		passengerSet.stream()
			.forEach(p -> p.setBusy(b));
	}
	
	/**
	 * Factory method which generates a group of a random set of passengers
	 * @return new Group
	 */
	public static Group generate(){
		int size = newGroupSize();
		Set<Passenger> passSet = new HashSet<>();
		while(size > 0){
			passSet.add(Passenger.generate());
			size--;
		}
		return new Group(passSet);
	}
	
	/**
	 * This helper method to generate() is used to set the size of a new group.
	 * It generates a number from 1 to 10, with 1 occurring 3 times more often than any other number
	 * @return int between 1 and 10
	 */
	private static int newGroupSize(){
		int r = (int)(Math.random() * 12);
		if(r < 10){
			return r + 1;
		} else {
			return 1;
		}
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Group)){
			return false;
		}
		
		Group other = (Group)o;
		return passengerSet.equals(other.getPassengerSet());
	}
	
	@Override
	public String toString(){
		return "Group id" + " (" + passengerSet.size() + " passengers)";
	}

	/**
	 * This inner class grants access to Group's private methods,
	 * namely its constructor for more controlled generation
	 * @author Cassidy
	 *
	 */
	public class Test{
		
		
		/**
		 * Creates a new group with the specified set of passengers
		 * @param set set of Passengers to make up group
		 * @return new Group containing specified set
		 */
		public Group newGroup(Set<Passenger> set){
			return new Group(set);
		}
		
		int testNewGroupSize(){
			return newGroupSize(); 
		}
		
		boolean testNeedsAssistance(Set<Circumstance> testCircumSet){
			Set<Circumstance> temp = circumSet;
			circumSet = testCircumSet;
			boolean result = needsAssistance();
			circumSet = temp;
			
			return result;
		}
		
		double testGetTimeMultiplier(Set<Passenger> testPassengerSet){
			Set<Passenger> temp = passengerSet;
			passengerSet = testPassengerSet;
			double result = getTimeMultiplier();
			passengerSet = temp;
			
			return result;
		}
	}
}
