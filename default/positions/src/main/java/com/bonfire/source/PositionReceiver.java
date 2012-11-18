package com.bonfire.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bonfire.observer.PositionListener;
import com.bonfire.task.EvaluationTask;

public class PositionReceiver extends Observable{
	private static final int PERIOD = 20;
	
	public static void main(String[] args) throws InterruptedException, IOException{
		final ConcurrentHashMap<String, Double> positions = new ConcurrentHashMap<String, Double>();
		PositionListener positionListener = new PositionListener(positions);
		ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		threadPoolExecutor.scheduleAtFixedRate(new EvaluationTask(positions), PERIOD, PERIOD, TimeUnit.SECONDS);
		PositionReceiver positionReceiver = new PositionReceiver();
		positionReceiver.addObserver(positionListener);
		if(args!=null && args.length>0){
			File file = new File(args[0]);
			positionReceiver.read(Source.FILE, new FileReader(file));
		}
		positionReceiver.read(Source.CONSOLE, new InputStreamReader(System.in));
		threadPoolExecutor.shutdown();
	}

	public void read(Source src, InputStreamReader inputStreamReader) {
		BufferedReader consoleReader = null;
		try{
			consoleReader = new BufferedReader(inputStreamReader);
			String inputString = null;
			
			while((inputString = consoleReader.readLine())!=null){
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
	
	public enum Source{
		CONSOLE, FILE
	}
}
