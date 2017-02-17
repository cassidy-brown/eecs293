package airville.employee;

import java.util.Optional;
import java.util.Set;

import airville.CheckInTime;
import airville.Settings;
import airville.assignment.CounterAssignment;
import airville.assignment.EmptyCounterAssignment;
import airville.counter.Counter;


/**
 * This class represents an Agent, a type of employee that
 * contributes to the time it takes to check in passengers
 * @author Cassidy
 *
 */
public class AgentWithCA extends Agent implements Employee, CheckInTime {
	
	private CounterAssignment counters;
	private boolean busy;
	
	public AgentWithCA(){
		counters = new EmptyCounterAssignment();
		busy = false;
	}
	
	@Override
	public void assignToCounter(Counter c) {
		if(c == null){
			return;
		}
		CounterAssignment prev = counters;
		counters = counters.offer(c);
		if(prev.equals(counters)){
			c.assignAgent(this);
		}		//TODO wut?	
	}

	@Override
	public void removeFromCounter(Counter c){
		c.removeAgent();
		counters = counters.remove(c);
	}

/*	@Override
	public Optional<Set<Counter>> getCounters() {
		return counters.getCounterSet();
	}
*/
	
	@Override
	public double getTimeMultiplier() {
		return Settings.AGENT_MULTIPLIER; 
	}

	@Override
	public boolean isBusy() {
		return busy;
	}
	
	@Override
	public void setBusy(boolean b){
		busy = b;
	}
	

	public class Test{
		
		/**
		 * Tests assignToCounter method based on the specified counter assignment and assigning the specified counter
		 * @param initial CounterAssignment to be used prior to assigning
		 * @param toAssign Counter to assign to employee
		 * @return CounterAssignment resulting from calling assignToCounter
		 */
		public CounterAssignment testAssignToCounter(CounterAssignment initial, Counter toAssign){
			CounterAssignment temp = counters;
			counters = initial;
			assignToCounter(toAssign);
			CounterAssignment result = counters;
			counters = temp;
			return result;
		}
		

		/**
		 * Tests removeFromCounter method based on the specified counter assignment and removing the specified counter
		 * @param initial CounterAssignment to be used prior to assigning
		 * @param toRemove Counter to remove from the employee's assignment
		 * @return CounterAssignment resulting from calling assignToCounter
		 */
		public CounterAssignment testRemoveFromCounter(CounterAssignment initial, Counter toRemove){
			CounterAssignment temp = counters;
			counters = initial;
			removeFromCounter(toRemove);
			CounterAssignment result = counters;
			counters = temp;
			return result;
		}
	}
}
