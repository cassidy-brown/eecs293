package eecs293.cmb195.orange.products;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.Test;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.exceptions.RequestException;
import eecs293.cmb195.orange.requests.Refund;
import eecs293.cmb195.orange.requests.RequestStatus;
import eecs293.cmb195.orange.requests.Refund.Builder;

public class Test_Process_Refund {

	@Test
	public void testOpodProcess() throws ProductException, RequestException {
		Opod opod = new Opod(new SerialNumber(BigInteger.valueOf(48)), Optional.empty());
		
		Builder b = new Builder();
		
		Refund r_noRMA = b.build();
		RequestStatus status = new RequestStatus();
		opod.process(r_noRMA, status);
		assertEquals("a refund request with no RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("a refund request with no RMA will produce a status with no result",
				Optional.empty(), status.getResult());
	
		
		b.setRMA(BigInteger.valueOf(30));
		Refund r_gcdIs6 = b.build();
		status = new RequestStatus();
		opod.process(r_gcdIs6, status);
		assertEquals("a refund request with an incompatible RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("a refund request with an incompatible RMA will produce a status with no result",
				Optional.empty(), status.getResult());
		
		b.setRMA(BigInteger.valueOf(72));
		Refund r_gcdIs24 = b.build();
		status = new RequestStatus();
		opod.process(r_gcdIs24, status);
		assertEquals("a refund request with an incompatible RMA will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		assertEquals("a refund request with an incompatible RMA will produce a status with no result",
				Optional.empty(), status.getResult());
	}
	
	@Test
	public void testOpadProcess() throws ProductException, RequestException {
		Opad opad = new Opad(new SerialNumber(BigInteger.valueOf(36)), Optional.empty());
		
		Builder b = new Builder();
		
		Refund r_noRMA = b.build();
		RequestStatus status = new RequestStatus();
		opad.process(r_noRMA, status);
		assertEquals("a refund request with no RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("a refund request with no RMA will produce a status with no result",
				Optional.empty(), status.getResult());
	
		
		b.setRMA(BigInteger.valueOf(30));
		Refund r_gcdIs6 = b.build();
		status = new RequestStatus();
		opad.process(r_gcdIs6, status);
		assertEquals("a refund request with an incompatible RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("a refund request with an incompatible RMA will produce a status with no result",
				Optional.empty(), status.getResult());
		
		b.setRMA(BigInteger.valueOf(24));
		Refund r_gcdIs12 = b.build();
		status = new RequestStatus();
		opad.process(r_gcdIs12, status);
		assertEquals("a refund request with an incompatible RMA will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		assertEquals("a refund request with an incompatible RMA will produce a status with no result",
				Optional.empty(), status.getResult());
	}
	
	@Test
	public void testOphoneProcess() throws ProductException, RequestException {
		Ophone ophone = new Ophone(new SerialNumber(BigInteger.valueOf(8)), Optional.empty());
		
		Builder b = new Builder();
		
		Refund r_noRMA = b.build();
		RequestStatus status = new RequestStatus();
		ophone.process(r_noRMA, status);
		assertEquals("A refund request with no RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("A refund request with no RMA will produce a status with no result",
				Optional.empty(), status.getResult());
		
		b.setRMA(BigInteger.valueOf(4));
		Refund r_shift1 = b.build();
		status = new RequestStatus();
		ophone.process(r_shift1, status);
		assertEquals("An ophone refund request with an RMA half this serial number will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		
		b.setRMA(BigInteger.valueOf(2));
		Refund r_shift2 = b.build();
		status = new RequestStatus();
		ophone.process(r_shift2, status);
		assertEquals("An ophone refund request with an RMA one-quarter this serial number will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());

		b.setRMA(BigInteger.valueOf(1));
		Refund r_shift3 = b.build();
		status = new RequestStatus();
		ophone.process(r_shift3, status);
		assertEquals("An ophone refund request with an RMA one-eighth this serial number will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		
		b.setRMA(BigInteger.valueOf(3));
		Refund r_incompatible = b.build();
		status = new RequestStatus();
		ophone.process(r_incompatible, status);
		assertEquals("An ophone refund request with an RMA that is not this serial number shifted will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
	}
	
	@Test
	public void testOwatchProcess() throws ProductException, RequestException {
		Owatch owatch = new Owatch(new SerialNumber(BigInteger.valueOf(8)), Optional.empty());
		
		Builder b = new Builder();
		
		Refund r_noRMA = b.build();
		RequestStatus status = new RequestStatus();
		owatch.process(r_noRMA, status);
		assertEquals("A refund request with no RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("A refund request with no RMA will produce a status with no result",
				Optional.empty(), status.getResult());
	
		b.setRMA(BigInteger.valueOf(10));
		Refund r_xorIs2 = b.build();
		status = new RequestStatus();
		owatch.process(r_xorIs2, status);
		assertEquals("An refund request with an RMA whose xor with this serial number is less than fourteen will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		
		b.setRMA(BigInteger.valueOf(7));
		Refund r_xorIs15 = b.build();
		status = new RequestStatus();
		owatch.process(r_xorIs15, status);
		assertEquals("An refund request with an RMA whose xor with this serial number is greater than to fourteen will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
	}
	
	@Test
	public void testOtvProcess() throws ProductException, RequestException {
		Otv otv = new Otv(new SerialNumber(BigInteger.ZERO), Optional.empty());
	
		Builder b = new Builder();
		
		Refund r_noRMA = b.build();
		RequestStatus status = new RequestStatus();
		otv.process(r_noRMA, status);
		assertEquals("A refund request with no RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
		assertEquals("A refund request with no RMA will produce a status with no result",
				Optional.empty(), status.getResult());
		
		b.setRMA(BigInteger.valueOf(1));
		Refund r_positive = b.build();
		status = new RequestStatus();
		otv.process(r_positive, status);
		assertEquals("An refund request with a positive RMA will produce an OK status",
				RequestStatus.StatusCode.OK, status.getStatus());
		
		b.setRMA(BigInteger.valueOf(-1));
		Refund r_negative = b.build();
		status = new RequestStatus();
		otv.process(r_negative, status);
		assertEquals("An refund request with a negative RMA will produce a FAIL status",
				RequestStatus.StatusCode.FAIL, status.getStatus());
	}

}
