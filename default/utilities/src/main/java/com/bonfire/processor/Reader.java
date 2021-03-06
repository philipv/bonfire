package com.bonfire.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class Reader implements Runnable {
	private BufferedReader reader;
	
	public Reader(BufferedReader reader){
		this.reader = reader;
	}
	
	public void run() {
		String inputString;
		try{
			while((inputString = reader.readLine())!=null){
				processUpdate(inputString);
			}
		}catch(SocketException socketException){
			socketException.printStackTrace();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			try{
				reader.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
	public void processUpdate(String update){
		System.out.println(update);
	}
	
	public void stopThread() throws IOException{
		reader.close();
	}
}