package airville.assignment;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import airville.assignment.AutomatedAssignment;
import airville.assignment.EmptyCounterAssignment;
import airville.assignment.InPersonAssignment;
import airville.counter.AutomatedCounter;
import airville.counter.InPersonCounter;

public class EmptyCounterAssignment_Test {

	private EmptyCounterAssignment eca = new EmptyCounterAssignment();
	private InPersonCounter ipc = new InPersonCounter();
	private AutomatedCounter ac = new AutomatedCounter();


	@Test
	public void testOffer() {
		assertEquals("Offering null will return this assignment", eca, eca.offer(null));
		assertEquals("Offering an in-person counter will return an InPersonAssignment with the counter stored",
				new InPersonAssignment(ipc), eca.offer(ipc));
		assertEquals("Offering an in-person counter will return an AutomatedAssignment with the counter stored",
				new AutomatedAssignment(ac), eca.offer(ac));
	}

	@Test
	public void testRemove() {
		assertEquals("Removing null will return this assignment", eca, eca.remove(null));
		assertEquals("Removing any InPersonCounter will return this assignment (this has no counters assigned)", eca, eca.remove(ipc));
		assertEquals("Removing any AutomatedCounter will return this assignment (this has no counters assigned)", eca, eca.remove(ac));
	}

	@Test
	public void testGetCounterSet() {
		assertEquals("An EmptyCounterAssignment does not have a counter set; it will return an empty optional", Optional.empty(), eca.getCounterSet());
	}

	@Test
	public void testIsInPerson() {
		assertFalse("An empty counter is not an in-person counter", eca.isInPerson());
	}

}
