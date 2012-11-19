package com.bonfire.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.bonfire.data.Position;
import com.bonfire.factory.FactoryUtility;
import com.bonfire.observer.PositionListener;
import com.bonfire.task.EvaluationTask;

/*
 * This component is responsible for:
 *  - Getting the update from different sources.
 *  - Convert the update into a position
 *  - Notify the required listeners.
 */
public class PositionReceiver extends Observable{
	private static final int PERIOD = 60;
	private static final String SEPARATOR = " ";
	private FactoryUtility factoryUtility = new FactoryUtility();
	private ConcurrentHashMap<String, Double> positions = new ConcurrentHashMap<String, Double>();
	
	public static void main(String[] args) throws FileNotFoundException{
		PositionReceiver positionReceiver = new PositionReceiver();
		positionReceiver.initialize(args);
	}

	public void initialize(String[] args) throws FileNotFoundException {
		PositionListener positionListener = new PositionListener(positions);
		ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);
		threadPoolExecutor.scheduleAtFixedRate(new EvaluationTask(positions), PERIOD, PERIOD, TimeUnit.SECONDS);
		addObserver(positionListener);
		if(args!=null && args.length>0){
			File file = new File(args[0]);
			read(Source.FILE, factoryUtility.createFileReader(file));
		}
		read(Source.CONSOLE, new InputStreamReader(System.in));
		threadPoolExecutor.shutdown();
	}

	public void read(Source src, InputStreamReader inputStreamReader) {
		BufferedReader reader = null;
		try{
			reader = factoryUtility.createBufferedReader(inputStreamReader);
			String inputString = null;
			
			while((inputString = reader.readLine())!=null){
				if(inputString.equals("quit"))
					break;
				Position position = convert(inputString);
				if(position!=null){
					setChanged();
					notifyObservers(position);
				}
			}
		}catch(IOException e){
			System.out.println("Cannot read from the input stream. Program will exit now");
			e.printStackTrace();
		}finally{
			try {
				reader.close();
				inputStreamReader.close();
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
					if(positionalComponents[0].length()==3){
						Position position = new Position();
						position.setCurrency(positionalComponents[0].toUpperCase());
						position.setValue(Double.valueOf(positionalComponents[1]));
						return position;
					}
					else{
						System.out.println("currency:" + positionalComponents[0] + " is not a 3 char currency code\n");
						return null;
					}
				}
			}
			System.out.println("\"" + data + "\" is not in valid format and not a special command\n");
		}catch(NumberFormatException nfe){
			System.out.println("value:" + positionalComponents[1] + " is not a number\n");
		}
		return null;
	}

	public void setFactoryUtility(FactoryUtility factoryUtility) {
		this.factoryUtility = factoryUtility;
	}

	public void setPositions(ConcurrentHashMap<String, Double> positions) {
		this.positions = positions;
	}
}
