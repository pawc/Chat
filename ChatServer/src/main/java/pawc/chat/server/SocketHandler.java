package pawc.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;

import pawc.chat.server.model.Client;
import pawc.chat.shared.model.Data;

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
	               return;
	           
	           case "message" :
	               sendMessageToAll(data.getArguments().get(0).toString());
	               return;
	           
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
	    
	}
	
	private void sendNicksToAll(){
	    
	}
	
	
}