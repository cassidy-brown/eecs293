package airville.assignment;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import airville.Settings;
import airville.counter.AutomatedCounter;
import airville.counter.Counter;
import airville.counter.InPersonCounter;

/**
 * This class contains the counters an employee specializing in 
 * automated counters is assigned to and functionality for changing
 * counter assignments
 * @author Cassidy
 *
 */
public final class AutomatedAssignment implements CounterAssignment {
	
	private Set<Counter> assignedCounters;
	private int maxSize;
	
	public AutomatedAssignment(AutomatedCounter c){
		assignedCounters = new HashSet<>();
		assignedCounters.add(c);
		maxSize = Settings.AUTO_ASSIGNMENT_THRESHOLD; 
	}
	
	/**
	 * This constructor is accessed via Test.newInstance() and is meant
	 * for testing purposes. 
	 * !! Does not check set size
	 * @param s set of AutomatedCounters to be assignedCounters in this Assignment
	 */
	private AutomatedAssignment(Set<AutomatedCounter> s){
		assignedCounters = s.stream().map(ac -> (Counter)ac).collect(Collectors.toSet());
		maxSize = Settings.AUTO_ASSIGNMENT_THRESHOLD; 
	}
	
	/**
	 * If c is an automatedCounter and the assignment set has
	 * already reached its threshold, the offered counter will
	 * not be added 
	 */
	@Override
	public CounterAssignment offer(Counter c){
		if(c == null){
			return this;
		} else if(c instanceof AutomatedCounter){
			updateCounterSet(c);
			return this;
		} else {	//c instanceof InPersonCounter
			return new InPersonAssignment((InPersonCounter)c);
		}
	}
	
	/**
	 * Helper method to offer which adds the specified counter to
	 * the counter set if it can fit
	 * @param c Counter to add
	 */
	private void updateCounterSet(Counter c){
		if(assignedCounters.size() < maxSize){
			assignedCounters.add(c);
		}
	}
	
	@Override
	public CounterAssignment remove(Counter c){
		if(noCounters(c)){
				return new EmptyCounterAssignment();
		} else {
			return this;
		}
	}
	
	/**
	 * Helper method to remove
	 * @param c counter to remove
	 * @ return <code>true</code> if the specified counter can be (and was) removed from assignedCounters
	 * 		and if that now makes the counter set empty
	 */
	private boolean noCounters(Counter c){
		return c != null && assignedCounters.remove(c) && assignedCounters.isEmpty();
	}
	
	@Override
	public Optional<Set<Counter>> getCounterSet(){
		return Optional.of(assignedCounters);
	}
	
	public boolean isInPerson(){
		return false;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof AutomatedAssignment)){
			return false;
		}
		
		AutomatedAssignment other = (AutomatedAssignment)o;
		return getCounterSet().equals(other.getCounterSet());
	}
	
	public class Test{
		
		/**
		 * Tests the private helper method
		 * @param initialSet Set of Counter representing initial state of assignedCounters before updateCounterSet called
		 * @param newCounter counter to add
		 * @return the Assignment's counter set after the update
		 */
		public Set<Counter> testUpdateCounterSet(Set<Counter> initialSet, Counter newCounter){
			Set<Counter> temp = assignedCounters;
			
			assignedCounters = initialSet;
			updateCounterSet(newCounter);
			Set<Counter> result = assignedCounters;
			assignedCounters = temp;
			return result;
		}
		
		/**
		 * Tests private helper method noCounters()
		 * @param c counter to pass to noCounters()
		 * @return result of noCounters()
		 */
		public boolean testNoCounters(Counter c){
			Set<Counter> temp = assignedCounters;
			
			boolean result = noCounters(c);
			assignedCounters = temp;
			
			return result;
		}
		
		/**
		 * To aid in testing, this method creates an AutomatedAssignment 
		 * with the specified counter set
		 * !! This method does not check set size, so threshold 
		 * 		constraints can be violated through improper use
		 * @param s Set of AutomatedCounter to be held in the new assignment
		 * @return new AutomatedAssignment
		 */
		public AutomatedAssignment newInstance(Set<AutomatedCounter> s){
			return new AutomatedAssignment(s);
		}
	}
	

}
