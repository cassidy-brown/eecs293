package eecs293.cmb195.orange.products;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.requests.Exchange;
import eecs293.cmb195.orange.requests.Refund;
import eecs293.cmb195.orange.requests.RequestStatus;

class Opad extends AbstractProduct {
	
	static final Integer OPAD_REFUND_GCD_MIN = 12;

	Opad(SerialNumber serialNumber, Optional<Set<String>> description) {
		super(serialNumber, description);
	}

	/**
	 * @return ProductType.OPAD, the type of the product
	 */
	@Override
	public ProductType getProductType(){
		return ProductType.OPAD;
	}
	
	/**
	 * This method tests if the specified serial number is a valid
	 * number for an oPad.
	 * @param serialNumber serial number to be tested
	 * @return true if the specified serial number represents an oPad
	 */
	public static Boolean isValidSerialNumber(SerialNumber serialNumber){
		// returns true if the serial number is even and its third bit is set
		return serialNumber.isEven() && serialNumber.testBit(2);
	}

	/* An oPad is exchanged with the product that has the largest
	   compatible serial number that is strictly less than the original oPad
	   serial number. If no such compatible product exists, the exchange fails.
	   Otherwise, the status is set to OK and the result is set to the serial
	   number of the new oPad.
	 */
	@Override
	public void process(Exchange request, RequestStatus status)
			throws ProductException {	
		SerialNumber compatibleSerial = request.getCompatibleProducts().lower(getSerialNumber());
		
		if(Objects.nonNull(compatibleSerial)){
			status.setResult(Optional.of(compatibleSerial.getSerialNumber()));
			status.setStatus(RequestStatus.StatusCode.OK);
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
	}

	/* An oPad refund succeeds if and only if the greatest common divisor of
	   the RMA and the serial number is at least 12.
	 */ 
	@Override
	public void process(Refund request, RequestStatus status)
			throws ProductException {
		if(Utilities.isValidRefundViaGCD(getSerialNumber(), request.getRma(), OPAD_REFUND_GCD_MIN)){
			status.setStatus(RequestStatus.StatusCode.OK);
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
	}

}
