package airville;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import airville.counter.AutomatedCounter;
import airville.counter.InPersonCounter;
import airville.employee.Agent;
import airville.employee.Supervisor;

/**
 * A singleton class which handles the in-game transactions, storing an account's point, 
 * diamond, and money balances and providing functionality to redeem them for in-game objects
 * @author Cassidy
 *
 */
class Bank {
	
	//Singleton instance of Bank
	private static final Bank INSTANCE = new Bank();
	
	//Map to store the amount of each currency associated with this bank/account
	private Map<Currency, Double> map;
	
	/**
	 * Creates default bank account with balances of zero for each currency
	 */
	private Bank(){
		map = new HashMap<>();
		map.put(Currency.MONEY, 0.0);
		map.put(Currency.POINTS, 0.0);
		map.put(Currency.DIAMONDS, 0.0);
	}

	/**
	 * @return the singleton instance of Bank
	 */
	public static Bank getInstance(){
		return INSTANCE;
	}

	/**
	 * @param c currency type to retrieve
	 * @return the amount of the specified currency stored in the bank
	 */
	public double getBalance(Currency c){
		return map.get(c);
	}
	
	/**
	 * Sets the balance of the specified currency in this bank
	 * @param c type of currency
	 * @param amount balance to set
	 */
	public void setBalance(Currency c, double amount){
		map.put(c, amount);
	}
	
	/**
	 * Redeem the specified currency for an agent, decrementing the bank's 
	 * currency values as necessary
	 * @param currency type of currency to be used in redemption
	 * @return new optional of Agent, or empty optional if the cost exceeds the current balance
	 */
	public Optional<Agent> redeemForAgent(Currency currency){
		double balance = getBalance(currency);
		double cost = currency.agentCost();
		if(balance >= cost){
			setBalance(currency, balance - cost);
			return Optional.of(new Agent());
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * Redeem the specified currency for a supervisor, decrementing the bank's 
	 * currency values as necessary
	 * @param currency type of currency to be used in redemption
	 * @return new optional of Supervisor, or empty optional if the cost exceeds the current balance
	 */
	public Optional<Supervisor> redeemForSupervisor(Currency currency){
		double balance = getBalance(currency);
		double cost = currency.supervisorCost();
		if(balance >= cost){
			setBalance(currency, balance - cost);
			return Optional.of(new Supervisor());
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * Redeem the specified currency for an in-person counter, decrementing the bank's 
	 * currency values as necessary
	 * @param currency type of currency to be used in redemption
	 * @return new optional of InPersonCounter, or empty optional if the cost exceeds the current balance
	 */
	public Optional<InPersonCounter> redeemForInPersonCounter(Currency currency){
		double balance = getBalance(currency);
		double cost = currency.inPersonCost();
		if(balance >= cost){
			setBalance(currency, balance - cost);
			return Optional.of(new InPersonCounter());
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * Redeem specified currency for an automated counter, decrementing the bank's 
	 * currency values as necessary
	 * @param currency type of currency to be used in redemption
	 * @return new optional of AutomatedCounter, or empty optional if the cost exceeds the current balance
	 */
	public Optional<AutomatedCounter> redeemForAutomatedCounter(Currency currency){
		double balance = getBalance(currency);
		double cost = currency.automatedCost();
		if(balance >= cost){
			setBalance(currency, balance - cost);
			return Optional.of(new AutomatedCounter());
		} else {
			return Optional.empty();
		}
	}
	
	@Override
	public String toString(){
		return "Bank(Money: " + map.get(Currency.MONEY)
				+ ", Points: " + map.get(Currency.POINTS)
				+ ", Diamonds: " + map.get(Currency.DIAMONDS)
				+ ")";
	}
	

	
	/**
	 * This enum represents the kinds of currency available to the player and stores the
	 * cost to purchase certain entities with the currency
	 * @author Cassidy
	 */
	public enum Currency{
		MONEY(Settings.M_AGENT, Settings.M_SUPER, Settings.M_IN_PERSON, Settings.M_AUTO), 
		POINTS(Settings.P_AGENT, Settings.P_SUPER, Settings.P_IN_PERSON, Settings.P_AUTO), 
		DIAMONDS(Settings.D_AGENT, Settings.D_SUPER, Settings.D_IN_PERSON, Settings.D_AUTO); 
	
		private double agent, supervisor, inPerson, automated;
		
		Currency(double agent, double supervisor, double inPerson, double automated){
			this.agent = agent;
			this.supervisor = supervisor;
			this.inPerson = inPerson;
			this.automated = automated;
		}
		
		/**
		 * @return the cost to buy an agent using this currency
		 */
		public double agentCost(){
			return agent;
		}

		/**
		 * @return the cost to buy a supervisor using this currency
		 */
		public double supervisorCost(){
			return supervisor;
		}

		/**
		 * @return the cost to buy an in-person counter using this currency
		 */
		public double inPersonCost(){
			return inPerson;
		}

		/**
		 * @return the cost to buy an automated counter using this currency
		 */
		public double automatedCost(){
			return automated;
		}
	
	};
	
}
