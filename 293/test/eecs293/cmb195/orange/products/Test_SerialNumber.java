package eecs293.cmb195.orange.products;

import static org.junit.Assert.*;

import org.junit.Test;

import eecs293.cmb195.orange.products.SerialNumber;

import java.math.BigInteger;

public class Test_SerialNumber {

	@Test
	public void testConstructor() {
		SerialNumber sn1 = new SerialNumber(BigInteger.ONE);
		
		assertEquals("A new SerialNumber instance stores the indicated BigInteger as its serial number",
				sn1.getSerialNumber(), BigInteger.ONE);
	}

	@Test
	public void testGcd() {
		BigInteger bi6 = new BigInteger("6");
		SerialNumber sn6 = new SerialNumber(bi6);
		
		BigInteger bi7 = new BigInteger("7");
		SerialNumber sn7 = new SerialNumber(bi7);
		
		BigInteger bi24 = new BigInteger("24");
		SerialNumber sn24 = new SerialNumber(bi24);

		assertEquals("Two SerialNumbers will generate the same greatest common "
				+ "divisor as their two representing BigInteger serialNumbers", sn6.gcd(sn24), bi6.gcd(bi24));
		
		assertEquals("The greatest common dvisor of a serial number 6 and serial number 24 is 6",
				sn6.gcd(sn24), bi6);

		assertEquals("The greatest common dvisor of a serial number 7 and serial number 24 is 1",
				sn7.gcd(sn24), BigInteger.ONE);
		
		assertEquals("The greatest common divisor of 6 and 24 is the same as the gcd of 24 and 6",
				sn6.gcd(sn24), sn24.gcd(sn6));
	}

	@Test
	public void testMod() {
		BigInteger bi6 = new BigInteger("6");
		SerialNumber sn6 = new SerialNumber(bi6);
		
		BigInteger bi7 = new BigInteger("7");
		SerialNumber sn7 = new SerialNumber(bi7);
		
		BigInteger bi24 = new BigInteger("24");
		SerialNumber sn24 = new SerialNumber(bi24);

		assertEquals("Two SerialNumbers will generate the same modulus result "
				+ "as their two representing BigInteger serialNumbers", sn24.mod(sn7), bi24.remainder(bi7));
		
		assertEquals("serial number 24 mod serial number 6 equals 0",
				sn24.mod(sn6), BigInteger.ZERO);

		assertEquals("serial number 24 mod serial number 7 equals 3",
				sn24.mod(sn7), new BigInteger("3"));
	}

	@Test
	public void testTestBit() {
		BigInteger bi6 = new BigInteger("6");
		SerialNumber sn6 = new SerialNumber(bi6);

		assertEquals("A SerialNumber will yield the same test bit result "
				+ "as its representing BigInteger serialNumber", sn6.testBit(1), bi6.testBit(1));
		
		assertFalse("The 0th bit of SerialNumber 6 is not set", sn6.testBit(0));
		assertTrue("The 1st bit of SerialNumber 6 is set", sn6.testBit(1));
		assertTrue("The 2nd bit of SerialNumber 6 is set", sn6.testBit(2));
		assertFalse("The 3rd bit of SerialNumber 6 is not set", sn6.testBit(3));
	}

	@Test
	public void testIsEven() {
		BigInteger bi6 = new BigInteger("6");
		SerialNumber sn6 = new SerialNumber(bi6);
		
		BigInteger bi7 = new BigInteger("7");
		SerialNumber sn7 = new SerialNumber(bi7);
		
		assertTrue("SerialNumber 6 is even", sn6.isEven());
		assertFalse("SerialNumber 7 is not even", sn7.isEven());
	}

	@Test
	public void testIsOdd() {
		BigInteger bi6 = new BigInteger("6");
		SerialNumber sn6 = new SerialNumber(bi6);
		
		BigInteger bi7 = new BigInteger("7");
		SerialNumber sn7 = new SerialNumber(bi7);
		
		assertFalse("SerialNumber 6 is not odd", sn6.isOdd());
		assertTrue("SerialNumber 7 is odd", sn7.isOdd());
	}

	@Test
	public void testEqualsObject() {
		BigInteger bi6 = new BigInteger("6");
		SerialNumber sn6 = new SerialNumber(bi6);
		
		assertTrue("SerialNumber equivalence is based solely on their serialNumber fields",
				sn6.equals(new SerialNumber(bi6)));
	}
}
