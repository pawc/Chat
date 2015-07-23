package pawc.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import pawc.chat.server.model.Client;
import pawc.chat.shared.model.Data;
import pawc.chat.shared.model.Message;

public class SocketHandler extends Thread{

	private Client client;
	
	public SocketHandler(Client client){
		this.client=client;
	}
	
	
	public void run(){

	  try{
	   while(true){
	       ObjectInputStream input = new ObjectInputStream(client.getSocket().getInputStream());
	       Data data = (Data) input.readObject();
	           switch(data.getCommand()){
	           case "introduction" :
	               client.setNick(data.getArguments().get(0).toString());
	               break;
	           
	           case "message" :
	               sendMessageToAll(data.getArguments().get(0).toString());
	               break;
	               
	           }
	   }
	  }
	  catch(Exception e){
	      Main.log.warning("Some error. Try to reconnect");
	      client.exit();
	      Main.clientContainer.remove(client);
	  }
	  this.interrupt();
	    
	}
	
	private void sendMessageToAll(String message){
	    List list = new ArrayList<Message>();
	    Message messageObject = new Message(message, this.client.getNick());
	    list.add(messageObject);
	    Data data = new Data("message", list);
	    
        for(Client client : Main.clientContainer){
            try{
            ObjectOutputStream objectOut = new ObjectOutputStream(client.getSocket().getOutputStream());
            objectOut.writeObject(data);
            objectOut.close();
            }
            catch(IOException e){
                Main.log.warning("Error sending message from "+this.client.getNick()+" to: "+client.getNick());
                continue;
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
            ObjectOutputStream objectOut = new ObjectOutputStream(client.getSocket().getOutputStream());
            objectOut.writeObject(data);
            objectOut.close();
	        }
	        catch(IOException e){
	            Main.log.warning("Error sending nicks list to: "+client.getNick());
	            continue;
	        }
        }
	    
	}
	
	
}