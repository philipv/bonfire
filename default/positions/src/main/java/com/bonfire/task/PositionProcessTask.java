package com.bonfire.task;

import java.util.concurrent.ConcurrentHashMap;

public class PositionProcessTask implements Runnable {
	private String currency;
	private double value;
	private ConcurrentHashMap<String, Double> positions;
	
	public PositionProcessTask(String currency, double value, ConcurrentHashMap<String, Double> positions){
		this.currency = currency;
		this.value = value;
		this.positions = positions;
	}
	
	public void run() {
		Double total = positions.get(currency);
		if(total==null)
			total = new Double(0.0);
		total += value;
		if(total!=0){
			positions.put(currency, total);
		}else{
			positions.remove(currency);
		}
	}
	
}
