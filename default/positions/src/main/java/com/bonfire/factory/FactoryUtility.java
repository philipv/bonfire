package com.bonfire.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

import com.bonfire.data.Position;
import com.bonfire.task.PositionProcessTask;

public class FactoryUtility {
	
	public Runnable createTaskFromConsole(Position position, ConcurrentHashMap<String, Double> positions){
		return new PositionProcessTask(position, positions);
	}
	
	public BufferedReader createBufferedReader(InputStreamReader inputStreamReader) {
		return new BufferedReader(inputStreamReader);
	}
	
	public FileReader createFileReader(File file) throws FileNotFoundException {
		return new FileReader(file);
	}

}
