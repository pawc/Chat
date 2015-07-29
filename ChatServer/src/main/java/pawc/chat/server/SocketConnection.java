package pawc.chat.server;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import pawc.chat.server.model.Client;
import pawc.chat.shared.model.Data;
import pawc.chat.shared.model.PrivateMessage;

public class SocketConnection extends Thread{

	private Client client;
	
	public SocketConnection(Client client){
		this.client=client;
	}
	
	public void run(){
	 try{
    	   while(true){
    	       Data data = (Data) client.in.readObject();
    	       String command = data.getCommand();
    	           switch(command){
    	           case "introduction" :
    	               String nick = (String) data.getArguments();
    	               if(checkIfNickAlreadyInUse(nick)){
    	                   client.out.writeObject(new Data("NickAlreadyInUse", null));
    	                   client.out.flush();
    	                   client.in.close();
    	                   client.out.close();
    	                   client.getSocket().close();
    	                   Main.clientContainer.remove(client);
    	                   Main.log.info("Client "+client.getSocket().getInetAddress().getHostName()+
    	                           " chose a nick already in use. Disconnecting ");
    	                   return;
    	               }
    	               client.setNick(nick);
    	               Main.clientContainer.add(client);
    	               sendNicksToAll();
    	               break;
    	           
    	           case "message" :
    	               sendMessageToAll((String) data.getArguments());
    	               break;
    	           
    	           
    	           case "privateMessage" :
    	               PrivateMessage privateMessage = (PrivateMessage) data.getArguments();
    	               String recipient = privateMessage.getRecipient();
    	               String sender = privateMessage.getSender();
    	               sendMessageToNicks(sender, recipient, data);
    	               break;
    	           }
    	   }
	 }
	  catch(IOException | ClassNotFoundException e){
	      Main.log.info("Disconnecting client "+client.getNick());
	      Main.log.warning(e.toString());
	      e.printStackTrace();
	      client.exit();
	      Main.clientContainer.remove(client);
	      sendNicksToAll();
	  }
	  this.interrupt();
	    
	}
	
	private void sendMessageToAll(String message){
	    
	    Data data = new Data("message", message);
	    
        for(Client client : Main.clientContainer){
            try{
                client.out.writeObject(data);
                client.out.flush();
            }
            catch(IOException e){
                Main.log.warning("Error sending message from "+this.client.getNick()+" to: "+client.getNick());
                continue;
            }
            
        }
	    
	}
	
	
	private void sendMessageToNicks(String nick1, String nick2, Data data) throws IOException{
	    for(Client client : Main.clientContainer){
	        if(client.getNick().equals(nick1)||client.getNick().equals(nick2)){
	            client.out.writeObject(data);
	            client.out.flush();	            
	        }
	    }
	}
	
	private void sendNicksToAll(){
	    List list = new ArrayList<String>();
	    for(Client client : Main.clientContainer){
	        list.add(client.getNick());
	    }
	    Data data = new Data("nicks", list);
	    
	    for(Client client : Main.clientContainer){
	        try{
	            client.out.writeObject(data);
	            client.out.flush();
	        }
	        catch(IOException e){
	            Main.log.warning("Error sending nicks list to: "+client.getNick());
	            e.printStackTrace();
	            continue;
	        }
        }
    }
	/*
	private ObjectOutputStream OOSofClientWithNick(String nick){
	    ObjectOutputStream out = null;
	    for(Client client : Main.clientContainer){
	        if(client.getNick().equals(nick)) out = client.out;
	        break;
	    }
	    return out;
	}
	*/
	private boolean checkIfNickAlreadyInUse(String nick){
	    boolean answer = false;
	    for(Client client : Main.clientContainer){
	        if(client.getNick().equals(nick)) answer = true;
	        break;
	    }
	    return answer;
	}
	
	
}