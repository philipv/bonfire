package com.bonfire.task;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class EvaluationTask implements Runnable {
	private final ConcurrentHashMap<String, Double> positions;

	public EvaluationTask(ConcurrentHashMap<String, Double> positions) {
		this.positions = positions;
	}

	public void run() {
		System.out.println("The positions are:");
		for(Entry<String, Double> positionEntry:positions.entrySet()){
			System.out.println(positionEntry.getKey() + " " + positionEntry.getValue());
		}
		System.out.println();
	}
}
