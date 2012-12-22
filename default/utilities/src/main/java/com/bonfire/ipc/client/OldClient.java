package com.bonfire.ipc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import com.bonfire.processor.ReaderThread;

public class OldClient implements Observer{

	private Socket clientSocket;
	private PrintWriter socketWriter;
	private BufferedReader socketReader;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OldClient client = new OldClient();
	}
	
	public OldClient(){
		
		try{
			BufferedReader consoleReader = null;
			clientSocket = new Socket("localhost", 8000);
			socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public void start(){
		try{
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
			String inputString = null;
			
			new Thread(new ReaderThread(socketReader)).start();
			
			while((inputString = consoleReader.readLine())!=null){
				if(inputString.equals("quit"))
					break;
				socketWriter.println(inputString);
			}
		}finally{
			try{
				socketWriter.close();
				socketReader.close();
				consoleReader.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
