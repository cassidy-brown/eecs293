package eecs293.cmb195.orange.products;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.requests.Exchange;
import eecs293.cmb195.orange.requests.Refund;
import eecs293.cmb195.orange.requests.RequestStatus;

class Ophone extends AbstractProduct {

	Ophone(SerialNumber serialNumber, Optional<Set<String>> description) {
		super(serialNumber, description);
	}

	/**
	 * @return ProductType.OPHONE, the type of the product
	 */
	@Override
	public ProductType getProductType(){
		return ProductType.OPHONE;
	}
	
	/**
	 * This method tests if the specified serial number is a valid
	 * number for an oPod.
	 * @param serialNumber serial number to be tested
	 * @return true if the specified serial number represents an oPod
	 */
	public static Boolean isValidSerialNumber(SerialNumber serialNumber){
		/* returns true if the serial number is odd and its greatest common divisor
		 * with 630 is greater than 42
		 */ 
		return serialNumber.isOdd() && Utilities.gcdInRangeForValidSerial(serialNumber, Utilities.VALID_SER_42, null);
	}	
	
	/* An oPhone exchange works as follows. Consider the compatible
	   serial numbers that are strictly greater than the oPhone’s, and take their
	   average. Then, exchange the oPhone with the largest compatible serial
	   number that is strictly greater than the oPhone’s and strictly less than
	   the average. If no such compatible product exists, then the exchange
	   fails.
	  */
	//TODO this double fail-setting is kinda bad
	@Override
	public void process(Exchange request, RequestStatus status)
			throws ProductException {
		
		NavigableSet<SerialNumber> compatibleSet = request.getCompatibleProducts();
		if(compatibleSet.isEmpty()){
			status.setStatus(RequestStatus.StatusCode.FAIL);
		} else {
			SerialNumber picked = Utilities.exchangeSerialViaAveragedSubset(getSerialNumber(), compatibleSet.last(), compatibleSet);		
			if(Objects.nonNull(picked)){
				status.setResult(Optional.of(picked.getSerialNumber()));
				status.setStatus(RequestStatus.StatusCode.OK);
			} else {
				status.setStatus(RequestStatus.StatusCode.FAIL);
			}
		}
				
	}

	/* An oPhone succeeds if and only if the serial number can be obtained
	   by shifting to the left the RMA by 1, 2, or 3 bits 
	 */
	@Override
	public void process(Refund request, RequestStatus status)
			throws ProductException {
		BigInteger serialNumber = getSerialNumber().getSerialNumber();
		BigInteger rma = request.getRma();
		if(Objects.isNull(rma)){
			status.setStatus(RequestStatus.StatusCode.FAIL);
		} else {		
			//TODO rename helper
			if(Utilities.equalsAnyShifts(serialNumber, rma, 1, 3)){
				status.setStatus(RequestStatus.StatusCode.OK);
			} else {
				status.setStatus(RequestStatus.StatusCode.FAIL);
			}
		}
		
		
	}

}
