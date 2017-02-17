package airville.employee;

import java.util.Optional;
import java.util.Set;

import airville.counter.Counter;


/**
 * This abstract class represents a generic employee in the check-in area
 * @author Cassidy
 *
 */
public interface Employee {
	
	/**
	 * Updates this employee's counter assignment
	 * @param c new Counter to which to be assigned
	 */
	public void assignToCounter(Counter c);
	
	/**
	 * Removes this counter from the counter's assignment,
	 * if it is currently assigned
	 * @param c Counter to be removed
	 */
	public void removeFromCounter(Counter c);
	
	/**
	 * @return the set of counters this employee is assigned to
	 */
	public Set<Counter> getCounters();
}
