package airville.counter;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import airville.CheckInTime;
import airville.RealTime;
import airville.employee.Agent;
import airville.employee.Supervisor;
import airville.passenger.Group;

/**
 * This abstract class represents a generic check-in counter. 
 * It contains several default implementations of method.
 * 
 * @author Cassidy
 *
 */
public abstract class Counter implements CheckInTime {
	
	private Optional<Agent> agent;
	private Optional<Supervisor> supervisor;
	private Optional<Group> group;
	private boolean busy;


	/**
	 * Default initialization with no assigned agent to the counter
	 */
	public Counter(){
		this(null, null);
	}
	
	/**
	 * Initializes a new counter with the specified Agent assigned to it
	 * @param a Agent to assign to counter
	 */
	public Counter(Agent a){
		this(a, null);
	}
	
	/**
	 * Initializes a new counter with the specified Agent and Supervisor assigned to it
	 * @param a Agent to assign to this counter
	 * @param s Supervisor to assign to this counter
	 */
	public Counter(Agent a, Supervisor s){
		agent = Optional.empty();
		if(a != null){ 
			assignAgent(a);
		}
		
		supervisor = Optional.empty();
		if(s != null){
			assignSupervisor(s);
		}
		group = Optional.empty();
		busy = false;
	}
	
	/**
	 * Removes the agent from the counter
	 * @return true if successful
	 */
	public boolean removeAgent(){
		if(agent.isPresent()){
			Agent a = agent.get();
			agent = Optional.empty();
			a.removeFromCounter(this);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes the current agent at this counter, if any, and assigns the
	 * specified agent instead
	 * @param newAgent agent to be assigned
	 */
	public void assignAgent(Agent newAgent){
		if(newAgent != null){
			removeAgent();
			agent = Optional.of(newAgent);
		}
	}
	
	/**
	 * @return Optional of the agent currently assigned to the counter
	 */
	public Optional<Agent> getAgent(){
		return agent;
	}
	
	/**
	 * Removes the supervisor from the counter
	 * @return true if successful
	 */
	public boolean removeSupervisor(){
		if(supervisor.isPresent()){
			Supervisor s = supervisor.get();
			supervisor = Optional.empty();
			s.removeFromCounter(this);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes the current supervisor at this counter, if any, and assigns the
	 * specified supervisor instead
	 * @param newSuper supervisor to be assigned
	 */
	public void assignSupervisor(Supervisor newSuper){
		if(newSuper != null){
			removeSupervisor();
			supervisor = Optional.of(newSuper);
		}
	}
	
	/**
	 * @return Optional of the Supervisor currently assigned to the counter
	 */
	public Optional<Supervisor> getSupervisor(){
		return supervisor;
	}
	
	/**
	 * Removes the group from the counter
	 */
	public void removeGroup(){
		group = Optional.empty();
	}
	
	/**
	 * Offers a Group to be assigned to the counter. The new group will not be assigned
	 * if there is already a group at the counter or if the offered group cannot
	 * be helped at this counter.
	 * @param g group to assign to the counter
	 * @return true if successful
	 */
	public boolean offerGroup(Group g){
		if(group.isPresent() || !isAppropriateCounterForGroup(g)){
			return false;
		} else {
			group = Optional.of(g);
			return true;
		}
	}
	
	/**
	 * @return Optional of the group assigned to the counter
	 */
	public Optional<Group> getGroup(){
		return group;
	}
	
	/**
	 * Checks if this counter meets all help requirements based on the specified group's
	 * circumstances
	 * @param g Group to check 
	 * @return true if the specified group can be checked in at this counter based on its special 
	 * 		requirements
	 */
	public abstract boolean isAppropriateCounterForGroup(Group g);
	
	/**
	 * Determines the timeMultiplier for the counter based on the agent, supervisor, and group assigned to it
	 * @return double value representing how much check-in is affect at this counter
	 */
	@Override
	public double getTimeMultiplier(){
		double result = 1.0;
		if(agent.isPresent()){
			result *= agent.get().getTimeMultiplier();
		}
		if(supervisor.isPresent()){
			result *= supervisor.get().getTimeMultiplier();
		}
		if(group.isPresent()){
			result *= group.get().getTimeMultiplier();
		}
		return result;
	}
	
	/**
	 * Returns queue of times indicating when each passenger in the group was successfully 
	 * checked in; removes group from Counter. The amount of time each check-in takes is 
	 * based on the timeMultiplier for the passenger and employees involved
	 *	+ Assumes this counter is appropriate for the group. 
	 * @return queue of long values representing times
	 */
	public Queue<RealTime.Time> checkInGroup(){
		Queue<RealTime.Time> q = new LinkedList<>(); 
		if(group.isPresent()){
			setBusy(true);
			Group g = group.get();
			double counterMultiplier = getTimeMultiplier();
			RealTime.Time t = RealTime.waitForTimeOfCheckin(counterMultiplier * g.getTimeMultiplier());
			setBusy(false);
			for(int i = 0; i < g.getPassengerSet().size(); i++){
				q.add(t);
			}
			removeGroup();
		}
		return q;
	}
	

	@Override
	public boolean isBusy() {
		return busy;
	}
	
	@Override
	public void setBusy(boolean b){
		busy = b;
		
		if(agent.isPresent()){
			agent.get().setBusy(b);
		}
		if(supervisor.isPresent()){
			supervisor.get().setBusy(b);
		}
		if(group.isPresent()){
			group.get().setBusy(b);
		}
		
	}		
	
}
