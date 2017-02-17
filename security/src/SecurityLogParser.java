import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SecurityLogParser {
	
	private static final int DEFAULT_THRESHOLD = 3;
	private static final String FAILED_LOGIN_LINE = "Failed logins from:";
	private static final String ILLEGAL_USERS_LINE = "Illegal users from:";
	private static Logger log = Logger.getLogger("SecurityLogParser");
	
	private final int threshold;
	//Flags marking when all data has been encountered
	private boolean failedLoginsFound;
	private boolean illegalUsersFound;
	private boolean streamEnded;
	
	//Reader for input
	private BufferedReader br;
	
	//Data structures for storing user information
	private Map<String, User> userMap;
	private List<String> blackList;
	
	/**
	 * An enumerated type representing the types of lines one encounters in an security log
	 * relevant to searching for invalid login actions
	 */
	private enum LineType{
		FailedLogin, IllegalLogin, EndOfStream, Boring;
	}
	
	/** 
	 * This constructor sets the threshold for invalid logins at which to black list a user
	 * to the specified value
	 * @param thresh blacklist threshold
	 */
	private SecurityLogParser(int thresh){
		if(thresh < 0){
			threshold = 0;
		} else {
			threshold = thresh;
		}
		blackList = Collections.emptyList();
		
		buildBlackList();
	}
	
	/**
	 * Finds the threshold for the blacklist from main args
	 * @param args String array passed from main
	 * @return the first element of the specified string (if valid) or the default threshold
	 */
	//3
	private static int findThreshold(String[] args){
		int thresh = DEFAULT_THRESHOLD;
		if(args.length > 0){
			try{
				thresh = Integer.parseInt(args[0]);
			}catch(NumberFormatException n){
				log.info("First argument was not a number. Threshold set to default.");
			}
		}
		//TODO log if thresh <= 0
		return thresh > 0 ? thresh : 0;
	}
	
	/**
	 * This method runs everything.
	 */
	//1 + stream
	private void buildBlackList(){
		failedLoginsFound = false;
		illegalUsersFound = false;
		streamEnded = false;
		br = new BufferedReader(new InputStreamReader(System.in)); 
		userMap = new HashMap<>();
		
		while(!done()){
			LineType typeOfLine = findNextInterestingLine();
			handleInterestingLine(typeOfLine);
		}
		
		blackList = userMap.values().stream()
							.filter(v -> v.getAttempts() >= threshold)
							.map(v -> v.getName())
							.collect(Collectors.toList());
		
	}
		
	/**
	 * Determines what type of line the given string is
	 * @param s line read from InputStream
	 * @return LineType enum based on value of s
	 */
	//3
	private LineType lineType(String s){
		if(s == null){
			return LineType.EndOfStream;
		} else if(s.contains(FAILED_LOGIN_LINE)){
			return LineType.FailedLogin;
		} else if(s.contains(ILLEGAL_USERS_LINE)){
			return LineType.IllegalLogin;
		} else {
			return LineType.Boring;
		}
	}
	
	/**
	 * Reads lines of br, checking their type and returning the type when the line is not boring
	 * @return type of the not-boring line found
	 */
	//2
	private LineType findNextInterestingLine(){
		String line = "";
		LineType typeOfLine = LineType.Boring;
		while(typeOfLine == LineType.Boring){
			try {
				line = br.readLine();
				typeOfLine = lineType(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.warning("An I/O Exception was thrown while reading the input stream. Now what?");
			}
		}
		return typeOfLine;
	}
	
	/**
	 * Calls a further method based on line type
	 * @param typeOfLine the LineType of the line in question
	 */
	//2
	private void handleInterestingLine(LineType typeOfLine){
		assert(typeOfLine != null);
		assert(typeOfLine != LineType.Boring);
		if(typeOfLine == LineType.FailedLogin){
			handleFailedLogins();
		} else if(typeOfLine == LineType.IllegalLogin){
			handleIllegalLogins();
		} else {
			streamEnded = true;
		}
	}
	
	/**
	 * This method sets failedLoginsFound to true and updates the blackList
	 */
	//0
	private void handleFailedLogins(){
		failedLoginsFound = true;
		traverseEntries();
	}
	
	/**
	 * This method sets illegalLoginsFound to true and updates the black list
	 */
	//0
	private void handleIllegalLogins(){
		illegalUsersFound = true;
		traverseEntries();
	}
	
	/**
	 * This method reads lines until it encounters an empty (all whitespace line) 
	 * and parses entries, adding them to the user map
	 */
	//3
	private void traverseEntries(){
		while(true){
			String line = "";
			try {
				line = br.readLine();
			} catch (IOException e) {
				log.warning("An I/O Exception was thrown while reading the input stream. Now what?");
			}
			if(line == null || allWhite(line)){
				break;
			} else {
				parseLine(line);
			}
		}
	}
	
	/**
	 * Determines if a String consists entirely of whitespace
	 * @param s String to examine
	 * @return true if the line contains only whitespace
	 */
	//2
	private boolean allWhite(String s){
		assert(s != null);
		for(int i = 0; i < s.length(); i++){
			if(!Character.isWhitespace(s.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method gleans the necessary information--ip, host, and attempts--
	 * from a single line. This information is then placed in the user map.
	 * @param line String to parse
	 */
	//1
	private void parseLine(String line){
		assert(line != null);
		try{
			String ip = determineIP(line);
			String host = determineHost(line);
			int numTimes = determineNumTimes(line);
			placeInUserMap(ip, host, numTimes);
		} catch (InvalidIPException e){
			log.warning("IP address was invalid (" + e.getMessage() + ")\n\tEntry not parsed further");
		} catch (ParseException p){
			log.warning(p.getMessage() + "\n\tEntry not parsed further");
		}
	}
	
	/**
	 * This method pulls the ip address from an input line
	 * @param line String from which to pull address
	 * @return String representation of IP
	 * @throws InvalidIPException if IP is invalid, having the wrong number of bytes or an value exceeding a byte's range
	 */
	//3
	private String determineIP(String line) throws InvalidIPException{
		assert(line != null);
		StringBuilder whole = new StringBuilder();
		StringBuilder part = new StringBuilder();
		int i = startOfIP(line);
		int numBytes = 0;

		while(true){
			if(!Character.isDigit(line.charAt(i))){
				numBytes++;
				//TODO validateByte()
				if(isValidByte(part.toString())){
					whole.append(part);
					whole.append('.');
				} else {
					throw new InvalidIPException("Invalid Byte in IP Address", part.toString());
				}
				part = new StringBuilder();
			} else {
				part.append(line.charAt(i));
			}
			if(!(isPartOfIP(line, i))){
				break;
			}

			i++;
		}
		whole.delete(whole.length()-1, whole.length());
		validateNumBytes(numBytes, whole.toString());
		
		return whole.toString();
	}
	
	/**
	 * This method finds the index of the first digit of the IP address
	 * Presumes IP is first digit in String
	 * @param line String in which IP is
	 * @return index of first digit of IP
	 * @throws InvalidIPException 
	 */
	//2
	private int startOfIP(String line) throws InvalidIPException{
		assert(line != null);
		int i = 0;
		while(i < line.length() && !Character.isDigit(line.charAt(i))){
			i++;
		}
		if(i == line.length()){
			throw new InvalidIPException("No digits present in line", line);
		}
		return i;
	}
	
	/**
	 * This method tests if a string representation of a byte is valid
	 * @param byteAsStr String representation of byte
	 * @return true if String can be parsed to int and is within the valid byte range
	 */
	//1
	private boolean isValidByte(String byteAsStr){
		assert(byteAsStr != null);
		try{
			int b = Integer.parseInt(byteAsStr);
			return 0 <= b && b <= 255;
		} catch (NumberFormatException n){
			return false;
		}
	}	
	
	/**
	 * This method tests if a character could be a part of an IP address
	 * @param c character to test
	 * @return true if c is a digit or period 
	 */
	//2
	private boolean isPartOfIP(String line, int i){
		assert(line != null);
		if(i < line.length()){
			char c = line.charAt(i);
			return Character.isDigit(c) || c == '.';
		} else {
			return false;
		}
	}
	
	private boolean validateNumBytes(int num, String ip) throws InvalidIPException{
		if(num != 4){
			throw new InvalidIPException("Incorrect number of bytes in IP address", ip);
		} else {
			return true;
		}
	}
	
	/**
	 * @param line String in which to find host
	 * @return String representation of host, or an empty string if it is not present
	 */
	//3
	private String determineHost(String line){
		assert(line != null);
		int indexOpening = line.indexOf('(') + 1;
		int indexClosing = line.indexOf(')');
		if(indexOpening == -1 || indexClosing == -1 || indexClosing < indexOpening){
			return "";
		} else {
			return line.substring(indexOpening, indexClosing);
		}
	}
	
	/**
	 * In a line, the number of times something failed
	 * @param line String to examine
	 * @return listed number of times a user failed attempts
	 * @throws ParseException 
	 */
	//3
	private int determineNumTimes(String line) throws ParseException{
		assert(line != null);
		int indexPast = line.indexOf(" time");
		if(indexPast == -1){
			throw new ParseException("Times attempted was parsed incorrectly", indexPast);
		}

		int i = indexPast;
		while(!Character.isDigit(line.charAt(i))){
			i--;
		}
	
		String num = "";
		while(Character.isDigit(line.charAt(i))){
			num = line.charAt(i) + num;
			i--;
		}
		
		return Integer.parseInt(num);
	}
	
	/**
	 * Examines userMap and for existing User with specified ip. If the user does not exist,
	 * a new user object is created and otherwise it is updated before being replaced in the table
	 * @param ip IP address of user to be placed
	 * @param host host address of user to be placed
	 * @param attempts number of attempts made of user to be placed
	 */
	//1
	private void placeInUserMap(String ip, String host, int attempts){
		assert(ip != null);
		assert(host != null);
		assert(attempts >= 0);
		User u = userMap.get(ip);
		if(u == null){
			u = new User(ip, host, attempts);
		} else {
			u.update(host, attempts);
		}
		userMap.put(ip, u);
	}

	/**
	 * Using the global black list, prints in the assigned manner
	 */
	//3
	public void printBlackList(){
		String fiveSpaces = "     ";
		int addedLength = ", \\".length();
		StringBuilder currentString = new StringBuilder(fiveSpaces);
		for(int i = 0; i < blackList.size(); i++) {
			String n = blackList.get(i);
			if(currentString.length() + n.length() + addedLength < 80){
				currentString.append(n);
				currentString.append(", ");
			} else {
				currentString.append("\\");
				System.out.println(currentString);
				currentString = new StringBuilder(fiveSpaces);
				currentString.append(n);
				currentString.append(", ");
			}	
			if(i == blackList.size() - 1){
				currentString.delete(currentString.length() - 2, currentString.length());
				System.out.println(currentString);
			}
		}
	}
	
	/**
	 * This method takes a string (readLine of an inputStream), and two booleans
	 * and if both booleans are true or the String is null, this indicates that for
	 * our purposes, we are done reading the stream
	 * @return true if streamEnded is true or both failedLoginsFound and illegalLogins found is true
	 */
	//2
	private boolean done(){
		return streamEnded || (failedLoginsFound && illegalUsersFound);
	}
	
	
	public static void main(String[] args) throws FileNotFoundException{
		System.setIn(new FileInputStream("test.txt"));
		
		SecurityLogParser parser = new SecurityLogParser(findThreshold(args));
		parser.printBlackList();		

	}
	
}