package airville.assignment;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import airville.counter.AutomatedCounter;
import airville.counter.Counter;
import airville.counter.InPersonCounter;

/**
 * This class contains the counter an employee specializing in 
 * in-person counters is assigned to and functionality for changing
 * counter assignments
 * @author Cassidy
 *
 */
public final class InPersonAssignment implements CounterAssignment {

	InPersonCounter assignedCounter;
	
	public InPersonAssignment(InPersonCounter c){
		assignedCounter = c;
	}
	
	/**
	 * If c is an InPersonCounter, it will replace the currently
	 * assigned counter 
	 */
	@Override
	public CounterAssignment offer(Counter c){
		if(c == null){ 
			return this;
		} else if(c instanceof InPersonCounter){
			assignedCounter = (InPersonCounter)c;
			return this;
		} else {	//c instanceof automatedCounter
			return new AutomatedAssignment((AutomatedCounter)c);
		}
	}
	
	@Override 
	public CounterAssignment remove(Counter c){
		if(c != null && c.equals(assignedCounter)){
			return new EmptyCounterAssignment();
		} else {
			return this;
		}
	}
	
	@Override
	public Optional<Set<Counter>> getCounterSet(){
		return Optional.of(Collections.singleton(assignedCounter));
	}
	
	public boolean isInPerson(){
		return true;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof InPersonAssignment)){
			return false;
		}
		
		InPersonAssignment other = (InPersonAssignment)o;
		return getCounterSet().equals(other.getCounterSet());
	}
}
