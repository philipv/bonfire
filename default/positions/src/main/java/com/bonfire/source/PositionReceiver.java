package com.bonfire.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bonfire.observer.ConsoleListener;

public class PositionReceiver extends Observable{
	
	public static void main(String[] args){
		final ConcurrentHashMap<String, Double> positions = new ConcurrentHashMap<String, Double>();
		ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		threadPoolExecutor.scheduleAtFixedRate(new Runnable() {
			
			public void run() {
				System.out.println("The positions are:");
				for(Entry<String, Double> positionEntry:positions.entrySet()){
					System.out.println(positionEntry.getKey() + " " + positionEntry.getValue());
				}
			}
		}, 60, 60, TimeUnit.SECONDS);
		ConsoleListener consoleListener = new ConsoleListener(positions);
		PositionReceiver positionReceiver = new PositionReceiver();
		positionReceiver.addObserver(consoleListener);
		positionReceiver.start();
		threadPoolExecutor.shutdown();
	}

	private void start() {
		BufferedReader consoleReader = null;
		try{
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
			String inputString = null;
			
			while(true){
				inputString = consoleReader.readLine();
				if(inputString.equals("quit"))
					break;
				setChanged();
				notifyObservers(inputString);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				consoleReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
