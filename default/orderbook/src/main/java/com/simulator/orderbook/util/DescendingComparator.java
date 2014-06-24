package com.simulator.orderbook.util;

import java.util.Comparator;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Sequenceable;

public class DescendingComparator<T extends Sequenceable<Quote>> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		int compareResult = o2.getEntry().compareTo(o1.getEntry());
		if(compareResult==0){
			compareResult = o1.compareTo(o2);
		}
		return compareResult;
	}

}
