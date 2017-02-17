package airville;

/**
 * This abstract class only contains constants. 
 * It holds all of the "magic numbers" of the Airville game,
 * allowing for easy access to adjust costs, frequencies, etc.
 * @author Cassidy
 */
public abstract class Settings {
	
	
	/* - - - - - BANK.CURRENCY - - - - - */
	//Cost of each object using MONEY
	public static final double M_AGENT = 1.99;
	public static final double M_SUPER = 4.99;
	public static final double M_IN_PERSON = 3.99;
	public static final double M_AUTO = 1.99;
	
	//Cost of each object using POINTS
	public static final double P_AGENT = 2000;
	public static final double P_SUPER = 5000;
	public static final double P_IN_PERSON = 4000;
	public static final double P_AUTO = 2000;
	
	//Cost of each object using DIAMONDS
	public static final double D_AGENT = 2;
	public static final double D_SUPER = 5;
	public static final double D_IN_PERSON = 4;
	public static final double D_AUTO = 2;
	
	
	/* - - - - - - - - - - GAMEENVIRONMENT - - - - - - - - - - */
	/*
	 * The maximum amount of time (in seconds) that a checked-in passenger is stored
	 * before being discarded. Used for maintaining accurate timeQ to track bonuses
	 */
	public static final int MAX_TIME_STORED = 300;
	
	/*
	 * Number of check-ins that need to be completed within MAX_TIME_STORED
	 * in order for the player to win a diamond
	 */
	public static final int CHECK_IN_FOR_DIAMOND_THRESH = 10;
	
	//The base number of points checking in a passenger is worth
	public static final double BASE_POINTS = 100.0;
	
	
	
	/* - - - - - - - - - - EMPLOYEE - - - - - - - - - - */
	// Time multiplier of an agent
	// m < 1.0 && m > SUPERVISOR_MULTIPLIER
	public static final double AGENT_MULTIPLIER = 0.8;
	//Time multiplier of a supervisor
	// m < l.0 && m < AGENT_MULTIPLIER
	public static final double SUPERVISOR_MULTIPLIER = 0.6;

	/* - - - - - EMPLOYEE / ASSIGNMENT - - - - - */
	/* The maximum number of automated counters an employee may be assigned to */
	public static final int AUTO_ASSIGNMENT_THRESHOLD = 3;
	
	
	/* - - - - - - - - - - PASSENGER - - - - - - - - - - */
	/*In calculating a group's multiplier, this value is taken to the 
	 * power of n where n is the size of the group, thus causing 
	 * processing time for larger groups to be more efficient 
	 * g < 1.0
	 */
	public static final double GROUP_SIZE_MULT = 0.95;
	
	/* - - - - - PASSENGER.CIRCUMSTANCE - - - - - */
	//Constants related to how a circumstance affects time to check in
	public static final double FF_TIME = 1.0;
	public static final double EB_TIME = 2.0;
	public static final double RR_TIME = 2.0;
	public static final double CB_TIME = 2.0;
	//Constants denoting how frequently a circumstance occurs
	public static final double FF_FREQ = 0.2;
	public static final double EB_FREQ = 0.3;
	public static final double RR_FREQ = 0.1;
	public static final double CB_FREQ = 0.1;
	
}
