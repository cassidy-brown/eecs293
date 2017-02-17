package eecs293.cmb195.orange.requests;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import eecs293.cmb195.orange.exceptions.RequestException;
import eecs293.cmb195.orange.requests.Refund.Builder;

public class Test_Refund {
	
	@Rule 
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testBuilderAndConstruction() throws RequestException{
		BigInteger rma = BigInteger.ONE;
		BigInteger rma0 = BigInteger.ZERO;
		BigInteger rma10 = BigInteger.TEN;
		
		Builder b = new Builder();
		b.setRMA(rma);
				
		assertEquals("A builder with a set rma will return that value as the rma",
				rma, b.getRma());
		
		Refund r1 = b.build();
		assertEquals("An exchange built from a builder will " +
				"have the same rma as the builder",
				b.getRma(), r1.getRma());	
		
		b.setRMA(rma0).setRMA(rma10);
		assertEquals("You can chain setRMA() calls, though there's no reason to do so",
				rma10, b.getRma());
		
		assertEquals("Despite changing builder, r1 has maintained the same rma",
				rma, r1.getRma());
		
		/* Trying to set the RMA to null will throw an exception */
		exception.expect(RequestException.class);
		b.setRMA(null);
	}

}
