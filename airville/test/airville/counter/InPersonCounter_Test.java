package airville.counter;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import airville.RealTime;
import airville.RealTime.Time;
import airville.counter.InPersonCounter;
import airville.employee.Agent;
import airville.employee.Supervisor;
import airville.passenger.Group;
import airville.passenger.Passenger;
import airville.passenger.Passenger.Circumstance;

public class InPersonCounter_Test {
	
	InPersonCounter c = new InPersonCounter();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsAppropriateCounterForGroup() {
		Group g = Group.generate();
		Group.Test gTest = g.new Test();
		
		Set<Circumstance> cSet = new HashSet<>();
		Passenger.Test pTest = new Passenger.Test();
		Group u = gTest.newGroup(Collections.singleton(pTest.newPassenger(cSet)));
		assertFalse("Without an agent at the counter, no group is allowed", c.isAppropriateCounterForGroup(u));
		
		c.assignAgent(new Agent());
		assertTrue("In-person counter is appropriate for a group with no circumstances", 
				c.isAppropriateCounterForGroup(u));
		
		cSet.add(Circumstance.COLLECTING_BONUS);
		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(cSet)));
		assertTrue("Any in-person counter is fine for collecting bonus", c.isAppropriateCounterForGroup(u));
		
		cSet.add(Circumstance.FREQUENT_FLYER);
		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(cSet)));
		assertTrue("Adding frequent flyer is still fine", c.isAppropriateCounterForGroup(u));
		
		cSet.add(Circumstance.EXCESS_BAGGAGE);
		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(cSet)));
		assertTrue("An in-person counter can handle excess baggage", c.isAppropriateCounterForGroup(u));
		
		cSet.add(Circumstance.REROUTED);
		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(cSet)));
		assertFalse("A rerouted group cannot be helped by any old agent", c.isAppropriateCounterForGroup(u));
		
		c.assignSupervisor(new Supervisor());
		assertTrue("With a supervisor on hand, rerouted passengers can be handled", c.isAppropriateCounterForGroup(u));
	}

	@Test
	public void testBusy() {
		assertFalse("By default, the counter is not busy", c.isBusy());
		
		c.setBusy(true);
		assertTrue("The counter has been set to be busy", c.isBusy());
		
		c.setBusy(false);
		assertFalse("And the counter is once again not busy", c.isBusy());
	}

	@Test
	public void testGetTimeMultiplier() {
		c = new InPersonCounter(new Agent(), new Supervisor());
		//This is intentionally gross: a group with a single frequent flyer passenger
		Group g = Group.generate().new Test()
					.newGroup(Collections.singleton(new Passenger.Test()
					.newPassenger(Collections.singleton(Circumstance.EXCESS_BAGGAGE))));
		c.offerGroup(g);
		assertEquals("Time multiplier is 0.8 * 0.6 * 0.95 * 2.0 = 0.", 0.912, c.getTimeMultiplier(), 0.001);
	}

	@Test
	public void testAssignRemoveAgent() {
		Agent a1 = new Agent();
		Agent a2 = new Agent();
		
		c = new InPersonCounter();
		assertFalse("Using nullary constructor, no agent is initially assigned", c.getAgent().isPresent());
		
		c = new InPersonCounter(a1);
		assertTrue("Using 1-arg constructor, an agen is initially assigned", c.getAgent().isPresent());
		
		c.removeAgent();
		assertFalse("The counter has no agent after removing it", c.getAgent().isPresent());
		
		c.removeAgent();
		assertFalse("Removing agent when none is assigned doesn't do anything", c.getAgent().isPresent());
		
		c.assignAgent(null);
		assertFalse("Assigning a null when empty agent doesn't work", c.getAgent().isPresent());
		
		c.assignAgent(a1);
		assertEquals("a1 has been assigned to the agent", a1, c.getAgent().get());
		
		c.assignAgent(a2);
		assertEquals("New agent overwrites old agent", a2, c.getAgent().get());
		
		c.assignAgent(null);
		assertEquals("Assigning a null agent will not overwrite a real one", a2, c.getAgent().get());
		
		
	}

	@Test
	public void testAssignRemoveSupervisor() {
		Supervisor s1 = new Supervisor();
		Supervisor s2 = new Supervisor();
		
		c = new InPersonCounter();
		assertFalse("Using nullary constructor, no supervisor is initially assigned", c.getSupervisor().isPresent());
		
		c = new InPersonCounter(null, s1);
		assertTrue("Using 1-arg constructor, a supervisor is initially assigned", c.getSupervisor().isPresent());
		
		c.removeSupervisor();
		assertFalse("The counter has no supervisor after removing it", c.getSupervisor().isPresent());
		
		c.removeSupervisor();
		assertFalse("Removing supervisor when none is assigned doesn't do anything", c.getSupervisor().isPresent());
		
		c.assignSupervisor(null);
		assertFalse("Assigning a null when empty supervisor doesn't work", c.getSupervisor().isPresent());
		
		c.assignSupervisor(s1);
		assertEquals("s1 has been assigned to the supervisor", s1, c.getSupervisor().get());
		
		c.assignSupervisor(s2);
		assertEquals("New supervisor overwrites old supervisor", s2, c.getSupervisor().get());
		
		c.assignSupervisor(null);
		assertEquals("Assigning a null supervisor will not overwrite a real one", s2, c.getSupervisor().get());		
	}

	@Test
	public void testOfferRemoveGroup() {
		Group gg = Group.generate();
		Group.Test gt = gg.new Test();
		
		Passenger.Test pt = new Passenger.Test();
		Set<Circumstance> cSet = new HashSet<>();
		Passenger p1 = pt.newPassenger(cSet);			//empty
		
		cSet.add(Circumstance.REROUTED);
		Passenger p2 = pt.newPassenger(cSet);
		
		cSet.remove(Circumstance.REROUTED);
		cSet.add(Circumstance.EXCESS_BAGGAGE);
		Passenger p3 = pt.newPassenger(cSet);

		Group g1 = gt.newGroup(Collections.singleton(p1));
		Group g2 = gt.newGroup(Collections.singleton(p2));
		Group g3 = gt.newGroup(Collections.singleton(p3));
		
		c = new InPersonCounter(new Agent());
		assertFalse("No group is initially assigned", c.getGroup().isPresent());
		
		assertTrue("A no-circumstance group is acceptable", c.offerGroup(g1));
		
		c.removeGroup();
		assertFalse("Group can be removed", c.getGroup().isPresent());
		
		assertFalse("Rerouted group cannot be added without supervisor at counter", c.offerGroup(g2));
		assertTrue("Excess baggaged group can be added to any inperson counter", c.offerGroup(g3));
		
		c.removeGroup();
		c.assignSupervisor(new Supervisor());
		assertTrue("Rerouted group is accepted if supervisor attending", c.offerGroup(g2));
		
	}


	// STRESS TEST
	@Test
	public void testCheckInGroup() {
		Passenger.Test pt = new Passenger.Test();
		Set<Circumstance> cSet = new HashSet<>();
		Passenger p1 = pt.newPassenger(cSet);			//empty
		Passenger p2 = pt.newPassenger(cSet);			//empty
		Passenger p3 = pt.newPassenger(cSet);			//empty
		
		cSet.add(Circumstance.EXCESS_BAGGAGE);
		Passenger p4 = pt.newPassenger(cSet);			//eb
		Passenger p5 = pt.newPassenger(cSet);			//eb
		
		cSet.add(Circumstance.FREQUENT_FLYER);
		Passenger p6 = pt.newPassenger(cSet);			//ff, eb
		
		cSet.remove(Circumstance.EXCESS_BAGGAGE);
		Passenger p7 = pt.newPassenger(cSet);			//ff
		
		cSet.add(Circumstance.COLLECTING_BONUS);
		Passenger p8 = pt.newPassenger(cSet);			//ff, cb
		
		cSet.remove(Circumstance.FREQUENT_FLYER);
		Passenger p9 = pt.newPassenger(cSet);			//cb
		
		Set<Passenger> pSet = new HashSet<>();
		pSet.add(p1); pSet.add(p2); pSet.add(p3);
		pSet.add(p4); pSet.add(p5); pSet.add(p6);
		pSet.add(p7); pSet.add(p8); pSet.add(p9);
		
		Group gg = Group.generate();
		Group.Test gt = gg.new Test();
		Group g = gt.newGroup(pSet);
		
		c.assignAgent(new Agent());
		c.offerGroup(g);
		assertTrue("g was accepted by the counter", c.getGroup().isPresent());
		
		// PassengerBase, excess baggage, collecting bonus, agent
		//0.95 ^ 9 * 2 ^ 3 * 2 ^ 2 * 0.8 = 16.13438
		assertEquals("the counter's multiplier is accurate", 16.13438, c.getTimeMultiplier(), 0.01);
		
		Queue<Time> tq = c.checkInGroup();
		assertEquals("tq should have 9 entries for the 9 passengers", 9, tq.size());
		
		for(Time t : tq){
			assertEquals("All checkins return Time 6000 because arbitrary decisions", 6000, t.getValue());
		}
		
		assertFalse("There is no longer a group assigned to the counter", c.getGroup().isPresent());
		
	}

}
