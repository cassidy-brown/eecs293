package airville.passenger;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import airville.passenger.Group;
import airville.passenger.Passenger;
import airville.passenger.Passenger.Circumstance;

public class Group_Test {

	Group.Test test; 
	Group group_solo_empty, group_solo_rr, group_many_empty, group_vari;
	Set<Passenger> pass_solo_empty, pass_ff, pass_eb, pass_rr, pass_cb, pass_many_empty, pass_vari;
	Set<Circumstance> cirum_empty = Collections.emptySet(), 
					  circum_ff = Collections.singleton(Circumstance.FREQUENT_FLYER),
					  circum_eb = Collections.singleton(Circumstance.EXCESS_BAGGAGE),
					  circum_rr = Collections.singleton(Circumstance.REROUTED),
					  circum_cb = Collections.singleton(Circumstance.COLLECTING_BONUS),
					  circum_vari;
	
	@Before
	public void setUp() throws Exception {
		test = (Group.generate()).new Test();
		Passenger.Test pt = new Passenger.Test();

		pass_solo_empty = Collections.singleton(pt.newPassenger(cirum_empty));
		group_solo_empty = test.newGroup(pass_solo_empty);
		
		pass_rr = Collections.singleton(pt.newPassenger(circum_rr));
		group_solo_rr = test.newGroup(pass_rr);

		pass_ff = Collections.singleton(pt.newPassenger(circum_ff));
		pass_eb = Collections.singleton(pt.newPassenger(circum_eb));
		pass_cb = Collections.singleton(pt.newPassenger(circum_cb));
		
		pass_many_empty = new HashSet<>();
		pass_many_empty.add(pt.newPassenger(Collections.emptySet()));
		pass_many_empty.add(pt.newPassenger(Collections.emptySet()));
		group_many_empty = test.newGroup(pass_many_empty);
		
		pass_vari = new HashSet<>();
		pass_vari.add(pt.newPassenger(Collections.singleton(Circumstance.EXCESS_BAGGAGE)));
		pass_vari.add(pt.newPassenger(Collections.emptySet()));
		circum_vari = new HashSet<>();
		circum_vari.add(Circumstance.EXCESS_BAGGAGE);
		circum_vari.add(Circumstance.FREQUENT_FLYER);
		circum_vari.add(Circumstance.COLLECTING_BONUS);
		pass_vari.add(pt.newPassenger(circum_vari));
		group_vari = test.newGroup(pass_vari);
	}

	@Test
	public void testGetPassengerSet() {
		assertEquals("This set has one passenger with no circumstances", pass_solo_empty, group_solo_empty.getPassengerSet());
		assertEquals("This set has one passenger who was rerouted", pass_rr, group_solo_rr.getPassengerSet());
		assertEquals("This set has multiple passengers with no circumstances", pass_many_empty, group_many_empty.getPassengerSet());
		assertEquals("This set has multiple passengers with different circumstances", pass_vari, group_vari.getPassengerSet());
	}

	@Test
	public void testGetCircumstanceSet() {
		assertEquals("This set has an empty circumstance set", cirum_empty, group_solo_empty.getCircumstanceSet());
		assertEquals("This set has only rerouted in its circumstance set", circum_rr, group_solo_rr.getCircumstanceSet());
		assertEquals("This set has an empty circumstance set", cirum_empty, group_many_empty.getCircumstanceSet());
		assertEquals("This set has a circumstance set with frequent flyer, excess baggage, and collecting bonus", circum_vari, group_vari.getCircumstanceSet());
	}

	@Test
	public void testNeedsAssistance() {
		assertFalse("A group with no circumstances does not need assistance", test.testNeedsAssistance(cirum_empty));
		assertFalse("A group that simply has a frequent flyer does not need assistance", test.testNeedsAssistance(circum_ff));
		assertTrue("A group that simply has excess baggage does need assistance", test.testNeedsAssistance(circum_eb));
		assertTrue("A group that simply has been rerouted does need assistance", test.testNeedsAssistance(circum_rr));
		assertTrue("A group that simply is collecting a bonus does need assistance", test.testNeedsAssistance(circum_cb));
		
		assertTrue("A group with multiple circumstances needs assistance", test.testNeedsAssistance(circum_vari));
	}

	@Test
	public void testGetTimeMultiplier() {
		assertEquals("A single passenger with no circumstances has a standard multiplier of 0.95 (due to group size)", 0.95, test.testGetTimeMultiplier(pass_solo_empty), 0.0);
		assertEquals("Two passengers without circumstances: 0.95 * 0.95 = 0.9025", 0.9025, test.testGetTimeMultiplier(pass_many_empty), 0.0001);
		
		assertEquals("A single passenger with only frequent flyer: 1.0 * 0.95 = 0.95", 0.95, test.testGetTimeMultiplier(pass_ff), 0.0001);
		assertEquals("A single passenger with only excess baggage: 2.0 * 0.95 = 1.90", 1.90, test.testGetTimeMultiplier(pass_eb), 0.0001);
		assertEquals("A single passenger with only being rerouted: 2.0 * 0.95 = 1.90", 1.90, test.testGetTimeMultiplier(pass_rr), 0.0001);
		assertEquals("A single passenger with only collecting bonus: 2.0 * 0.95 = 1.90", 1.90, test.testGetTimeMultiplier(pass_cb), 0.0001);
		
		//2 * EB(2), 1 * FF(1), 1 * CB(2), 3 * Pass(0.95) 
		assertEquals("Multiple passengers with multiple circumstances multiplies all factors", 6.859, test.testGetTimeMultiplier(pass_vari), 0.0001);
	}

	@Test
	public void testBusy() {
		Group c = Group.generate();
		assertFalse("By default, the counter is not busy", c.isBusy());
		
		c.setBusy(true);
		assertTrue("The counter has been set to be busy", c.isBusy());
		
		c.setBusy(false);
		assertFalse("And the counter is once again not busy", c.isBusy());
		
		//Should also examine stored children's isBusy()
	}


/*	@Test
	public void testGenerate() {
		/* As evidenced when testing Passenger.generate(),
		 * there's no real use to testing a method with random
		 * calculations in it
		 *
	} */

	@Test
	public void testNewGroupSize() {
		/* This test method ensures inputs fall within the desired range
		 * but makes no claims about the distribution of results
		 */
		for(int i = 0; i < 100; i++){
			int n = test.testNewGroupSize();
			assertTrue("newGroupSize should always return a number between 1 and 10, inclusive", 1 <= n && n <= 10);
		}
	}

}
