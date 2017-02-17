package eecs293.cmb195.orange.requests;

import eecs293.cmb195.orange.exceptions.RequestException;
import eecs293.cmb195.orange.products.Product;

public interface Request {
	
	/**
	 * This method processes the request for the given product and
	 * sets the RequestStatus to the appropriate code and result
	 * @param product product on which request should be processed
	 * @param status status to be updated during processing
	 * @throws RequestException
	 */
	public void process(Product product, RequestStatus status)
			throws RequestException;

}
