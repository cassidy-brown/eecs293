
/** 
 * This exception is thrown from SecurityLogParser when an IP address is found to be invalid
 * @author Cassidy
 *
 */
public class InvalidIPException extends Exception {

	//Message: What is wrong with the IP
	private String message;
	//Str: Relevant portion of IP
	private String str;
	
	public InvalidIPException(String message, String str){
		this.message = message;
		this.str = str;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getProblemString(){
		return str;		
	}
}
