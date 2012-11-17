package com.bonfire.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

public class PositionReceiver extends Observable{
	
	public static void main(String[] args){
		PositionReceiver positionReceiver = new PositionReceiver();
		positionReceiver.start();
	}

	private void start() {
		BufferedReader consoleReader = null;
		try{
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
			String inputString = null;
			
			while(inputString==null || !inputString.equals("quit")){
				inputString = consoleReader.readLine();
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
