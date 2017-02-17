package eecs293.cmb195.orange.products;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.requests.Exchange;
import eecs293.cmb195.orange.requests.Exchange.Builder;
import eecs293.cmb195.orange.requests.RequestStatus;

public class Test_Process_Exchange {

	@Test
	public void testOpodProcess() throws ProductException {
		Opod opod = new Opod(new SerialNumber(BigInteger.valueOf(2)), Optional.empty());
		Builder b = new Builder();
		
		/* Compatible set = [] */
		Exchange e_noCompatible = b.build();
		RequestStatus status = new RequestStatus();
		opod.process(e_noCompatible, status);
		assertEquals("An exchange request with no compatible serial numbers will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with no compatible serial numbers will produce a status with no result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [8] */
		BigInteger eight = BigInteger.valueOf(8);
		b.addCompatible(new SerialNumber(eight));
		Exchange e_withCompatible = b.build();
		status = new RequestStatus();
		opod.process(e_withCompatible, status);
		assertEquals("An exchange request with a compatible serial number will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		assertEquals("An exchange request with no compatible serial numbers will produce a status " +
				"with the new serial number as result",
				Optional.of(eight), status.getResult());
	}
	
	@Test
	public void testOpadProcess() throws ProductException {
		Opad opad = new Opad(new SerialNumber(BigInteger.valueOf(6)), Optional.empty());
		Builder b = new Builder();
		
		/* Compatible set = [] */
		Exchange e_noCompatible = b.build();
		RequestStatus status = new RequestStatus();
		opad.process(e_noCompatible, status);
		assertEquals("An exchange request with no compatible serial numbers will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with no compatible serial numbers will produce a status with no result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [10] */
		b.addCompatible(new SerialNumber(BigInteger.TEN));
		Exchange e_noLessThan = b.build();
		status = new RequestStatus();
		opad.process(e_noLessThan, status);
		assertEquals("An exchange request with no compatible serial number less than this serial number will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with no compatible serial number less than this serial number will produce a status with no result",
				Optional.empty(), status.getResult());

		/* Compatible set = [2, 4, 10] */
		BigInteger four = BigInteger.valueOf(4);
		BigInteger two = BigInteger.valueOf(2);
		b.addCompatible(new SerialNumber(two)).addCompatible(new SerialNumber(four));
		Exchange e_someLessThan = b.build();
		status = new RequestStatus();
		opad.process(e_someLessThan, status);
		assertEquals("An exchange request with multiple compatible serial numbers less than this serial number " + 
				"will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		assertEquals("An exchange request with multiple compatible serial numbers less than this serial number " +
				" will produce a status with the largest compatible number less than this serial number as the result",
				Optional.of(four), status.getResult());
	}
	
	@Test
	public void testOphoneProcess() throws ProductException {
		Ophone ophone = new Ophone(new SerialNumber(BigInteger.valueOf(450)), Optional.empty());
		Builder b = new Builder();
		
		/* Compatible set = [] */
		Exchange e_noCompatible = b.build();
		RequestStatus status = new RequestStatus();
		ophone.process(e_noCompatible, status);
		assertEquals("An exchange request with no compatible serial numbers will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with no compatible serial numbers will produce a status with no result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [405] */
		BigInteger fourOhFive = BigInteger.valueOf(405);
		b.addCompatible(new SerialNumber(fourOhFive));
		Exchange e_onlyLessThan = b.build();
		status = new RequestStatus();
		ophone.process(e_onlyLessThan, status);
		assertEquals("An exchange request with only a compatible serial number less than this will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with only a compatible serial number less than this will produce a status " +
				"with an empty result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [405, 495] */
		BigInteger fourNineFive = BigInteger.valueOf(495);
		b.addCompatible(new SerialNumber(fourNineFive));
		Exchange e_oneGreater = b.build();
		status = new RequestStatus();
		ophone.process(e_oneGreater, status);
		assertEquals("An exchange request with only one compatible serial number greater than this will produce a FAIL status " +
				"(there's no value strictly less than average)",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with only one compatible serial number greater than this will produce a status " +
				"with an empty result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [405, 495, 585] */
		BigInteger fiveEightFive = BigInteger.valueOf(585);
		b.addCompatible(new SerialNumber(fiveEightFive));
		Exchange e_twoGreater = b.build();
		status = new RequestStatus();
		ophone.process(e_twoGreater, status);
		assertEquals("An exchange request with at least two compatible serial numbers greater than this will produce a OK status ",
				RequestStatus.StatusCode.OK, status.getStatus());
		assertEquals("An exchange request with at least two compatible serial numbers greater than this will produce a status " +
				"with the new serial number as result",
				Optional.of(fourNineFive), status.getResult());
	}
	

	@Test
	public void testOwatchProcess() throws ProductException {
		Owatch owatch = new Owatch(new SerialNumber(BigInteger.valueOf(6)), Optional.empty());
		Builder b = new Builder();
		
		/* Compatible set = [] */
		Exchange e_noCompatible = b.build();
		RequestStatus status = new RequestStatus();
		owatch.process(e_noCompatible, status);
		assertEquals("An exchange request with no compatible serial numbers will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with no compatible serial numbers will produce a status with no result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [2] */
		b.addCompatible(new SerialNumber(BigInteger.valueOf(2)));
		Exchange e_noLessThan = b.build();
		status = new RequestStatus();
		owatch.process(e_noLessThan, status);
		assertEquals("An exchange request with no compatible serial number greater than this serial number will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with no compatible serial number less than this serial number will produce a status with no result",
				Optional.empty(), status.getResult());

		/* Compatible set = [2, 8, 10] */
		BigInteger eight = BigInteger.valueOf(8);
		BigInteger ten = BigInteger.valueOf(10);
		b.addCompatible(new SerialNumber(eight)).addCompatible(new SerialNumber(ten));
		Exchange e_someLessThan = b.build();
		status = new RequestStatus();
		owatch.process(e_someLessThan, status);
		assertEquals("An exchange request with any compatible serial numbers greater than this serial number " + 
				"will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		assertEquals("An exchange request with any compatible serial numbers greater than this serial number " +
				" will produce a status with the smallest compatible number greater than this serial number as the result",
				Optional.of(eight), status.getResult());
	}
	
	@Test
	public void testOtvProcess() throws ProductException {
		Otv otv = new Otv(new SerialNumber(BigInteger.valueOf(450)), Optional.empty());
		Builder b = new Builder();
		
		/* Compatible set = [] */
		Exchange e_noCompatible = b.build();
		RequestStatus status = new RequestStatus();
		otv.process(e_noCompatible, status);
		assertEquals("An exchange request with no compatible serial numbers will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with no compatible serial numbers will produce a status with no result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [405] */
		BigInteger fourOhFive = BigInteger.valueOf(405);
		b.addCompatible(new SerialNumber(fourOhFive));
		Exchange e_onlyLessThan = b.build();
		status = new RequestStatus();
		otv.process(e_onlyLessThan, status);
		assertEquals("An exchange request with only a compatible serial number less than this will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with only a compatible serial number less than this will produce a status " +
				"with an empty result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [405, 2000] */
		BigInteger twoTripleOh = BigInteger.valueOf(2000);
		b.addCompatible(new SerialNumber(twoTripleOh));
		Exchange e_onlyOutsideRange = b.build();
		status = new RequestStatus();
		otv.process(e_onlyOutsideRange, status);
		assertEquals("An exchange request with compatible serial number greater than this but also " +
				"greater than this plus 1024 will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with only a compatible serial number less than this will produce a status " +
				"with an empty result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [405, 495, 2000] */
		BigInteger fourNineFive = BigInteger.valueOf(495);
		b.addCompatible(new SerialNumber(fourNineFive));
		Exchange e_oneGreater = b.build();
		status = new RequestStatus();
		otv.process(e_oneGreater, status);
		assertEquals("An exchange request with only one compatible serial number greater than this will produce a FAIL status " +
				"(there's no value strictly less than average)",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("An exchange request with only one compatible serial number greater than this will produce a status " +
				"with an empty result",
				Optional.empty(), status.getResult());
		
		/* Compatible set = [405, 495, 585 2000] */
		BigInteger fiveEightFive = BigInteger.valueOf(585);
		b.addCompatible(new SerialNumber(fiveEightFive));
		Exchange e_twoGreater = b.build();
		status = new RequestStatus();
		otv.process(e_twoGreater, status);
		assertEquals("An exchange request with at least two compatible serial numbers greater than this will produce a OK status ",
				RequestStatus.StatusCode.OK, status.getStatus());
		assertEquals("An exchange request with at least two compatible serial numbers greater than this will produce a status " +
				"with the new serial number as result",
				Optional.of(fourNineFive), status.getResult());
	}
	
	@Test
	public void testAssignmentsTest() throws ProductException {
		/* Furthermore, write one more test cases to exchange oPad 1048 with
		 * either 1032 or 1244, assuming that the final request status does not have
		 * to be checked. This test case should consist of a single instruction in
		 * which multiple methods are invoked.
		 */
		Builder b = new Builder();
		Opad opad = new Opad(new SerialNumber(BigInteger.valueOf(1048)), Optional.empty());
		RequestStatus status = new RequestStatus();
		opad.process(b.addCompatible(new SerialNumber(BigInteger.valueOf(1032))).addCompatible(new SerialNumber(BigInteger.valueOf(1244))).build(), status);
		assertEquals("This lovely multi-method-invoking line will yield a status with result 1032", 
				Optional.of(BigInteger.valueOf(1032)), status.getResult());
		
		//Why don't we have to check the status? Unless get result is supposed to be accessed via our lovely single line.
		//But I don't see how you can do that from a void method
	}
	
	@Test
	public void testUtilitiesExchangeSerialViaAveragedSubset() {
		//Implicitly tested via the others
	}

}
