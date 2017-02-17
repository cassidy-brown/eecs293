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

class Otv extends AbstractProduct {
	
	private static final BigInteger TV_EXCHANGE_1024 = BigInteger.valueOf(1024);

	Otv(SerialNumber serialNumber, Optional<Set<String>> description) {
		super(serialNumber, description);
	}

	/**
	 * @return ProductType.OTV, the type of the product
	 */
	@Override
	public ProductType getProductType(){
		return ProductType.OTV;
	}
	
	/**
	 * This method tests if the specified serial number is a valid
	 * number for an oPod.
	 * @param serialNumber serial number to be tested
	 * @return true if the specified serial number represents an oPod
	 */
	public static Boolean isValidSerialNumber(SerialNumber serialNumber){
		/* returns true if the serial number is odd and its greatest common divisor
		 * with 630 is less than or equal to 14
		 */ 
		return serialNumber.isOdd() && Utilities.gcdInRangeForValidSerial(serialNumber, null, Utilities.VALID_SER_14);
	}
	
	/* For exchanges, consider the compatible serial numbers that are
	   strictly greater than the oTv’s and less than or equal than the oTv’s plus
	   1024. Then, take their average. Then, exchange the oTv with the largest
	   compatible serial number that is strictly greater than the oPhone’s and
	   strictly less than the average. If no such compatible product exists, then
	   the exchange fails.
	 */
	@Override
	public void process(Exchange request, RequestStatus status)
			throws ProductException {
		SerialNumber serPlus1024 = new SerialNumber(getSerialNumber().getSerialNumber().add(TV_EXCHANGE_1024));
		NavigableSet<SerialNumber> compatibleSet = request.getCompatibleProducts();
				
		SerialNumber picked = Utilities.exchangeSerialViaAveragedSubset(getSerialNumber(), serPlus1024, compatibleSet);
		
		if(Objects.nonNull(picked)){
			status.setResult(Optional.of(picked.getSerialNumber()));
			status.setStatus(RequestStatus.StatusCode.OK);
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
				
	}

	/* A refund succeeds only if the RMA is positive */
	@Override
	public void process(Refund request, RequestStatus status)
			throws ProductException {
		BigInteger rma = request.getRma();
		if(Objects.nonNull(rma) && rma.signum() != -1){
			status.setStatus(RequestStatus.StatusCode.OK);
		} else {
			status.setStatus(RequestStatus.StatusCode.FAIL);
		}
	}
}
