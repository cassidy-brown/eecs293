package eecs293.cmb195.orange.products;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.products.AbstractProduct;
import eecs293.cmb195.orange.products.Opad;
import eecs293.cmb195.orange.products.Ophone;
import eecs293.cmb195.orange.products.Opod;
import eecs293.cmb195.orange.products.Otv;
import eecs293.cmb195.orange.products.Owatch;
import eecs293.cmb195.orange.products.Product;
import eecs293.cmb195.orange.products.ProductType;
import eecs293.cmb195.orange.products.Utilities;
import eecs293.cmb195.orange.products.SerialNumber;

public class Test_Products {

	@Test
	public void testGetSerialNumberAndDescription() {
		SerialNumber sn6 = new SerialNumber(new BigInteger("6"));
		Product pod6 = new Opod(sn6, Optional.empty());
		Optional<Set<String>> strSet = Optional.of(Collections.singleton("description"));
		Product pad10 = new Opad(new SerialNumber(BigInteger.TEN), strSet);
		
		assertEquals("getSerialNumber returns the serial number specified in a product's constructor",
				pod6.getSerialNumber(), sn6);
		assertEquals("getDescription returns an empty Optional if no description was specified at construction",
				pod6.getDescription(), Optional.empty());
		assertEquals("getDescription returns the descriptions specified in a product's constructor",
				pad10.getDescription(), strSet);
	}
	
	@Test
	public void testGetProductName() {
		SerialNumber sn = new SerialNumber(BigInteger.ONE);
		
		Product pod = new Opod(sn, Optional.empty());
		assertEquals("An instance of Opod has product name \"oPod\"",
				"oPod", pod.getProductName());
		
		Product pad = new Opad(sn, Optional.empty());
		assertEquals("An instance of Opad has product name \"oPad\"",
				"oPad", pad.getProductName());
		
		Product phone = new Ophone(sn, Optional.empty());
		assertEquals("An instance of Ophone has product name \"oPhone\"",
				"oPhone", phone.getProductName());
		
		Product watch = new Owatch(sn, Optional.empty());
		assertEquals("An instance of Owatch has product name \"oWatch\"",
				"oWatch", watch.getProductName());
		
		Product tv = new Otv(sn, Optional.empty());
		assertEquals("An instance of Otv has product name \"oTv\"",
				"oTv", tv.getProductName());
		
		Product podProduct = new Opod(sn, Optional.empty());
		assertEquals("oPod", podProduct.getProductName());		
	}
	

	@Test
	public void testGetProductType() {
		SerialNumber sn = new SerialNumber(BigInteger.ONE);
		
		Product pod = new Opod(sn, Optional.empty());
		assertEquals("An instance of Opod has product type OPOD regardless of serial number",
				ProductType.OPOD, pod.getProductType());
		
		Product pad = new Opad(sn, Optional.empty());
		assertEquals("An instance of Opad has product type OPAD",
				ProductType.OPAD, pad.getProductType());
		
		Product phone = new Ophone(sn, Optional.empty());
		assertEquals("An instance of Ophone has product type OPHONE",
				ProductType.OPHONE, phone.getProductType());
		
		Product watch = new Owatch(sn, Optional.empty());
		assertEquals("An instance of Owatch has product type OWATCH",
				ProductType.OWATCH, watch.getProductType());
		
		Product tv = new Otv(sn, Optional.empty());
		assertEquals("An instance of Otv has product type OTV",
				ProductType.OTV, tv.getProductType());
		
		Product podProduct = new Opod(sn, Optional.empty());
		assertEquals(ProductType.OPOD, podProduct.getProductType());	
	}
	
	@Test
	public void testGcdInRange() {
		SerialNumber snTv = new SerialNumber(BigInteger.valueOf(9));	//gcd(630) = 9
		SerialNumber snWatch = new SerialNumber(BigInteger.valueOf(35));	//gcd(630) = 35
		SerialNumber snPhone = new SerialNumber(BigInteger.valueOf(45));	//gcd(630) = 45
		SerialNumber sn10 = new SerialNumber(BigInteger.TEN);
		
		assertTrue("gcdInRange with null min and max values will return true",
				Utilities.gcdInRangeForValidSerial(sn10, null, null));

		assertTrue("Serial number 9 has a GCD less than or equal to 14 (in the appropriate range for oTvs)",
				Utilities.gcdInRangeForValidSerial(snTv, null, Utilities.VALID_SER_14));
		assertTrue("Serial number 35 has a GCD between 14 exclusive and 42 inclusive (in the appropriate range for oWatches)",
				Utilities.gcdInRangeForValidSerial(snWatch, Utilities.VALID_SER_14, Utilities.VALID_SER_42));
		assertTrue("Serial number 45 has a GCD greater than 42 (in the appropriate range for oPhones)",
				Utilities.gcdInRangeForValidSerial(snPhone, Utilities.VALID_SER_42, null));
		
		assertFalse("Serial number 45 does not have a GCD between 14 exclusive and 42 inclusive (in the appropriate range for oWatches)",
				Utilities.gcdInRangeForValidSerial(snPhone, Utilities.VALID_SER_14, Utilities.VALID_SER_42));
		assertTrue("gcdInRange pays no heed to the \"serial number is odd\" requirement. " +
				"Serial number 10 is still in range for oTvs", Utilities.gcdInRangeForValidSerial(snTv, null, Utilities.VALID_SER_14));
		
	}

	@Test
	public void testIsValidSerialNumber() {
		SerialNumber snPod = new SerialNumber(BigInteger.valueOf(8));	//1000
		SerialNumber snPad = new SerialNumber(BigInteger.valueOf(12));	//1100
		SerialNumber snTv = new SerialNumber(BigInteger.valueOf(9));	//gcd(630) = 9
		SerialNumber snWatch = new SerialNumber(BigInteger.valueOf(35));	//gcd(630) = 35
		SerialNumber snPhone = new SerialNumber(BigInteger.valueOf(45));	//gcd(630) = 45
		
		assertTrue("Product with serial number 8 (b1000) is a valid oPod (even and 3rd bit not set)",
				Opod.isValidSerialNumber(snPod));
		assertTrue("Product with serial number 12 (b1100) is a valid oPad (even and 3rd bit set)",
				Opad.isValidSerialNumber(snPad));
		assertTrue("Product with serial number 45 is a valid oPhone (odd and gcd(630) > 42)",
				Ophone.isValidSerialNumber(snPhone));	
		assertTrue("Product with serial number 35 is a valid oWatch (odd and 14 < gcd(630) < 42)",
				Owatch.isValidSerialNumber(snWatch));
		assertTrue("Product with serial number 9 is a valid oTv (odd and gcd(630) < 14)",
				Otv.isValidSerialNumber(snTv));
		
		assertFalse("A serial number which is valid for one product cannot be valid for any others. " +
				"A valid oPod serial number is not a valid oPad serial number", Opad.isValidSerialNumber(snPod));
		assertFalse("A valid oPod serial number is not a valid oPhone serial number", Ophone.isValidSerialNumber(snPod));
		assertFalse("A valid oPod serial number is not a valid oWatch serial number", Owatch.isValidSerialNumber(snPod));
		assertFalse("A valid oPod serial number is not a valid oTv serial number", Otv.isValidSerialNumber(snPod));
	}
	
	@Rule 
	public final ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testMake() throws InstantiationException, IllegalAccessException, ProductException{
		SerialNumber snPod = new SerialNumber(BigInteger.valueOf(8));	//1000
		SerialNumber snPad = new SerialNumber(BigInteger.valueOf(12));	//1100
		SerialNumber snTv = new SerialNumber(BigInteger.valueOf(9));	//gcd(630) = 9
		SerialNumber snWatch = new SerialNumber(BigInteger.valueOf(35));	//gcd(630) = 35
		SerialNumber snPhone = new SerialNumber(BigInteger.valueOf(45));	//gcd(630) = 45
		
		Optional<Set<String>> emptyDesc = Optional.empty();
		Optional<Set<String>> desc = Optional.of(Collections.singleton("this is a description"));

		assertEquals("make() can create a new Opod instance", new Opod(snPod, emptyDesc),
				AbstractProduct.make(ProductType.OPOD, snPod, emptyDesc));
		assertEquals("make() can create a new Opad instance", new Opad(snPad, emptyDesc),
				AbstractProduct.make(ProductType.OPAD, snPad, emptyDesc));
		assertEquals("make() can create a new Ophone instance", new Ophone(snPhone, emptyDesc),
				AbstractProduct.make(ProductType.OPHONE, snPhone, emptyDesc));
		assertEquals("make() can create a new Owatch instance", new Owatch(snWatch, emptyDesc),
				AbstractProduct.make(ProductType.OWATCH, snWatch, emptyDesc));
		assertEquals("make() can create a new Otv instance", new Otv(snTv, emptyDesc),
				AbstractProduct.make(ProductType.OTV, snTv, emptyDesc));

		Product madeOpod = AbstractProduct.make(ProductType.OPOD, snPod, desc);
		assertEquals("make() creates Product (here, Opod) instance with the specified SerialNumber", 
				snPod, madeOpod.getSerialNumber());
		assertEquals("make() creates a Product (here, Opod) instance with the specified Description", 
				desc, madeOpod.getDescription());
		
		exception.expect(ProductException.class);
		AbstractProduct.make(ProductType.OPOD, snWatch, emptyDesc);
		
	}
}