package airville.assignment;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;

import airville.assignment.AutomatedAssignment;
import airville.assignment.EmptyCounterAssignment;
import airville.assignment.InPersonAssignment;
import airville.counter.AutomatedCounter;
import airville.counter.InPersonCounter;
import airville.employee.Agent;

public class InPersonAssignment_Test {
	
	private InPersonCounter ipc = new InPersonCounter();
	private InPersonAssignment ipa = new InPersonAssignment(ipc);
	private InPersonCounter ipc_a = new InPersonCounter(new Agent());

	private EmptyCounterAssignment eca = new EmptyCounterAssignment();
	private AutomatedCounter ac = new AutomatedCounter();
	
	@Test
	public void testOffer() {
		assertEquals("Offering null will return the current assignment", ipa, ipa.offer(null));
		
		assertEquals("Offering an in-person counter will return an InPersonAssignment with that counter stored", 
				new InPersonAssignment(ipc_a), ipa.offer(ipc_a));
		
		assertEquals("Offering an automated counter will return an AutomatedAssignment with that counter stored",
				new AutomatedAssignment(ac), ipa.offer(ac));
	}

	@Test
	public void testRemove() {
		assertEquals("Removing null returns this assignment again", ipa, ipa.remove(null));
		assertEquals("Removing the in-person counter stored in this assignment will return an EmptyCounterSet",
				eca, ipa.remove(ipc));
		assertEquals("Removing a different in-person counter than the one assigned will return this same assignment",
				ipa, ipa.remove(ipc_a));
		assertEquals("Removing an automated counter will return the current assignment", ipa, ipa.remove(ac));
	}

	@Test
	public void testGetCounterSet() {
		assertEquals("An InPersonAssignment's counter set is a singleton, wrapped in an optional", 
				Optional.of(Collections.singleton(ipc)), ipa.getCounterSet());
	}

	@Test
	public void testIsInPerson() {
		assertTrue("InPersonAsignments are in person", ipa.isInPerson());
	}

}
