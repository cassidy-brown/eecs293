package eecs293.cmb195.orange.products;

import java.util.Optional;
import java.util.Set;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.requests.Exchange;
import eecs293.cmb195.orange.requests.Refund;
import eecs293.cmb195.orange.requests.RequestStatus;

class Opod extends AbstractProduct {
	
	static final Integer OPOD_REFUND_GCD_MIN = 24;

	Opod(SerialNumber serialNumber, Optional<Set<String>> description){
		super(serialNumber, description);
	}
	
	/**
	 * @return ProductType.OPOD, the type of the product
	 */
	@Override
	public ProductType getProductType(){
		return ProductType.OPOD;
	}
	
	/**
	 * This method tests if the specified serial number is a valid
	 * number for an oPod.
	 * @param serialNumber serial number to be tested
	 * @return true if the specified serial number represents an oPod
	 */
	public static Boolean isValidSerialNumber(SerialNumber serialNumber){
		// returns true if the serial number is even and its third bit is not set
		return serialNumber.isEven() && !serialNumber.testBit(2);
	}
	
	/* An oPod is exchanged with any product that has a compatible
	   serial number; the status is set to OK and result to the new serial
	   number. If no compatible product exists, the exchange fails.
	 */
	@Override
	public void process(Exchange request, RequestStatus status)
			throws ProductException {
		if(!request.getCompatibleProducts().isEmpty()){
			SerialNumber compatibleSerial = request.getCompatibleProducts().first();
			status.setResult(Optional.of(compatibleSerial.getSerialNumber()));
			status.setStatus(RequestStatus.StatusCode.OK);
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
	}

	/* An oPod refund succeeds if and only if the greatest common divisor of
	   the RMA and the serial number is at least 24, in which case the status
	   is set to OK and the result is set to undefined.
	 */
	@Override
	public void process(Refund request, RequestStatus status)
			throws ProductException {
		if(Utilities.isValidRefundViaGCD(getSerialNumber(), request.getRma(), OPOD_REFUND_GCD_MIN)){
			status.setStatus(RequestStatus.StatusCode.OK);
			status.setResult(Optional.empty());
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
	}
}
