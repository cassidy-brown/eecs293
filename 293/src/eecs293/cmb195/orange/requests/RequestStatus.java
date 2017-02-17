package eecs293.cmb195.orange.requests;

import java.math.BigInteger;
import java.util.Optional;

public class RequestStatus {
	
	private StatusCode status;
	private Optional<BigInteger> result;

	public enum StatusCode {
		UNKNOWN,
		OK,
		FAIL
	}
	
	public RequestStatus(){
		status = StatusCode.UNKNOWN;
		result = Optional.empty();
	}
	
	/**
	 * Set this status's associated status code
	 * @param newStatus the new status code to be set
	 */
	public void setStatus(StatusCode newStatus){
		this.status = newStatus;
	}
	
	/**
	 * @return this status's associated status code
	 */
	public StatusCode getStatus(){
		return status;
	}
	
	/**
	 * Set this status's result
	 * @param newResult result to be set
	 */
	public void setResult(Optional<BigInteger> newResult){
		this.result = newResult;
	}
	
	/**
	 * @return this status's result
	 */
	public Optional<BigInteger> getResult(){
		return result;
	}
	
}
