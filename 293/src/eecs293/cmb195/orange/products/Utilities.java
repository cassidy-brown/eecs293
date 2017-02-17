package eecs293.cmb195.orange.products;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This class contains utility methods for working with 
 * and processing OCC Product sub-classes
 * @author Cassidy
 *
 */
class Utilities {

	public static final int VALID_SER_14 = 14;
	public static final int VALID_SER_42 = 42;
	public static final int VALID_SER_GCD_630 = 630;
	
	
	/**
	 * This is a helper method for Ophone, Owatch, and Otv's isValidSerialNumber methods.
	 * It calculates whether serialNumber is within the range (min, max]
	 * Entering null for min or max makes a one-sided comparison
	 * @param serialNumber the serial number being considered
	 * @param min minimum value to which a comparison is made
	 * @param max maximum value to which a comparison is made
	 * @return true if the gcd of serialNumber and 630 is in the 
	 *     appropriate range for the specified product type.
	 */
	protected static boolean gcdInRangeForValidSerial(SerialNumber serialNumber, Integer min, Integer max){
		int gcd = serialNumber.gcd(new SerialNumber(BigInteger.valueOf(VALID_SER_GCD_630))).intValue();
		boolean greaterThanMin = (min == null) ? true : gcd > min;
		boolean lessOrEqualToMax = (max == null) ? true : gcd <= max;
		return greaterThanMin && lessOrEqualToMax;
	}	
	
	
	/**
	 * This is a helper method for Opod's and Opad's refund process method.
	 * It calculates the gcd between the specified serialNumber and RMA and
	 * returns whether it exceeds the specified minimum
	 * @param serialNumber serial number of product to be refunded
	 * @param rma authorization code for the refund transaction
	 * @param min minimum value of gcd
	 * @return true if serialNumber.gcd(rma) >= min
	 */
	protected static boolean isValidRefundViaGCD(SerialNumber serialNumber, BigInteger rma, Integer min){
		if(Objects.isNull(rma)){
			return false;
		}
		int gcd = serialNumber.gcd(new SerialNumber(rma)).intValue();
		return gcd >= min;
	}
	
	/**
	 * This is a helper method for Owatch's process refund process method.
	 * It calculates the exclusive or value between the specified serialNumber
	 * rma and compares it to the specified minimum value
	 * @param serialNumber serialNumber of the Owatch
	 * @param rma refund merchandise authorization number for the refund
	 * @param min minimum value in a comparison
	 * @return true if serialNumber.xor(rma) > min
	 */
	protected static boolean isValidRefundViaXor(SerialNumber serialNumber, BigInteger rma, Integer min){
		if(Objects.isNull(rma)){
			return false;
		}
		int xor = serialNumber.getSerialNumber().xor(rma).intValue();
		return xor > min;
	}
	
	/**
	 * This is a helper method to Otv's and Ophone's exchange process method
	 * Given a serial number and an upper bound, it returns the SerialNumber 
	 * from a specified set that 
	 * @param serialNumber serial number of the product to be exchanged
	 * @param upperBound inclusive upper bound of the subset to be considering
	 * @param compatibleSet navigable set of serial numbers compatible for exchange
	 * @return the serial number of the new product; null if no such product exists
	 */
	//TODO Complexity = 3!?
	protected static SerialNumber exchangeSerialViaAveragedSubset(
			SerialNumber serialNumber, 
			SerialNumber upperBound, 
			NavigableSet<SerialNumber> compatibleSet){
		if(serialNumber.compareTo(upperBound) > 0){
			return null;
		}
		
		NavigableSet<SerialNumber> subset = compatibleSet.subSet(serialNumber, false, upperBound, true);
		SerialNumber picked = getElementBelowSetAverage(subset);
		return (picked == null || serialNumber.compareTo(picked) >= 0) ? null : picked;
	}
	
	/**
	 * This is a helper method to exchangeSerialViaAveragedSubset which
	 * returns the element of a set of serial numbers that is directly
	 * less than the average value of the set
	 * @param set set of SerialNumbers on which the average is computed
	 * @return the serial number less than the average of the set; null if the set is empty
	 */
	private static SerialNumber getElementBelowSetAverage(NavigableSet<SerialNumber> set){
		if (set.isEmpty()){
			return null;
		}
		IntStream numsStream = set.stream().mapToInt(compSer -> compSer.getSerialNumber().intValue());
		BigInteger avg = BigInteger.valueOf(numsStream.sum() / set.size());
		return set.lower(new SerialNumber(avg));
	}
	
	/**
	 * Helper method to process refund requests in oPhones. Checks if the first BigInteger is
	 * equal to the second value shifted left anywhere from shiftFrom to shiftTo bits
	 * @param comparing BigInteger to compare
	 * @param shifter BigInteger that is shifted
	 * @param shiftFrom 
	 * @param shiftTo
	 * @return
	 */
	public static boolean equalsAnyShifts(BigInteger comparing, BigInteger shifter, int shiftFrom, int shiftTo){
		boolean equal = false;
		for(int i = shiftFrom; i <= shiftTo; i++){
			equal = equal || comparing.equals(shifter.shiftLeft(i));
		}
		return equal;
	}
}
