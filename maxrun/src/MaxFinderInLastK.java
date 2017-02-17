import java.util.ArrayList;
import java.util.Objects;

/**	
 * Data Structure: MaxFinderInLast
 * EECS 293 - Assignment 8
 * @author Cassidy cmb195
 */
public class MaxFinderInLastK<T extends Comparable<T>> {

	/** Circular array to hold the most recently offered elements */
	private ArrayList<T> itemArray;
	
	/** The maximum capacity of the structure. Set in the constructor */
	private final int capacity;
	
	/** Stores the index of the maximum element currently stored in the structure */
	private int maxIndex;
	
	/** Stores the index in itemArray to next be (over)written with a call to offer */ 
	private int nextIndex;
	
	public Test test = new Test();


	/**
	 * This constructor sets the integer parameter k > 0, which will never be modified afterwards.
	 * Output: (none) new data structure with storage set to k numbers
	 * Complexity: Low	Running Time: O(1) 
	 * 
	 * @param k number specifying how many numbers may be stored in the data structure
	 * @throws NegativeArraySizeException if specified k <= 0
	 */
	public MaxFinderInLastK(int k) throws NegativeArraySizeException{

		if(k > 0){
			itemArray = new ArrayList<>(k);
			
			capacity = k;
			maxIndex = 0;
			nextIndex = 0;
		
		} else {
			throw new NegativeArraySizeException(k + " is not a valid size");
		}
	}


	/**
  	 * This method adds x to the data structure if possible and returns true in such cases
	 * Complexity: Low	Running Time: average case O(1), worst case O(k)
	 * 
	 * @param x item to add to the data structure
	 * @returns true if x is accepted
	 */
	public boolean offer(T x){
		if(Objects.isNull(x)){
			return false;		
		}
	
		setElement(x, nextIndex);
		maxIndex = updatedMaxIndex(maxIndex, nextIndex, itemArray);
		nextIndex = incrementIndex(nextIndex, capacity);
		
		return true;
	}
	
	/**
	 * This helper method to offer() sets the given element at the specified index
	 * in the global field itemArray
	 * 
	 * @param element object to be places in itemArray
	 * @param index location in itemArray where it is to be placed
	 */
	private void setElement(T element, int index){
		//TODO: ensure array size <= capacity
		if(index == itemArray.size()){
			itemArray.add(element);
		} else if (index < itemArray.size()){
			itemArray.set(index, element);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * This helper method to offer() determines the maximum value in the array given indices of 
	 * the newest addition to the array and the maximum value prior to the new addition
	 * Complexity: Medium	Running Time: average case O(1), worst case O(k)
	 * 
	 * @param max index of the previous maximum element in the array
	 * @param current index to which a new item was just assigned
	 * @param array array being manipulated
	 * @returns index of the current maximum value in array
	 */
	private int updatedMaxIndex(int maxIndex, int currentIndex, ArrayList<T> array){
		if(maxIndex == currentIndex){
			return findMaxIndex(array);
			
		} else if (array.get(currentIndex).compareTo(array.get(maxIndex)) >= 0){
			return currentIndex;
			
		} else {
			return maxIndex;
		}
	}

	/**
	 * This helper method to offer() finds the maximum value in the given array	
	 * Complexity: Medium		Running Time: O(k)
	 * 
	 * @param arr array in which to search
	 * @returns Index of the largest element in the global itemArray
	 */
	private int findMaxIndex(ArrayList<T> arr){
		int maxIndex = 0;
		int i = 0;
		while(i < arr.size()){
			if(arr.get(i).compareTo(arr.get(maxIndex)) > 0){
				maxIndex = i;
			}
			i++;
		}
		return maxIndex;
	}

	/**
	 * This helper method to offer() increments the given index variable in 
	 * relation to the specified array length, accounting for wraparound
	 * Complexity: Low		Running Time: O(1)
	 * 
	 * @param index current index in array
	 * @param arrLength length of array being traversed
  	 * @returns next index to be examined
	 */
	private int incrementIndex(int index, int arrLength){
		if(index >= arrLength - 1){
			return 0;
		} else {
			return index + 1;
		}	
	}


	/**
	 * This method returns the maximum of the last k offered elements. If the data structure contains 
	 * fewer than k elements, it returns the maximum element offered so far.
	 * Complexity: Low	Running Time: O(1)
	 * 
	 * @returns Maximum of the last k items added to the structure
	 */
	public T max(){
		if(maxIndex < itemArray.size() && maxIndex >= 0){
			return itemArray.get(maxIndex);			
		} else {				
			return null;		
		}	
	}
	
	
	/* GETTERS for testing purposes */
	public ArrayList<T> getItemArray(){
		return itemArray;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public int getMaxIndex(){
		return maxIndex;
	}
	
	public int getNextIndex(){
		return nextIndex;
	}
	
	/**
	 * This nested class is intended for testing purposes and provides access to
	 * private fields and methods inside MaxFinderInLastK
	 * @author Cassidy
	 */
	public class Test{		
		//This method changes a global variable in the outer class
		//TODO Solution: change state via param
		public void testSetElement(T element, int index){
			setElement(element, index);
		}
		
		public int testUpdatedMaxIndex(int maxIndex, int currentIndex, ArrayList<T> array){
			return updatedMaxIndex(maxIndex, currentIndex, array);
		}
		
		public int testFindMaxIndex(ArrayList<T> arr){
			return findMaxIndex(arr);
		}
		
		public int testIncrementIndex(int index, int arrLength){
			return incrementIndex(index, arrLength);
		}
	}

}
