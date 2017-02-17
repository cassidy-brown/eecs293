package airville.counter;

import airville.CheckInTime;
import airville.employee.Agent;
import airville.employee.Supervisor;
import airville.passenger.Group;
import airville.passenger.Passenger.Circumstance;

/**
 * This class represents an automated check-in counter
 * @author Cassidy
 *
 */
public class AutomatedCounter extends Counter implements CheckInTime {
		
	public AutomatedCounter(){
		super();		
	}
	
	public AutomatedCounter(Agent a){
		super(a);
	}
	
	public AutomatedCounter(Agent a, Supervisor s){
		super(a, s);
	}
		
	@Override
	public boolean isAppropriateCounterForGroup(Group g) {
		return 0 == g.getCircumstanceSet().stream()
				.filter(c -> !c.equals(Circumstance.FREQUENT_FLYER))
				.count();
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
