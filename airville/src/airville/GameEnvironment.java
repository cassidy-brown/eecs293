package airville;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import airville.Bank.Currency;
import airville.counter.AutomatedCounter;
import airville.counter.Counter;
import airville.counter.InPersonCounter;
import airville.employee.Agent;
import airville.employee.Supervisor;
import airville.passenger.Group;
import airville.passenger.Passenger.Circumstance;

/**
 * This singleton class stores the game state, maintaining the sets
 * of entities present in the game and providing some manipulators
 * @author Cassidy
 *
 */
public class GameEnvironment {
	
	//Singleton instance of this class
	private static final GameEnvironment INSTANCE = new GameEnvironment();	
	
	//Queues storing the groups of passengers waiting in line
	private Queue<Group> inPersonFrequentFlyerQ;
	private Queue<Group> inPersonRegularQ;
	private Queue<Group> automatedQ;
	
	//Queue to store the time at which a check-in is successful, used to track efficiency
	private Queue<RealTime.Time> timeQ;
	
	//Sets to store the entities present in the game
	private Set<Agent> agentSet;
	private Set <Supervisor> supervisorSet;
	private Set<InPersonCounter> inPersonCounterSet;
	private Set<AutomatedCounter> automatedCounterSet;
	
	//Bank instance associated with this environment
	private Bank bank;
	
	/**
	 * Initializes a game with:
	 * - 1 agent; 1 supervisor; 
	 * - 1 in-person counter; 1 automated counter; 
	 * - 10 groups in line
	 */
	private GameEnvironment(){
		agentSet = new HashSet<>();
		agentSet.add(new Agent());
		
		supervisorSet = new HashSet<>();
		supervisorSet.add(new Supervisor());
		
		inPersonCounterSet = new HashSet<>();
		inPersonCounterSet.add(new InPersonCounter());
		
		automatedCounterSet = new HashSet<>();
		automatedCounterSet.add(new AutomatedCounter());
		automatedCounterSet.add(new AutomatedCounter());
		
		timeQ = new LinkedList<>();
		
		inPersonFrequentFlyerQ = new LinkedList<>();
		inPersonRegularQ = new LinkedList<>();
		automatedQ = new LinkedList<>();
		for(int i = 0; i < 10; i++){
			addGroup();
		}
		
		bank = Bank.getInstance();
	}
	
	/**
	 * @return the singleton instance of GameEnvironment
	 */
	public GameEnvironment getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Adds {@link InPersonCounter} to set, redeeming it with the specified currency from the bank
	 * @param c Type of currency to trade for the counter
	 * @return <code>true</code> if new counter added
	 */
	public boolean addInPersonCounter(Bank.Currency c){
		Optional<InPersonCounter> counter = bank.redeemForInPersonCounter(c);
		if(counter.isPresent()){
			inPersonCounterSet.add(counter.get());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adds {@link AutomatedCounter} to set, redeeming it with the specified currency from the bank
	 * @param c Type of currency to trade for the counter
	 * @return <code>true</code> if new counter added
	 */
	public boolean addAutomatedCounter(Bank.Currency c){
		Optional<AutomatedCounter> counter = bank.redeemForAutomatedCounter(c);
		if(counter.isPresent()){
			automatedCounterSet.add(counter.get());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds new {@link Agent} to set, redeeming it with the specified currency
	 * @param c Type of currency to trade for the agent
	 * @return <code>true</code> if new agent added
	 */
	public boolean addAgent(Bank.Currency c){
		Optional<Agent> agent = bank.redeemForAgent(c);
		if(agent.isPresent()){
			agentSet.add(agent.get());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds new {@link Supervisor} to set, redeeming it with the specified currency
	 * @param c Type of currency to trade for the supervisor
	 * @return <code>true</code> if new supervisor added
	 */
	public boolean addSupervisor(Bank.Currency c){
		Optional<Supervisor> supervisor = bank.redeemForSupervisor(c);
		if(supervisor.isPresent()){
			supervisorSet.add(supervisor.get());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Generates a group of passengers and adds it to the appropriate queue. 
	 *		Utilized by system such that "Passengers autonomously queue at a check-in line" 
	 */
	public void addGroup(){
		Group g = Group.generate();
		if(Math.random() < .5){
			automatedQ.add(g);
		} else {
			addToInPersonQ(g);
		}
	}
	
	/**
	 * Takes a group and adds it to the proper in-person queue
	 * based on its frequent flyer status
	 * @param g group to add to queue
	 */
	private void addToInPersonQ(Group g){
		if(g.getCircumstanceSet().contains(Circumstance.FREQUENT_FLYER)){
			inPersonFrequentFlyerQ.add(g);
		} else {
			inPersonRegularQ.add(g);
		}
	}	
	
	/**
	 * Effectively a wrapper for the specified Counter's check-in. 
	 * Adds check-in times to timeQ (meanwhile handling 10-passengers-in-5-minutes
	 * bonus), adds points earned to player's bank
	 * @param c Counter at which to execute check-in
	 */
	public void checkInAtCounter(Counter c){
		double points = pointsEarned(c);	
		Queue<RealTime.Time> results = c.checkInGroup();
		results.stream().forEach(time -> updateTimeQ(time));
		bank.setBalance(Currency.POINTS, points);
	}
	
	/**
	 * Adds check-in times to timeQ (meanwhile handling 10-passengers-in-5-minutes
	 * bonus)
	 * @param time time to be added to queue
	 */
	private void updateTimeQ(RealTime.Time time){
		while(time.compareTo(timeQ.peek()) > Settings.MAX_TIME_STORED){
			timeQ.poll();
		}
		if(timeQ.size() > Settings.CHECK_IN_FOR_DIAMOND_THRESH){
			double diamondBalance = bank.getBalance(Currency.DIAMONDS);
			bank.setBalance(Currency.DIAMONDS, diamondBalance + 1);
			timeQ.clear();
		}
	}
		
	/**
	 * Determines the number of points earned by successfully performing a check-in
	 * at a given counter
	 * @param c Counter to check
	 * @return double value of points earned
	 */
	private double pointsEarned(Counter c){
		return Math.round(Settings.BASE_POINTS / c.getTimeMultiplier());
	}
}
