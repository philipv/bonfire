package com.bonfire.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.bonfire.processor.Reader;

public class OldSocketReader{

	protected Socket socket;
	protected BufferedReader socketReader;
	
	public OldSocketReader(Socket socket) throws IOException{
		this.socket = socket;
		socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void start(Reader socketReaderTask) {
		if(socketReaderTask==null)
			socketReaderTask = new Reader(socketReader);
		Thread socketReaderThread = new Thread(socketReaderTask);
		socketReaderThread.start();
	}
	
	public void destroy(){
		try{
			socket.close();
			socketReader.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
}
