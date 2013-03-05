package com.bonfire.ipc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.bonfire.ipc.OldSocketReader;
import com.bonfire.processor.Reader;

public class OldClient extends OldSocketReader {
	
	protected PrintWriter socketWriter;
	
	public OldClient(Socket socket) throws IOException {
		super(socket);
		socketWriter = new PrintWriter(socket.getOutputStream(), true);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		socketWriter.close();
	}

	public void start(){
		super.start(null);
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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OldClient client = null;
		try{
			client = new OldClient(new Socket("localhost", 8000));
			client.start();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			client.destroy();
		}
	}
}
