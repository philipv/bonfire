package com.bonfire.ipc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.bonfire.processor.Reader;

public class OldClient{

	private Socket clientSocket;
	private PrintWriter socketWriter;
	private BufferedReader socketReader;
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
		socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	
	public void start() throws IOException{
		Reader socketReaderTask = null;
		socketReaderTask = new Reader(socketReader);
		Thread socketReaderThread = new Thread(socketReaderTask);
		socketReaderThread.start();
		
		Reader consoleReaderTask = null;
		consoleReaderTask = new Reader(new BufferedReader(new InputStreamReader(System.in))){
			public void processUpdate(String update){
				if(update.equals("quit"))
					System.exit(0);
				else
					socketWriter.println(update);
			}
		};
		consoleReaderTask.run();
	}
	
	public void destroy(){
		try{
			clientSocket.close();
			socketWriter.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
}
