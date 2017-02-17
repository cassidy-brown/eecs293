package eecs293.cmb195.orange.products;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import eecs293.cmb195.orange.products.AbstractProduct;
import eecs293.cmb195.orange.products.Opad;
import eecs293.cmb195.orange.products.Ophone;
import eecs293.cmb195.orange.products.Opod;
import eecs293.cmb195.orange.products.Otv;
import eecs293.cmb195.orange.products.Owatch;

/**
 * An enum whose values represent the products sold by 
 * Orange Computers Corporation
 * @author Cassidy
 */
public enum ProductType {

	OPOD("oPod", Opod::new, Opod::isValidSerialNumber), 
	OPAD("oPad", Opad::new, Opad::isValidSerialNumber), 
	OPHONE("oPhone", Ophone::new, Ophone::isValidSerialNumber), 
	OWATCH("oWatch", Owatch::new, Owatch::isValidSerialNumber), 
	OTV("oTv", Otv::new, Otv::isValidSerialNumber);
	
	
	private String name;
	private BiFunction<SerialNumber, Optional<Set<String>>, AbstractProduct> instantiator;
	private Function<SerialNumber, Boolean> validSerFunction;
	
	ProductType(String name, 
			BiFunction<SerialNumber, Optional<Set<String>>, AbstractProduct> instantiator, 
			Function<SerialNumber, Boolean> validSerFunction){
		this.name = name;
		this.instantiator = instantiator;
		this.validSerFunction = validSerFunction;
	}
	
	/**
	 * @return the name of this enum
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Return a new instance of the associated product class with the specified
	 * serial number and description
	 * @param ser Serial number of the new product
	 * @param desc description of the new product
	 * @return an instance of the associated product class
	 */
	public AbstractProduct instantiate(SerialNumber ser, Optional<Set<String>> desc){
		return instantiator.apply(ser, desc);
	}
	
	/**
	 * @param serialNumber serial number to test
	 * @return true if the specified serial number is valid for this product type
	 */
	public boolean isValidSerialNumber(SerialNumber serialNumber){
		return this.validSerFunction.apply(serialNumber);
	}
}
