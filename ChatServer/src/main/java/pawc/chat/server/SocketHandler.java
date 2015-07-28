package pawc.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import pawc.chat.server.model.Client;
import pawc.chat.shared.model.Data;

public class SocketHandler extends Thread{

	private Client client;
	
	public SocketHandler(Client client){
		this.client=client;
	}
	
	public void run(){
	 try{ 
	   client.out = new ObjectOutputStream(client.getSocket().getOutputStream());
	   client.out.flush();
	   client.in = new ObjectInputStream(client.getSocket().getInputStream());
	   
	   while(true){
	      
	       Data data = (Data) client.in.readObject();
	       String command = data.getCommand();
	           switch(command){
	           case "introduction" :
	               client.setNick((String) data.getArguments());
	               sendNicksToAll();
	               break;
	           
	           case "message" :
	               sendMessageToAll((String) data.getArguments());
	               break;
	           }
	   }
	   
	 }
	  catch(IOException | ClassNotFoundException | NullPointerException e){
	      Main.log.info("Disconnecting client "+client.getNick());
	      Main.log.warning(e.toString());
	      client.exit();
	      Main.clientContainer.remove(client);
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
	
	
}