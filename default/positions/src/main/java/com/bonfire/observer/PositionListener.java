package com.bonfire.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import com.bonfire.data.Position;
import com.bonfire.task.factory.TaskFactory;

public class PositionListener implements Observer {

	private ConcurrentHashMap<String, Double> positions;

	public PositionListener(ConcurrentHashMap<String, Double> positions){
		this.positions = positions;
	}
	
	public void update(Observable observable, Object updatedObject) {
		try{
			TaskFactory.createTaskFromConsole((Position)updatedObject, positions).run();
		} catch(ClassCastException ex){
			System.out.println("Wrong update received on the " + getClass().getCanonicalName());
		}
	}

}
