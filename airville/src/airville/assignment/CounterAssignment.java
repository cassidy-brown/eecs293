package airville.assignment;

import java.util.Optional;
import java.util.Set;

import airville.counter.Counter;

/**
 * Classes which implement this interface represent  the type and
 * specific set of counters which a certain employee is assigned to attend
 * with functionality to change the set of counters
 * @author Cassidy
 *
 */
public interface CounterAssignment {

	/**
	 * Offers the specified counter to the Assignment object.
	 * Returns the appropriate type of Assignment object with 
	 * correct assignments
	 * @param c counter to be assigned
	 * @return appropriate CounterAssignment object 
	 */
	public CounterAssignment offer(Counter c);
	
	/**
	 * Removes the specified counter from this object,
	 * if possible. Returns an appropriate CounterAssignment
	 * for the remaining assignments
	 * @param c Counter to remove from assignments
	 * @return CounterAssignment object representing remaining assignments
	 */
	public CounterAssignment remove(Counter c);
	
	/**
	 * @return Optional of the set of counters assigned
	 */
	public Optional<Set<Counter>> getCounterSet();
}
