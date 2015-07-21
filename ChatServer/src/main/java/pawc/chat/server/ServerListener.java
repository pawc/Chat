package pawc.chat.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import pawc.chat.server.model.Client;

public class ServerListener extends Thread {

	protected ServerSocket serverSocket;
	
	public ServerListener(ServerSocket s){
		serverSocket=s;
	}
	
	public void run(){
		while(true){
			try{
				Socket socket = serverSocket.accept();
				Main.log.info("new connection from "+socket.getInetAddress().toString());
				
				Client client = new Client(socket);
				SocketHandler socketHandler = new SocketHandler(client);
				
				Main.clientThreadsContainer.add(socketHandler);
				socketHandler.start();
				
			;
			}
			catch(IOException e){
				Main.log.warning("Error accepting incoming connection "+e.toString());
				continue;
			}
		}
	}
	
	
}
