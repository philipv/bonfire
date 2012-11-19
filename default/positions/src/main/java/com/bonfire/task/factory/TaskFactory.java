package com.bonfire.task.factory;

import java.util.concurrent.ConcurrentHashMap;

import com.bonfire.data.Position;
import com.bonfire.task.PositionProcessTask;

public class TaskFactory {
	
	public static Runnable createTaskFromConsole(Position position, ConcurrentHashMap<String, Double> positions){
		return new PositionProcessTask(position, positions);
	}
}
