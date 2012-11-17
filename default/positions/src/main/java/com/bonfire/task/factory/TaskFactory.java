package com.bonfire.task.factory;

import java.util.HashMap;

import com.bonfire.task.PositionProcessTask;

public class TaskFactory {
	private HashMap<String, Double>[] bucketedPositions;
	
	public TaskFactory(HashMap<String, Double>[] bucketedPostions){
		this.bucketedPositions = bucketedPostions;
	}
	
	public Runnable createTaskFromConsole(String[] components){
		String currency = components[0].toUpperCase();
		double value = Double.valueOf(components[1]);
		return new PositionProcessTask(currency, value, bucketedPositions[getBucket(currency)]);
	}
	
	private int getBucket(String currency){
		return currency.hashCode()%bucketedPositions.length;
	}
	
}
