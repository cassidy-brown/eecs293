package eecs293.cmb195.orange.exceptions;

import eecs293.cmb195.orange.products.ProductType;
import eecs293.cmb195.orange.products.SerialNumber;

public class ProductException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private final ProductType productType;
	private final SerialNumber serialNumber;
	private final ErrorCode errorCode;
	
	public enum ErrorCode{
		INVALID_SERIAL_NUMBER,
		INVALID_PRODUCT_TYPE,
		UNSUPPORTED_OPERATION
	}
	
	public ProductException(ProductType productType, 
							SerialNumber serialNumber,
							ErrorCode errorCode){
		this.productType = productType;
		this.serialNumber = serialNumber;
		this.errorCode = errorCode;
	}
	
	/**
	 * @return the ProductType associated with this exception
	 */
	public ProductType getProductType(){
		return productType;
	}
	
	/**
	 * @return the name of the product associated with this error
	 */
	public String getProductName(){
		return productType.getName();
	}

	/**
	 * @return the SerialNumber associated with this exception
	 */
	public SerialNumber getSerialNumber(){
		return serialNumber;
	}

	/**
	 * @return the ErrorCode associated with this exception
	 */
	public ErrorCode getErrorCode(){
		return errorCode;
	}
}
