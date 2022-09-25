package pl.pawc.chat.server;

import java.io.IOException;

import pl.pawc.chat.shared.Data;
import pl.pawc.chat.shared.PrivateMessage;
import pl.pawc.chat.server.model.Client;
import pl.pawc.chat.server.util.Util;

public class SocketConnection extends Thread{

	private final Client client;

	public SocketConnection(Client client){
		this.client = client;
	}
	
	public void run(){
		try{
			while(true){
				Data data = (Data) client.in.readObject();
    	        String command = data.getCommand();
    	            switch(command){
    	            case "introduction" :
    	                String nick = (String) data.getArguments();
	                	if(Util.checkIfNickAlreadyInUse(nick)){
    	                    client.out.writeObject(new Data("NickAlreadyInUse", null));
    	                    client.out.flush();
    	                    client.in.close();
    	                    client.out.close();
    	                    client.getSocket().close();
    	                    MainServer.logger.info("Client "+client.getSocket().getInetAddress().getHostName()+
                    		"Nick already in use. Disconnecting ");
    	                   return;
    	                }
    	                client.setNick(nick);
    	                MainServer.clients.add(client);
						Util.sendNicksToAll();
    	                break;
    	           
    	            case "message" :
						Util.sendMessage((String) data.getArguments());
    	                break;
    	           
    	           
    	            case "privateMessage" :
    	                PrivateMessage privateMessage = (PrivateMessage) data.getArguments();
    	                String recipient = privateMessage.getRecipient();
    	                String sender = privateMessage.getSender();
						Util.sendMessageToNicks(sender, recipient, data);
    	                break;
    	            }
    	    }
	}
	catch(IOException | ClassNotFoundException e){
		MainServer.logger.info("Disconnecting client "+client.getNick());
		MainServer.logger.warn(e.toString());
		client.exit();
		MainServer.clients.remove(client);
		Util.sendNicksToAll();
	}
	this.interrupt();    
	}
	
}