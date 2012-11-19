package com.bonfire.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import com.bonfire.data.Position;
import com.bonfire.factory.FactoryUtility;

/*
 * This class is responsible for:
 *  - Creating a task for the update.
 *  - Process the task in a synchronous or asynchronous manner (we can make it async by passing task to a thread pool)
 */
public class PositionListener implements Observer {

	private ConcurrentHashMap<String, Double> positions;
	private FactoryUtility factoryUtility = new FactoryUtility();

	public PositionListener(ConcurrentHashMap<String, Double> positions){
		this.positions = positions;
	}
	
	public void update(Observable observable, Object updatedObject) {
		try{
			Runnable task = factoryUtility.createTaskFromConsole((Position)updatedObject, positions);
			task.run();
		} catch(ClassCastException ex){
			System.out.println("Wrong update received on the " + getClass().getCanonicalName());
		}
	}

}
