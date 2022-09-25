package pl.pawc.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pl.pawc.chat.server.model.Client;

public class SocketListener extends Thread {

	private final ServerSocket serverSocket;
	
	public SocketListener(ServerSocket serverSocket){
		this.serverSocket = serverSocket;
	}
	
	public void run(){
		while(MainServer.isRunning){
			try{
				Socket socket = serverSocket.accept();
				MainServer.logger.info("New connection from {}", socket.getInetAddress().toString());
				Client client = new Client(socket);
				SocketConnection socketHandler = new SocketConnection(client);
				socketHandler.start();
			}
			catch(IOException e){
				MainServer.logger.warn("Couldn't handle incoming connection");
				MainServer.logger.error(e);
			}
		}
	}	
	
}