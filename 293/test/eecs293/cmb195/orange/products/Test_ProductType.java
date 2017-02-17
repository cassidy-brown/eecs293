package eecs293.cmb195.orange.products;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import eecs293.cmb195.orange.products.Opad;
import eecs293.cmb195.orange.products.Ophone;
import eecs293.cmb195.orange.products.Opod;
import eecs293.cmb195.orange.products.Otv;
import eecs293.cmb195.orange.products.Owatch;
import eecs293.cmb195.orange.products.ProductType;
import eecs293.cmb195.orange.products.SerialNumber;

public class Test_ProductType {

	@Test
	public void testGetName() {
		assertEquals("An OPOD's name is \"oPod\"", ProductType.OPOD.getName(), "oPod");
		assertEquals("An OPAD's name is \"oPad\"", ProductType.OPAD.getName(), "oPad");
		assertEquals("An OPHONE's name is \"oPhone\"", ProductType.OPHONE.getName(), "oPhone");
		assertEquals("An OWATCH's name is \"oWatch\"", ProductType.OWATCH.getName(), "oWatch");
		assertEquals("An OTV's name is \"oTv\"", ProductType.OTV.getName(), "oTv");
	}
	
	@Test
	public void testInstantiate() {
		SerialNumber sn = new SerialNumber(BigInteger.ZERO);
		Optional<Set<String>> desc = Optional.empty();
		assertEquals("An OPOD's instantiate method creates a new Opod instance" +
				"(doesn't check serial validity)",  
				ProductType.OPOD.instantiate(sn, desc), new Opod(sn, desc));
		assertEquals("An OPAD's instantiate method creates a new Opad instance" +
				"(doesn't check serial validity)", 
				ProductType.OPAD.instantiate(sn, desc), new Opad(sn, desc));
		assertEquals("An OPHONE's instantiate method creates a new Ophone instance" +
				"(doesn't check serial validity)",  
				ProductType.OPOD.instantiate(sn, desc), new Ophone(sn, desc));
		assertEquals("An OWATCH's instantiate method creates a new Owatch instance" +
				"(doesn't check serial validity)",  
				ProductType.OWATCH.instantiate(sn, desc), new Owatch(sn, desc));
		assertEquals("An OTV's instantiate method creates a new Otv instance" +
				"(doesn't check serial validity)", 
				ProductType.OTV.instantiate(sn, desc), new Otv(sn, desc));
	}
	
	@Test
	public void testIsValidSerialNumber() {
		SerialNumber snPod = new SerialNumber(BigInteger.valueOf(8));	//1000
		SerialNumber snPad = new SerialNumber(BigInteger.valueOf(12));	//1100
		SerialNumber snTv = new SerialNumber(BigInteger.valueOf(9));	//gcd(630) = 9
		SerialNumber snWatch = new SerialNumber(BigInteger.valueOf(35));	//gcd(630) = 35
		SerialNumber snPhone = new SerialNumber(BigInteger.valueOf(45));	//gcd(630) = 45
		
		assertTrue("Product with serial number 8 (b1000) is a valid oPod (even and 3rd bit not set)",
				ProductType.OPOD.isValidSerialNumber(snPod));
		assertTrue("Product with serial number 12 (b1100) is a valid oPad (even and 3rd bit set)",
				ProductType.OPAD.isValidSerialNumber(snPad));
		assertTrue("Product with serial number 45 is a valid oPhone (odd and gcd(630) > 42)",
				ProductType.OPHONE.isValidSerialNumber(snPhone));	
		assertTrue("Product with serial number 35 is a valid oWatch (odd and 14 < gcd(630) < 42)",
				ProductType.OWATCH.isValidSerialNumber(snWatch));
		assertTrue("Product with serial number 9 is a valid oTv (odd and gcd(630) < 14)",
				ProductType.OTV.isValidSerialNumber(snTv));
		
		assertEquals("ProductType.{ENUM}.isValidSerialNumber() returns the same value as the " +
				"isValidSerialNumber() method in the associated product class",
				ProductType.OPOD.isValidSerialNumber(snPod), Opod.isValidSerialNumber(snPod));
	}

}
