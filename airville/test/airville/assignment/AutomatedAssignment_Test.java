package airville.assignment;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
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

public class AutomatedAssignment_Test {

	private EmptyCounterAssignment eca = new EmptyCounterAssignment();

	private InPersonCounter ipc = new InPersonCounter();
	private InPersonAssignment ipa = new InPersonAssignment(ipc);

	private AutomatedCounter ac1 = new AutomatedCounter(new Agent());
	private AutomatedCounter ac2 = new AutomatedCounter(new Agent());
	private AutomatedCounter ac3 = new AutomatedCounter(new Agent());
	private AutomatedCounter ac4 = new AutomatedCounter(new Agent());
	private AutomatedAssignment aa;	
	
	private AutomatedAssignment.Test test;

	@Before
	public void setUp() throws Exception{
		aa = new AutomatedAssignment(ac1);
		test = aa.new Test();
	}
	
	@Test
	public void testOffer() {
		assertEquals("Offering null will return this assignment", aa, aa.offer(null));
		
		Set<AutomatedCounter> ac_alt = new HashSet<>();
		ac_alt.add(ac1);
		ac_alt.add(ac2);
		assertEquals("Offering an automated counter when the set is not full returns the assignment with that counter added",
				test.newInstance(ac_alt), aa.offer(ac2));
		
		ac_alt.add(ac3);
		aa.offer(ac3);
		assertEquals("Offering an automated counter when the threshold is already exceeded returns the original assignment",
				test.newInstance(ac_alt), aa.offer(ac4));
		assertEquals("If the set somehow exceeds the set size threshold and another is offered, it return the original (still exceeding) assignment",
				test.newInstance(ac_alt), test.newInstance(ac_alt).offer(new AutomatedCounter()));
		assertEquals("Offering an in-person counter will return an InPersonAssignment", ipa, aa.offer(ipc));
	}

	@Test
	public void testRemove() {
		assertEquals("Removing null will return the same assignment", aa, aa.remove(null));
		assertEquals("Removing the only counter will return an empty assignment", eca, aa.remove(ac1));
		
		aa.offer(ac1);
		aa.offer(ac2);
		assertEquals("Removing a counter from a multiset will return an automated assignment with that counter removed from its set",
				test.newInstance(Collections.singleton(ac1)), aa.remove(ac2));
		assertEquals("Removing a counter not in the counter set will return the same assignment", aa, aa.remove(ac3));
		assertEquals("Removing a counter not in the counter set (because it's an in-person counter) will return the same assignment",
				aa, aa.remove(ipc));
	}

	@Test
	public void testGetCounterSet() {
		Set<Counter> set = new HashSet<>();
		set.add(ac1);
		
		assertEquals("The counter set contains whatever assignments have been set",
				Optional.of(set), aa.getCounterSet());
		
		aa.offer(ac2);
		set.add(ac2);
		assertEquals("It works for any number of assignments withing the allowed range",
				Optional.of(set), aa.getCounterSet());
		
		aa.offer(ac3);
		set.add(ac3);
		assertEquals("getCounterSet works at the threshold", 
				Optional.of(set), aa.getCounterSet());
	}

	@Test
	public void testIsInPerson() {
		assertFalse("An automated counter assignment is not in-person", aa.isInPerson());
	}

}
