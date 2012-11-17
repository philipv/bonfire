package com.bonfire.observer;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

public class ConsoleListener implements Observer {

	private static final String SEPARATOR = " ";

	public void update(Observable observable, Object updatedObject) {
		String newPosition = (String) updatedObject;
		String[] positionalComponents = StringUtils.split(newPosition, SEPARATOR);
		
	}

}
