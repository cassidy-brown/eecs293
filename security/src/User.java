import java.util.Objects;

/**
 * This class represents a user--identified by an IP address--and
 * stores the ip, host name, and number of invalid access attempts
 * with getters
 * @author Cassidy
 *
 */
public class User {
	
	private final String ip;
	private String host;
	private int attempts = 0;
	
	public User(String ip){
		this(ip, "", 0);
	}
		
	/**
	 * Builds a new User
	 * @param ip ip of user
	 * @param host host name of user
	 * @param attempts number of invalid attempts user has made
	 */
	public User(String ip, String host, int attempts){
		this.ip = ip;
		this.host = host;
		addToAttempts(attempts);
	}
	
	/**
	 * The best identifier for the user
	 * @return host if it is not empty, otherwise ip
	 */
	public String getName(){
		if(host.isEmpty()){
			return ip;
		} else {
			return host;
		}
	}
	
	public String getIP(){
		return ip;
	}
	
	public String getHost(){
		return host;
	}
	
	/**
	 * Will only set host if host is currently empty and new host is null
	 * @param newHost host to be set
	 * @return true if new host was set
	 */
	public boolean offerHost(String newHost){
		if(host.isEmpty() && newHost != null){
			host = newHost;
			return true;
		} else {
			return false;
		}
	}
	
	public int getAttempts(){
		return attempts;
	}
	
	/** 
	 * Adds the specified number to the current attempts field
	 * @param more number to add
	 */
	public void addToAttempts(int more){
		if(more > 0){
			attempts += more;
		}
	}
	
	public boolean update(String host, int more){
		boolean offer = offerHost(host);
		addToAttempts(more);
		
		return offer;
	}
	
	//Equivalence is based on IP address
	@Override
	public boolean equals(Object other){
		if(!(other instanceof User)){
			return false;
		}
		
		User o = (User)other;
		return ip.equals(o.getIP());
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(ip);
	}
	
	@Override
	public String toString(){
		return ip + " (" + host + "): " + attempts;
	}
}
