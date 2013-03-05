package com.bonfire.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.bonfire.processor.Reader;

public class OldServerSocketAcceptor {
	private ServerSocket serverSocket;

	public OldServerSocketAcceptor(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void start(){
		Socket clientEndPoint = null;
		
		try{
			while((clientEndPoint = serverSocket.accept()) != null){
				final Socket endPoint = clientEndPoint;
				OldSocketReader socketReader = new OldSocketReader(endPoint);
				socketReader.start(new Reader(new BufferedReader(new InputStreamReader(endPoint.getInputStream()))){
					private PrintWriter writer = new PrintWriter(endPoint.getOutputStream(), true);
					public void processUpdate(String update){
						System.out.println("Received update: " + update);
						writer.println("From server: " + update);
					}
				});
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
