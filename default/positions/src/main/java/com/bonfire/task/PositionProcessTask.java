package com.bonfire.task;

import java.util.concurrent.ConcurrentHashMap;

import com.bonfire.data.Position;

/*
 * Task for processing the position updates
 */
public class PositionProcessTask implements Runnable {
	private Position position;
	private ConcurrentHashMap<String, Double> positions;
	
	public PositionProcessTask(Position position, ConcurrentHashMap<String, Double> positions){
		this.position = position;
		this.positions = positions;
	}
	
	public void run() {
		Double total = positions.get(position.getCurrency());
		if(total==null)
			total = new Double(0.0);
		total += position.getValue();
		if(total!=0){
			positions.put(position.getCurrency(), total);
		}else{
			positions.remove(position.getCurrency());
		}
	}
	
}
