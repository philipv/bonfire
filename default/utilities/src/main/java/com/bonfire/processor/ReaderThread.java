package com.bonfire.processor;

import java.io.BufferedReader;
import java.io.IOException;

public class ReaderThread implements Runnable {
	private BufferedReader reader;
	
	public ReaderThread(BufferedReader reader){
		this.reader = reader;
	}
	
	public void run() {
		String inputString;
		try{
			while((inputString = reader.readLine())!=null){
				processUpdate(inputString);
			}
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
}