package com.bonfire.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.bonfire.task.factory.TaskFactory;

public class ConsoleListener implements Observer {

	private static final String SEPARATOR = " ";
	private ConcurrentHashMap<String, Double> positions;

	public ConsoleListener(ConcurrentHashMap<String, Double> positions){
		this.positions = positions;
	}
	
	public void update(Observable observable, Object updatedObject) {
		String newPosition = (String) updatedObject;
		String[] positionalComponents = StringUtils.split(newPosition, SEPARATOR);
		TaskFactory.createTaskFromConsole(positionalComponents, positions).run();
	}

}
