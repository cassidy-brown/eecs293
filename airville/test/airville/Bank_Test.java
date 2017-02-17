package airville;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import airville.Bank.Currency;

public class Bank_Test {

	private Bank bank = Bank.getInstance();
	
	@Before
	public void setUp() throws Exception {
		empty();
	}
	
	private void fill(){
		bank.setBalance(Currency.MONEY, 10.00);
		bank.setBalance(Currency.POINTS, 10000);
		bank.setBalance(Currency.DIAMONDS, 10);
	}
	
	private void empty(){
		bank.setBalance(Currency.MONEY, 0);
		bank.setBalance(Currency.POINTS, 0);
		bank.setBalance(Currency.DIAMONDS, 0);
	}

	@Test
	public void testGetSetBalance() {
		assertEquals("Empty bank will have 0 money", 0.0, bank.getBalance(Currency.MONEY), 0.0);
		assertEquals("Empty bank will have 0 points", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		assertEquals("Empty bank will have 0 diamonds", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);

		bank.setBalance(Currency.MONEY, 5.00);
		assertEquals("Empty bank will now have 5 money (dollars)", 5.0, bank.getBalance(Currency.MONEY), 0.0);
		bank.setBalance(Currency.POINTS, 5.00);
		assertEquals("Empty bank will now have 5 points", 5.0, bank.getBalance(Currency.POINTS), 0.0);
		bank.setBalance(Currency.DIAMONDS, 5.00);
		assertEquals("Empty bank will now have 5 diamonds", 5.0, bank.getBalance(Currency.DIAMONDS), 0.0);
	}

	@Test
	public void testRedeemForAgent() {
		assertEquals("Not having enough money, the bank will return an empty optional rather than an Agent", 
				Optional.empty(), bank.redeemForAgent(Currency.MONEY));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.MONEY), 0.0);
		
		assertEquals("Not having enough points, the bank will return an empty optional rather than an Agent", 
				Optional.empty(), bank.redeemForAgent(Currency.POINTS));
		assertEquals("Bank's points balance didn't change", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		assertEquals("Not having enough diamonds, the bank will return an empty optional rather than an Agent", 
				Optional.empty(), bank.redeemForAgent(Currency.DIAMONDS));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
		
		fill();
		assertTrue("Having enough money, the bank will return a new Agent (not empty)", 
				bank.redeemForAgent(Currency.MONEY).isPresent());
		assertEquals("banks's money balance has been decremented to 8.01", 8.01, bank.getBalance(Currency.MONEY), 0.001);

		assertTrue("Having enough points, the bank will return a new Agent (not empty)", 
				bank.redeemForAgent(Currency.POINTS).isPresent());
		assertEquals("bank's points balance has been decremented to 8000", 8000, bank.getBalance(Currency.POINTS), 0.0);

		assertTrue("Having enough diamonds, the bank will return a new Agent (not empty)", 
				bank.redeemForAgent(Currency.DIAMONDS).isPresent());
		assertEquals("bank's diamond balance has been decremented to 8", 8.0, bank.getBalance(Currency.DIAMONDS), 0);
		
		bank.setBalance(Currency.MONEY, 1.99);
		assertTrue("Having exactly enough money, the bank will return a new Agent (not empty)", 
				bank.redeemForAgent(Currency.MONEY).isPresent());
		assertEquals("Bank's money balance is now 0.00", 0.0, bank.getBalance(Currency.MONEY), 0.001);
		
		bank.setBalance(Currency.POINTS, 2000.0);
		assertTrue("Having exactly enough points, the bank will return a new Agent (not empty)", 
				bank.redeemForAgent(Currency.POINTS).isPresent());
		assertEquals("Bank's points balance is now", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		bank.setBalance(Currency.DIAMONDS, 2.0);
		assertTrue("Having exactly enough diamonds, the bank will return a new Agent (not empty)", 
				bank.redeemForAgent(Currency.DIAMONDS).isPresent());
		assertEquals("Bank's diamond balance is now 0", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
	}

	@Test
	public void testRedeemForSupervisor() {
		assertEquals("Not having enough money, the bank will return an empty optional rather than a Supervisor", 
				Optional.empty(), bank.redeemForSupervisor(Currency.MONEY));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.MONEY), 0.0);
		
		assertEquals("Not having enough points, the bank will return an empty optional rather than a Supervisor", 
				Optional.empty(), bank.redeemForSupervisor(Currency.POINTS));
		assertEquals("Bank's points balance didn't change", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		assertEquals("Not having enough diamonds, the bank will return an empty optional rather than a Supervisor", 
				Optional.empty(), bank.redeemForSupervisor(Currency.DIAMONDS));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
		
		fill();
		assertTrue("Having enough money, the bank will return a new Supervisor (not empty)", 
				bank.redeemForSupervisor(Currency.MONEY).isPresent());
		assertEquals("bank's money balance has been decremented to 5.01", 5.01, bank.getBalance(Currency.MONEY), 0.001);

		assertTrue("Having enough points, the bank will return a new Supervisor (not empty)", 
				bank.redeemForSupervisor(Currency.POINTS).isPresent());
		assertEquals("bank's points balance has been decremented to 5000", 5000, bank.getBalance(Currency.POINTS), 0.0);

		assertTrue("Having enough diamonds, the bank will return a new Supervisor (not empty)", 
				bank.redeemForSupervisor(Currency.DIAMONDS).isPresent());
		assertEquals("Bank's diamond balance has been decremented to 5", 5.0, bank.getBalance(Currency.DIAMONDS), 0);
		
		bank.setBalance(Currency.MONEY, 4.99);
		assertTrue("Having exactly enough money, the bank will return a new Supervisor (not empty)", 
				bank.redeemForSupervisor(Currency.MONEY).isPresent());
		assertEquals("Bank's money balance is now 0.00", 0.0, bank.getBalance(Currency.MONEY), 0.001);
		
		bank.setBalance(Currency.POINTS, 5000.0);
		assertTrue("Having exactly enough points, the bank will return a new Supervisor (not empty)", 
				bank.redeemForSupervisor(Currency.POINTS).isPresent());
		assertEquals("Bank's points balance is now", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		bank.setBalance(Currency.DIAMONDS, 5.0);
		assertTrue("Having exactly enough diamonds, the bank will return a new Supervisor (not empty)", 
				bank.redeemForSupervisor(Currency.DIAMONDS).isPresent());
		assertEquals("Bank's diamond balance is now 0", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
	}

	@Test
	public void testRedeemForInPersonCounterCounter() {
		assertEquals("Not having enough money, the bank will return an empty optional rather than an InPersonCounter", 
				Optional.empty(), bank.redeemForInPersonCounter(Currency.MONEY));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.MONEY), 0.0);
		
		assertEquals("Not having enough points, the bank will return an empty optional rather than an InPersonCounter", 
				Optional.empty(), bank.redeemForInPersonCounter(Currency.POINTS));
		assertEquals("Bank's points balance didn't change", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		assertEquals("Not having enough diamonds, the bank will return an empty optional rather than an InPersonCounter", 
				Optional.empty(), bank.redeemForInPersonCounter(Currency.DIAMONDS));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
		

		fill();
		assertTrue("Having enough money, the bank will return a new InPersonCounter (not empty)", 
				bank.redeemForInPersonCounter(Currency.MONEY).isPresent());
		assertEquals("Bank's money balance has been decremented to 6.01", 6.01, bank.getBalance(Currency.MONEY), 0.001);

		assertTrue("Having enough points, the bank will return a new InPersonCounter (not empty)", 
				bank.redeemForInPersonCounter(Currency.POINTS).isPresent());
		assertEquals("Bank's points balance has been decremented to 6000", 6000, bank.getBalance(Currency.POINTS), 0.0);

		assertTrue("Having enough diamonds, the bank will return a new InPersonCounter (not empty)", 
				bank.redeemForInPersonCounter(Currency.DIAMONDS).isPresent());
		assertEquals("Bank's diamond balance has been decremented to 6", 6.0, bank.getBalance(Currency.DIAMONDS), 0);
		
		bank.setBalance(Currency.MONEY, 3.99);
		assertTrue("Having exactly enough money, the bank will return a new InPersonCounter (not empty)", 
				bank.redeemForInPersonCounter(Currency.MONEY).isPresent());
		assertEquals("Bank's money balance is now 0.00", 0.0, bank.getBalance(Currency.MONEY), 0.001);
		
		bank.setBalance(Currency.POINTS, 4000.0);
		assertTrue("Having exactly enough points, the bank will return a new InPersonCounter (not empty)", 
				bank.redeemForInPersonCounter(Currency.POINTS).isPresent());
		assertEquals("Bank's points balance is now", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		bank.setBalance(Currency.DIAMONDS, 4.0);
		assertTrue("Having exactly enough diamonds, the bank will return a new InPersonCounter (not empty)", 
				bank.redeemForInPersonCounter(Currency.DIAMONDS).isPresent());
		assertEquals("Bank's diamond balance is now 0", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
	}

	@Test
	public void testRedeemForAutomatedCounter() {
		assertEquals("Not having enough money, the bank will return an empty optional rather than an AutomatedCounter", 
				Optional.empty(), bank.redeemForAutomatedCounter(Currency.MONEY));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.MONEY), 0.0);
		
		assertEquals("Not having enough points, the bank will return an empty optional rather than an AutomatedCounter", 
				Optional.empty(), bank.redeemForAutomatedCounter(Currency.POINTS));
		assertEquals("Bank's points balance didn't change", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		assertEquals("Not having enough diamonds, the bank will return an empty optional rather than an AutomatedCounter", 
				Optional.empty(), bank.redeemForAutomatedCounter(Currency.DIAMONDS));
		assertEquals("Bank's money balance didn't change", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
		

		fill();
		assertTrue("Having enough money, the bank will return a new AutomatedCounter (not empty)", 
				bank.redeemForAutomatedCounter(Currency.MONEY).isPresent());
		assertEquals("Bank's money balance has been decremented to 8.01", 8.01, bank.getBalance(Currency.MONEY), 0.001);

		assertTrue("Having enough points, the bank will return a new AutomatedCounter (not empty)", 
				bank.redeemForAutomatedCounter(Currency.POINTS).isPresent());
		assertEquals("Bank's points balance has been decremented to 8000", 8000, bank.getBalance(Currency.POINTS), 0.0);

		assertTrue("Having enough diamonds, the bank will return a new AutomatedCounter (not empty)", 
				bank.redeemForAutomatedCounter(Currency.DIAMONDS).isPresent());
		assertEquals("Bank's diamond balance has been decremented to 8", 8.0, bank.getBalance(Currency.DIAMONDS), 0);
		
		bank.setBalance(Currency.MONEY, 1.99);
		assertTrue("Having exactly enough money, the bank will return a new AutomatedCounter (not empty)", 
				bank.redeemForAutomatedCounter(Currency.MONEY).isPresent());
		assertEquals("Bank's money balance is now 0.00", 0.0, bank.getBalance(Currency.MONEY), 0.001);
		
		bank.setBalance(Currency.POINTS, 2000.0);
		assertTrue("Having exactly enough points, the bank will return a new AutomatedCounter (not empty)", 
				bank.redeemForAutomatedCounter(Currency.POINTS).isPresent());
		assertEquals("Bank's points balance is now", 0.0, bank.getBalance(Currency.POINTS), 0.0);
		
		bank.setBalance(Currency.DIAMONDS, 2.0);
		assertTrue("Having exactly enough diamonds, the bank will return a new AutomatedCounter (not empty)", 
				bank.redeemForAutomatedCounter(Currency.DIAMONDS).isPresent());
		assertEquals("Bank's diamond balance is now 0", 0.0, bank.getBalance(Currency.DIAMONDS), 0.0);
	}

}
