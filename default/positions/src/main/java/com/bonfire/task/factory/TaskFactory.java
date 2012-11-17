package com.bonfire.task.factory;

import java.util.concurrent.ConcurrentHashMap;

import com.bonfire.task.PositionProcessTask;

public class TaskFactory {
	
	public static Runnable createTaskFromConsole(String[] components, ConcurrentHashMap<String, Double> positions){
		String currency = components[0].toUpperCase();
		double value = Double.valueOf(components[1]);
		return new PositionProcessTask(currency, value, positions);
	}
}
