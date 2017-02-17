package eecs293.cmb195.orange.products;

import java.util.Optional;
import java.util.Set;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.products.ProductType;
import eecs293.cmb195.orange.products.SerialNumber;
import eecs293.cmb195.orange.requests.Exchange;
import eecs293.cmb195.orange.requests.Refund;
import eecs293.cmb195.orange.requests.RequestStatus;

public interface Product {
	
	/**
	 * @return this product's serial number
	 */
	public SerialNumber getSerialNumber();
	
	/**
	 * @return the type of this product
	 */
	public ProductType getProductType();
	
	/**
	 * @return the name of this product type
	 */
	public String getProductName();
	
	/**
	 * @return an optional set of human-readable descriptions
	 *     and notes regarding this product
	 */
	public Optional<Set<String>> getDescription();
	
	/**
	 * Processes the specified exchange and appropriately sets 
	 * the request status
	 * @param request exchange to be processed
	 * @param status status of the request
	 * @throws ProductException
	 */
	public void process(Exchange request, RequestStatus status) 
			throws ProductException;
	
	/**
	 * Processes the specified refund and appropriately sets 
	 * the request status
	 * @param request refund to be processed
	 * @param status status of the request
	 * @throws ProductException
	 */
	public void process(Refund request, RequestStatus status) 
			throws ProductException;

}
