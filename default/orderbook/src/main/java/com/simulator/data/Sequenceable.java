package com.simulator.data;

import java.util.concurrent.atomic.AtomicLong;

public class Sequenceable<E> implements Comparable<Sequenceable<E>>{
	private static AtomicLong sequencer = new AtomicLong(0);
	
	private Long sequence;
	private E entry;
	
	public Sequenceable(E entry){
		this.entry = entry;
		this.sequence = sequencer.getAndIncrement();
	}

	public Long getSequence() {
		return sequence;
	}

	public E getEntry() {
		return entry;
	}

	@Override
	public int compareTo(Sequenceable<E> o) {
		return getSequence().compareTo(o.getSequence());
	}
}
