package com.bonfire.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bonfire.observer.ConsoleListener;
import com.bonfire.task.EvaluationTask;

public class PositionReceiver extends Observable{
	
	public static void main(String[] args) throws InterruptedException{
		final ConcurrentHashMap<String, Double> positions = new ConcurrentHashMap<String, Double>();
		Thread consoleReceiverThread = new ConsoleReceiverThread(positions);
		consoleReceiverThread.join();
		consoleReceiverThread.start();
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
	
	private static class ConsoleReceiverThread extends Thread{
		private static final int PERIOD = 20;
		private ConcurrentHashMap<String, Double> positions;
		
		public ConsoleReceiverThread(ConcurrentHashMap<String, Double> positions) {
			this.positions = positions;
		}
		@Override
		public void run() {
			ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);
			threadPoolExecutor.scheduleAtFixedRate(new EvaluationTask(positions), PERIOD, PERIOD, TimeUnit.SECONDS);
			ConsoleListener consoleListener = new ConsoleListener(positions);
			PositionReceiver positionReceiver = new PositionReceiver();
			positionReceiver.addObserver(consoleListener);
			positionReceiver.start();
			threadPoolExecutor.shutdown();
		}
		
	}

}
