import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class MaxFinderInLastKTest {
	
	private MaxFinderInLastK<Integer> mfilk;
	private ArrayList<Integer> initial;
	

	@Before
	public void setUp() throws Exception {
		mfilk = new MaxFinderInLastK<>(3);
		mfilk.offer(1);
		
		initial = new ArrayList<>();
		initial.add(1);
		
	}

	/* - - - - - - - - - - - - - - - - - - - - -
	   - - - - - - - - CONSTRUCTOR - - - - - - - 
	   - - - - - - - - - - - - - - - - - - - - - */
	
	/* 
	 * Structured Basis: nominal, boolean condition is true
	 * Boundary: k > 0
	 */
	@Test
	public void testConstructor_Nominal() {
		MaxFinderInLastK<Integer> mf = new MaxFinderInLastK<>(1);
		
		assertEquals("itemArray was initialized", new ArrayList<Integer>(), mf.getItemArray());
		assertEquals("capacity was set to 1", 1, mf.getCapacity());
		assertEquals("maxIndex was set to 0", 0, mf.getNextIndex());
		assertEquals("nextIndex was set to 0", 0, mf.getNextIndex());
	}
	
	/* 
	 * Structured Basis
	 * Boundary: k < 0
	 * 		--> boolean is false
	 */
	@Test(expected = NegativeArraySizeException.class)
	public void testConstructor_KLessThan0(){
		new MaxFinderInLastK<>(-1);
		
		fail("This indicates no exception was caught");
	}
	
	/* Boundary: k == 0
	 * 		--> boolean is false
	 */
	@Test(expected = NegativeArraySizeException.class)
	public void testConstructor_KEqualTo0(){
		new MaxFinderInLastK<>(0);
		
		fail("This indicates no exception was caught");
	}
	
	/* - - - - - - - - - - - - - - - - - - - - -
	   - - - - - - - - - OFFER - - - - - - - - - 
	   - - - - - - - - - - - - - - - - - - - - - */
	/* Structured Basis
	 * Nominal, boolean condition is false
	 * 		x is not null
	 */
	@Test
	public void testOffer_nonNull(){
		MaxFinderInLastK<Integer> mf = new MaxFinderInLastK<>(3);
		ArrayList<Integer> arr = new ArrayList<>();
		
		assertTrue("1 is accepted in the structure", mf.offer(1));
		arr.add(1);
		assertEquals("mf's itemArray solely contains 1", arr, mf.getItemArray());
		assertEquals("mf's nextIndex is now 1", 1, mf.getNextIndex());
		assertEquals("mf's maxIndex is now 0, the index of the sole and largest element", 0, mf.getMaxIndex());
		
		assertTrue("0 is accepted in the structure", mf.offer(0));
		arr.add(0);
		assertEquals("mf's itemArray is [1 0]", arr, mf.getItemArray());
		assertEquals("mf's nextIndex is again updated to 2", 2, mf.getNextIndex());
		assertEquals("mf's maxIndex remains 0, the index of the still-largest element", 0, mf.getMaxIndex());
		
	}
	
	/*
	 * Structured basis, bad data
	 * boolean condition is true (x is null)
	 */
	@Test
	public void testOffer_null(){
		assertFalse("a null value is not accepted in the structure", mfilk.offer(null));
	}

	/* - - - - - - - - - - - - - - - - - - - - -
	   - - - - - - - SET ELEMENT - - - - - - - - 
	   - - - - - - - - - - - - - - - - - - - - - */
	/* Structured Basis
	 * Nominal cases, a boolean condition evaluates to true 
	 */
	@Test
	public void testSetElement_GoodData(){
		assertEquals("Initially: The 1st (index 1) of itemArray is not yet set", initial, mfilk.getItemArray());
		
		/* Boundary: index == itemArray.size()*/
		mfilk.test.testSetElement(1, 1);
		ArrayList<Integer> expectedNew = new ArrayList<>();
		expectedNew.add(1);
		expectedNew.add(1);
		
		assertEquals("The 1st element of itemArray is set to 1", expectedNew, mfilk.getItemArray());
	
		/* Boundary: index < itemArray.size() */
		mfilk.test.testSetElement(10, 1);
		ArrayList<Integer> expectedOverwrite = new ArrayList<>();
		expectedOverwrite.add(1);
		expectedOverwrite.add(10);
		
		assertEquals("The 1st element of itemArray is set to 10", expectedOverwrite, mfilk.getItemArray());
	}
	
	/*
	 * Boundary: index > itemArray.size()
	 * 		(but size < capacity)
	 * Bad Data -- index to set should never exceed size of array 
	 */
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSetElement_BadData(){
		//size == 0
		mfilk.test.testSetElement(2, 2);
		
		fail("This indicates that no exception was caught");
	}
	
	/* - - - - - - - - - - - - - - - - - - - - -
	   - - - - - - UPDATED MAX INDEX - - - - - - 
	   - - - - - - - - - - - - - - - - - - - - - */
	//Test names refer to the first and second comparisons (if and else if, respectively)
	/* 
	 * Boundary: maxIndex == currentIndex <-- initial condition is true
	 */
	@Test
	public void testUpdatedMaxIndex_Equal_NA(){
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(1); arr.add(2); arr.add(0);
	  //arr = [1 2 0]
		
		assertEquals("Treating index as being just overwritten, " +
		"the method searches itemArray for the new max, finding it in index 1",
				1, mfilk.test.testUpdatedMaxIndex(0, 0, arr));
	}
	
	/*
	 * Boundary: maxIndex > currentIndex
	 * Boundary: curr > max
	 * 		Branch coverage: else if is true
	 */
	@Test
	public void testUpdatedMaxIndex_Greater_Greater(){
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(0); arr.add(2); arr.add(1);	//arr = [0 2 1]
		
		assertEquals("The first if condition is not satisfied, but the if else is, so " +
				"currentIndex is returned", 1, mfilk.test.testUpdatedMaxIndex(2, 1, arr));
	}
	
	/*
	 * Boundary: maxIndex < currentIndex
	 * Boundary: curr == max
	 * 		Branch Coverage: else if is true
	 */
	@Test
	public void testUpdatedMaxIndex_Less_Equal(){
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(1); arr.add(1); arr.add(10);	//arr = [1 1 10]
		
		assertEquals("The first if condition is not satisfied, but the if else is, so " +
				"currentIndex is returned. Note: The true max is not technically determined, " +
				"as by induction, the result is guaranteed to be right in the program context",
				1, mfilk.test.testUpdatedMaxIndex(0, 1, arr));
	}
	
	/*
	 * Boundary: curr < max			(maxIndex < currentIndex)
	 * 		Branch Coverage: else if is false
	 * 						 else executed
	 */	
	@Test
	public void testUpdatedMaxIndex_Less_Less(){
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(2); arr.add(1); arr.add(0);	//arr = [1 1 0]
		
		assertEquals("The first if condition is not satisfied, nor is if else, so " +
				"maxIndex is returned", 0, mfilk.test.testUpdatedMaxIndex(0, 2, arr));
	}
	
	/* - - - - - - - - - - - - - - - - - - - - -
	   - - - - - - - FIND MAX INDEX- - - - - - - 
	   - - - - - - - - - - - - - - - - - - - - - */
	/* Nominal
	 * arr randomly distributed --> max in middle
	 */
	@Test
	public void testFindMaxIndex_mid(){
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(1); arr.add(2); arr.add(0); //arr = [1 2 0]
		
		assertEquals("The method correctly identifies the index of the maximum value as 1",
				1, mfilk.test.testFindMaxIndex(arr));
	}
	
	/*
	 * Array values monotonically decreasing --> max first
	 * Boundary: item at i < item at maxIndex
	 */
	@Test
	public void testFindMaxIndex_first(){
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(2); arr.add(1); //arr = [2 1]
		
		assertEquals("The method correctly identifies the index of the maximum value as 0",
				0, mfilk.test.testFindMaxIndex(arr));
		
		arr.add(0); //arr = [2 1 0]
		assertEquals("The method correctly identifies the index of the maximum value still as 0",
				0, mfilk.test.testFindMaxIndex(arr));
	}
	
	/*
	 * Array values monotonically increasing --> max last
	 *		Boundary: item at i > item at maxIndex
	 */	@Test
		public void testFindMaxIndex_last(){
			ArrayList<Integer> arr = new ArrayList<>();
			arr.add(0); arr.add(1); 
			
			//arr = [0 1]
			assertEquals("The method correctly identifies the index of the maximum value as the last: 1",
					1, mfilk.test.testFindMaxIndex(arr));
			
			arr.add(2); //arr = [0 1 2]
			assertEquals("The method correctly identifies the index of the maximum value still as the last: 2",
					2, mfilk.test.testFindMaxIndex(arr));
		}
	
	/*
	 * Boundary: item at i == item at maxIndex
	 */
	@Test
	public void testFindMaxIndex_equal(){
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(1); arr.add(2); arr.add(2); //arr = [1 2 2]
		
		assertEquals("When multiple indices both match the max value, the first index will be returned",
				1, mfilk.test.testFindMaxIndex(arr));
		
		arr.add(0); arr.add(2); //arr = [1 2 2 0]
		assertEquals("When multiple indices both match the max value, the first index will be returned",
				1, mfilk.test.testFindMaxIndex(arr));
		}
	
	/* Bad Data
	 * arr.size == 0
	 */
	@Test
	public void testFindMaxIndex_empty(){
		assertEquals("Calling the method on an empty array will return 0",
				0, mfilk.test.testFindMaxIndex(new ArrayList<Integer>()));
	}
	
	/* Bad Data
	 * arr is null
	 * Note: in the normal execution of the program, this is guaranteed
	 * 		to never happen (findMaxIndex only called with itemArray as
	 * 		input, itemArray is initialized in the constructor) and 
	 * 		thus is not tested for in the routine
	 */
	
	/* - - - - - - - - - - - - - - - - - - - - -
	   - - - - - - - INCREMENT INDEX - - - - - - 
	   - - - - - - - - - - - - - - - - - - - - - */
	/* Nominal
	 * Boundary: index < arrLength - 1
	 */
	@Test
	public void testIncrementIndex_less(){
		assertEquals("Since index is less than (arrLength - 1), the method returns one more than index",
				3, mfilk.test.testIncrementIndex(2, 4));
	}
	
	/*
	 * Boundary: index == arrLength - 1
	 */
	@Test
	public void testIncrementIndex_equal(){
		assertEquals("Since index equals (arrLength - 1), the method returns 0",
				0, mfilk.test.testIncrementIndex(3, 4));
	}
	
	/* Bad Data
	 * Boundary: index > arrLength - 1
	 * Should not occur. For good measure, set to reset back to 0
	 */
	@Test
	public void testIncrementIndex_greater(){
		assertEquals("Since index is greater than (arrLength - 1), though this shouldn't "
				+ "happen in normal program running, and something is wrong, the method returns 0",
				0, mfilk.test.testIncrementIndex(4, 4));
	}
	
	/* Bad Data
	 * arrLength == 0
	 */
	@Test
	public void testIncrementIndex_zero(){
		assertEquals("When the array length is 0, something is kind of wrong, but the method will return 0 "
				+ "(and the problem is handled in a different routine)", 0, mfilk.test.testIncrementIndex(4, 4));
	}
	
	/* - - - - - - - - - - - - - - - - - - - - -
	   - - - - - - - - - - MAX - - - - - - - - - 
	   - - - - - - - - - - - - - - - - - - - - - */
	/* Nominal
	 * Boundary: maxIndex < size 
	 */
	@Test
	public void testMax_less(){
		assertTrue(mfilk.getMaxIndex() < mfilk.getItemArray().size());
		assertTrue(mfilk.getItemArray().size() < mfilk.getCapacity());
		assertEquals("A structure with elements will return the maximum element, despite not being full", 
				1, (int)mfilk.max());
		
		mfilk.offer(2); mfilk.offer(0);
		assertTrue(mfilk.getMaxIndex() < mfilk.getItemArray().size());
		assertEquals("A structure with elements will return the maximum element", 
				2, (int)mfilk.max());
	}
	
	/*
	 * Boundary: maxIndex == size
	 */
	@Test
	public void testMax_equal(){
		MaxFinderInLastK<Integer> mf = new MaxFinderInLastK<>(3);

		assertTrue(mf.getMaxIndex() == mf.getItemArray().size());
		assertNull("An empty structure will return null when max is called", mf.max());
	}
	
	/*
	 * Stress test: Very large k, monotonically decreasing offerings
	 * 		non-number generic
	 */
	@Test
	public void testStress(){
		MaxFinderInLastK<String> mfStr = stressSetup();
		//mfStr's itemArray = ["ZZZZ", "ZZZ", "ZZ", "Z", "YYYY", "YYY", "YY", "Y",. . . "BBBB", "BBB", "BB", "B"]
		//		This is monotonically decreasing according to compareTo
		
		assertEquals("The very first element offered was the maximum: ZZZZ", "ZZZZ", mfStr.max());
		
		mfStr.offer("A");
		assertEquals("The previous max was overwritten by a smaller value. "
				+ "The new max is the next in the array: ZZZ", "ZZZ", mfStr.max());
		
		mfStr.offer("a");
		assertEquals("The previous max was overwritten by a greater value. "
				+ "The new max is the most recently offered item: a", "a", mfStr.max());
	}
	
	//Helper method which creates a large max finder
	private MaxFinderInLastK<String> stressSetup(){
		MaxFinderInLastK<String> mf = new MaxFinderInLastK<>(100);
		char c = 'Z';
		String last = "";
		for(int i = 0; i < 100; i++){
			if(i % 4 == 0){
				c--;
				last = "" + c + c + c + c;
			}
			mf.offer(last);
			last = last.substring(1);
		}
		return mf;
	}
}

//TODO better, bigger stress test
