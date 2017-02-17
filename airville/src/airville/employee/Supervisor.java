package airville.employee;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import airville.CheckInTime;
import airville.Settings;
import airville.assignment.CounterAssignment;
import airville.assignment.EmptyCounterAssignment;
import airville.counter.AutomatedCounter;
import airville.counter.Counter;
import airville.counter.InPersonCounter;

/**
 * This class represents a Supervisor, a type of employee that
 * contributes to the time it takes to check in passengers
 * @author Cassidy
 *
 */
public class Supervisor implements Employee, CheckInTime {

	private Set<Counter> counterSet;
	private boolean busy;
	private boolean atInPerson; //true if currently assigned to in-person counter
	private int maxCounters; //the maximum number of automated counters to which an agent can be assigned

	public Supervisor(){
		counterSet = Collections.emptySet();
		busy = false;
		atInPerson = false;
		maxCounters = Settings.AUTO_ASSIGNMENT_THRESHOLD;
	}

	@Override
	public void assignToCounter(Counter c) {
		if(c == null){
			return;
		} else if(c instanceof InPersonCounter){
			assignInPerson((InPersonCounter)c);
		} else {	//c instanceof AutomatedCounter
			assignAutomated((AutomatedCounter) c);
		}
	}

	/**
	 * Helper method to assignToCounter which assigns the supervisor to the
	 * specified in-person counter and updates the counter set as necessary
	 * @param c InPersonCounter to add to this agent's set of counters
	 */
	private void assignInPerson(InPersonCounter c){
		removeAllCounters();
		atInPerson = true;
		counterSet = Collections.singleton(c);
	}

	/**
	 * Helper method to assignToCounter which assigns this supervisor to the
	 * specified automated counter, if able, and updates the counter set 
	 * as necessary
	 * @param c AutomatedCounter to add to this supervisor's set of counters
	 */
	private void assignAutomated(AutomatedCounter c){
		if(atInPerson){
			removeAllCounters();
			atInPerson = false;
			counterSet = new HashSet<>();
			counterSet.add(c);
		} else {
			if(counterSet.size() < maxCounters){
				counterSet.add(c);
			}
		}
	}

	/**
	 * Helper method to assignToCounter which removes all counters from the counter set
	 */
	private void removeAllCounters(){
		for(Counter c : counterSet){
			removeFromCounter(c);
		}
	}

	@Override
	public void removeFromCounter(Counter c){
		c.removeSupervisor();
		counterSet.remove(c);
	}

	@Override
	public Set<Counter> getCounters() {
		return counterSet;
	}

	@Override
	public double getTimeMultiplier() {
		return Settings.SUPERVISOR_MULTIPLIER;
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
		 * @param inPerson true if the test supervisor is assigned to an in-person counter
		 * @param toAssign Counter to assign to employee
		 * @return CounterAssignment resulting from calling assignToCounter
		 */
		public Set<Counter> testAssignToCounter(Set<Counter> initial, boolean inPerson, Counter toAssign){
			Set<Counter> temp = counterSet;
			boolean tempBool = atInPerson;
			counterSet = initial;
			atInPerson = inPerson;
			assignToCounter(toAssign);
			Set<Counter> result = counterSet;
			counterSet = temp;
			atInPerson = tempBool;
			return result;
		}


		/**
		 * Tests removeFromCounter method based on the specified counter assignment and removing the specified counter
		 * @param initial CounterAssignment to be used prior to assigning
		 * @param toRemove Counter to remove from employee's assignment
		 * @return CounterAssignment resulting from calling assignToCounter
		 */
		public Set<Counter> testRemoveFromCounter(Set<Counter> initial, Counter toRemove){
			Set<Counter> temp = counterSet;
			counterSet = initial;
			removeFromCounter(toRemove);
			Set<Counter> result = counterSet;
			counterSet = temp;
			return result;
		}
	}

}
