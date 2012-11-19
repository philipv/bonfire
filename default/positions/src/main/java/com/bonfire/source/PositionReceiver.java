package com.bonfire.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.bonfire.data.Position;
import com.bonfire.observer.PositionListener;
import com.bonfire.task.EvaluationTask;

public class PositionReceiver extends Observable{
	private static final int PERIOD = 20;
	private static final String SEPARATOR = " ";
	
	public static void main(String[] args) throws FileNotFoundException{
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
				Position position = convert(inputString);
				if(position!=null){
					setChanged();
					notifyObservers(position);
				}
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
	
	public Position convert(Object data){
		String[] positionalComponents = null;
		try{
			if(data!=null && data instanceof String){
				String stringData = (String)data;
				positionalComponents = StringUtils.split(stringData, SEPARATOR);
				if(positionalComponents!=null && positionalComponents.length==2){
					Position position = new Position();
					position.setCurrency(positionalComponents[0]);
					position.setValue(Double.valueOf(positionalComponents[1]));
					return position;
				}
			}
		}catch(NumberFormatException nfe){
			System.out.println(positionalComponents[1] + " is not a number");
		}
		return null;
	}
}
