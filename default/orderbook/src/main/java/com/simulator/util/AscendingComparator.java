package com.simulator.util;

import java.util.Comparator;

import com.simulator.data.Sequenceable;

public class AscendingComparator<T extends Sequenceable<E>, E extends Comparable<E>> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		int compareResult = o1.getEntry().compareTo(o2.getEntry());
		if(compareResult==0){
			compareResult = o1.compareTo(o2);
		}
		return compareResult;
	}

}
