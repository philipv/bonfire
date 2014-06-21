package com.simulator.orderbook;

import java.util.Comparator;

public class ReverseComparator<T extends Comparable<T>> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		return o2!=null? o2.compareTo(o1) : (o1==null?0:-1);
	}

}
