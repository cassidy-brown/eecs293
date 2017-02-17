package eecs293.cmb195.orange.requests;

import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.exceptions.RequestException;
import eecs293.cmb195.orange.products.Product;
import eecs293.cmb195.orange.products.SerialNumber;

public final class Exchange implements Request {
	
	private final NavigableSet<SerialNumber> compatibleSet;
	
	/**
	 * This constructor initializes an Exchange object with the same set of 
	 * compatible serial numbers as the specified builder
	 * @param builder object containing Serial Number set to be associated with this exchange
	 */
	private Exchange(Builder builder){
		compatibleSet = new TreeSet<>(builder.getCompatibleProducts());
	}

	@Override
	public void process(Product product, RequestStatus status)
			throws RequestException {
		try {
			product.process(this, status);
		} catch (ProductException e) {
			status.setStatus(RequestStatus.StatusCode.FAIL);
			throw new RequestException(
				product, 
				RequestException.RequestType.EXCHANGE,
				RequestException.ErrorCode.UNSUPPORTED_OPERATION);			
		}
	}
	
	/**
	 * @return the set of serial numbers for the products that satisfy
	 *     the request
	 */
	public NavigableSet<SerialNumber> getCompatibleProducts(){
		return compatibleSet;
	}
	
	public static class Builder{
		
		private Set<SerialNumber> compatibleSet = new HashSet<>();
		
		/**
		 * Adds the serial number to the set of products compatible with
		 *     this exchange
		 * @param serialNumber serial number to be added to the set
		 * @return builder with the new serial number added to its set
		 */
		public Builder addCompatible(SerialNumber serialNumber){
			compatibleSet.add(serialNumber);
			return this;
		}
		
		/**
		 * @return the set of compatible products
		 */
		public Set<SerialNumber> getCompatibleProducts(){
			return compatibleSet;
		}
		
		/**
		 * @return an exchange with the given list of compatible products
		 */
		public Exchange build(){
			return new Exchange(this);
		}
	}

}
