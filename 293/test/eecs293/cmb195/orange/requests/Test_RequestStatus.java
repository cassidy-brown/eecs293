package eecs293.cmb195.orange.requests;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.Test;

import eecs293.cmb195.orange.requests.RequestStatus.StatusCode;

public class Test_RequestStatus {

	@Test
	public void testSetStatus() {
		RequestStatus status = new RequestStatus();

		assertEquals("A request status instance starts with an unknown status",
				StatusCode.UNKNOWN, status.getStatus());

		status.setStatus(StatusCode.OK);
		assertEquals("An instance's status can be set to OK",
				StatusCode.OK, status.getStatus());

		status.setStatus(StatusCode.FAIL);
		assertEquals("An instance's status can be set to FAIL",
				StatusCode.FAIL, status.getStatus());
	}

	@Test
	public void testSetResult() {
		RequestStatus status = new RequestStatus();
		
		assertEquals("A request status instance starts with an empty result",
				Optional.empty(), status.getResult());
		
		Optional<BigInteger> result = Optional.of(BigInteger.ONE);
				status.setResult(result);
		assertEquals("An instance's result can be set to any Optional BigInteger",
				result, status.getResult());
	}

}
