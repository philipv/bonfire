package com.bonfire.source;

import java.util.Observable;
import java.util.Observer;

public abstract class UpdateReceiver<T> extends Observable {
	public void initialize(){
		addObserver(getObserver());
	}
	
	public abstract Observer getObserver();
	public abstract T convert(Object data);
	
	protected void processUpdate(Object update) {
		T processedUpdate = convert(update);
		if(processedUpdate!=null){
			setChanged();
			notifyObservers(processedUpdate);
		}
	}
}
