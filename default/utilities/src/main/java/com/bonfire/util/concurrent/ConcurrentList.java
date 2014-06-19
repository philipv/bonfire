package com.bonfire.util.concurrent;

import java.util.LinkedList;

public class ConcurrentList<T> {
	private LinkedList<T> elements = new LinkedList<T>();
	private Object lock = new Object();
	private volatile T tail;//last
	private volatile T head;//first

	public void add(T element) throws InterruptedException{
		if(tail!=head){
			head = element;
			elements.addLast(element);
		}else{
			if(tail==null){
				head = element;
				tail = element;
				elements.addLast(element);
			}else{
				synchronized (lock) {
					while(true)
						lock.wait();
				}
			}
				
		}
	}
}
