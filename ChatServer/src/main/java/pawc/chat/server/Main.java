package pawc.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;
import pawc.chat.server.model.Client;

public class Main {
	
	public static Logger log = Logger.getLogger("Main Server");
	protected static ServerSocket serverSocket = null;
	protected static ArrayList<Client> clientContainer = new ArrayList<Client>();
	
	public static void main(String args[]){
		
	    int port = 0;
		
		try{
			port = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e){
			log.warning("Enter a valid port number");
			System.exit(0);
		}
		
		log.info("Starting server");
		
		try{
			serverSocket = new ServerSocket(port);
		}
		catch(IOException e){
			log.warning("Couldnt start server on port "+args[0]+" "+e.toString());
			System.exit(0);
		}
		
		new ServerListener(serverSocket).start();
		
		log.info("Server started. Awaiting connections...");
		
		
		Scanner sc = new Scanner(System.in);
		String line = "";
		
		while(true){
			
			try{
			line = sc.nextLine();
			}
			catch(NoSuchElementException e){}
			switch (line){
				case "clients" : {
					for(Client client : clientContainer){
						System.out.printf(client.toString()+", ");
						System.out.println();
					}
					break;
				}
				
				case "shutdown" : {
					for(Client client : clientContainer){
						client.exit();
					}
					System.out.println("Server shutdown");
					sc.close();
					System.exit(0);
					
				}
				
				
			}
			
		}

		
	}
	
}
