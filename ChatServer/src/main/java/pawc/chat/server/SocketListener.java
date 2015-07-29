package pawc.chat.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pawc.chat.server.model.Client;

public class SocketListener extends Thread {

	protected ServerSocket serverSocket;
	
	public SocketListener(ServerSocket s){
		serverSocket=s;
	}
	
	public void run(){
		while(true){
			try{
				Socket socket = serverSocket.accept();
				Main.log.info("new connection from "+socket.getInetAddress().toString());
				Client client = new Client(socket);
				SocketConnection socketHandler = new SocketConnection(client);
				socketHandler.start();
				Main.log.info("thread for the new connection started");
			
			}
			catch(IOException e){
				Main.log.warning("Error accepting incoming connection "+e.toString());
				continue;
			}
		}
	}
	
	
}
