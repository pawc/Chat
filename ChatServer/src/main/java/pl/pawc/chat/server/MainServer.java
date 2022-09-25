package pl.pawc.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.pawc.chat.server.model.Client;

public class MainServer {

	public static final Logger logger = LogManager.getLogger(MainServer.class);
	private final static int DEFAULT_PORT = 3000;
	protected static ServerSocket serverSocket = null;
	public static ArrayList<Client> clients = new ArrayList<>();
	public static boolean isRunning = true;
	
	public static void main(String[] args){
	    int port;
		
		try{
			port = Integer.parseInt(args[0]);
		}
		catch(ArrayIndexOutOfBoundsException | NumberFormatException e){
			logger.info("Starting server using default port {}", DEFAULT_PORT);
			port = DEFAULT_PORT;
		}

		try{
			serverSocket = new ServerSocket(port);
		}
		catch(IOException e){
			logger.error("Couldn't start server on port {}", port);
			isRunning = false;
			return;
		}

		new SocketListener(serverSocket).start();

		logger.info("Server listening on port {}. Awaiting connections...", port);
		
		Scanner sc = new Scanner(System.in);
		String line;
		
		while(isRunning){
			try{
				line = sc.nextLine();
				switch (line){
					case "clients" : {
						logger.info(clients);
						break;
					}
					case "shutdown" : {
						clients.forEach(Client::exit);
						logger.info("Server shutdown");
						sc.close();
						isRunning = false;
						break;
					}
				}
			}
			catch(Throwable e){
				logger.error(e);
				e.printStackTrace();
			}
		}

	}
	
}