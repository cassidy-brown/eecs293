
package airville;

/**
 * Classes which implement this interface are involved in the check-in procedure. 
 * They are used in calculating how long it will take to check in a passenger. 
 * Additionally, these classes may become busy checking in a passenger, so methods
 * are provided to gauge it.
 * @author Cassidy
 *
 */
public interface CheckInTime {
	
	/** 
	 * The time multiplier is used in calculating how long a check-in will take and varies 
	 * by implementing class.
	 * @return a number that factors into the time it takes to check in a passenger.
	 */
	public double getTimeMultiplier();
	
	/**
	 * @return <code>true</code> if the object is currently involved in checking a passenger in
	 */
	public boolean isBusy();
	
	/**
	 * Sets whether or not the object is busy
	 * @param b boolean that is true if the entity is busy
	 */
	public void setBusy(boolean b);

}
