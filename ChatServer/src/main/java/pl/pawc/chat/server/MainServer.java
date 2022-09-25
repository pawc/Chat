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
	protected static ServerSocket serverSocket = null;
	public static ArrayList<Client> clients = new ArrayList<>();
	public static boolean isRunning = true;
	
	public static void main(String[] args){
	    int port;
		
		try{
			port = Integer.parseInt(args[0]);
			serverSocket = new ServerSocket(port);
		}
		catch(NumberFormatException e){
			logger.error("Enter a valid port number");
			System.exit(0);
		}
		catch(IOException e){
			logger.error("Couldn't start server on port {}", args[0]);
			System.exit(0);
		}

		logger.info("Starting server");

		new SocketListener(serverSocket).start();

		logger.info("Server started. Awaiting connections...");
		
		Scanner sc = new Scanner(System.in);
		String line;
		
		while(true){
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
						System.exit(0);
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