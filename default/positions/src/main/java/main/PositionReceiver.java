package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PositionReceiver {
	
	public static void main(String[] args){
		BufferedReader consoleReader = null;
		try{
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
			String inputString = null;
			
			while(inputString==null || !inputString.equals("quit")){
				inputString = consoleReader.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				consoleReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
