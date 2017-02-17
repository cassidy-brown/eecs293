package airville.employee;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import airville.assignment.AutomatedAssignment;
import airville.assignment.EmptyCounterAssignment;
import airville.assignment.InPersonAssignment;
import airville.counter.AutomatedCounter;
import airville.counter.InPersonCounter;
import airville.employee.Supervisor;

public class SupervisorWithCA_Test {
	
	private SupervisorWithCA s = new SupervisorWithCA();
	private SupervisorWithCA.Test test = s.new Test();
	
	private EmptyCounterAssignment eca = new EmptyCounterAssignment();

	private InPersonCounter ipc = new InPersonCounter();
	private InPersonAssignment ipa = new InPersonAssignment(ipc);

	private AutomatedCounter ac1 = new AutomatedCounter(null, new SupervisorWithCA());
	private AutomatedCounter ac2 = new AutomatedCounter(null, new SupervisorWithCA());
	private AutomatedCounter ac3 = new AutomatedCounter(null, new SupervisorWithCA());
	private AutomatedCounter ac4 = new AutomatedCounter(null, new SupervisorWithCA());
	private AutomatedAssignment aa;

	@Before
	public void setUp() throws Exception {
		aa = new AutomatedAssignment(ac1);
	}

	@Test
	public void testAssignToCounter() {
		//null to empty
		assertEquals("Assigning null counter from no counter does nothing", 
				eca, test.testAssignToCounter(eca, null));
		//null to i
		assertEquals("Assigning null counter from inperson counter returns initial counter",
				ipa, test.testAssignToCounter(ipa, null));
		//null to a
		assertEquals("Assigning null counter from automated counter returns initial counter",
				aa, test.testAssignToCounter(aa, null));
		
		//a to i
		assertEquals("Assigning automated counter when at in-person will swap assignment",
				aa, test.testAssignToCounter(ipa, ac1));
		//i to a
		assertEquals("Assigning in-person when at automated counter will swap assignments",
				ipa, test.testAssignToCounter(aa, ipc));
		//swap i
		InPersonCounter ipc2 = new InPersonCounter();
		assertEquals("Assigning in-person when in-person already assigned will replace old with new",
				new InPersonAssignment(ipc2), test.testAssignToCounter(ipa, ipc2));
		//add a
		Set<AutomatedCounter> aaAltSet = new HashSet<>();
		aaAltSet.add(ac1);
		aaAltSet.add(ac2);
		AutomatedAssignment.Test aaTest = aa.new Test();
		assertEquals("Assigning an automated counter with one already assigned and staying below threshold will return automated assignment",
				aaTest.newInstance(aaAltSet), test.testAssignToCounter(aa, ac2));
		//reject a
		aaAltSet.add(ac3);
		aa.offer(ac3);
		assertEquals("Assigning an automated counter when at the max number of assingment will do nothing",
				aaTest.newInstance(aaAltSet), test.testAssignToCounter(aa, ac4));
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
		assertTrue("Supervisor is initially not assigned to any counters", s.getCounters().isEmpty());
	}

	@Test
	public void testGetTimeMultiplier() {
		assertEquals("An agent's time multiplier is always 0.6", 0.6, s.getTimeMultiplier(), 0.0);
	}

	@Test
	public void testBusy() {
		Supervisor c = new Supervisor();
		assertFalse("By default, the counter is not busy", c.isBusy());
		
		c.setBusy(true);
		assertTrue("The counter has been set to be busy", c.isBusy());
		
		c.setBusy(false);
		assertFalse("And the counter is once again not busy", c.isBusy());
	}

}
