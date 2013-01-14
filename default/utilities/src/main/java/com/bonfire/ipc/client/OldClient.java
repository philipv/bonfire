package com.bonfire.ipc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.bonfire.processor.ReaderThread;

public class OldClient{

	private Socket clientSocket;
	private PrintWriter socketWriter;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OldClient client = null;
		try{
			client = new OldClient();
			client.start();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			client.destroy();
		}
	}
	
	public OldClient() throws IOException{
		clientSocket = new Socket("localhost", 8000);
		socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
	}
	
	public void start() throws IOException{
		BufferedReader consoleReader = null;
		Thread readerThread = null;
		ReaderThread readerRunnable = null;
		try{
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
			String inputString = null;
			readerRunnable = new ReaderThread(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
			readerThread = new Thread(readerRunnable);
			readerThread.start();
			
			while((inputString = consoleReader.readLine())!=null){
				if(inputString.equals("quit"))
					break;
				processUpdate(inputString);
			}
		}finally{
			try{
				consoleReader.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	
	public void destroy(){
		try{
			clientSocket.close();
			socketWriter.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	public void processUpdate(Object update){
		socketWriter.println(update);
	}
}
