package com.bonfire.ipc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.bonfire.processor.Reader;

public class OldServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket clientEndPoint = null;
		try{
			serverSocket = new ServerSocket(8000);
			while((clientEndPoint = serverSocket.accept()) != null){
				final Socket endPoint = clientEndPoint;
				new Reader(new BufferedReader(new InputStreamReader(clientEndPoint.getInputStream()))){
					
					private PrintWriter writer = new PrintWriter(endPoint.getOutputStream(), true);
					public void processUpdate(String update){
						System.out.println("Received update: " + update);
						writer.println("From server: " + update);
					}
				}.run();
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			try{
				serverSocket.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
		
	}

}
