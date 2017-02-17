package eecs293.cmb195.orange.products;

import java.util.Optional;
import java.util.Set;

import eecs293.cmb195.orange.exceptions.ProductException;
import eecs293.cmb195.orange.exceptions.ProductException.ErrorCode;
import eecs293.cmb195.orange.requests.Exchange;
import eecs293.cmb195.orange.requests.Refund;
import eecs293.cmb195.orange.requests.RequestStatus;

public abstract class AbstractProduct implements Product {
	
	private SerialNumber serialNumber;
	private Optional<Set<String>> description;
	
	public AbstractProduct(
			SerialNumber serialNumber, 
			Optional<Set<String>> description){
		this.serialNumber = serialNumber;
		this.description = description;
	}
	
	@Override
	public SerialNumber getSerialNumber() {
		return serialNumber;
	}
	
	@Override
	public abstract ProductType getProductType();

	@Override
	public String getProductName() {
		return getProductType().getName();
	}

	@Override
	public Optional<Set<String>> getDescription() {
		return description;
	}
	
	/**
	 * This factory method creates a new instance of the specified type with
	 * the specified serial number and description
	 * @param productType type of the new product
	 * @param serialNumber serial number of the new product
	 * @param description description of the new product
	 * @return a new product instance
	 * @throws ProductException if the specified serial number is not valid for the given type
	 */
	public static Product make(ProductType productType, SerialNumber serialNumber, 
								Optional<Set<String>> description) throws ProductException{
		if(productType.isValidSerialNumber(serialNumber)){
			return productType.instantiate(serialNumber, description);
		} else {
			throw new ProductException(productType, serialNumber, ErrorCode.INVALID_SERIAL_NUMBER);
		}
	}
	
	public abstract void process(Exchange request, RequestStatus status) 
			throws ProductException;
	
	public abstract void process(Refund request, RequestStatus status) 
			throws ProductException;
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Product)){
			return false;
		}
		AbstractProduct other = (AbstractProduct)o;
		return serialNumber.equals(other.getSerialNumber());
	}
	
	@Override
	public int hashCode(){
		return serialNumber.hashCode();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(getProductName());
		sb.append(" ");
		sb.append(getSerialNumber());
		sb.append(System.lineSeparator());
		if(description.isPresent()){
			Set<String> descSet = description.get();
			sb.append("Description:\n");
			descSet.forEach(desc -> {
				sb.append(Character.toUpperCase(desc.charAt(0)) + desc.substring(1));
				sb.append(System.lineSeparator());
			});
		}
		return sb.toString();
	}
}
