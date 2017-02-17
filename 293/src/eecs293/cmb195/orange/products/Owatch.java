package eecs293.cmb195.orange.products;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.products.ProductType;
import eecs293.cmb195.orange.products.Utilities;
import eecs293.cmb195.orange.products.SerialNumber;
import eecs293.cmb195.orange.requests.Exchange;
import eecs293.cmb195.orange.requests.Refund;
import eecs293.cmb195.orange.requests.RequestStatus;

class Owatch extends AbstractProduct {
	
	static final int OWATCH_REFUND_MIN = 14;

	Owatch(SerialNumber serialNumber, Optional<Set<String>> description) {
		super(serialNumber, description);
	}	

	/**
	 * @return ProductType.OPWATCH, the type of the product
	 */
	@Override
	public ProductType getProductType(){
		return ProductType.OWATCH;
	}
	
	/**
	 * This method tests if the specified serial number is a valid
	 * number for an oPod.
	 * @param serialNumber serial number to be tested
	 * @return true if the specified serial number represents an oPod
	 */
	public static Boolean isValidSerialNumber(SerialNumber serialNumber){
		/* returns true if the serial number is odd and its greatest common divisor
		 * with 630 is between 14 and 42
		 */ 
		return serialNumber.isOdd() && Utilities.gcdInRangeForValidSerial(serialNumber, Utilities.VALID_SER_14, Utilities.VALID_SER_42);
	}
	
	/* As for exchanges, consider the smallest compatible serial number
	   that is strictly greater than the original oWatch serial number. If no
	   such compatible product exists, the exchange fails. Otherwise, the 
	   status is set to OK and the result is set to the serial number of the new
	   oWatch
	 */
	@Override
	public void process(Exchange request, RequestStatus status)
			throws ProductException {	
		SerialNumber compatibleSerial = request.getCompatibleProducts().higher(getSerialNumber());

		if(Objects.nonNull(compatibleSerial)){
			status.setResult(Optional.of(compatibleSerial.getSerialNumber()));
			status.setStatus(RequestStatus.StatusCode.OK);
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
	}

	/* A refund succeeds if and only if the exclusive OR of the serial number
	   and the RMA is greater than 14.
	 */
	@Override
	public void process(Refund request, RequestStatus status)
			throws ProductException {
		if(Utilities.isValidRefundViaXor(getSerialNumber(), request.getRma(), OWATCH_REFUND_MIN)){
			status.setStatus(RequestStatus.StatusCode.OK);
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
	}

}
