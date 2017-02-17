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
import airville.counter.AutomatedCounter;
import airville.employee.Agent;
import airville.employee.Supervisor;
import airville.passenger.Group;
import airville.passenger.Passenger;
import airville.passenger.Passenger.Circumstance;

public class AutomatedCounter_Test {
	
	AutomatedCounter c = new AutomatedCounter();

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
		assertTrue("Without an agent at the counter, groups are still allowed", c.isAppropriateCounterForGroup(u));
		
		assertTrue("In-person counter is appropriate for a group with no circumstances", 
				c.isAppropriateCounterForGroup(u));
		
		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(Collections.singleton(Circumstance.COLLECTING_BONUS))));
		assertFalse("Automated counter cannot handle collecting bonus", c.isAppropriateCounterForGroup(u));
		
		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(Collections.singleton(Circumstance.FREQUENT_FLYER))));
		assertTrue("Frequent flyer is also fine", c.isAppropriateCounterForGroup(u));

		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(Collections.singleton(Circumstance.EXCESS_BAGGAGE))));
		assertFalse("An automated counter cannot handle excess baggage", c.isAppropriateCounterForGroup(u));

		u = gTest.newGroup(Collections.singleton(pTest.newPassenger(Collections.singleton(Circumstance.REROUTED))));
		assertFalse("A rerouted group cannot be helped by any old agent", c.isAppropriateCounterForGroup(u));
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
		c = new AutomatedCounter(new Agent(), new Supervisor());
		//This is intentionally gross: a group with a single frequent flyer passenger
		Group g = Group.generate().new Test()
					.newGroup(Collections.singleton(new Passenger.Test()
					.newPassenger(Collections.singleton(Circumstance.FREQUENT_FLYER))));
		c.offerGroup(g);
		assertEquals("Time multiplier is 0.8 * 0.6 * 0.95 = 0.456", 0.456, c.getTimeMultiplier(), 0.001);
	}

	@Test
	public void testAssignRemoveAgent() {
		Agent a1 = new Agent();
		Agent a2 = new Agent();
		
		c = new AutomatedCounter();
		assertFalse("Using nullary constructor, no agent is initially assigned", c.getAgent().isPresent());
		
		c = new AutomatedCounter(a1);
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
		
		c = new AutomatedCounter();
		assertFalse("Using nullary constructor, no supervisor is initially assigned", c.getSupervisor().isPresent());
		
		c = new AutomatedCounter(null, s1);
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
		
		Group g1 = gt.newGroup(Collections.singleton(p1));
		Group g2 = gt.newGroup(Collections.singleton(p2));
		Group g3 = gt.newGroup(Collections.singleton(p1));
		
		c = new AutomatedCounter(new Agent());
		assertFalse("No group is initially assigned", c.getGroup().isPresent());
		
		assertTrue("A no-circumstance group is acceptable", c.offerGroup(g1));
		
		assertFalse("Cannot add group when one is already assigned", c.offerGroup(g3));
		
		c.removeGroup();
		assertFalse("Group can be removed", c.getGroup().isPresent());
		
		assertFalse("Rerouted group cannot be added", c.offerGroup(g2));
		
	}


	// STRESS TEST
	@Test
	public void testCheckInGroup() {
		Passenger.Test pt = new Passenger.Test();
		Set<Circumstance> cSet = new HashSet<>();
		Passenger p1 = pt.newPassenger(cSet);			//empty
		Passenger p2 = pt.newPassenger(cSet);			//empty
		Passenger p3 = pt.newPassenger(cSet);			//empty
		
		cSet.add(Circumstance.FREQUENT_FLYER);
		Passenger p4 = pt.newPassenger(cSet);			//eb
		Passenger p5 = pt.newPassenger(cSet);			//eb
			
		Set<Passenger> pSet = new HashSet<>();
		pSet.add(p1); pSet.add(p2); pSet.add(p3);
		pSet.add(p4); pSet.add(p5); 
		
		Group gg = Group.generate();
		Group.Test gt = gg.new Test();
		Group g = gt.newGroup(pSet);
		
		c.assignAgent(new Agent());
		c.offerGroup(g);
		assertTrue("g was accepted by the counter", c.getGroup().isPresent());
		
		// PassengerBase, excess baggage, collecting bonus, agent
		//0.95 ^ 5 * 0.8 = 0.6190
		assertEquals("the counter's multiplier is accurate", 0.6190, c.getTimeMultiplier(), 0.01);
		
		Queue<Time> tq = c.checkInGroup();
		assertEquals("tq should have 5 entries for the 5 passengers", 5, tq.size());
		
		for(Time t : tq){
			assertEquals("All checkins return Time 6000 because arbitrary decisions", 6000, t.getValue());
		}
		
		assertFalse("There is no longer a group assigned to the counter", c.getGroup().isPresent());
		
	}

}
