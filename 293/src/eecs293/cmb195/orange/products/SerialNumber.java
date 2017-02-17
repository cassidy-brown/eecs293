package eecs293.cmb195.orange.products;

import java.math.BigInteger;

/**
 * This class represents the serial number on an individual
 * product from Orange Computer Corporation
 * @author cmb195
 *
 */
public class SerialNumber implements Comparable<SerialNumber>{
	
	private BigInteger serialNumber;
	
	/**
	 * This constructor creates a serial number instance 
	 * with the specified identifier
	 * @param serialNumber
	 */
	public SerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	/**
	 * Returns the BigInteger value of the instance's serial number
	 * @return this serial number
	 */
	public BigInteger getSerialNumber(){
		//TODO don't let it initialize as null?
		return serialNumber;
	}
	
	/**
	 * This method compares the specified serial number with
	 * this instance's serial number and returns the greatest common
	 * denominator of the two
	 * @param other SerialNumber to compare with
	 * @return greatest common divisor of this serial number 
	 *     and the specified other instance
	 */
	public BigInteger gcd(SerialNumber other){
		return serialNumber.gcd(other.getSerialNumber());
	}
	
	/**
	 * Returns this serial number modulus the specified other serial number
	 * @param other
	 * @return this serial number modulus the specified number
	 */
	public BigInteger mod(SerialNumber other){
		return serialNumber.remainder(other.getSerialNumber());
	}
	
	/**
	 * This method tests if the bit at the given index is
	 * set in this serial number
	 * @param bit index of the bit to check
	 * @return true if the specified bit is set
	 */
	public boolean testBit(int bit) {
		return serialNumber.testBit(bit);
	}
	
	/**
	 * Tests if this serial number instance represents an even number
	 * @return true if this serial number is even
	 */
	public boolean isEven(){
		return !isOdd();
	}
	
	/**
	 * Tests if this serial number instance represents an odd number
	 * @return true if this serial number is odd
	 */
	public boolean isOdd() {
		return serialNumber.testBit(0);
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof SerialNumber)){
			return false;
		}
		
		SerialNumber other = (SerialNumber)o;
		return serialNumber.equals(other.getSerialNumber());
	}
	
	@Override
	public int hashCode(){
		return serialNumber.hashCode();
	}
	
	@Override
	public int compareTo(SerialNumber other){
		return serialNumber.compareTo(other.getSerialNumber());
	}
	
	@Override
	public String toString(){
		return serialNumber.toString();
	}

}
