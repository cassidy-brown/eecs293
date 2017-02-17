package airville.employee;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import airville.assignment.AutomatedAssignment;
import airville.assignment.EmptyCounterAssignment;
import airville.assignment.InPersonAssignment;
import airville.counter.AutomatedCounter;
import airville.counter.Counter;
import airville.counter.InPersonCounter;
import airville.employee.Agent;

public class Agent_Test {
	
	private Agent a = new Agent();
	private Agent.Test test = a.new Test();
	
	private Set<Counter> eca = Collections.emptySet();

	private InPersonCounter ipc = new InPersonCounter();
	private Set<Counter> ipa = Collections.singleton(ipc);

	private AutomatedCounter ac1 = new AutomatedCounter(new Agent());
	private AutomatedCounter ac2 = new AutomatedCounter(new Agent());
	private AutomatedCounter ac3 = new AutomatedCounter(new Agent());
	private AutomatedCounter ac4 = new AutomatedCounter(new Agent());
	private Set<Counter> aa;

	@Before
	public void setUp() throws Exception {
		aa = new HashSet<>();
		aa.add(ac1);
	}

	@Test
	public void testAssignToCounter() {
		//null to empty
		assertEquals("Assigning null counter from no counter does nothing", 
				eca, test.testAssignToCounter(eca, true, null));
		//null to i
		assertEquals("Assigning null counter from inperson counter returns initial counter",
				ipa, test.testAssignToCounter(ipa, true, null));
		//null to a
		assertEquals("Assigning null counter from automated counter returns initial counter",
				aa, test.testAssignToCounter(aa, false, null));
		
		//a to i
		assertEquals("Assigning automated counter when at in-person will swap assignment",
				 aa, test.testAssignToCounter(ipa, true, ac1));
		//i to a
		assertEquals("Assigning in-person when at automated counter will swap assignments",
				ipa, test.testAssignToCounter(aa, false, ipc));
		//swap i
		InPersonCounter ipc2 = new InPersonCounter();
		assertEquals("Assigning in-person when in-person already assigned will replace old with new",
				new InPersonAssignment(ipc2), test.testAssignToCounter(ipa, true, ipc2));
		//add a
		Set<AutomatedCounter> aaAltSet = new HashSet<>();
		aaAltSet.add(ac1);
		aaAltSet.add(ac2);
		assertEquals("Assigning an automated counter with one already assigned and staying below threshold will return automated assignment",
				aaAltSet, test.testAssignToCounter(aa, false, ac2));
		//reject a
		aaAltSet.add(ac3);
		aa.add(ac3);
		assertEquals("Assigning an automated counter when at the max number of assingment will do nothing",
				aaAltSet, test.testAssignToCounter(aa, false, ac4));
	}

	@Test
	public void testRemoveFromCounter() {
		/* To be fair, this is pretty much exactly the 
		 * same as testing CounterAssignments themselves */
		
		//remove a 
		//remove a to empty
		//remove null from a
		//remove i from a
		//remove i
		//remove null from i
		//remove a from i
	}
	
	@Test
	public void testGetCounters() {
		assertTrue("Supervisor is initially not assigned to any counters", a.getCounters().isEmpty());
		
		//check for other counter sets; related to CounterAssignment's tests
	}

	@Test
	public void testGetTimeMultiplier() {
		assertEquals("An agent's time multiplier is always 0.8", 0.8, a.getTimeMultiplier(), 0.0);
	}

	@Test
	public void testBusy() {
		Agent c = new Agent();
		assertFalse("By default, the counter is not busy", c.isBusy());
		
		c.setBusy(true);
		assertTrue("The counter has been set to be busy", c.isBusy());
		
		c.setBusy(false);
		assertFalse("And the counter is once again not busy", c.isBusy());
	}

}
