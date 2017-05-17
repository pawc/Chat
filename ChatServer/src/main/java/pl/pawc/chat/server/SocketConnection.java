package pl.pawc.chat.server;

import java.io.IOException;

import pl.pawc.chat.shared.Data;
import pl.pawc.chat.shared.PrivateMessage;
import pl.pawc.chat.server.model.Client;
import pl.pawc.chat.server.util.Util;

public class SocketConnection extends Thread{

	private Client client;
	private Util util;
	
	public SocketConnection(Client client){
		this.client=client;
		this.util = new Util();
	}
	
	public void run(){
		try{
			while(true){
				Data data = (Data) client.in.readObject();
    	        String command = data.getCommand();
    	            switch(command){
    	            case "introduction" :
    	                String nick = (String) data.getArguments();
	                	if(util.checkIfNickAlreadyInUse(nick)){
    	                    client.out.writeObject(new Data("NickAlreadyInUse", null));
    	                    client.out.flush();
    	                    client.in.close();
    	                    client.out.close();
    	                    client.getSocket().close();
    	                    Main.log.info("Client "+client.getSocket().getInetAddress().getHostName()+
                    		"Nick already in use. Disconnecting ");
    	                   return;
    	                }
    	                client.setNick(nick);
    	                Main.clientContainer.add(client);
    	                util.sendNicksToAll();
    	                break;
    	           
    	            case "message" :
    	                util.sendMessageToAll((String) data.getArguments());
    	                //System.out.println("message" + (String) data.getArguments()); //to test message encryption server side
    	                break;
    	           
    	           
    	            case "privateMessage" :
    	                PrivateMessage privateMessage = (PrivateMessage) data.getArguments();
    	                String recipient = privateMessage.getRecipient();
    	                String sender = privateMessage.getSender();
    	                util.sendMessageToNicks(sender, recipient, data);
    	                //System.out.println("private message text" + privateMessage.getMessage()); //to test private message encryption server side
    	                break;
    	            }
    	    }
	}
	catch(IOException | ClassNotFoundException e){
		Main.log.info("Disconnecting client "+client.getNick());
		Main.log.warning(e.toString());
		client.exit();
		Main.clientContainer.remove(client);
		util.sendNicksToAll();
	}
	this.interrupt();    
	}
	
}