package eecs293.cmb195.orange.exceptions;

import eecs293.cmb195.orange.products.Product;

/**
 * This exception is thrown if errors occur while processing requests
 * @author Cassidy
 *
 */
public class RequestException extends Exception {

	private static final long serialVersionUID = 1L;
	private final Product product;
	private final RequestType requestType;
	private final ErrorCode errorCode;
	
	public enum ErrorCode{
		INVALID_RMA,
		UNSUPPORTED_OPERATION
	}
	
	/* May eventually want to extend this to its own
	 * class like ProductType */
	public enum RequestType{
		EXCHANGE,
		REFUND,
		REFUND_BUILDER
	}
	
	public RequestException(Product product, RequestType requestType, ErrorCode errorCode){
		this.product = product;
		this.requestType = requestType;
		this.errorCode = errorCode;
	}
	
	/**
	 * @return the product associated with this exception
	 */
	public Product getProduct(){
		return product;
	}
	
	/**
	 * @return the type of request associated with this exception
	 */
	public RequestType getRequestType(){
		return requestType;
	}
	
	/**
	 * @return the error associated with this exception
	 */
	public ErrorCode getErrorCode(){
		return errorCode;
	}
	
}
