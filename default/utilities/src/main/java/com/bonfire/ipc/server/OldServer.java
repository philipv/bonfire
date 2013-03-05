package com.bonfire.ipc.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.bonfire.ipc.OldServerSocketAcceptor;

public class OldServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		OldServerSocketAcceptor serverSocketAcceptor = new OldServerSocketAcceptor(new ServerSocket(8000));
		serverSocketAcceptor.start();
	}

}
