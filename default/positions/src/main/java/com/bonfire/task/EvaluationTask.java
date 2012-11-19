package com.bonfire.task;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Task for printing the aggregated positions
 */
public class EvaluationTask implements Runnable {
	private final ConcurrentHashMap<String, Double> positions;

	public EvaluationTask(ConcurrentHashMap<String, Double> positions) {
		this.positions = positions;
	}

	public void run() {
		System.out.println("The positions on " + new Date() + " are:");
		for(Entry<String, Double> positionEntry:positions.entrySet()){
			System.out.println(positionEntry.getKey() + " " + positionEntry.getValue());
		}
		System.out.println();
	}
}
