package eecs293.cmb195.orange.requests;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import eecs293.cmb195.orange.products.SerialNumber;
import eecs293.cmb195.orange.requests.Exchange.Builder;

public class Test_Exchange {	
	
	@Test
	public void testBuilderAndConstruction(){
		SerialNumber sn1 = new SerialNumber(BigInteger.ONE);
		SerialNumber sn2 = new SerialNumber(BigInteger.valueOf(2));
		SerialNumber sn3 = new SerialNumber(BigInteger.valueOf(3));
		SerialNumber sn4 = new SerialNumber(BigInteger.valueOf(4));
		
		Builder b = new Builder();
		b.addCompatible(sn1).addCompatible(sn2).addCompatible(sn3);
		
		Set<SerialNumber> set = new HashSet<>();
		set.add(sn1);
		set.add(sn2);
		set.add(sn3);
		
		assertEquals("A builder with three serial numbers added " +
				"will return a set of those three numbers",
				set, b.getCompatibleProducts());
		
		Exchange e1 = b.build();
		TreeSet<SerialNumber> t1 = new TreeSet<>(set);
		assertEquals("An exchange built from a builder will " +
				"have the same set of compatible products as the builder",
				t1, e1.getCompatibleProducts());
		
		b.addCompatible(sn4);
		set.add(sn4);
		
		assertEquals("Adding another serialNumber to the Builder will not affect " +
				"an already-constructed exchange", t1, e1.getCompatibleProducts());
		

		Exchange e2 = b.build();
		TreeSet<SerialNumber> t2 = new TreeSet<>(set);
		
		assertEquals("An exchange from the builder with four compatible products " +
				"will also have four compatible products", t2, e2.getCompatibleProducts());
		
	}

}
