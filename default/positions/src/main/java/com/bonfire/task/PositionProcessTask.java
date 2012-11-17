package com.bonfire.task;

import java.util.HashMap;

public class PositionProcessTask implements Runnable {
	private String currency;
	private double value;
	private HashMap<String, Double> positions;
	
	public PositionProcessTask(String currency, double value, HashMap<String, Double> positions){
		this.currency = currency;
		this.value = value;
		this.positions = positions;
	}
	
	public void run() {
		Double total = positions.get(currency);
		total += value;
		if(total!=0){
			positions.put(currency, total);
		}else{
			positions.remove(currency);
		}
	}
	
}
