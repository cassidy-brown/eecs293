package airville.counter;

import java.util.Set;

import airville.CheckInTime;
import airville.employee.Agent;
import airville.employee.Supervisor;
import airville.passenger.Group;
import airville.passenger.Passenger.Circumstance;

/**
 * This class represents an in-person check-in counter
 * @author Cassidy
 *
 */
public class InPersonCounter extends Counter implements CheckInTime {

	public InPersonCounter(){
		super();
	}

	public InPersonCounter(Agent a){
		super(a);
	}
	
	public InPersonCounter(Agent a, Supervisor s){
		super(a, s);
	}

	@Override
	public boolean isAppropriateCounterForGroup(Group g) {
		if(isUnattended()){
			return false;
		}
		Set<Circumstance> circumSet = g.getCircumstanceSet();
		
		/* "Passengers who have been repeatedly re-routed due to flight delays and cancellations 
		 *  [are a slow class of passenger]. Most of these passengers can only be helped by the supervisor"
		 *  Rerouting is the only circumstance where needing a supervisor is specified
		 */
		boolean supervisorNeedsMet = !circumSet.contains(Circumstance.REROUTED) || getSupervisor().isPresent();
		
		return supervisorNeedsMet;
	}
	
	/**
	 * Helper method to isAppropriateCounterForGroup which ultimately prevents
	 * groups from being assigned to this counter if there is no agent
	 * or supervisor at it.
	 * @ return <code>true</code> if there is no agent or supervisor assigned to this counter
	 */
	private boolean isUnattended(){
		return !getAgent().isPresent() && !getSupervisor().isPresent();
	}
	
	@Override
	public double getTimeMultiplier() {
		return super.getTimeMultiplier();
	}

	@Override
	public boolean isBusy() {
		return super.isBusy();
	}
	
	@Override
	public void setBusy(boolean b){
		super.setBusy(b);
	}


}
