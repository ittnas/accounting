package dataStructures;

import java.util.Collections;
import java.util.List;

import java.util.LinkedList;

public class SortedList<E extends Comparable<? super E>> extends LinkedList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<E> getRange(E start, E end) {
		
		int startIndex;
		try {
		startIndex = Collections.binarySearch(this, start);
		} catch (NullPointerException e) {
			startIndex = 0;
		}
		if(startIndex < 0) {
			startIndex = -startIndex -1;
		}
		
		int endIndex;
		try {
			endIndex = Collections.binarySearch(this, end);
			} catch (NullPointerException e) {
				endIndex = size();
			}
		if(endIndex < 0) {
			endIndex = -endIndex -1;
		}
		if(endIndex < startIndex) {
			int temp = endIndex;
			endIndex = startIndex;
			startIndex = temp;
		}
		return subList(startIndex, endIndex);
	}

	public boolean add(E element) {
		modCount += 1;
		int index = Collections.binarySearch(this, element);
		if (index < 0) {
			super.add(-index - 1, element);
			return true;
		}
		if (index >= 0) {
			super.add(index, element);
			return true;
		}
		return false;
	}

	public void add(int index, E element) {
		add(element);
	}

}
