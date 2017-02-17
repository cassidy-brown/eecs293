package airville.assignment;

import java.util.Optional;
import java.util.Set;

import airville.counter.AutomatedCounter;
import airville.counter.Counter;
import airville.counter.InPersonCounter;

/**
 * This class is effectively a placeholder for when an employee
 * is not assigned to any counters
 * @author Cassidy
 *
 */
public final class EmptyCounterAssignment implements CounterAssignment{
	
	@Override
	public CounterAssignment offer(Counter c){
		if(c == null){
			return this;
		} else if(c instanceof InPersonCounter){
			return new InPersonAssignment((InPersonCounter)c);
		} else {	//c instanceof AutomatedCounter
			return new AutomatedAssignment((AutomatedCounter)c);
		}
	}
	
	@Override
	public CounterAssignment remove(Counter c) {
		return this;
	}
	
	/**
	 * Returns an Optional value of the set of Counters assigned 
	 * via this Assignment object
	 * @return Optional set of counters
	 */
	@Override
	public Optional<Set<Counter>> getCounterSet(){
		return Optional.empty();
	}

	public boolean isInPerson(){
		return false;
	}
	
	@Override
	public boolean equals(Object o){
		return o instanceof EmptyCounterAssignment;
		//TODO should this class be a singleton?
	}
}
