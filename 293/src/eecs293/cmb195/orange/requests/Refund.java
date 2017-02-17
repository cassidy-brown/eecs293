package eecs293.cmb195.orange.requests;

import java.math.BigInteger;
import java.util.Objects;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.exceptions.RequestException;
import eecs293.cmb195.orange.products.Product;

public final class Refund implements Request {
	
	private final BigInteger rma;
	
	/**
	 * This constructor sets the refund's RMA to the same as the specified builder's
	 * @param builder object containing RMA to be associated with this refund
	 */
	private Refund(Builder builder){
		rma = builder.getRma();
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
					RequestException.RequestType.REFUND,
					RequestException.ErrorCode.UNSUPPORTED_OPERATION);
		}
	}
	
	/**
	 * @return  the RMA compatible with the customer request
	 */
	public BigInteger getRma(){
		return rma;
	}
	
	
	/**
	 * Internal class used to assign an RMA and create a new Refund object
	 */
	public static class Builder{
		
		private BigInteger rma;
		
		/**
		 * sets the RMA to the given value. 
		 * If the RMA is null, it throws a RequestException.
		 * @param rma new rma to set
		 * @return builder with the new rma set
		 */
		public Builder setRMA(BigInteger rma) throws RequestException{
			if(!Objects.isNull(rma)){
				this.rma = rma;
				return this;
			} else {
				throw new RequestException(
						null,
						RequestException.RequestType.REFUND_BUILDER,
						RequestException.ErrorCode.INVALID_RMA);
			}
		}
		
		/**
		 * @return the builder’s RMA code.
		 */
		public BigInteger getRma(){
			return rma;
		}
		
		/**
		 * @return a Refund object with this builder's rma code
		 */
		public Refund build(){
			return new Refund(this);
		}
	}

}
