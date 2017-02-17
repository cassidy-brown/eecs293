package airville.passenger;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import airville.passenger.Passenger;
import airville.passenger.Passenger.Circumstance;

public class Passenger_Test {

	Passenger noSpecial, ff, eb, rr, cb, ff_rr, ff_eb_cb;
	Set<Circumstance> ff_rr_set, ff_eb_cb_set;
	
	@Before
	public void setUp() throws Exception {
		Passenger.Test t = new Passenger.Test();
		
		noSpecial = t.newPassenger(Collections.emptySet());
		ff = t.newPassenger(Collections.singleton(Circumstance.FREQUENT_FLYER));
		eb = t.newPassenger(Collections.singleton(Circumstance.EXCESS_BAGGAGE));
		rr = t.newPassenger(Collections.singleton(Circumstance.REROUTED));
		cb = t.newPassenger(Collections.singleton(Circumstance.COLLECTING_BONUS));
		
		ff_rr_set = new HashSet<>();
		ff_rr_set.add(Circumstance.FREQUENT_FLYER);
		ff_rr_set.add(Circumstance.REROUTED);
		ff_rr = t.newPassenger(ff_rr_set);
		
		ff_eb_cb_set = new HashSet<>();
		ff_eb_cb_set.add(Circumstance.FREQUENT_FLYER);
		ff_eb_cb_set.add(Circumstance.EXCESS_BAGGAGE);
		ff_eb_cb_set.add(Circumstance.COLLECTING_BONUS);
		ff_eb_cb = t.newPassenger(ff_eb_cb_set);		
	}

	@Test
	public void testGetCircumstanceSet() {
		assertEquals("A Passenger created with an empty circumstance set will get an empty circumstance set",
				Collections.emptySet(), noSpecial.getCircumstanceSet());
		assertEquals("A Passenger created with only a frequent flyer flag will get a circumstance with only frequent flyer",
				Collections.singleton(Circumstance.FREQUENT_FLYER), ff.getCircumstanceSet());
		assertEquals("A Passenger created with only an excess baggage flag will get a circumstance with only excess baggage",
				Collections.singleton(Circumstance.EXCESS_BAGGAGE), eb.getCircumstanceSet());
		assertEquals("A Passenger created with only a rerouted flag will get a circumstance with only rerouted",
				Collections.singleton(Circumstance.REROUTED), rr.getCircumstanceSet());
		assertEquals("A Passenger created with only a collecting bonus flag will get a circumstance with only collecting bonus",
				Collections.singleton(Circumstance.COLLECTING_BONUS), cb.getCircumstanceSet());
		assertEquals("A Passenger created with two traits will get a circumstance set with both those traits",
				ff_rr_set, ff_rr.getCircumstanceSet());
		assertEquals("A Passenger created with three traits will get a circumstance set with all of those traits",
				ff_eb_cb_set, ff_eb_cb.getCircumstanceSet());
	}

	@Test
	public void testGetTimeMultiplier() {
		//dependent on current constant settings
		assertEquals("No special circumstances, no time modifier", 1.0, noSpecial.getTimeMultiplier(), 0.0);
		assertEquals("Frequent flyer status does not affect time", 1.0, ff.getTimeMultiplier(), 0.0);
		assertEquals("simply having excess baggage doubles time", 2.0, eb.getTimeMultiplier(), 0.0);
		assertEquals("Simply rerouting doubles time", 2.0, rr.getTimeMultiplier(), 0.0);
		assertEquals("Simply collecting a bonus doubles time", 2.0, rr.getTimeMultiplier(), 0.0);
		assertEquals("Being a frequent flyer and being rerouted doubles time (1 * 2)", 2.0, ff_rr.getTimeMultiplier(), 0.001);
		assertEquals("Being a frequent flyer, having excess baggage, and collecting a bonus requires quadruple time (1 * 2 * 2)", 
				4.0, ff_eb_cb.getTimeMultiplier(), 0.001);
	}
	
	@Test
	public void testBusy() {
		Passenger c = Passenger.generate();
		assertFalse("By default, the counter is not busy", c.isBusy());
		
		c.setBusy(true);
		assertTrue("The counter has been set to be busy", c.isBusy());
		
		c.setBusy(false);
		assertFalse("And the counter is once again not busy", c.isBusy());
	}

/*	@Test
	public void testGenerate() {
		//It's pretty much impossible to test stochastic methods...
		
		//We're testing circumstance distributions over many generations
		//Dependent on constant frequencies set 
		List<Set<Circumstance>> listOfCircumSets = new ArrayList<>();
		for(int i = 0; i < 100; i++){
			Passenger p = Passenger.generate();
			listOfCircumSets.add(p.getCircumstanceSet());
			p.getCircumstanceSet().stream().forEach(c -> System.out.print(c + ", "));
			System.out.println();
		}

		long frequentFlyers = listOfCircumSets.stream().filter(set -> set.contains(Circumstance.FREQUENT_FLYER)).count();
		assertEquals(frequentFlyers + " (about 20) frequent flyers were generated out of 100", 20, frequentFlyers, 2);
		
		long excessBaggage = listOfCircumSets.stream().filter(set -> set.contains(Circumstance.EXCESS_BAGGAGE)).count();
		assertEquals(excessBaggage + " (about 30) passengers out of 100 were generated with excess baggage", 30, excessBaggage, 2);
		
		long rerouted = listOfCircumSets.stream().filter(set -> set.contains(Circumstance.REROUTED)).count();
		assertEquals(rerouted + " (about 10) passengers out of 100 generated were rerouted", 10, rerouted, 2);
		
		long collectingBonus = listOfCircumSets.stream().filter(set -> set.contains(Circumstance.COLLECTING_BONUS)).count();
		assertEquals(collectingBonus + " (about 10) passengers out of 100 generated were collecting a bonus", 10, collectingBonus, 2);
		
	} */

}
